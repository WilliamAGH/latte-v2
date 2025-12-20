package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Port of Lip Gloss leaf.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class Leaf implements Node {

    private String value;
    private boolean hidden;

    public Leaf(String value) {
        this.value = value;
    }

    public Leaf() {
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public Children children() {
        return new NodeChildren();
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return value;
    }
}
