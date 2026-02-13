package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.MessageShim;

import java.util.Arrays;
import java.util.Objects;

/**
 * Port of Bubbles fetched current page items.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 *
 * @param fetchedItems fetched items payload
 * @param postFetch callbacks to run after fetch
 * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.list.FetchedCurrentPageItems} instead.
 *   This shim implements {@link MessageShim} so that messages created with the deprecated type are
 *   automatically normalized to the canonical type in the event loop.
 */
@Deprecated(since = "0.3.0")
public record FetchedCurrentPageItems(
        FetchedItems fetchedItems,
        Runnable... postFetch
) implements MessageShim {

    /**
     * Converts this deprecated message to the canonical {@link com.williamcallahan.tui4j.compat.bubbles.list.FetchedCurrentPageItems}.
     *
     * @return canonical message
     */
    @Deprecated(since = "0.3.0")
    @Override
    public Message toMessage() {
        return new com.williamcallahan.tui4j.compat.bubbles.list.FetchedCurrentPageItems(
                fetchedItems.toCanonical(),
                postFetch
        );
    }

    @Deprecated(since = "0.3.0")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FetchedCurrentPageItems(FetchedItems fi, Runnable[] pf))) return false;
        return Objects.equals(fetchedItems, fi)
                && Arrays.equals(postFetch, pf);
    }

    @Deprecated(since = "0.3.0")
    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(fetchedItems) + Arrays.hashCode(postFetch);
    }

    @Deprecated(since = "0.3.0")
    @Override
    public String toString() {
        return "FetchedCurrentPageItems[fetchedItems=" + fetchedItems
                + ", postFetch=" + Arrays.toString(postFetch) + "]";
    }
}
