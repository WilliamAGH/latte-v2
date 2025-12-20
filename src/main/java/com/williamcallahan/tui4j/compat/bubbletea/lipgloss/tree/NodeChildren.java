package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Port of Lip Gloss node children.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class NodeChildren implements Children {

    private List<Node> children = new ArrayList<>();

    public NodeChildren(List<Node> children) {
        this.children = children;
    }

    public NodeChildren() {
    }

    @Override
    public Node at(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    @Override
    public Children remove(int index) {
        children.remove(index);
        return this;
    }

    @Override
    public int length() {
        return children.size();
    }

    public Children append(Node child) {
        children.add(child);
        return this;
    }
}
