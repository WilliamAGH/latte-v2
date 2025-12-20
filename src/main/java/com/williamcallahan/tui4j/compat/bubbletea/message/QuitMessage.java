package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Signals that the program should quit.
 * Bubble Tea: bubbletea/tea.go
 */
public record QuitMessage() implements Message {
}
