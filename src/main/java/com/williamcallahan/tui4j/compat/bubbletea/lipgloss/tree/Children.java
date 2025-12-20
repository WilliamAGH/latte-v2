package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import java.util.Arrays;

/**
 * Port of Lip Gloss children.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public interface Children {

    static Children newStringData(String... strings) {
        return new NodeChildren(Arrays.stream(strings)
                .map(Leaf::new)
                .map(Node.class::cast)
                .toList());
    }

    Node at(int index);

    Children remove(int index);

    Children append(Node child);

    int length();
}
