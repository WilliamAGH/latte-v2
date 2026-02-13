package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeIndenter;

/**
 * Compatibility shim for {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Tree}.
 * Canonical source: {@code src/main/java/com/williamcallahan/tui4j/compat/lipgloss/tree/Tree.java}.
 * <p>
 * Lipgloss: tree/tree.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Tree}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Tree extends com.williamcallahan.tui4j.compat.lipgloss.tree.Tree {

    /**
     * Creates an empty tree shim.
     */
    @Deprecated(since = "0.3.0")
    public Tree() {
        super();
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree hide() {
        super.hide();
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree hide(boolean hide) {
        super.hide(hide);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree setHidden(boolean hidden) {
        super.setHidden(hidden);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree offset(int start, int end) {
        super.offset(start, end);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree child(Object... children) {
        super.child(children);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree enumeratorStyle(Style style) {
        super.enumeratorStyle(style);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree enumeratorStyleFunc(StyleFunction function) {
        super.enumeratorStyleFunc(function);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree rootStyle(Style style) {
        super.rootStyle(style);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree itemStyle(Style style) {
        super.itemStyle(style);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree itemStyleFunc(StyleFunction function) {
        super.itemStyleFunc(function);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree enumerator(TreeEnumerator enumerator) {
        super.enumerator(enumerator);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree indenter(TreeIndenter indenter) {
        super.indenter(indenter);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree setValue(Object value) {
        super.setValue(value);
        return this;
    }

    @Deprecated(since = "0.3.0")
    @Override
    public Tree root(Object root) {
        super.root(root);
        return this;
    }
}
