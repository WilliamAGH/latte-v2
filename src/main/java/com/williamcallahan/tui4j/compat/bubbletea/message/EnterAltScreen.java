package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests entering the alternate screen buffer.
 * Bubble Tea: bubbletea/screen.go
 */
public record EnterAltScreen() implements Message {
}
