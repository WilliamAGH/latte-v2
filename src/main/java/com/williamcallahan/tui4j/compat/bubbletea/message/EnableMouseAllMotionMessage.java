package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Enables "all motion" mouse tracking (report motion without a pressed button).
 * Bubble Tea: bubbletea/screen.go
 */
public record EnableMouseAllMotionMessage() implements Message {
}
