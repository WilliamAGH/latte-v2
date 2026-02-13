package examples.compat.bubbletea.listfancy;

import com.williamcallahan.tui4j.compat.bubbles.list.DefaultItem;

/**
 * Represents a list item with title and description for the fancy list.
 * Upstream: bubbletea/examples/list-fancy/delegate.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/FancyItem.java
 */
public record FancyItem(String title, String description) implements DefaultItem  {
    /**
     * Returns the value used for filtering.
     *
     * @return filter value
     */
    @Override
    public String filterValue() {
        return title;
    }
}
