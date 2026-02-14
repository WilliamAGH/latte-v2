package com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Spinner extends com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner {

    /**
     * Creates a spinner with the given type.
     *
     * @param type spinner type
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner#Spinner(com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType)} instead.
     */
    @Deprecated(since = "0.3.0")
    public Spinner(SpinnerType type) {
        super(com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType.valueOf(type.name()));
    }

}
