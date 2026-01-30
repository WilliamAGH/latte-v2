package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.InputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import com.williamcallahan.tui4j.compat.bubbletea.input.NewInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.NoopInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.WindowsInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.render.NilRenderer;
import com.williamcallahan.tui4j.compat.bubbletea.render.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.render.StandardRenderer;
import com.williamcallahan.tui4j.input.MouseClickMessage;
import com.williamcallahan.tui4j.input.MouseClickTracker;
import com.williamcallahan.tui4j.input.MouseCursor;
import com.williamcallahan.tui4j.input.MouseHoverTextDetector;
import com.williamcallahan.tui4j.input.MouseSelectionAutoScroller;
import com.williamcallahan.tui4j.input.MouseSelectionTracker;
import com.williamcallahan.tui4j.input.MouseSelectionUpdate;
import com.williamcallahan.tui4j.input.MouseTarget;
import com.williamcallahan.tui4j.input.MouseTargetProvider;
import com.williamcallahan.tui4j.input.MouseTargets;
import com.williamcallahan.tui4j.message.SequencedMessage;
import com.williamcallahan.tui4j.runtime.CommandExecutor;
import com.williamcallahan.tui4j.runtime.UrlOpener;
import com.williamcallahan.tui4j.term.TerminalInfo;
import com.williamcallahan.tui4j.term.jline.JLineTerminalInfoProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.Signals;

/**
 * Runs the TUI event loop and manages terminal IO.
 * <p>
 * Port of Bubble Tea's `Program` (tea.go).
 * Manages the lifecycle of a TUI application, including terminal
 * initialization,
 * event polling, and rendering loops.
 * <p>
 * Bubble Tea: tea.go, tea_init.go.
 */
public class Program {

    private static final int DEFAULT_FPS = 60;
    private static final int MAX_FPS = 120;
    private static final Logger logger = Logger.getLogger(
        Program.class.getName()
    );

    static {
        try {
            com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer().hasDarkBackground();
        } catch (Throwable ignored) {
            // Best-effort parity with bubbletea/tea_init.go.
        }
    }

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final CountDownLatch initLatch = new CountDownLatch(1);
    private final BlockingQueue<Message> messageQueue =
        new LinkedBlockingQueue<>();
    private final CommandExecutor commandExecutor;
    private InputHandler inputHandler;

    private Throwable lastError;
    private volatile Model currentModel;
    private Renderer renderer;
    private Terminal terminal;
    private boolean systemTerminal;
    private boolean terminalIsTty;
    private final MouseSelectionTracker mouseSelectionTracker =
        new MouseSelectionTracker();
    private final MouseHoverTextDetector mouseHoverTextDetector =
        new MouseHoverTextDetector();
    private final MouseClickTracker mouseClickTracker = new MouseClickTracker();
    private boolean extendSelectionOnScroll;
    private boolean manageMouseSelectionCursor;
    private boolean mouseSelectionCursorActive;
    private boolean hoverTextCursorEnabled;
    private boolean hoverTextCursorActive;
    private boolean mouseTargetCursorEnabled;
    private MouseCursor currentTargetCursor;
    private boolean mouseClicksEnabled;
    private volatile boolean isSuspended = false;

    private MouseSelectionAutoScroller mouseSelectionAutoScroller;

    private int fps = DEFAULT_FPS;
    private boolean withoutSignalHandler;
    private boolean withoutCatchPanics;
    private final AtomicBoolean ignoreSignals = new AtomicBoolean(false);
    private boolean withoutBracketedPaste;
    private boolean withoutRenderer;

    /**
     * Whether ANSI sequence compression is enabled (currently ignored).
     * <p>
     * Mirrors Go Bubble Tea's {@code WithANSICompressor()} option and is accepted
     * for API compatibility only.
     */
    private boolean ansiCompressor;

    private boolean enableAltScreen;
    private boolean enableMouseAllMotion;
    private boolean enableMouseCellMotion;
    private boolean enableReportFocus;
    private BiFunction<Model, Message, Message> filter;
    private CompletableFuture<?> cancelSignal;
    private InputStream input = System.in;
    private OutputStream output = System.out;
    private boolean inputDisabled;
    private boolean useInputTTY;
    private InputStream openedInput;
    // SSH/remote session support - passes environment to lipgloss for terminal capability detection
    private List<String> environment;
    private boolean selectionAutoScrollEnabled;
    private int selectionAutoScrollEdgeRows = 1;
    private int selectionAutoScrollIntervalMs = 50;

    private long currentSequenceId = 0;
    private long lastHandledSequenceId = 0;

    /**
     * Creates Program to keep this component ready for use.
     *
     * @param initialModel initial model
     */
    public Program(Model initialModel) {
        this(initialModel, (ProgramOption[]) null);
    }

    /**
     * Creates Program to keep this component ready for use.
     *
     * @param initialModel initial model
     * @param options options
     */
    public Program(Model initialModel, ProgramOption... options) {
        this.currentModel = initialModel;
        this.commandExecutor = new CommandExecutor();
        if (options != null) {
            for (ProgramOption option : options) {
                if (option != null) {
                    option.apply(this);
                }
            }
        }
        initializeTerminal();
    }

    /**
     * Handles initialize terminal for this component.
     */
    private void initializeTerminal() {
        try {
            InputStream resolvedInput = resolveInputStream();
            OutputStream resolvedOutput = output == null ? System.out : output;
            this.systemTerminal = isSystemTerminal(
                resolvedInput,
                resolvedOutput
            );
            boolean useDumbTerminal = shouldUseDumbTerminal(
                resolvedInput,
                resolvedOutput
            );

            if (useDumbTerminal) {
                this.terminal = new DumbTerminal(
                    "tui4j-dumb",
                    "dumb",
                    resolvedInput,
                    resolvedOutput,
                    Charset.defaultCharset()
                );
            } else {
                TerminalBuilder builder = TerminalBuilder.builder()
                    .jni(true)
                    .system(this.systemTerminal);

                if (!systemTerminal) {
                    builder.streams(resolvedInput, resolvedOutput);
                }

                this.terminal = builder.build();
            }

            this.terminalIsTty = !isDumbTerminal(terminal);
            if (terminalIsTty) {
                terminal.enterRawMode();
            }

            TerminalInfo.provide(new JLineTerminalInfoProvider(terminal));

            // Wire environment to lipgloss for SSH/remote session support
            if (environment != null && !environment.isEmpty()) {
                com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer().setEnvironment(
                    environment
                );
            }

            if (withoutRenderer) {
                this.renderer = new NilRenderer();
            } else {
                this.renderer = new StandardRenderer(
                    terminal,
                    normalizeFps(fps)
                );
            }
            this.inputHandler = inputDisabled
                ? new NoopInputHandler()
                : createInputHandler(terminal);
            this.mouseSelectionAutoScroller = new MouseSelectionAutoScroller(
                terminal::getHeight,
                mouseSelectionTracker,
                this::send
            );
            applySelectionAutoScrollConfig();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal", e);
        }
    }

    /**
     * Creates the input handler appropriate for the current platform.
     *
     * @param terminal terminal to read from
     * @return input handler
     */
    private InputHandler createInputHandler(Terminal terminal) {
        if (isWindows()) {
            return new WindowsInputHandler(terminal, this::send);
        }
        return new NewInputHandler(terminal, this::send);
    }

    /**
     * Determines whether the current runtime is Windows.
     *
     * @return true when running on Windows
     */
    private boolean isWindows() {
        String osName = System.getProperty("os.name");
        return (
            osName != null && osName.toLowerCase(Locale.ROOT).contains("win")
        );
    }

    /**
     * Handles with alt screen for this component.
     *
     * @return result
     */
    public Program withAltScreen() {
        enableAltScreen = true;
        if (renderer != null) {
            renderer.enterAltScreen();
        }
        return this;
    }

    /**
     * Handles with report focus for this component.
     *
     * @return result
     */
    public Program withReportFocus() {
        enableReportFocus = true;
        if (renderer != null) {
            renderer.enableReportFocus();
        }
        return this;
    }

    /**
     * Handles with mouse all motion for this component.
     *
     * @return result
     */
    public Program withMouseAllMotion() {
        enableMouseAllMotion = true;
        enableMouseCellMotion = false;
        if (renderer != null) {
            renderer.enableMouseAllMotion();
            renderer.enableMouseSGRMode();
        }
        return this;
    }

    /**
     * Handles with mouse cell motion for this component.
     *
     * @return result
     */
    public Program withMouseCellMotion() {
        enableMouseCellMotion = true;
        enableMouseAllMotion = false;
        if (renderer != null) {
            renderer.enableMouseCellMotion();
            renderer.enableMouseSGRMode();
        }
        return this;
    }

    /**
     * While selecting, translate wheel events into selection motion updates.
     *
     * @return this program for chaining
     */
    public Program withMouseSelectionExtendOnScroll() {
        this.extendSelectionOnScroll = true;
        return this;
    }

    /**
     * While selecting, automatically scroll when the mouse is at the top/bottom
     * edge.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     */
    public Program withMouseSelectionAutoScroll() {
        // Auto-scroll is implemented by emitting wheel events while selecting, so we
        // must also
        // preserve selection during scroll to avoid breaking the user's selection
        // state.
        this.extendSelectionOnScroll = true;
        selectionAutoScrollEnabled = true;
        if (mouseSelectionAutoScroller != null) {
            mouseSelectionAutoScroller.enable();
        }
        return this;
    }

    /**
     * Configure selection auto-scroll behavior.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @param edgeRows number of rows from the edge that trigger auto-scroll
     * @param intervalMs interval in milliseconds between auto-scroll steps
     * @return this program for chaining
     */
    public Program withMouseSelectionAutoScroll(int edgeRows, int intervalMs) {
        this.extendSelectionOnScroll = true;
        selectionAutoScrollEnabled = true;
        selectionAutoScrollEdgeRows = edgeRows;
        selectionAutoScrollIntervalMs = intervalMs;
        if (mouseSelectionAutoScroller != null) {
            mouseSelectionAutoScroller.configure(edgeRows, intervalMs);
        }
        return this;
    }

    /**
     * Manage the mouse cursor during selection (OSC 22).
     *
     * @return this program for chaining
     */
    public Program withMouseSelectionCursor() {
        this.manageMouseSelectionCursor = true;
        return this;
    }

    /**
     * Manage the mouse cursor when hovering non-whitespace text (OSC 22).
     * Requires mouse motion events (e.g. {@link #withMouseAllMotion()}).
     *
     * @return this program for chaining
     */
    public Program withMouseHoverTextCursor() {
        this.hoverTextCursorEnabled = true;
        return this;
    }

    /**
     * Manage the mouse cursor based on MouseTarget cursor hints (OSC 22).
     * When hovering a target with {@link MouseCursor#POINTER}, shows pointer cursor.
     * Requires mouse motion events (e.g. {@link #withMouseAllMotion()}).
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     */
    public Program withMouseTargetCursor() {
        this.mouseTargetCursorEnabled = true;
        return this;
    }

    /**
     * When enabled, emits {@link MouseClickMessage} on press/release clicks.
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     */
    public Program withMouseClicks() {
        this.mouseClicksEnabled = true;
        return this;
    }

    /**
     * Blocks the calling thread, enters raw mode, and starts the event loop.
     * Takes control of the terminal until the model returns a {@link QuitMessage}.
     */
    public void run() {
        runInternal();
    }

    /**
     * Blocks the calling thread and returns the final model state.
     * <p>
     * tui4j extension; no Bubble Tea equivalent because the Java {@code run()} API is void.
     *
     * @return final model state
     */
    public Model runWithFinalModel() {
        return runInternal();
    }

    /**
     * Runs the program and returns the final model state.
     *
     * @return final model state
     */
    private Model runInternal() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new IllegalStateException("Program is already running!");
        }

        handleTerminationSignals();
        handleSuspendSignals();
        handleTerminalResize();
        if (mouseSelectionAutoScroller != null) {
            mouseSelectionAutoScroller.start();
        }

        // start reading keyboard input
        inputHandler.start();

        applyStartupOptions();

        // starting renderer
        renderer.hideCursor();
        renderer.start();
        installCancelSignal();

        Model finalModel = currentModel;
        boolean renderFinalView = false;

        try {
            // execute init command
            Command initCommand = currentModel.init();
            commandExecutor
                .executeIfPresent(initCommand, this::send, this::sendError)
                .thenRun(initLatch::countDown);

            // render the initial view
            renderer.write(currentModel.view());

            // run event loop
            finalModel = eventLoop();
            renderFinalView = true;
        } catch (Throwable t) {
            if (withoutCatchPanics) {
                throw t;
            }
            lastError = t;
        } finally {
            cleanup(renderFinalView, finalModel);
        }

        if (lastError != null) {
            throw new RuntimeException(lastError);
        }
        return finalModel;
    }

    /**
     * Handles cleanup for this component.
     *
     * @param renderFinalView render final view
     * @param finalModel final model
     */
    private void cleanup(boolean renderFinalView, Model finalModel) {
        // stop reading keyboard input
        inputHandler.stop();

        if (renderFinalView) {
            // render final model view before closing
            renderer.write(finalModel.view());
        }
        renderer.showCursor();
        renderer.stop();

        if (mouseSelectionAutoScroller != null) {
            mouseSelectionAutoScroller.stop();
        }

        if (renderer.bracketedPaste()) {
            renderer.disableBracketedPaste();
        }

        if (manageMouseSelectionCursor && mouseSelectionCursorActive) {
            renderer.resetMouseCursor();
        }
        if (hoverTextCursorEnabled && hoverTextCursorActive) {
            renderer.resetMouseCursor();
        }
        if (
            mouseTargetCursorEnabled &&
            currentTargetCursor != null &&
            currentTargetCursor != MouseCursor.DEFAULT
        ) {
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
            // Chain with any existing error to preserve context
            if (lastError != null) {
                e.addSuppressed(lastError);
            }
            throw new RuntimeException(e);
        } finally {
            closeOpenedInput();
        }
    }

    /**
     * Handles handle termination signals for this component.
     */
    private void handleTerminationSignals() {
        if (withoutSignalHandler) {
            return;
        }
        Signals.register("INT", () -> {
            if (ignoreSignals.get()) {
                return;
            }
            commandExecutor.executeIfPresent(
                QuitMessage::new,
                this::send,
                this::sendError
            );
        });
        Signals.register("TERM", () -> {
            if (ignoreSignals.get()) {
                return;
            }
            commandExecutor.executeIfPresent(
                QuitMessage::new,
                this::send,
                this::sendError
            );
        });
    }

    /**
     * Handles handle suspend signals for this component.
     */
    private void handleSuspendSignals() {
        if (withoutSignalHandler) {
            return;
        }
        Signals.register("TSTP", () -> {
            if (ignoreSignals.get()) {
                return;
            }
            commandExecutor.executeIfPresent(
                SuspendMessage::new,
                this::send,
                this::sendError
            );
        });
        Signals.register("CONT", () -> {
            if (ignoreSignals.get()) {
                return;
            }
            commandExecutor.executeIfPresent(
                ResumeMessage::new,
                this::send,
                this::sendError
            );
        });
    }

    /**
     * Handles handle terminal resize for this component.
     */
    private void handleTerminalResize() {
        Signals.register("WINCH", () ->
            commandExecutor.executeIfPresent(
                com.williamcallahan.tui4j.message.CheckWindowSizeMessage::new,
                this::send,
                this::sendError
            )
        );
        commandExecutor.executeIfPresent(
            com.williamcallahan.tui4j.message.CheckWindowSizeMessage::new,
            this::send,
            this::sendError
        );
    }

    /**
     * Handles event loop for this component.
     *
     * @return result
     */
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
                if (filter != null) {
                    msg = filter.apply(currentModel, msg);
                }
                if (msg == null) {
                    continue;
                }
                Message internalMsg = normalizeMessage(msg);
                Message updateMsg = internalMsg;

                if (internalMsg instanceof SequencedMessage seqMsg) {
                    if (seqMsg.sequenceId() < lastHandledSequenceId) {
                        continue;
                    }
                    lastHandledSequenceId = seqMsg.sequenceId();
                    updateMsg = normalizeMessage(seqMsg.message());
                    internalMsg = updateMsg;
                }

                if (handleSystemMessage(internalMsg)) {
                    continue;
                }

                if (internalMsg instanceof QuitMessage) {
                    return currentModel;
                } else if (internalMsg instanceof ErrorMessage errorMessage) {
                    this.lastError = errorMessage.error();
                    return currentModel;
                }

                if (internalMsg instanceof MouseMessage mouseMessage) {
                    if (mouseSelectionAutoScroller != null) {
                        mouseSelectionAutoScroller.onMouse(mouseMessage);
                    }
                    handleMouseClickTracking(mouseMessage);
                    handleMouseSelectionTracking(mouseMessage);
                    handleMouseHoverCursor(mouseMessage);
                    handleMouseTargetCursor(mouseMessage);
                }

                // process internal messages for the renderer
                renderer.handleMessage(internalMsg);

                UpdateResult<? extends Model> updateResult =
                    currentModel.update(updateMsg);

                currentModel = updateResult.model();
                renderer.notifyModelChanged();
                commandExecutor.executeIfPresent(
                    updateResult.command(),
                    this::send,
                    this::sendError
                );

                renderer.write(currentModel.view());
            }
        }
        return currentModel;
    }

    /**
     * Handles handle system message for this component.
     *
     * @param msg msg
     * @return whether ndle system message
     */
    private boolean handleSystemMessage(Message msg) {
        return switch (msg) {
            case ClearScreenMessage ignored -> {
                renderer.clearScreen();
                yield true;
            }
            case EnterAltScreenMessage ignored -> {
                renderer.enterAltScreen();
                yield true;
            }
            case ExitAltScreenMessage ignored -> {
                renderer.exitAltScreen();
                yield true;
            }
            case BatchMessage batchMessage -> {
                handleBatch(batchMessage.commands());
                yield true;
            }
            case SequenceMessage sequenceMessage -> {
                handleSequence(sequenceMessage.commands());
                yield true;
            }
            case com.williamcallahan.tui4j.message.CheckWindowSizeMessage ignored -> {
                commandExecutor.executeIfPresent(
                    this::checkSize,
                    this::send,
                    this::sendError
                );
                yield true;
            }
            case OpenUrlMessage openUrlMessage -> {
                handleOpenUrl(openUrlMessage.url());
                yield true;
            }
            case ExecProcessMessage execProcessMessage -> {
                executeProcess(execProcessMessage);
                yield true;
            }
            case SuspendMessage ignored -> {
                suspend();
                yield true;
            }
            case ResumeMessage ignored -> {
                resume();
                yield true;
            }
            default -> false;
        };
    }

    /**
     * Handles normalize message for this component.
     *
     * @param msg msg
     * @return result
     */
    private Message normalizeMessage(Message msg) {
        if (msg instanceof MessageShim shim) {
            return shim.toMessage();
        }
        return msg;
    }

    /**
     * Handles handle batch for this component.
     *
     * @param commands commands
     */
    private void handleBatch(Command... commands) {
        Arrays.stream(commands).forEach(command ->
            commandExecutor.executeIfPresent(
                command,
                this::send,
                this::sendError
            )
        );
    }

    /**
     * Handles handle sequence for this component.
     *
     * @param commands commands
     */
    private void handleSequence(Command... commands) {
        long sequenceId = ++currentSequenceId;
        Arrays.stream(commands).reduce(
            CompletableFuture.completedFuture(null),
            (CompletableFuture<Void> future, Command command) ->
                future.thenCompose(__ ->
                    commandExecutor.executeIfPresent(
                        command,
                        msg -> send(new SequencedMessage(msg, sequenceId)),
                        this::sendError
                    )
                ),
            (f1, f2) -> f2
        );
    }

    /**
     * Handles handle open url for this component.
     *
     * @param url url
     */
    private void handleOpenUrl(String url) {
        boolean success = UrlOpener.open(url);
        if (!success) {
            logger.log(Level.WARNING, "Failed to open URL: " + url);
        }
    }

    /**
     * Handles handle mouse selection tracking for this component.
     *
     * @param mouseMessage mouse message
     */
    private void handleMouseSelectionTracking(MouseMessage mouseMessage) {
        MouseSelectionUpdate selectionUpdate = mouseSelectionTracker.update(
            mouseMessage
        );

        if (
            extendSelectionOnScroll &&
            selectionUpdate.selectionScrollUpdate() != null
        ) {
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

            if (
                mouseMessage.getAction() == MouseAction.MouseActionMotion ||
                mouseMessage.getAction() == MouseAction.MouseActionPress
            ) {
                setMouseSelectionCursorText();
            }
        }
    }

    /**
     * Handles handle mouse hover cursor for this component.
     *
     * @param mouseMessage mouse message
     */
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
        if (
            mouseMessage.getAction() != MouseAction.MouseActionMotion &&
            mouseMessage.getAction() != MouseAction.MouseActionPress &&
            mouseMessage.getAction() != MouseAction.MouseActionRelease
        ) {
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

    /**
     * Handles mouse cursor changes based on MouseTarget cursor hints.
     *
     * @param mouseMessage mouse message
     */
    private void handleMouseTargetCursor(MouseMessage mouseMessage) {
        if (!mouseTargetCursorEnabled) {
            return;
        }
        if (mouseSelectionTracker.isSelecting() || mouseSelectionCursorActive) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates
            return;
        }
        if (hoverTextCursorActive) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates
            return;
        }
        if (mouseMessage.isWheel()) {
            currentTargetCursor = null; // Clear cache so next motion re-evaluates
            return;
        }
        if (
            mouseMessage.getAction() != MouseAction.MouseActionMotion &&
            mouseMessage.getAction() != MouseAction.MouseActionPress &&
            mouseMessage.getAction() != MouseAction.MouseActionRelease
        ) {
            return;
        }

        MouseTarget target = resolveMouseTarget(mouseMessage);
        MouseCursor desiredCursor = (target != null)
            ? target.cursor()
            : MouseCursor.DEFAULT;

        if (desiredCursor == currentTargetCursor) {
            return;
        }

        switch (desiredCursor) {
            case POINTER -> renderer.setMouseCursorPointer();
            case TEXT -> renderer.setMouseCursorText();
            default -> renderer.resetMouseCursor();
        }
        currentTargetCursor = desiredCursor;
    }

    /**
     * Updates the mouse selection cursor text.
     */
    private void setMouseSelectionCursorText() {
        if (mouseSelectionCursorActive) {
            return;
        }
        renderer.setMouseCursorText();
        mouseSelectionCursorActive = true;
    }

    /**
     * Handles reset mouse selection cursor for this component.
     */
    private void resetMouseSelectionCursor() {
        if (!mouseSelectionCursorActive) {
            return;
        }
        renderer.resetMouseCursor();
        mouseSelectionCursorActive = false;
    }

    /**
     * Handles handle mouse click tracking for this component.
     *
     * @param mouseMessage mouse message
     */
    private void handleMouseClickTracking(MouseMessage mouseMessage) {
        if (!mouseClicksEnabled) {
            return;
        }
        MouseTarget target = resolveMouseTarget(mouseMessage);
        MouseClickMessage clickMessage = mouseClickTracker.handle(
            mouseMessage,
            target
        );
        if (clickMessage != null) {
            send(clickMessage);
        }
    }

    /**
     * Handles resolve mouse target for this component.
     *
     * @param mouseMessage mouse message
     * @return result
     */
    private MouseTarget resolveMouseTarget(MouseMessage mouseMessage) {
        if (!(currentModel instanceof MouseTargetProvider provider)) {
            return null;
        }
        return MouseTargets.hitTest(
            provider.mouseTargets(),
            mouseMessage.column(),
            mouseMessage.row()
        );
    }

    /**
     * Handles check size for this component.
     *
     * @return result
     */
    private Message checkSize() {
        Size size = terminal.getSize();
        return new WindowSizeMessage(size.getColumns(), size.getRows());
    }

    /**
     * Handles send error for this component.
     *
     * @param error error
     */
    private void sendError(Throwable error) {
        send(new ErrorMessage(error));
    }

    /**
     * Handles send for this component.
     *
     * @param msg msg
     */
    public void send(Message msg) {
        if (isRunning.get() && msg != null) {
            messageQueue.offer(msg);
        }
    }

    /**
     * Reports whether running.
     *
     * @return whether running
     */
    public boolean isRunning() {
        return isRunning.get();
    }

    /**
     * Handles wait for init for this component.
     */
    public void waitForInit() {
        try {
            initLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles disable mouse for this component.
     */
    private void disableMouse() {
        renderer.disableMouseSGRMode();
        renderer.disableMouseNormalTracking();
        renderer.disableMouseCellMotion();
        renderer.disableMouseAllMotion();
    }

    /**
     * Handles execute process for this component.
     *
     * @param execProcessMessage exec process message
     */
    private void executeProcess(ExecProcessMessage execProcessMessage) {
        // Run synchronously to block the event loop, matching Bubble Tea's behavior
        Process process = execProcessMessage.process();
        BiConsumer<Integer, byte[]> outputHandler =
            execProcessMessage.outputHandler();
        BiConsumer<Integer, byte[]> errorHandler =
            execProcessMessage.errorHandler();

        suspend();

        try {
            // Drain stdout/stderr concurrently to prevent deadlock from filled buffers
            CompletableFuture<byte[]> stdoutFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return process.getInputStream().readAllBytes();
                    } catch (IOException e) {
                        throw new UncheckedIOException(
                            "Failed to read stdout",
                            e
                        );
                    }
                });
            CompletableFuture<byte[]> stderrFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return process.getErrorStream().readAllBytes();
                    } catch (IOException e) {
                        throw new UncheckedIOException(
                            "Failed to read stderr",
                            e
                        );
                    }
                });

            int exitCode = process.waitFor();

            if (outputHandler != null) {
                byte[] output = stdoutFuture.get();
                outputHandler.accept(exitCode, output);
            }
            if (errorHandler != null) {
                byte[] error = stderrFuture.get();
                errorHandler.accept(exitCode, error);
            }

            send(new ExecCompletedMessage(exitCode, null));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            send(new ExecCompletedMessage(-1, e));
        } catch (java.util.concurrent.ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            logger.log(Level.WARNING, "Error reading process streams", cause);
            send(new ExecCompletedMessage(-1, cause));
        } finally {
            resume(); // Restore terminal and renderer
        }
    }

    /**
     * Handles suspend for this component.
     */
    private void suspend() {
        if (isSuspended) {
            return;
        }
        isSuspended = true;
        renderer.showCursor();
        renderer.pause();
        if (terminalIsTty) {
            terminal.pause();
        }
    }

    /**
     * Handles resume for this component.
     */
    private void resume() {
        if (!isSuspended) {
            return;
        }
        if (terminalIsTty) {
            terminal.resume();
        }
        renderer.resume();
        renderer.hideCursor();
        renderer.write(currentModel.view());
        isSuspended = false;
    }

    /**
     * Handles apply startup options for this component.
     */
    private void applyStartupOptions() {
        if (enableAltScreen && !renderer.altScreen()) {
            renderer.enterAltScreen();
        }
        if (!withoutBracketedPaste) {
            renderer.enableBracketedPaste();
        }
        if (enableMouseCellMotion) {
            renderer.enableMouseCellMotion();
            renderer.enableMouseSGRMode();
        } else if (enableMouseAllMotion) {
            renderer.enableMouseAllMotion();
            renderer.enableMouseSGRMode();
        }
        if (enableReportFocus && !renderer.reportFocus()) {
            renderer.enableReportFocus();
        }
    }

    /**
     * Handles install cancel signal for this component.
     */
    private void installCancelSignal() {
        if (cancelSignal == null) {
            return;
        }
        cancelSignal.whenComplete((result, error) -> send(new QuitMessage()));
    }

    /**
     * Handles apply selection auto scroll config for this component.
     */
    private void applySelectionAutoScrollConfig() {
        if (!selectionAutoScrollEnabled || mouseSelectionAutoScroller == null) {
            return;
        }
        mouseSelectionAutoScroller.configure(
            selectionAutoScrollEdgeRows,
            selectionAutoScrollIntervalMs
        );
    }

    /**
     * Handles normalize fps for this component.
     *
     * @param value value
     * @return result
     */
    private int normalizeFps(int value) {
        if (value < 1) {
            return DEFAULT_FPS;
        }
        return Math.min(value, MAX_FPS);
    }

    /**
     * Handles resolve input stream for this component.
     *
     * @return result
     */
    private InputStream resolveInputStream() throws IOException {
        InputStream resolved = input;
        if (useInputTTY) {
            resolved = openInputTTY();
        }
        if (resolved == null) {
            inputDisabled = true;
            return System.in;
        }
        return resolved;
    }

    /**
     * Reports whether system terminal.
     *
     * @param resolvedInput resolved input
     * @param resolvedOutput resolved output
     * @return whether system terminal
     */
    private boolean isSystemTerminal(
        InputStream resolvedInput,
        OutputStream resolvedOutput
    ) {
        if (useInputTTY) {
            return false;
        }
        boolean inputIsSystem = resolvedInput == System.in;
        boolean outputIsSystem =
            resolvedOutput == System.out || resolvedOutput == System.err;
        return inputIsSystem && outputIsSystem;
    }

    /**
     * Reports whether a dumb terminal should be used for the resolved streams.
     *
     * @param resolvedInput resolved input
     * @param resolvedOutput resolved output
     * @return whether to use a dumb terminal
     */
    private boolean shouldUseDumbTerminal(
        InputStream resolvedInput,
        OutputStream resolvedOutput
    ) {
        if (useInputTTY) {
            return false;
        }
        return !isSystemTerminal(resolvedInput, resolvedOutput);
    }

    /**
     * Reports whether the terminal is a dumb terminal.
     *
     * @param terminal terminal
     * @return whether dumb
     */
    private boolean isDumbTerminal(Terminal terminal) {
        String type = terminal.getType();
        return type != null && type.startsWith("dumb");
    }

    /**
     * Handles open input tty for this component.
     *
     * @return result
     */
    private InputStream openInputTTY() throws IOException {
        InputStream tty = null;
        String osName = System.getProperty("os.name", "").toLowerCase(
            Locale.ROOT
        );
        if (osName.contains("win")) {
            tty = new FileInputStream("CONIN$");
        } else {
            tty = new FileInputStream("/dev/tty");
        }
        openedInput = tty;
        return tty;
    }

    /**
     * Handles close opened input for this component.
     */
    private void closeOpenedInput() {
        if (openedInput == null || openedInput == System.in) {
            return;
        }
        try {
            openedInput.close();
        } catch (IOException ignored) {}
    }

    /**
     * Updates the output.
     *
     * @param output output
     */
    void setOutput(OutputStream output) {
        this.output = output;
    }

    /**
     * Updates the input.
     *
     * @param input input
     */
    void setInput(InputStream input) {
        this.input = input;
        this.inputDisabled = input == null;
    }

    /**
     * Updates the input tty.
     *
     * @param useInputTTY use input tty
     */
    void setInputTTY(boolean useInputTTY) {
        this.useInputTTY = useInputTTY;
    }

    /**
     * Updates the environment.
     *
     * @param environment environment
     */
    void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    /**
     * Updates the without signal handler.
     *
     * @param withoutSignalHandler without signal handler
     */
    void setWithoutSignalHandler(boolean withoutSignalHandler) {
        this.withoutSignalHandler = withoutSignalHandler;
    }

    /**
     * Updates the without catch panics.
     *
     * @param withoutCatchPanics without catch panics
     */
    void setWithoutCatchPanics(boolean withoutCatchPanics) {
        this.withoutCatchPanics = withoutCatchPanics;
    }

    /**
     * Updates the ignore signals.
     *
     * @param ignoreSignals ignore signals
     */
    void setIgnoreSignals(boolean ignoreSignals) {
        this.ignoreSignals.set(ignoreSignals);
    }

    /**
     * Updates the without bracketed paste.
     *
     * @param withoutBracketedPaste without bracketed paste
     */
    void setWithoutBracketedPaste(boolean withoutBracketedPaste) {
        this.withoutBracketedPaste = withoutBracketedPaste;
    }

    /**
     * Updates the without renderer.
     *
     * @param withoutRenderer without renderer
     */
    void setWithoutRenderer(boolean withoutRenderer) {
        this.withoutRenderer = withoutRenderer;
    }

    /**
     * Enables ANSI sequence compression to reduce output size.
     * <p>
     * Accepted for API compatibility but has no effect in tui4j.
     *
     * @param ansiCompressor whether to enable ANSI compression (ignored)
     * @see <a href="https://pkg.go.dev/github.com/charmbracelet/bubbletea#WithANSICompressor">
     *      bubbletea.WithANSICompressor (Go docs)</a>
     */
    void setAnsiCompressor(boolean ansiCompressor) {
        setAnsiCompressorInternal(ansiCompressor);
    }

    /**
     * Updates the ansi compressor internal.
     *
     * @param ansiCompressor ansi compressor
     */
    void setAnsiCompressorInternal(boolean ansiCompressor) {
        if (this.ansiCompressor == ansiCompressor) {
            return;
        }
        this.ansiCompressor = ansiCompressor;
    }

    /**
     * Updates the filter.
     *
     * @param filter filter
     */
    void setFilter(BiFunction<Model, Message, Message> filter) {
        this.filter = filter;
    }

    /**
     * Updates the fps.
     *
     * @param fps fps
     */
    void setFps(int fps) {
        this.fps = fps;
    }

    /**
     * Updates the cancel signal.
     *
     * @param cancelSignal cancel signal
     */
    void setCancelSignal(CompletableFuture<?> cancelSignal) {
        this.cancelSignal = cancelSignal;
    }
}
