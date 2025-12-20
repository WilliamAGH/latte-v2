package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import java.util.List;

/**
 * Port of Bubbles fetched items.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public record FetchedItems(List<FilteredItem> items, long matchedItems, long totalItems, int totalPages) {

    public FetchedItems() {
        this(List.of(), 0, 0, 0);
    }
}
