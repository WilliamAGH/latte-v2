package com.williamcallahan.tui4j.compat.bubbletea.render;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

/**
 * Core interface for rendering the TUI and controlling terminal modes.
 * <p>
 * Port of Bubble Tea's `Renderer` interface (renderer.go).
 * Upstream: bubbletea/renderer.go
 * Handles the actual output to the terminal, including screen clearing,
 * cursor management, and mouse event tracking.
 */
public interface Renderer {
    /**
     * Starts the renderer loop.
     */
    void start();

    /** Stops the renderer loop and cleans up resources. Not resumable; use pause()/resume() instead. */
    void stop();

    /** Pauses rendering without destroying resources. */
    default void pause() {
    }

    /** Resumes rendering after pause(). */
    default void resume() {
    }

    /**
     * Writes the given view string to the terminal buffer.
     *
     * @param view rendered view
     */
    void write(String view);

    /**
     * Forces a full repaint of the screen on the next cycle.
     */
    void repaint();

    /**
     * Clears the terminal screen.
     */
    void clearScreen();

    /**
     * Returns true if the renderer is currently using the alternate screen buffer.
     *
     * @return {@code true} when using the alt screen
     */
    boolean altScreen();

    /**
     * Switches to the alternate screen buffer (e.g. for full-screen apps).
     */
    void enterAltScreen();

    /**
     * Restores the main screen buffer.
     */
    void exitAltScreen();

    /**
     * Shows the terminal cursor.
     */
    void showCursor();

    /**
     * Hides the terminal cursor.
     */
    void hideCursor();

    /**
     * Enables basic mouse reporting (click/wheel).
     */
    default void enableMouseNormalTracking() {
    }

    /**
     * Disables basic mouse reporting.
     */
    default void disableMouseNormalTracking() {
    }

    /**
     * Enables mouse click, release, wheel and motion events if a button is pressed (drag).
     */
    default void enableMouseCellMotion() {
    }

    /**
     * Disables mouse cell motion tracking.
     */
    default void disableMouseCellMotion() {
    }

    /**
     * Enables all mouse events (click, release, wheel, motion) regardless of button state.
     */
    void enableMouseAllMotion();

    /**
     * Disables all motion mouse tracking.
     */
    void disableMouseAllMotion();

    /**
     * Enables extended mouse mode (SGR) for better coordinate support.
     */
    void enableMouseSGRMode();

    /**
     * Disables extended mouse mode.
     */
    void disableMouseSGRMode();

    /**
     * Changes mouse pointer to I-beam shape (OSC 22).
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     */
    default void setMouseCursorText() {
    }

    /**
     * Changes mouse pointer to hand shape (OSC 22).
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     */
    default void setMouseCursorPointer() {
    }

    /**
     * Resets mouse pointer to default shape (OSC 22).
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     */
    default void resetMouseCursor() {
    }

    /**
     * Copies text to the system clipboard (OSC 52).
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @param text text to copy
     */
    default void copyToClipboard(String text) {
    }

    /**
     * Returns whether focus event reporting is enabled.
     *
     * @return {@code true} when focus reporting is enabled
     */
    boolean reportFocus();

    /**
     * Enables reporting of terminal focus gain/loss events.
     */
    void enableReportFocus();

    /**
     * Disables reporting of terminal focus events.
     */
    void disableReportFocus();

    /**
     * Enables bracketed paste mode.
     */
    void enableBracketedPaste();

    /**
     * Disables bracketed paste mode.
     */
    void disableBracketedPaste();

    /**
     * Returns whether bracketed paste mode is enabled.
     *
     * @return {@code true} when bracketed paste is enabled
     */
    boolean bracketedPaste();

    /**
     * Enables Kitty keyboard protocol for enhanced key reporting.
     * <p>
     * This enables the terminal to report modifier keys on Enter (Shift+Enter, Ctrl+Enter)
     * via CSI-u sequences. Supported by Kitty, Ghostty, WezTerm, and other modern terminals.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     *
     * @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/">Kitty Keyboard Protocol</a>
     */
    default void enableKittyKeyboard() {
    }

    /**
     * Disables Kitty keyboard protocol.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     */
    default void disableKittyKeyboard() {
    }

    /**
     * Returns whether Kitty keyboard protocol is enabled.
     *
     * @return {@code true} when Kitty keyboard is enabled
     */
    default boolean kittyKeyboard() {
        return false;
    }

    /**
     * Notifies the renderer that the model has changed and a render is needed.
     * <p>
     * tui4j extension; no Bubble Tea equivalent.
     */
    void notifyModelChanged();

    /**
     * Processes a message that might affect the renderer state (e.g. window resize).
     *
     * @param msg message to handle
     */
    void handleMessage(Message msg);
}
