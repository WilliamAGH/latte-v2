package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.TreeEnumerator;

/**
 * Port of Lip Gloss list enumerator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public interface ListEnumerator {

    int abcLen = 26;

    static TreeEnumerator alphabet() {
        return (children, index) -> {
            if (index >= abcLen * abcLen + abcLen) {
                return String.format("%c%c%c.", 'A' + index / abcLen / abcLen - 1, 'A' + (index / abcLen) % abcLen - 1, 'A' + index % abcLen);
            }
            if (index >= abcLen) {
                return String.format("%c%c.", 'A' + index / abcLen - 1, 'A' + (index) % abcLen);
            }
            return String.format("%c.", 'A' + index % abcLen);
        };
    }

    static TreeEnumerator arabic() {
        return (children, index) -> String.format("%d.", index + 1);
    }

    static TreeEnumerator roman() {
        return (children, index) -> {
            String[] roman = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
            int[] arabic = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

            StringBuilder result = new StringBuilder();
            int i = index;
            for (int v = 0; v < arabic.length; v++) {
                int value = arabic[v];

                while (i >= value - 1) {
                    i -= value;
                    result.append(roman[v]);
                }
            }
            return result.append('.').toString();
        };
    }

    static TreeEnumerator bullet() {
        return (children, index) -> "•";
    }

    static TreeEnumerator asterisk() {
        return (children, index) -> "*";
    }

    static TreeEnumerator dash() {
        return (children, index) -> "-";
    }
}
