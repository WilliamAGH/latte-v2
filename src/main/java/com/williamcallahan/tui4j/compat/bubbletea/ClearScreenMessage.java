package com.williamcallahan.tui4j.compat.bubbletea;

/**
 * Message to clear the terminal screen.
 * <p>
 * Bubble Tea: screen.go.
 *
 * @see <a href="https://github.com/charmbracelet/bubbletea/blob/main/screen.go">bubbletea/screen.go</a>
 */
public class ClearScreenMessage implements Message {

    /**
     * Creates a clear screen message.
     */
    public ClearScreenMessage() {
        // Marker message — carries no state
    }
}
