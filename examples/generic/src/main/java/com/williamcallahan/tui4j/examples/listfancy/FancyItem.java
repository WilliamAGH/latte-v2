package com.williamcallahan.tui4j.examples.listfancy;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultItem;

/**
 * Example program for fancy item.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/FancyItem.java
 */
public record FancyItem(String title, String description) implements DefaultItem  {
    @Override
    public String filterValue() {
        return title;
    }
}
