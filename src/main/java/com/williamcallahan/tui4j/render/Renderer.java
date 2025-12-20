package com.williamcallahan.tui4j.render;

import com.williamcallahan.tui4j.Message;

/**
 * Abstraction for rendering and terminal mode control.
 * tui4j: src/main/java/com/williamcallahan/tui4j/render/Renderer.java
 */
public interface Renderer {
    // Start the renderer.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go start behavior.
    void start();
    // Stop the renderer.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go stop behavior.
    void stop();

    // Bubble Tea: seeks to replicate bubbletea/renderer.go write behavior.
    void write(String view);
    // Bubble Tea: seeks to replicate bubbletea/renderer.go repaint behavior.
    void repaint();
    // Bubble Tea: seeks to replicate bubbletea/renderer.go clearScreen behavior.
    void clearScreen();

    // Bubble Tea: seeks to replicate bubbletea/renderer.go altScreen behavior.
    boolean altScreen();
    // Bubble Tea: seeks to replicate bubbletea/renderer.go enterAltScreen behavior.
    void enterAltScreen();
    // Bubble Tea: seeks to replicate bubbletea/renderer.go exitAltScreen behavior.
    void exitAltScreen();

    // Bubble Tea: seeks to replicate bubbletea/renderer.go showCursor behavior.
    void showCursor();
    // Bubble Tea: seeks to replicate bubbletea/renderer.go hideCursor behavior.
    void hideCursor();

    // enableMouseCellMotion enables mouse click, release, wheel and motion
    // events if a mouse button is pressed (i.e., drag events).
    // Bubble Tea: seeks to replicate bubbletea/renderer.go enableMouseCellMotion behavior.
    default void enableMouseCellMotion() {
    }
    // disableMouseCellMotion disables Mouse Cell Motion tracking.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go disableMouseCellMotion behavior.
    default void disableMouseCellMotion() {
    }

    // enableMouseAllMotion enables mouse click, release, wheel and motion
    // events, regardless of whether a mouse button is pressed. Many modern
    // terminals support this, but not all.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go enableMouseAllMotion behavior.
    void enableMouseAllMotion();
    // disableMouseAllMotion disables All Motion mouse tracking.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go disableMouseAllMotion behavior.
    void disableMouseAllMotion();

    // enableMouseSGRMode enables mouse extended mode (SGR).
    // Bubble Tea: seeks to replicate bubbletea/renderer.go enableMouseSGRMode behavior.
    void enableMouseSGRMode();
    // disableMouseSGRMode disables mouse extended mode (SGR).
    // Bubble Tea: seeks to replicate bubbletea/renderer.go disableMouseSGRMode behavior.
    void disableMouseSGRMode();

    // setMouseCursorText changes mouse pointer to I-beam shape (OSC 22).
    // tui4j extension; no Bubble Tea equivalent.
    default void setMouseCursorText() {
    }

    // setMouseCursorPointer changes mouse pointer to hand shape (OSC 22).
    // tui4j extension; no Bubble Tea equivalent.
    default void setMouseCursorPointer() {
    }

    // resetMouseCursor resets mouse pointer to default shape (OSC 22).
    // tui4j extension; no Bubble Tea equivalent.
    default void resetMouseCursor() {
    }

    // copyToClipboard copies text to system clipboard (OSC 52).
    // tui4j extension; no Bubble Tea equivalent.
    default void copyToClipboard(String text) {
    }

    // reportFocus returns whether reporting focus events is enabled.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go reportFocus behavior.
    boolean reportFocus();
    // enableReportFocus reports focus events to the program.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go enableReportFocus behavior.
    void enableReportFocus();
    // disableReportFocus stops reporting focus events to the program.
    // Bubble Tea: seeks to replicate bubbletea/renderer.go disableReportFocus behavior.
    void disableReportFocus();

    // tui4j extension; no Bubble Tea equivalent.
    void notifyModelChanged();

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go handleMessages behavior.
    void handleMessage(Message msg);
}
