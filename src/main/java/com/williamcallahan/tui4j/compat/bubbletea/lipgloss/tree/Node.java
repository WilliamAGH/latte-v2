package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Port of Lip Gloss node.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public interface Node {

    String value();
    Children children();
    boolean isHidden();
}
