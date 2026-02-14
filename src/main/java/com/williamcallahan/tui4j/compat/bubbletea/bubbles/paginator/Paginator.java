package com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.paginator.Paginator} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Paginator extends com.williamcallahan.tui4j.compat.bubbles.paginator.Paginator {

    /**
     * Creates a deprecated compatibility paginator wrapper.
     *
     * @param options paginator options
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.paginator.Paginator} instead.
     */
    @Deprecated(since = "0.3.0")
    public Paginator(Option... options) {
        super(options);
    }

}
