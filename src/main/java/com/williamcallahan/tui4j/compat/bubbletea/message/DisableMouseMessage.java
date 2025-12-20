package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Disables mouse tracking (motion modes and SGR extended mouse mode).
 * Bubble Tea: bubbletea/screen.go
 */
public record DisableMouseMessage() implements Message {
}
