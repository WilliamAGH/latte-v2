package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Port of Lip Gloss tree indenter.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
@FunctionalInterface
public interface TreeIndenter {

    class DefaultIndenter implements TreeIndenter {

        @Override
        public String indent(Children children, int index) {
            if (children.length() - 1 == index) {
                return "   ";
            }
            return "│  ";
        }
    }

    String indent(Children children, int index);
}
