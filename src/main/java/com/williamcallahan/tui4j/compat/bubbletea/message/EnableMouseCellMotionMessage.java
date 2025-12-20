package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Enables "cell motion" mouse tracking (report motion while a button is pressed).
 * Bubble Tea: bubbletea/screen.go
 */
public record EnableMouseCellMotionMessage() implements Message {
}
