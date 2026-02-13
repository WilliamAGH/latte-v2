package com.williamcallahan.tui4j.compat.bubbletea;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(Program.class.getName());

    // Kept as a field on Program for ProgramOption compatibility and tests that
    // assert option behavior via reflection.
    private final ProgramConfiguration config = new ProgramConfiguration();

    private final ProgramCore core;

    static {
        try {
            com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer().hasDarkBackground();
        } catch (Exception e) {
            // Best-effort parity with bubbletea/tea_init.go.
            logger.log(Level.FINE, "Dark-background probe failed during static init", e);
        }
    }

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
        this.core = new ProgramCore(initialModel, config);
        if (options != null) {
            for (ProgramOption option : options) {
                if (option != null) {
                    option.apply(this);
                }
            }
        }
        core.initializeTerminal();
    }

    /**
     * Enables the alternate screen buffer.
     * @return this program for chaining
     */
    public Program withAltScreen() {
        core.withAltScreen();
        return this;
    }

    /**
     * Enables focus/blur reporting.
     * @return this program for chaining
     */
    public Program withReportFocus() {
        core.withReportFocus();
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
        core.withKittyKeyboard();
        return this;
    }

    /**
     * Enables mouse tracking for all motion events.
     * @return this program for chaining
     */
    public Program withMouseAllMotion() {
        core.withMouseAllMotion();
        return this;
    }

    /**
     * Enables mouse tracking for cell motion events.
     * @return this program for chaining
     */
    public Program withMouseCellMotion() {
        core.withMouseCellMotion();
        return this;
    }

    /**
     * Extends selection while scrolling.
     * @return this program for chaining
     */
    public Program withMouseSelectionExtendOnScroll() {
        core.withMouseSelectionExtendOnScroll();
        return this;
    }

    /**
     * Enables selection auto-scroll.
     * @return this program for chaining
     */
    public Program withMouseSelectionAutoScroll() {
        core.withMouseSelectionAutoScroll();
        return this;
    }

    /**
     * Configures selection auto-scroll.
     *
     * @param edgeRows number of rows from the edge that trigger auto-scroll
     * @param intervalMs interval in milliseconds between auto-scroll steps
     * @return this program for chaining
     */
    public Program withMouseSelectionAutoScroll(int edgeRows, int intervalMs) {
        core.withMouseSelectionAutoScroll(edgeRows, intervalMs);
        return this;
    }

    /**
     * Enables selection cursor management.
     * @return this program for chaining
     */
    public Program withMouseSelectionCursor() {
        core.withMouseSelectionCursor();
        return this;
    }

    /**
     * Enables hover-text cursor management.
     * @return this program for chaining
     */
    public Program withMouseHoverTextCursor() {
        core.withMouseHoverTextCursor();
        return this;
    }

    /**
     * Enables mouse target cursor hints.
     * @return this program for chaining
     */
    public Program withMouseTargetCursor() {
        core.withMouseTargetCursor();
        return this;
    }

    /**
     * Enables mouse click messages.
     * @return this program for chaining
     */
    public Program withMouseClicks() {
        core.withMouseClicks();
        return this;
    }

    /**
     * Blocks the calling thread, enters raw mode, and starts the event loop.
     * Takes control of the terminal until the model returns a {@link QuitMessage}.
     */
    public void run() {
        core.run();
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
        return core.runWithFinalModel();
    }

    /**
     * Enqueues a message while the program is running.
     * @param msg message to enqueue
     */
    public void send(Message msg) {
        core.send(msg);
    }

    /**
     * Returns whether the program is running.
     * @return whether running
     */
    public boolean isRunning() {
        return core.isRunning();
    }

    /** Blocks until the model init command has completed. */
    public void waitForInit() {
        core.waitForInit();
    }

    /**
     * Sets the output stream.
     * @param output output stream
     */
    void setOutput(OutputStream output) {
        config.setOutput(output);
    }

    /**
     * Sets the input stream.
     * @param input input stream
     */
    void setInput(InputStream input) {
        config.setInput(input);
    }

    /**
     * Sets whether to use an input TTY.
     * @param useInputTTY whether to use input TTY
     */
    void setInputTTY(boolean useInputTTY) {
        config.setInputTTY(useInputTTY);
    }

    /**
     * Sets environment variables.
     * @param environment environment variables
     */
    void setEnvironment(List<String> environment) {
        config.setEnvironment(environment);
    }

    /**
     * Disables the signal handler.
     * @param withoutSignalHandler whether to disable the signal handler
     */
    void setWithoutSignalHandler(boolean withoutSignalHandler) {
        config.setWithoutSignalHandler(withoutSignalHandler);
    }

    /**
     * Disables panic catching.
     * @param withoutCatchPanics whether to disable panic catching
     */
    void setWithoutCatchPanics(boolean withoutCatchPanics) {
        config.setWithoutCatchPanics(withoutCatchPanics);
    }

    /**
     * Enables ignoring signals.
     * @param ignoreSignals whether to ignore signals
     */
    void setIgnoreSignals(boolean ignoreSignals) {
        config.setIgnoreSignals(ignoreSignals);
    }

    /**
     * Disables bracketed paste.
     * @param withoutBracketedPaste whether to disable bracketed paste
     */
    void setWithoutBracketedPaste(boolean withoutBracketedPaste) {
        config.setWithoutBracketedPaste(withoutBracketedPaste);
    }

    /**
     * Disables the renderer.
     * @param withoutRenderer whether to disable the renderer
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
     * Sets ANSI compression (ignored).
     * @param ansiCompressor whether to enable ANSI compression (ignored)
     */
    void setAnsiCompressorInternal(boolean ansiCompressor) {
        config.setAnsiCompressorInternal(ansiCompressor);
    }

    /**
     * Sets a message filter.
     * @param filter message filter function
     */
    void setFilter(BiFunction<Model, Message, Message> filter) {
        config.setFilter(filter);
    }

    /**
     * Sets frames per second.
     * @param fps frames per second
     */
    void setFps(int fps) {
        config.setFps(fps);
    }

    /**
     * Sets the cancel signal.
     * @param cancelSignal cancel signal
     */
    void setCancelSignal(CompletableFuture<?> cancelSignal) {
        config.setCancelSignal(cancelSignal);
    }
}
