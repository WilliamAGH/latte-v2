package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.list.List} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class List extends com.williamcallahan.tui4j.compat.bubbles.list.List {

    /**
     * Creates a deprecated compatibility list wrapper backed by static items.
     *
     * @param items list items
     * @param width list width
     * @param height list height
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.list.List} instead.
     */
    @Deprecated(since = "0.3.0")
    public List(Item[] items, int width, int height) {
        super(items, width, height);
    }

    /**
     * Creates a deprecated compatibility list wrapper with a custom delegate.
     *
     * @param items list items
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.list.List} instead.
     */
    @Deprecated(since = "0.3.0")
    public List(Item[] items, ItemDelegate delegate, int width, int height) {
        super(items, delegate, width, height);
    }

    /**
     * Creates a deprecated compatibility list wrapper backed by a data source.
     *
     * @param dataSource list data source
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.list.List} instead.
     */
    @Deprecated(since = "0.3.0")
    public List(ListDataSource dataSource, ItemDelegate delegate, int width, int height) {
        super(dataSource, delegate, width, height);
    }

    /**
     * Creates a deprecated compatibility list wrapper backed by a data source.
     *
     * @param dataSource list data source
     * @param width list width
     * @param height list height
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.list.List} instead.
     */
    @Deprecated(since = "0.3.0")
    public List(ListDataSource dataSource, int width, int height) {
        super(dataSource, width, height);
    }

}
