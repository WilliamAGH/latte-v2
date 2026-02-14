package com.williamcallahan.tui4j.compat.bubbletea.bubbles.key;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.key.Binding} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Binding extends com.williamcallahan.tui4j.compat.bubbles.key.Binding {

    /**
     * Creates a legacy binding with the provided options.
     *
     * @param opts binding options
     */
    @Deprecated(since = "0.3.0")
    public Binding(BindingOption... opts) {
        super(opts);
    }

}
