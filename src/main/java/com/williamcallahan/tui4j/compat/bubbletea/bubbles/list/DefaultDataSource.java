package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.fuzzy.FuzzyFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Port of Bubbles default data source.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class DefaultDataSource implements ListDataSource {

    private final List list;
    private FilterFunction filterFunction;
    private java.util.List<Item> items;

    public DefaultDataSource(List list, Item... items) {
        this.list = list;
        this.items = new ArrayList<>(Arrays.asList(items));
        this.filterFunction = FuzzyFilter::defaultFilter;
    }

    public Command setItem(int index, Item item) {
        this.items.set(index, item);
        return list.refresh();
    }

    public Command insertItem(int index, Item item) {
        this.items.add(index, item);
        return list.refresh();
    }

    public Command removeItem(int index, Runnable... postRemove) {
        this.items.remove(index);
        return list.refresh(postRemove);
    }

    public Command setItems(Item... items) {
        this.items = java.util.List.of(items);
        return list.refresh();
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    @Override
    public FetchedItems fetchItems(int page, int perPage, String filterValue) {
        java.util.List<FilteredItem> filteredItems;

        if (filterValue == null || filterValue.isEmpty()) {
            filteredItems = allItemsAsFilteredItems();
        } else {
            String[] targets = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                targets[i] = items.get(i).filterValue();
            }

            java.util.List<FilteredItem> filterMatches = new LinkedList<>();
            Rank[] ranks = filterFunction.apply(filterValue, targets);
            for (Rank rank : ranks) {
                filterMatches.add(new FilteredItem(
                        rank.getIndex(),
                        items.get(rank.getIndex()),
                        rank.getMatchedIndexes()
                ));
            }

            filteredItems = filterMatches;
        }

        int offset = page * perPage;
        int toIndex = Math.min(offset + perPage, filteredItems.size());
        long matchedItems = filteredItems.size();
        long totalItems = items.size();
        int totalPages = (int) Math.ceil((double) matchedItems / perPage);

        if (offset >= matchedItems/* && offset - perPage >= 0*/) {
            offset = offset - perPage;
        }
        return new FetchedItems(filteredItems.subList(offset, toIndex), matchedItems, totalItems, totalPages);
    }

    private java.util.List<FilteredItem> allItemsAsFilteredItems() {
        return items.stream().map(FilteredItem::new).toList();
    }

    public int indexOf(DefaultItem defaultItem) {
        return items.indexOf(defaultItem);
    }
}
