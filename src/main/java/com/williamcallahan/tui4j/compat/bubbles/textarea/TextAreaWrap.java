package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.ansi.TextWidth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Word wrap for textarea input lines.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} wrap logic.
 */
final class TextAreaWrap {

    private TextAreaWrap() {}

    /**
     * Wraps a line of runes to the given width.
     *
     * @param runes input runes
     * @param width wrap width
     * @return wrapped lines
     */
    static List<int[]> wrap(int[] runes, int width) {
        if (width <= 0) {
            return new ArrayList<>(List.of(Arrays.copyOf(runes, runes.length)));
        }

        List<int[]> lines = new ArrayList<>();
        int[] currentLine = new int[0];
        int[] word = new int[0];
        int[] spaces = new int[0];

        for (int rune : runes) {
            if (Character.isWhitespace(rune)) {
                // If we have a word accumulated, process it first before the space
                if (word.length > 0) {
                    int lineW = cellWidth(currentLine);
                    int wordW = cellWidth(word);
                    int spaceW = cellWidth(spaces);

                    // Check if adding the spaces + word exceeds the width
                    if (lineW + spaceW + wordW > width) {
                        if (lineW > 0) {
                            // Current line is full, push it.
                            lines.add(currentLine);
                            currentLine = new int[0];
                            lineW = 0;
                        }
                        
                        // We must preserve spaces. Attach them to the start of the new line (or current if empty).
                        // In standard text editing, spaces at EOL might wrap or be hidden, but for cursor
                        // consistency, they must exist somewhere. Attaching to new line ensures index count matches.
                        currentLine = concat(currentLine, spaces);
                        currentLine = concat(currentLine, word);
                    } else {
                        // Fits on current line
                        currentLine = concat(currentLine, spaces);
                        currentLine = concat(currentLine, word);
                    }
                    word = new int[0];
                    spaces = new int[0];
                }
                spaces = append(spaces, rune);
            } else {
                word = append(word, rune);
            }
        }

        // Flush remaining content
        int lineW = cellWidth(currentLine);
        int wordW = cellWidth(word);
        int spaceW = cellWidth(spaces);

        if (lineW + spaceW + wordW > width) {
            if (lineW > 0) {
                lines.add(currentLine);
                currentLine = new int[0];
            }
            currentLine = concat(currentLine, spaces); // Preserve trailing spaces on wrap
            currentLine = concat(currentLine, word);
        } else {
            currentLine = concat(currentLine, spaces);
            currentLine = concat(currentLine, word);
        }
        
        if (currentLine.length > 0 || lines.isEmpty()) {
             lines.add(currentLine);
        }

        return lines;
    }
    
    private static int cellWidth(int[] runes) {
        if (runes == null || runes.length == 0) return 0;
        StringBuilder sb = new StringBuilder();
        for (int r : runes) sb.appendCodePoint(r);
        return TextWidth.measureCellWidth(sb.toString());
    }

    private static int[] concat(int[] a, int[] b) {
        if (a.length == 0) return b;
        if (b.length == 0) return a;
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static int[] append(int[] a, int v) {
        int[] result = new int[a.length + 1];
        System.arraycopy(a, 0, result, 0, a.length);
        result[a.length] = v;
        return result;
    }
}
