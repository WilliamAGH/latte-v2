package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import com.williamcallahan.tui4j.Message;

/**
 * Port of Bubbles tick message.
 * Bubble Tea: bubbletea/examples/timer/main.go
 */
public record TickMessage(
        int id,
        boolean timeout,
        int tag
) implements Message {
}
