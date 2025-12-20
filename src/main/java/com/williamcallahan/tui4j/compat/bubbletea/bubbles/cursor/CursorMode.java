package com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor;

import java.util.stream.Stream;

/**
 * Port of Bubbles cursor mode.
 * Bubble Tea: bubbletea/examples/textinputs/main.go
 */
public enum CursorMode {
    Blink,
    Static,
    Hide;

    public static CursorMode fromOrdinal(int value) {
        return Stream.of(CursorMode.values()).filter(mode -> mode.ordinal() == value).findAny().orElse(null);
    }
}