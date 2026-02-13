package com.williamcallahan.tui4j.compat.bubbletea.bubbles.runeutil;

import java.util.function.Consumer;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.runeutil.Sanitizer} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Sanitizer extends com.williamcallahan.tui4j.compat.bubbles.runeutil.Sanitizer {

    /**
     * Creates a sanitizer with the given options.
     *
     * @param options configuration options
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.runeutil.Sanitizer#Sanitizer(java.util.function.Consumer[])} instead.
     */
    @Deprecated(since = "0.3.0")
    @SafeVarargs
    public Sanitizer(Consumer<Sanitizer>... options) {
        super();
        for (Consumer<Sanitizer> option : options) {
            option.accept(this);
        }
    }

}
