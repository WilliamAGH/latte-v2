package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.InputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.render.NilRenderer;
import com.williamcallahan.tui4j.compat.bubbletea.render.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.render.StandardRenderer;
import com.williamcallahan.tui4j.input.MouseClickMessage;
import com.williamcallahan.tui4j.input.MouseCursor;
import com.williamcallahan.tui4j.runtime.CommandExecutor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

/**
 * Runs the TUI event loop and manages terminal IO.
 * <p>
 * Port of Bubble Tea's {@code Program} (tea.go).
 * Manages the lifecycle of a TUI application, including terminal initialization,
 * event polling, and rendering loops.
 * <p>
 * Bubble Tea: tea.go, tea_init.go.
 */
public class Program {

    private static final Logger logger = Logger.getLogger(
        Program.class.getName()
    );

    private final ProgramConfiguration config = new ProgramConfiguration();

    static {
        try {
            com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer().hasDarkBackground();
        } catch (Exception e) {
            // Best-effort parity with bubbletea/tea_init.go.
            logger.log(
                Level.FINE,
                "Dark-background probe failed during static init",
                e
            );
        }
    }

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final CountDownLatch initLatch = new CountDownLatch(1);
    private final BlockingQueue<Message> messageQueue =
        new LinkedBlockingQueue<>();
    private final CommandExecutor commandExecutor;
    private final AtomicReference<Model> currentModel;

    private Throwable lastError;
    private Terminal terminal;
    private Renderer renderer;
    private InputHandler inputHandler;

    private ProgramTerminal programTerminal;
    private ProgramMouseHandler mouseHandler;
    private ProgramProcessExecutor processExecutor;

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
        this.currentModel = new AtomicReference<>(initialModel);
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
            programTerminal = ProgramTerminal.initialize(config, this::send);
            terminal = programTerminal.terminal();
            inputHandler = programTerminal.inputHandler();

            if (config.isWithoutRenderer()) {
                renderer = new NilRenderer();
            } else {
                renderer = new StandardRenderer(
                    terminal,
                    config.normalizeFps(config.fps())
                );
            }

            mouseHandler = new ProgramMouseHandler(
                config,
                terminal::getHeight,
                this::send
            );
            processExecutor = new ProgramProcessExecutor(
                terminal,
                programTerminal.terminalIsTty(),
                renderer,
                currentModel::get,
                this::send
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to initialize terminal", e);
        }
    }

    /**
     * Handles with alt screen for this component.
     *
     * @return result
     */
    public Program withAltScreen() {
        config.setEnableAltScreen(true);
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
        config.setEnableReportFocus(true);
        if (renderer != null) {
            renderer.enableReportFocus();
        }
        return this;
    }

    /**
     * Enables Kitty keyboard protocol for enhanced key reporting.
     * <p>
     * This enables the terminal to report modifier keys on Enter (Shift+Enter,
     * Ctrl+Enter) via CSI-u sequences. Supported by Kitty, Ghostty, WezTerm, and
     * other modern terminals.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     * @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/">Kitty
     *      Keyboard Protocol</a>
     */
    public Program withKittyKeyboard() {
        config.setEnableKittyKeyboard(true);
        if (renderer != null) {
            renderer.enableKittyKeyboard();
        }
        return this;
    }

    /**
     * Handles with mouse all motion for this component.
     *
     * @return result
     */
    public Program withMouseAllMotion() {
        config.setEnableMouseAllMotion(true);
        config.setEnableMouseCellMotion(false);
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
        config.setEnableMouseCellMotion(true);
        config.setEnableMouseAllMotion(false);
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
        config.setExtendSelectionOnScroll(true);
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
        // must also preserve selection during scroll to avoid breaking the user's
        // selection state.
        config.setExtendSelectionOnScroll(true);
        config.setSelectionAutoScrollEnabled(true);
        if (mouseHandler != null) {
            mouseHandler.enableSelectionAutoScroll();
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
        config.setExtendSelectionOnScroll(true);
        config.setSelectionAutoScrollEnabled(true);
        config.setSelectionAutoScrollEdgeRows(edgeRows);
        config.setSelectionAutoScrollIntervalMs(intervalMs);
        if (mouseHandler != null) {
            mouseHandler.configureSelectionAutoScroll(edgeRows, intervalMs);
        }
        return this;
    }

    /**
     * Manage the mouse cursor during selection (OSC 22).
     *
     * @return this program for chaining
     */
    public Program withMouseSelectionCursor() {
        config.setManageMouseSelectionCursor(true);
        return this;
    }

    /**
     * Manage the mouse cursor when hovering non-whitespace text (OSC 22).
     * Requires mouse motion events (e.g. {@link #withMouseAllMotion()}).
     *
     * @return this program for chaining
     */
    public Program withMouseHoverTextCursor() {
        config.setHoverTextCursorEnabled(true);
        return this;
    }

    /**
     * Manage the mouse cursor based on MouseTarget cursor hints (OSC 22).
     * When hovering a target with {@link MouseCursor#POINTER}, shows pointer
     * cursor. Requires mouse motion events (e.g. {@link #withMouseAllMotion()}).
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     */
    public Program withMouseTargetCursor() {
        config.setMouseTargetCursorEnabled(true);
        return this;
    }

    /**
     * When enabled, emits {@link MouseClickMessage} on press/release clicks.
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @return this program for chaining
     */
    public Program withMouseClicks() {
        config.setMouseClicksEnabled(true);
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
     * tui4j extension; no Bubble Tea equivalent because the Java {@code run()} API
     * is void.
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

        ProgramSignals.registerAll(
            config,
            commandExecutor,
            this::send,
            this::sendError
        );
        if (mouseHandler != null) {
            mouseHandler.startAutoScroll();
        }

        // start reading keyboard input
        inputHandler.start();

        applyStartupOptions();

        // starting renderer
        renderer.hideCursor();
        renderer.start();
        installCancelSignal();

        Model finalModel = currentModel.get();
        boolean renderFinalView = false;

        try {
            // execute init command
            Command initCommand = currentModel.get().init();
            commandExecutor
                .executeIfPresent(initCommand, this::send, this::sendError)
                .thenRun(initLatch::countDown);

            // render the initial view
            renderer.write(currentModel.get().view());

            // run event loop
            ProgramEventLoop.Result loopResult = new ProgramEventLoop(
                isRunning,
                messageQueue,
                config,
                renderer,
                terminal,
                currentModel,
                commandExecutor,
                mouseHandler,
                processExecutor,
                this::send,
                this::sendError
            ).run();

            finalModel = loopResult.finalModel();
            if (loopResult.error() != null) {
                lastError = loopResult.error();
            }
            renderFinalView = true;
        } catch (Exception e) {
            if (config.isWithoutCatchPanics()) {
                throw e;
            }
            lastError = e;
        } finally {
            cleanup(renderFinalView, finalModel);
        }

        if (lastError != null) {
            throw new ProgramException(lastError);
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

        if (mouseHandler != null) {
            mouseHandler.stopAutoScroll();
        }

        if (renderer.bracketedPaste()) {
            renderer.disableBracketedPaste();
        }

        if (mouseHandler != null) {
            mouseHandler.cleanupCursors(renderer);
        }

        // disabling mouse support
        disableMouse();

        if (renderer.reportFocus()) {
            renderer.disableReportFocus();
        }

        if (renderer.kittyKeyboard()) {
            renderer.disableKittyKeyboard();
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
            throw new UncheckedIOException(e);
        } finally {
            if (programTerminal != null) {
                programTerminal.closeOpenedInput();
            }
        }
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
        if (isRunning.get() && msg != null && !messageQueue.offer(msg)) {
            logger.log(Level.WARNING, "Failed to enqueue message: {0}", msg);
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
            Thread.currentThread().interrupt();
            throw new ProgramException(e);
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
     * Handles apply startup options for this component.
     */
    private void applyStartupOptions() {
        if (config.isEnableAltScreen() && !renderer.altScreen()) {
            renderer.enterAltScreen();
        }
        if (!config.isWithoutBracketedPaste()) {
            renderer.enableBracketedPaste();
        }
        if (config.isEnableMouseCellMotion()) {
            renderer.enableMouseCellMotion();
            renderer.enableMouseSGRMode();
        } else if (config.isEnableMouseAllMotion()) {
            renderer.enableMouseAllMotion();
            renderer.enableMouseSGRMode();
        }
        if (config.isEnableReportFocus() && !renderer.reportFocus()) {
            renderer.enableReportFocus();
        }
        if (config.isEnableKittyKeyboard() && !renderer.kittyKeyboard()) {
            renderer.enableKittyKeyboard();
        }
    }

    /**
     * Handles install cancel signal for this component.
     */
    private void installCancelSignal() {
        if (config.cancelSignal() == null) {
            return;
        }
        config.cancelSignal().whenComplete((result, error) ->
            send(new QuitMessage())
        );
    }

    /**
     * Updates the output.
     *
     * @param output output
     */
    void setOutput(OutputStream output) {
        config.setOutput(output);
    }

    /**
     * Updates the input.
     *
     * @param input input
     */
    void setInput(InputStream input) {
        config.setInput(input);
    }

    /**
     * Updates the input tty.
     *
     * @param useInputTTY use input tty
     */
    void setInputTTY(boolean useInputTTY) {
        config.setInputTTY(useInputTTY);
    }

    /**
     * Updates the environment.
     *
     * @param environment environment
     */
    void setEnvironment(List<String> environment) {
        config.setEnvironment(environment);
    }

    /**
     * Updates the without signal handler.
     *
     * @param withoutSignalHandler without signal handler
     */
    void setWithoutSignalHandler(boolean withoutSignalHandler) {
        config.setWithoutSignalHandler(withoutSignalHandler);
    }

    /**
     * Updates the without catch panics.
     *
     * @param withoutCatchPanics without catch panics
     */
    void setWithoutCatchPanics(boolean withoutCatchPanics) {
        config.setWithoutCatchPanics(withoutCatchPanics);
    }

    /**
     * Updates the ignore signals.
     *
     * @param ignoreSignals ignore signals
     */
    void setIgnoreSignals(boolean ignoreSignals) {
        config.setIgnoreSignals(ignoreSignals);
    }

    /**
     * Updates the without bracketed paste.
     *
     * @param withoutBracketedPaste without bracketed paste
     */
    void setWithoutBracketedPaste(boolean withoutBracketedPaste) {
        config.setWithoutBracketedPaste(withoutBracketedPaste);
    }

    /**
     * Updates the without renderer.
     *
     * @param withoutRenderer without renderer
     */
    void setWithoutRenderer(boolean withoutRenderer) {
        config.setWithoutRenderer(withoutRenderer);
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
        config.setAnsiCompressorInternal(ansiCompressor);
    }

    /**
     * Updates the ansi compressor internal.
     *
     * @param ansiCompressor ansi compressor
     */
    void setAnsiCompressorInternal(boolean ansiCompressor) {
        config.setAnsiCompressorInternal(ansiCompressor);
    }

    /**
     * Updates the filter.
     *
     * @param filter filter
     */
    void setFilter(BiFunction<Model, Message, Message> filter) {
        config.setFilter(filter);
    }

    /**
     * Updates the fps.
     *
     * @param fps fps
     */
    void setFps(int fps) {
        config.setFps(fps);
    }

    /**
     * Updates the cancel signal.
     *
     * @param cancelSignal cancel signal
     */
    void setCancelSignal(CompletableFuture<?> cancelSignal) {
        config.setCancelSignal(cancelSignal);
    }
}

