package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.list.DefaultDataSource} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class DefaultDataSource extends com.williamcallahan.tui4j.compat.bubbles.list.DefaultDataSource {

    /**
     * Creates a default data source for the legacy list.
     *
     * @param list list component
     * @param items initial items
     */
    @Deprecated(since = "0.3.0")
    public DefaultDataSource(List list, Item... items) {
        super(list, items);
    }

}
