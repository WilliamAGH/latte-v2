package org.flatscrew.latte;

import org.flatscrew.latte.input.InputHandler;
import org.flatscrew.latte.input.MouseAction;
import org.flatscrew.latte.input.MouseClickMessage;
import org.flatscrew.latte.input.MouseClickTracker;
import org.flatscrew.latte.input.MouseHoverTextDetector;
import org.flatscrew.latte.input.MouseMessage;
import org.flatscrew.latte.input.MouseSelectionTracker;
import org.flatscrew.latte.input.MouseSelectionUpdate;
import org.flatscrew.latte.input.MouseTarget;
import org.flatscrew.latte.input.MouseTargetProvider;
import org.flatscrew.latte.input.MouseTargets;
import org.flatscrew.latte.input.NewInputHandler;
import org.flatscrew.latte.message.BatchMessage;
import org.flatscrew.latte.message.CheckWindowSizeMessage;
import org.flatscrew.latte.message.ClearScreenMessage;
import org.flatscrew.latte.message.EnterAltScreen;
import org.flatscrew.latte.message.ErrorMessage;
import org.flatscrew.latte.message.ExitAltScreen;
import org.flatscrew.latte.message.OpenUrlMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.SequenceMessage;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.term.TerminalInfo;
import org.flatscrew.latte.term.jline.JLineTerminalInfoProvider;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.Signals;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Program {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final CountDownLatch initLatch = new CountDownLatch(1);
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final CommandExecutor commandExecutor;
    private final InputHandler inputHandler;

    private Throwable lastError;
    private volatile Model currentModel;
    private final Renderer renderer;
    private final Terminal terminal;
    private final MouseSelectionTracker mouseSelectionTracker = new MouseSelectionTracker();
    private final MouseHoverTextDetector mouseHoverTextDetector = new MouseHoverTextDetector();
    private final MouseClickTracker mouseClickTracker = new MouseClickTracker();
    private boolean extendSelectionOnScroll;
    private boolean manageMouseSelectionCursor;
    private boolean mouseSelectionCursorActive;
    private boolean hoverTextCursorEnabled;
    private boolean hoverTextCursorActive;
    private boolean mouseClicksEnabled;

    public Program(Model initialModel) {
        this.currentModel = initialModel;
        this.commandExecutor = new CommandExecutor();

        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .jni(true)
                    .build();
            terminal.enterRawMode();

            TerminalInfo.provide(new JLineTerminalInfoProvider(terminal));

            this.renderer = new StandardRenderer(terminal);
            this.inputHandler = new NewInputHandler(terminal, this::send);
        } catch (IOException e) {
            throw new ProgramException("Failed to initialize terminal", e);
        }
    }

    public Program withAltScreen() {
        renderer.enterAltScreen();
        return this;
    }

    public Program withReportFocus() {
        renderer.enableReportFocus();
        return this;
    }

    public Program withMouseAllMotion() {
        renderer.enableMouseAllMotion();
        renderer.enableMouseSGRMode();
        return this;
    }

    public Program withMouseCellMotion() {
        renderer.enableMouseCellMotion();
        renderer.enableMouseSGRMode();
        return this;
    }

    /**
     * While selecting, translate wheel events into selection motion updates.
     */
    public Program withMouseSelectionExtendOnScroll() {
        this.extendSelectionOnScroll = true;
        return this;
    }

    /**
     * Manage the mouse cursor during selection (OSC 22).
     */
    public Program withMouseSelectionCursor() {
        this.manageMouseSelectionCursor = true;
        return this;
    }

    /**
     * Manage the mouse cursor when hovering non-whitespace text (OSC 22).
     * Requires mouse motion events (e.g. {@link #withMouseAllMotion()}).
     */
    public Program withMouseHoverTextCursor() {
        this.hoverTextCursorEnabled = true;
        return this;
    }

    /**
     * When enabled, emits {@link MouseClickMessage} on press/release clicks.
     * Latte extension; no Bubble Tea equivalent.
     */
    public Program withMouseClicks() {
        this.mouseClicksEnabled = true;
        return this;
    }
    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new IllegalStateException("Program is already running!");
        }

        handleTerminationSignals();
        handleTerminalResize();

        // start reading keyboard input
        inputHandler.start();

        // starting renderer
        renderer.hideCursor();
        renderer.start();

        // execute init command
        Command initCommand = currentModel.init();
        commandExecutor
                .executeIfPresent(initCommand, this::send, this::sendError)
                .thenRun(initLatch::countDown);

        // render the initial view
        renderer.write(currentModel.view());

        // run event loop
        Model finalModel = eventLoop();

        // stop reading keyboard input
        inputHandler.stop();

        // render final model view before closing
        renderer.write(finalModel.view());
        renderer.showCursor();
        renderer.stop();

        if (manageMouseSelectionCursor && mouseSelectionCursorActive) {
            renderer.resetMouseCursor();
        }
        if (hoverTextCursorEnabled && hoverTextCursorActive) {
            renderer.resetMouseCursor();
        }

        // disabling mouse support
        disableMouse();

        if (renderer.reportFocus()) {
            renderer.disableReportFocus();
        }

        if (renderer.altScreen()) {
            renderer.exitAltScreen();
        }

        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.puts(InfoCmp.Capability.cursor_down);
        terminal.flush();

        // Finally clean up
        isRunning.set(false);
        commandExecutor.shutdown();
        try {
            terminal.close();
        } catch (IOException e) {
            throw new ProgramException(e);
        }

        if (lastError != null) {
            throw new ProgramException(lastError);
        }
    }

    private void handleTerminationSignals() {
        Signals.register("INT", () -> commandExecutor.executeIfPresent(QuitMessage::new, this::send, this::sendError));
        Signals.register("TERM", () -> commandExecutor.executeIfPresent(QuitMessage::new, this::send, this::sendError));
    }

    private void handleTerminalResize() {
        Signals.register("WINCH", () -> commandExecutor.executeIfPresent(CheckWindowSizeMessage::new, this::send, this::sendError));
        commandExecutor.executeIfPresent(CheckWindowSizeMessage::new, this::send, this::sendError);
    }

    private Model eventLoop() {
        while (isRunning.get()) {
            Message msg;
            try {
                msg = messageQueue.poll(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (msg != null) {
                if (msg instanceof ClearScreenMessage) {
                    renderer.clearScreen();
                    continue;
                } else if (msg instanceof QuitMessage) {
                    return currentModel;
                } else if (msg instanceof EnterAltScreen) {
                    renderer.enterAltScreen();
                    continue;
                } else if (msg instanceof ExitAltScreen) {
                    renderer.exitAltScreen();
                    continue;
                } else if (msg instanceof BatchMessage batchMessage) {
                    Arrays.stream(batchMessage.commands())
                            .forEach(command -> commandExecutor.executeIfPresent(command, this::send, this::sendError));
                    continue;
                } else if (msg instanceof SequenceMessage sequenceMessage) {
                    Arrays.stream(sequenceMessage.commands())
                            .reduce(
                                    CompletableFuture.completedFuture(null),
                                    (CompletableFuture<Void> future, Command command) ->
                                            future.thenCompose(__ ->
                                                    commandExecutor.executeIfPresent(command, this::send, this::sendError)
                                            ),
                                    (f1, f2) -> f2
                            ).join();
                    continue;
                } else if (msg instanceof ErrorMessage errorMessage) {
                    this.lastError = errorMessage.error();
                    return currentModel;
                } else if (msg instanceof CheckWindowSizeMessage) {
                    commandExecutor.executeIfPresent(this::checkSize, this::send, this::sendError);
                    continue;
                } else if (msg instanceof OpenUrlMessage openUrlMessage) {
                    openUrl(openUrlMessage.url());
                    continue;
                }

                if (msg instanceof MouseMessage mouseMessage) {
                    handleMouseClickTracking(mouseMessage);
                    handleMouseSelectionTracking(mouseMessage);
                    handleMouseHoverCursor(mouseMessage);
                }

                // process internal messages for the renderer
                renderer.handleMessage(msg);

                UpdateResult<? extends Model> updateResult = currentModel.update(msg);

                currentModel = updateResult.model();
                renderer.notifyModelChanged();
                commandExecutor.executeIfPresent(updateResult.command(), this::send, this::sendError);

                renderer.write(currentModel.view());
            }

        }
        return currentModel;
    }

    private void handleMouseSelectionTracking(MouseMessage mouseMessage) {
        MouseSelectionUpdate selectionUpdate = mouseSelectionTracker.update(mouseMessage);

        if (extendSelectionOnScroll && selectionUpdate.selectionScrollUpdate() != null) {
            send(selectionUpdate.selectionScrollUpdate());
        }

        if (!manageMouseSelectionCursor) {
            return;
        }

        if (selectionUpdate.selectionStarted()) {
            setMouseSelectionCursorText();
            return;
        }

        if (selectionUpdate.selectionEnded()) {
            resetMouseSelectionCursor();
            return;
        }

        if (selectionUpdate.selectionActive()) {
            if (mouseMessage.isWheel()) {
                resetMouseSelectionCursor();
                return;
            }

            if (mouseMessage.getAction() == MouseAction.MouseActionMotion
                    || mouseMessage.getAction() == MouseAction.MouseActionPress) {
                setMouseSelectionCursorText();
            }
        }
    }

    private void handleMouseHoverCursor(MouseMessage mouseMessage) {
        if (!hoverTextCursorEnabled) {
            return;
        }
        if (mouseSelectionTracker.isSelecting() || mouseSelectionCursorActive) {
            return;
        }
        if (mouseMessage.isWheel()) {
            return;
        }
        if (mouseMessage.getAction() != MouseAction.MouseActionMotion
                && mouseMessage.getAction() != MouseAction.MouseActionPress
                && mouseMessage.getAction() != MouseAction.MouseActionRelease) {
            return;
        }

        boolean overText = mouseHoverTextDetector.isHoveringText(
                currentModel.view(),
                mouseMessage.column(),
                mouseMessage.row()
        );

        if (overText && !hoverTextCursorActive) {
            renderer.setMouseCursorText();
            hoverTextCursorActive = true;
        } else if (!overText && hoverTextCursorActive) {
            renderer.resetMouseCursor();
            hoverTextCursorActive = false;
        }
    }

    private void setMouseSelectionCursorText() {
        if (mouseSelectionCursorActive) {
            return;
        }
        renderer.setMouseCursorText();
        mouseSelectionCursorActive = true;
    }

    private void resetMouseSelectionCursor() {
        if (!mouseSelectionCursorActive) {
            return;
        }
        renderer.resetMouseCursor();
        mouseSelectionCursorActive = false;
    }

    private void handleMouseClickTracking(MouseMessage mouseMessage) {
        if (!mouseClicksEnabled) {
            return;
        }
        MouseTarget target = resolveMouseTarget(mouseMessage);
        MouseClickMessage clickMessage = mouseClickTracker.handle(mouseMessage, target);
        if (clickMessage != null) {
            send(clickMessage);
        }
    }

    private MouseTarget resolveMouseTarget(MouseMessage mouseMessage) {
        if (!(currentModel instanceof MouseTargetProvider provider)) {
            return null;
        }
        return MouseTargets.hitTest(provider.mouseTargets(), mouseMessage.column(), mouseMessage.row());
    }

    private Message checkSize() {
        Size size = terminal.getSize();
        return new WindowSizeMessage(size.getColumns(), size.getRows());
    }

    private void sendError(Throwable error) {
        send(new ErrorMessage(error));
    }

    public void send(Message msg) {
        if (isRunning.get() && msg != null) {
            messageQueue.offer(msg);
        }
    }

    public void waitForInit() {
        try {
            initLatch.await();
        } catch (InterruptedException e) {
            throw new ProgramException(e);
        }
    }

    private void disableMouse() {
        renderer.disableMouseSGRMode();
        renderer.disableMouseCellMotion();
        renderer.disableMouseAllMotion();
    }

    private void openUrl(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("linux")) {
                new ProcessBuilder("xdg-open", url).start();
            } else if (os.contains("win")) {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
            }
        } catch (Exception ignored) {
        }
    }
}
