package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeIndenter;

/**
 * Compatibility shim for {@link com.williamcallahan.tui4j.compat.lipgloss.List}.
 * Canonical source: {@code src/main/java/com/williamcallahan/tui4j/compat/lipgloss/List.java}.
 * <p>
 * Lip Gloss: list/list.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.List}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class List extends com.williamcallahan.tui4j.compat.lipgloss.List {

    /**
     * Creates an empty list shim.
     */
    @Deprecated(since = "0.3.0")
    public List() {
        super();
    }

    /**
     * Creates a list shim with object items.
     *
     * @param items items
     */
    @Deprecated(since = "0.3.0")
    public List(Object... items) {
        super(items);
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List hide(boolean hide) {
        super.hide(hide);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List offset(int start, int end) {
        super.offset(start, end);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List enumeratorStyle(Style style) {
        super.enumeratorStyle(style);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List enumeratorStyleFunc(StyleFunction function) {
        super.enumeratorStyleFunc(function);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List itemStyleFunc(StyleFunction function) {
        super.itemStyleFunc(function);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List indenter(TreeIndenter indenter) {
        super.indenter(indenter);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List enumerator(TreeEnumerator enumerator) {
        super.enumerator(enumerator);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List item(Object item) {
        super.item(item);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public List items(Object... items) {
        super.items(items);
        return this;
    }
}
