package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Port of Bubbles fetched items.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 *
 * @param items filtered items
 * @param matchedItems number of matched items
 * @param totalItems total item count
 * @param totalPages total page count
 * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.list.FetchedItems} instead.
 *   This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public record FetchedItems(List<FilteredItem> items, long matchedItems, long totalItems, int totalPages) {

    /**
     * Creates an empty fetched items payload.
     */
    @Deprecated(since = "0.3.0")
    public FetchedItems() {
        this(List.of(), 0, 0, 0);
    }

    /**
     * Converts to the canonical {@link com.williamcallahan.tui4j.compat.bubbles.list.FetchedItems}.
     * <p>
     * The item list converts safely because deprecated {@link FilteredItem} extends
     * canonical {@link com.williamcallahan.tui4j.compat.bubbles.list.FilteredItem}.
     *
     * @return canonical fetched items
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.bubbles.list.FetchedItems toCanonical() {
        List<com.williamcallahan.tui4j.compat.bubbles.list.FilteredItem> canonicalItems = new ArrayList<>();
        canonicalItems.addAll(items);
        return new com.williamcallahan.tui4j.compat.bubbles.list.FetchedItems(
                canonicalItems, matchedItems, totalItems, totalPages
        );
    }
}
