package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Bubbles: timer/timer.go.
 */
@Deprecated(since = "0.3.0")
public class TimeoutMessage implements Message {
    private final com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage delegate;

    /**
     * Creates TimeoutMessage.
     *
     * @param id timer id
     */
    @Deprecated(since = "0.3.0")
    public TimeoutMessage(int id) {
        this.delegate = new com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage(id);
    }

    /**
     * Returns the timer id.
     *
     * @return timer id
     */
    @Deprecated(since = "0.3.0")
    public int id() {
        return delegate.id();
    }

    /**
     * Returns the canonical delegate.
     *
     * @return canonical TimeoutMessage
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage toCanonical() {
        return delegate;
    }
}
