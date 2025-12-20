package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests exiting the alternate screen buffer.
 * Bubble Tea: bubbletea/screen.go
 */
public record ExitAltScreen() implements Message {
}
