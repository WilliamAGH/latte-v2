package com.williamcallahan.tui4j.compat.bubbletea;

/**
 * Message to request the current window size.
 * <p>
 * tui4j extension; no Bubble Tea equivalent.
 * <p>
 * Bubble Tea: screen.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0; use {@link com.williamcallahan.tui4j.message.CheckWindowSizeMessage} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0", forRemoval = true)
public class CheckWindowSizeMessage
        extends com.williamcallahan.tui4j.message.CheckWindowSizeMessage {

    /**
     * Creates a check window size message.
     */
    @Deprecated(since = "0.3.0", forRemoval = true)
    public CheckWindowSizeMessage() {
        super();
    }
}
