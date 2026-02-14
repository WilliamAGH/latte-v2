package com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor;

import java.util.stream.Stream;

/**
 * Port of Bubbles cursor mode.
 * Bubble Tea: bubbletea/examples/textinputs/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.cursor.CursorMode} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum CursorMode {
    /** Cursor blinks on and off. */
    @Deprecated(since = "0.3.0")
    Blink,
    /** Cursor remains visible without blinking. */
    @Deprecated(since = "0.3.0")
    Static,
    /** Cursor is hidden. */
    @Deprecated(since = "0.3.0")
    Hide;

    /**
     * Returns the cursor mode for the provided ordinal value.
     *
     * @param value enum ordinal
     * @return matching mode or null when no match exists
     */
    @Deprecated(since = "0.3.0")
    public static CursorMode fromOrdinal(int value) {
        return Stream.of(CursorMode.values()).filter(mode -> mode.ordinal() == value).findAny().orElse(null);
    }
}
