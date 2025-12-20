package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.Message;

/**
 * Port of Bubbles fetched current page items.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public record FetchedCurrentPageItems(
        FetchedItems fetchedItems,
        Runnable... postFetch
) implements Message {
}
