package com.williamcallahan.tui4j.compat.bubbles.textarea;

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
                    int lineW = TextAreaRunes.cellWidth(currentLine);
                    int wordW = TextAreaRunes.cellWidth(word);
                    int spaceW = TextAreaRunes.cellWidth(spaces);

                    // Check if adding the spaces + word exceeds the width
                    if (lineW + spaceW + wordW > width) {
                        if (lineW > 0) {
                            // Current line is full, push it.
                            lines.add(currentLine);
                            currentLine = new int[0];
                            lineW = 0;
                        }
                        
                        // We must preserve spaces. Attach them to the start of the new line.
                        currentLine = TextAreaRunes.concat(currentLine, spaces);
                        
                        // Check if spaces + word fits on the NEW line
                        // (e.g. width 5, "hello world" -> "hello" / " " / "world")
                        int newLineW = TextAreaRunes.cellWidth(currentLine);
                        if (newLineW + wordW > width) {
                             if (newLineW > 0) {
                                 lines.add(currentLine);
                                 currentLine = new int[0];
                             }
                             currentLine = TextAreaRunes.concat(currentLine, word);
                        } else {
                             currentLine = TextAreaRunes.concat(currentLine, word);
                        }
                    } else {
                        // Fits on current line
                        currentLine = TextAreaRunes.concat(currentLine, spaces);
                        currentLine = TextAreaRunes.concat(currentLine, word);
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
        int lineW = TextAreaRunes.cellWidth(currentLine);
        int wordW = TextAreaRunes.cellWidth(word);
        int spaceW = TextAreaRunes.cellWidth(spaces);

        if (lineW + spaceW + wordW > width) {
            if (lineW > 0) {
                lines.add(currentLine);
                currentLine = new int[0];
            }
            currentLine = TextAreaRunes.concat(currentLine, spaces);
            
            // Check overflow on new line again
            int newLineW = TextAreaRunes.cellWidth(currentLine);
            if (newLineW + wordW > width) {
                 if (newLineW > 0) {
                     lines.add(currentLine);
                     currentLine = new int[0];
                 }
                 currentLine = TextAreaRunes.concat(currentLine, word);
            } else {
                 currentLine = TextAreaRunes.concat(currentLine, word);
            }
        } else {
            currentLine = TextAreaRunes.concat(currentLine, spaces);
            currentLine = TextAreaRunes.concat(currentLine, word);
        }
        
        if (currentLine.length > 0 || lines.isEmpty()) {
             lines.add(currentLine);
        }

        return lines;
    }

    private static int[] append(int[] a, int v) {
        return TextAreaRunes.concat(a, new int[]{v});
    }
}
