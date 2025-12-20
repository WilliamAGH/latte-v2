package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.StyleFunction;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.TreeEnumerator;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.TreeIndenter;

/**
 * Port of Lip Gloss list.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class List {

    private Tree tree;

    public List(Object... items) {
        this.tree = new Tree();
        this.items(items).enumerator(ListEnumerator.bullet()).indenter((children, index) -> " ");
    }

    public boolean isHidden() {
        return tree.isHidden();
    }

    public List hide(boolean hide) {
        tree.hide();
        return this;
    }

    public List offset(int start, int end) {
        tree.offset(start, end);
        return this;
    }

    public List enumeratorStyle(Style style) {
        tree.enumeratorStyle(style);
        return this;
    }

    public List enumeratorStyleFunc(StyleFunction function) {
        tree.enumeratorStyleFunc(function);
        return this;
    }

    public List itemStyleFunc(StyleFunction function) {
        tree.itemStyleFunc(function);
        return this;
    }

    public List indenter(TreeIndenter indenter) {
        tree.indenter(indenter);
        return this;
    }

    public List enumerator(TreeEnumerator enumerator) {
        tree.enumerator(enumerator);
        return this;
    }

    public List item(Object item) {
        if (item instanceof List list) {
            tree.child(list.tree);
        } else {
            tree.child(item);
        }
        return this;
    }

    public List items(Object... items) {
        for (Object item : items) {
            item(item);
        }
        return this;
    }

    public String render() {
        return tree.render();
    }

    @Override
    public String toString() {
        return render();
    }
}
