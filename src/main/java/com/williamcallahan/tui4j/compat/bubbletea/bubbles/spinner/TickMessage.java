package com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

import java.time.LocalDateTime;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Bubbles: spinner/spinner.go.
 */
@Deprecated(since = "0.3.0")
public class TickMessage implements Message {
    private final com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage delegate;

    /**
     * Creates TickMessage.
     *
     * @param time tick time
     * @param tag spinner tag
     * @param id spinner id
     */
    @Deprecated(since = "0.3.0")
    public TickMessage(LocalDateTime time, int tag, int id) {
        this.delegate = new com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage(time, tag, id);
    }

    /**
     * Returns the tick time.
     *
     * @return tick time
     */
    @Deprecated(since = "0.3.0")
    public LocalDateTime time() {
        return delegate.time();
    }

    /**
     * Returns the spinner tag.
     *
     * @return spinner tag
     */
    @Deprecated(since = "0.3.0")
    public int tag() {
        return delegate.tag();
    }

    /**
     * Returns the spinner id.
     *
     * @return spinner id
     */
    @Deprecated(since = "0.3.0")
    public int id() {
        return delegate.id();
    }

    /**
     * Returns the canonical delegate.
     *
     * @return canonical TickMessage
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage toCanonical() {
        return delegate;
    }
}
