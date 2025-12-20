package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * Port of Bubbles filtered item.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class FilteredItem {
    private int rankIndex;
    private Item item;
    private int[] matches;

    public FilteredItem(int rankIndex, Item item, int[] matches) {
        this.rankIndex = rankIndex;
        this.item = item;
        this.matches = matches;
    }

    public FilteredItem(Item item) {
        this.item = item;
        this.matches = new int[0];
    }

    public Item item() {
        return item;
    }

    public int[] matches() {
        return matches;
    }

    public int rankIndex() {
        return rankIndex;
    }
}