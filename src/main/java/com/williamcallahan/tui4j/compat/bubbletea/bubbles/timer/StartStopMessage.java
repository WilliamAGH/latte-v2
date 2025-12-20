package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import com.williamcallahan.tui4j.Message;

/**
 * Port of Bubbles start stop message.
 * Bubble Tea: bubbletea/examples/timer/main.go
 */
public record StartStopMessage(
        int id,
        boolean running
) implements Message {
}
