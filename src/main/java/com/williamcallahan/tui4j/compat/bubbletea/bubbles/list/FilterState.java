package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * Port of Bubbles filter state.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public enum FilterState {
    Unfiltered("unfiltered"), // no filter set
    Filtering("filtering"), // user is actively setting a filter
    FilterApplied("filter applied");

    private final String stateName;

    FilterState(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
