package com.williamcallahan.tui4j.compat.lipgloss;

import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;
import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeIndenter;

/**
 * List component.
 * <p>
 * Port of `lipgloss/list`.
 * Upstream: lipgloss/list/list.go
 * Renders an ordered or unordered list of items using the underlying Tree component.
 */
public class List {

    private Tree tree;

    /**
     * Creates a list with the provided items.
     *
     * @param items initial list items
     */
    public List(Object... items) {
        this.tree = new Tree();
        this.items(items).enumerator(ListEnumerator.bullet()).indenter((children, index) -> " ");
    }

    /**
     * Reports whether the list is hidden.
     *
     * @return {@code true} when hidden
     */
    public boolean isHidden() {
        return tree.isHidden();
    }

    /**
     * Sets whether the list is hidden.
     *
     * @param hide hide flag
     * @return this list for chaining
     */
    public List hide(boolean hide) {
        tree.hide(hide);
        return this;
    }

    /**
     * Sets the render offset.
     *
     * @param start start offset
     * @param end end offset
     * @return this list for chaining
     */
    public List offset(int start, int end) {
        tree.offset(start, end);
        return this;
    }

    /**
     * Sets the enumerator style.
     *
     * @param style enumerator style
     * @return this list for chaining
     */
    public List enumeratorStyle(Style style) {
        tree.enumeratorStyle(style);
        return this;
    }

    /**
     * Sets a dynamic enumerator style function.
     *
     * @param function enumerator style function
     * @return this list for chaining
     */
    public List enumeratorStyleFunc(StyleFunction function) {
        tree.enumeratorStyleFunc(function);
        return this;
    }

    /**
     * Sets a dynamic item style function.
     *
     * @param function item style function
     * @return this list for chaining
     */
    public List itemStyleFunc(StyleFunction function) {
        tree.itemStyleFunc(function);
        return this;
    }

    /**
     * Sets the indenter used for nested items.
     *
     * @param indenter tree indenter
     * @return this list for chaining
     */
    public List indenter(TreeIndenter indenter) {
        tree.indenter(indenter);
        return this;
    }

    /**
     * Sets the enumerator used for list items.
     *
     * @param enumerator enumerator function
     * @return this list for chaining
     */
    public List enumerator(TreeEnumerator enumerator) {
        tree.enumerator(enumerator);
        return this;
    }

    /**
     * Adds a single item, flattening nested lists.
     *
     * @param item item or nested list
     * @return this list for chaining
     */
    public List item(Object item) {
        if (item instanceof List list) {
            tree.child(list.tree);
        } else {
            tree.child(item);
        }
        return this;
    }

    /**
     * Adds multiple items to the list.
     *
     * @param items items to add
     * @return this list for chaining
     */
    public List items(Object... items) {
        for (Object item : items) {
            item(item);
        }
        return this;
    }

    /**
     * Renders the list to a string.
     *
     * @return rendered list
     */
    public String render() {
        return tree.render();
    }

    @Override
    public String toString() {
        return render();
    }
}
