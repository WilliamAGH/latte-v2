package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * Port of Bubbles filter state.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.list.FilterState} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum FilterState {
    /** No filter is set. */
    @Deprecated(since = "0.3.0")
    Unfiltered("unfiltered"),
    /** User is actively setting a filter. */
    @Deprecated(since = "0.3.0")
    Filtering("filtering"),
    /** A filter has been applied. */
    @Deprecated(since = "0.3.0")
    FilterApplied("filter applied");

    private final String stateName;

    /**
     * Creates a filter state with a display name.
     *
     * @param stateName display name
     */
    FilterState(String stateName) {
        this.stateName = stateName;
    }

    /**
     * Returns the display name for this state.
     *
     * @return display name
     */
    @Deprecated(since = "0.3.0")
    @Override
    public String toString() {
        return stateName;
    }
}
