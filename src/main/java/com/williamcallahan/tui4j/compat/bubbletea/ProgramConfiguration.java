package com.williamcallahan.tui4j.compat.bubbletea;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * Pre-run configuration state for the {@link Program} lifecycle.
 * <p>
 * Holds every option that can be set before {@code Program.run()} is called.
 * Fields are populated via {@link ProgramOption#apply(Program)} during
 * construction; Program delegates its setters and reads to this class.
 * <p>
 * Upstream: bubbletea/options.go
 *
 * @see Program
 * @see ProgramOption
 */
class ProgramConfiguration {

    static final int DEFAULT_FPS = 60;
    static final int MAX_FPS = 120;

    private int fps = DEFAULT_FPS;
    private boolean withoutSignalHandler;
    private boolean withoutCatchPanics;
    private boolean withoutBracketedPaste;
    private boolean withoutRenderer;
    private boolean ansiCompressor;
    private boolean enableAltScreen;
    private boolean enableMouseAllMotion;
    private boolean enableMouseCellMotion;
    private boolean enableReportFocus;
    private boolean enableKittyKeyboard;
    private BiFunction<Model, Message, Message> filter;
    private CompletableFuture<?> cancelSignal;
    private InputStream input = System.in;
    private OutputStream output = System.out;
    private boolean inputDisabled;
    private boolean useInputTTY;
    private List<String> environment;
    private final AtomicBoolean ignoreSignals = new AtomicBoolean(false);
    private boolean extendSelectionOnScroll;
    private boolean manageMouseSelectionCursor;
    private boolean hoverTextCursorEnabled;
    private boolean mouseTargetCursorEnabled;
    private boolean mouseClicksEnabled;
    private boolean selectionAutoScrollEnabled;
    private int selectionAutoScrollEdgeRows = 1;
    private int selectionAutoScrollIntervalMs = 50;

    // -- Getters (package-private) --

    int fps() {
        return fps;
    }

    boolean isWithoutSignalHandler() {
        return withoutSignalHandler;
    }

    boolean isWithoutCatchPanics() {
        return withoutCatchPanics;
    }

    boolean isWithoutBracketedPaste() {
        return withoutBracketedPaste;
    }

    boolean isWithoutRenderer() {
        return withoutRenderer;
    }

    boolean isAnsiCompressor() {
        return ansiCompressor;
    }

    boolean isEnableAltScreen() {
        return enableAltScreen;
    }

    boolean isEnableMouseAllMotion() {
        return enableMouseAllMotion;
    }

    boolean isEnableMouseCellMotion() {
        return enableMouseCellMotion;
    }

    boolean isEnableReportFocus() {
        return enableReportFocus;
    }

    boolean isEnableKittyKeyboard() {
        return enableKittyKeyboard;
    }

    BiFunction<Model, Message, Message> filter() {
        return filter;
    }

    CompletableFuture<?> cancelSignal() {
        return cancelSignal;
    }

    InputStream input() {
        return input;
    }

    OutputStream output() {
        return output;
    }

    boolean isInputDisabled() {
        return inputDisabled;
    }

    boolean isUseInputTTY() {
        return useInputTTY;
    }

    List<String> environment() {
        return environment;
    }

    AtomicBoolean ignoreSignals() {
        return ignoreSignals;
    }

    boolean isExtendSelectionOnScroll() {
        return extendSelectionOnScroll;
    }

    boolean isManageMouseSelectionCursor() {
        return manageMouseSelectionCursor;
    }

    boolean isHoverTextCursorEnabled() {
        return hoverTextCursorEnabled;
    }

    boolean isMouseTargetCursorEnabled() {
        return mouseTargetCursorEnabled;
    }

    boolean isMouseClicksEnabled() {
        return mouseClicksEnabled;
    }

    boolean isSelectionAutoScrollEnabled() {
        return selectionAutoScrollEnabled;
    }

    int selectionAutoScrollEdgeRows() {
        return selectionAutoScrollEdgeRows;
    }

    int selectionAutoScrollIntervalMs() {
        return selectionAutoScrollIntervalMs;
    }

    // -- Setters (package-private) --

    /** Configures the target frames per second for the renderer. */
    void setFps(int fps) {
        this.fps = fps;
    }

    /** Disables the default signal handler for INT/TERM/TSTP/CONT. */
    void setWithoutSignalHandler(boolean withoutSignalHandler) {
        this.withoutSignalHandler = withoutSignalHandler;
    }

    /** Disables panic (exception) catching so errors propagate immediately. */
    void setWithoutCatchPanics(boolean withoutCatchPanics) {
        this.withoutCatchPanics = withoutCatchPanics;
    }

    /** Disables bracketed paste mode in the terminal. */
    void setWithoutBracketedPaste(boolean withoutBracketedPaste) {
        this.withoutBracketedPaste = withoutBracketedPaste;
    }

    /** Replaces the standard renderer with a no-op renderer. */
    void setWithoutRenderer(boolean withoutRenderer) {
        this.withoutRenderer = withoutRenderer;
    }

    /**
     * Enables or disables ANSI sequence compression (no-op in tui4j).
     * <p>
     * Skips the write when the value is unchanged to match upstream behavior.
     */
    void setAnsiCompressorInternal(boolean ansiCompressor) {
        if (this.ansiCompressor == ansiCompressor) {
            return;
        }
        this.ansiCompressor = ansiCompressor;
    }

    /** Enables the alternate screen buffer on startup. */
    void setEnableAltScreen(boolean enableAltScreen) {
        this.enableAltScreen = enableAltScreen;
    }

    /** Enables mouse tracking for all motion events. */
    void setEnableMouseAllMotion(boolean enableMouseAllMotion) {
        this.enableMouseAllMotion = enableMouseAllMotion;
    }

    /** Enables mouse tracking for cell-based motion events. */
    void setEnableMouseCellMotion(boolean enableMouseCellMotion) {
        this.enableMouseCellMotion = enableMouseCellMotion;
    }

    /** Enables terminal focus/blur reporting. */
    void setEnableReportFocus(boolean enableReportFocus) {
        this.enableReportFocus = enableReportFocus;
    }

    /** Enables Kitty keyboard protocol for enhanced key reporting. */
    void setEnableKittyKeyboard(boolean enableKittyKeyboard) {
        this.enableKittyKeyboard = enableKittyKeyboard;
    }

    /** Configures a message filter applied before the model's update method. */
    void setFilter(BiFunction<Model, Message, Message> filter) {
        this.filter = filter;
    }

    /** Configures a cancellation signal that triggers program quit when completed. */
    void setCancelSignal(CompletableFuture<?> cancelSignal) {
        this.cancelSignal = cancelSignal;
    }

    /** Configures the input stream for terminal reads. */
    void setInput(InputStream input) {
        this.input = input;
        this.inputDisabled = input == null;
    }

    /** Configures the output stream for terminal writes. */
    void setOutput(OutputStream output) {
        this.output = output;
    }

    /** Marks input as disabled (set automatically when input is null). */
    void setInputDisabled(boolean inputDisabled) {
        this.inputDisabled = inputDisabled;
    }

    /** Opens /dev/tty (or CONIN$ on Windows) as the input source. */
    void setInputTTY(boolean useInputTTY) {
        this.useInputTTY = useInputTTY;
    }

    /** Configures the environment variables passed to lipgloss for capability detection. */
    void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    /** When true, INT/TERM/TSTP/CONT signal handlers are suppressed. */
    void setIgnoreSignals(boolean ignoreSignals) {
        this.ignoreSignals.set(ignoreSignals);
    }

    /** Extends the active selection when a wheel event occurs during selection. */
    void setExtendSelectionOnScroll(boolean extendSelectionOnScroll) {
        this.extendSelectionOnScroll = extendSelectionOnScroll;
    }

    /** Manages the mouse cursor shape during text selection. */
    void setManageMouseSelectionCursor(boolean manageMouseSelectionCursor) {
        this.manageMouseSelectionCursor = manageMouseSelectionCursor;
    }

    /** Enables a text cursor when hovering non-whitespace content. */
    void setHoverTextCursorEnabled(boolean hoverTextCursorEnabled) {
        this.hoverTextCursorEnabled = hoverTextCursorEnabled;
    }

    /** Enables cursor changes based on MouseTarget cursor hints. */
    void setMouseTargetCursorEnabled(boolean mouseTargetCursorEnabled) {
        this.mouseTargetCursorEnabled = mouseTargetCursorEnabled;
    }

    /** Enables emission of MouseClickMessage on press/release. */
    void setMouseClicksEnabled(boolean mouseClicksEnabled) {
        this.mouseClicksEnabled = mouseClicksEnabled;
    }

    /** Enables auto-scroll when the mouse is near the terminal edge during selection. */
    void setSelectionAutoScrollEnabled(boolean selectionAutoScrollEnabled) {
        this.selectionAutoScrollEnabled = selectionAutoScrollEnabled;
    }

    /** Configures the number of rows from the edge that trigger auto-scroll. */
    void setSelectionAutoScrollEdgeRows(int selectionAutoScrollEdgeRows) {
        this.selectionAutoScrollEdgeRows = selectionAutoScrollEdgeRows;
    }

    /** Configures the interval in milliseconds between auto-scroll steps. */
    void setSelectionAutoScrollIntervalMs(int selectionAutoScrollIntervalMs) {
        this.selectionAutoScrollIntervalMs = selectionAutoScrollIntervalMs;
    }

    /**
     * Clamps an FPS value to the valid range [1..{@value MAX_FPS}].
     * Values below 1 fall back to {@value DEFAULT_FPS}.
     *
     * @param value raw FPS value
     * @return normalized FPS
     */
    int normalizeFps(int value) {
        if (value < 1) {
            return DEFAULT_FPS;
        }
        return Math.min(value, MAX_FPS);
    }
}
