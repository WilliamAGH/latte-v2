package com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner;

import com.williamcallahan.tui4j.Message;

import java.time.LocalDateTime;

/**
 * Port of Bubbles tick message.
 * Bubble Tea: bubbletea/examples/spinner/main.go
 */
public final class TickMessage implements Message {
    private final LocalDateTime time;
    private final int tag;
    private final int id;

    public TickMessage(
            LocalDateTime time,
            int tag,
            int id) {
        this.time = time;
        this.tag = tag;
        this.id = id;
    }

    public LocalDateTime time() {
        return time;
    }

    public int tag() {
        return tag;
    }

    public int id() {
        return id;
    }

}