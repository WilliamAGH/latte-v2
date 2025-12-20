package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Port of Lip Gloss tree enumerator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
@FunctionalInterface
public interface TreeEnumerator {

    class DefaultEnumerator implements TreeEnumerator {

        @Override
        public String enumerate(Children children, int index) {
            if (children.length() - 1 == index) {
                return "└──";
            }
            return "├──";
        }
    }

    class RounderEnumerator implements TreeEnumerator {

        @Override
        public String enumerate(Children children, int index) {
            if (children.length() - 1 == index) {
                return "╰──";
            }
            return "├──";
        }
    }

    String enumerate(Children children, int index);
}
