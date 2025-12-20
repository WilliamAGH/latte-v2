package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * Port of Bubbles list data source.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public interface ListDataSource {
    FetchedItems fetchItems(int page, int perPage, String filterValue);
}
