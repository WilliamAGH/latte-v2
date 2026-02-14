package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Filter} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: tree/children.go.
 */
@Deprecated(since = "0.3.0")
public class Filter extends com.williamcallahan.tui4j.compat.lipgloss.tree.Filter {

    /**
     * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use
     *             {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Filter.FilterFunction} instead.
     *             This transitional shim is temporary and will be removed in an upcoming release.
     */
    @Deprecated(since = "0.3.0")
    public interface FilterFunction
        extends com.williamcallahan.tui4j.compat.lipgloss.tree.Filter.FilterFunction {}

    /**
     * Creates Filter to keep this component ready for use.
     *
     * @param data data
     */
    @Deprecated(since = "0.3.0")
    public Filter(Children data) {
        super(data);
    }

    /**
     * Handles at for this component.
     *
     * @param index index
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public Node at(int index) {
        return (Node) super.at(index);
    }

    /**
     * Handles filter for this component.
     *
     * @param filterFunction filter function
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public Filter filter(FilterFunction filterFunction) {
        super.filter(filterFunction);
        return this;
    }

    /**
     * Handles remove for this component.
     *
     * @param index index
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public Children remove(int index) {
        return (Children) super.remove(index);
    }

    /**
     * Handles append for this component.
     *
     * @param child child
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public Children append(Node child) {
        return (Children) super.append(child);
    }
}
