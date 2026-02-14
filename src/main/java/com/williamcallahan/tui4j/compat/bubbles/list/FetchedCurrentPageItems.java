package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

import java.util.Arrays;
import java.util.Objects;

/**
 * Port of Bubbles fetched current page items.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 *
 * @param fetchedItems fetched page items
 * @param postFetch callbacks to run after fetching
 */
public record FetchedCurrentPageItems(
        FetchedItems fetchedItems,
        Runnable... postFetch
) implements Message {

    /** Compares fetched items and post-fetch callbacks by value. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedCurrentPageItems(FetchedItems fi, Runnable[] pf))) return false;
        return Objects.equals(fetchedItems, fi)
                && Arrays.equals(postFetch, pf);
    }

    /** Computes a hash using fetched items and post-fetch callbacks. */
    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(fetchedItems) + Arrays.hashCode(postFetch);
    }

    /** Returns a readable representation for diagnostics. */
    @Override
    public String toString() {
        return "FetchedCurrentPageItems[fetchedItems=" + fetchedItems
                + ", postFetch=" + Arrays.toString(postFetch) + "]";
    }
}
