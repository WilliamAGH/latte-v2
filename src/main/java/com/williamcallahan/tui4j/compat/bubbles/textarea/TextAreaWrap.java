package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.ibm.icu.lang.UCharacter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Word wrap for textarea input lines.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} wrap logic.
 */
final class TextAreaWrap {

    /**
     * Prevents instantiation.
     */
    private TextAreaWrap() {
    }

    /**
     * Wraps a line of runes to the given width.
     *
     * @param runes input runes
     * @param width wrap width
     * @return wrapped lines
     */
    static List<int[]> wrap(int[] runes, int width) {
        if (width <= 0) {
            return List.of(Arrays.copyOf(runes, runes.length));
        }

        List<int[]> lines = new ArrayList<>();
        lines.add(new int[0]);
        int row = 0;
        int spaces = 0;
        IntAccumulator word = new IntAccumulator();

        for (int rune : runes) {
            if (UCharacter.isWhitespace(rune)) {
                spaces++;
            } else {
                word.add(rune);
            }

            if (spaces > 0) {
                int currentLineWidth = TextAreaRunes.cellWidth(lines.get(row));
                int wordWidth = TextAreaRunes.cellWidth(word.toArray());
                if (currentLineWidth + wordWidth + spaces > width) {
                    row++;
                    lines.add(word.toArray());
                    lines.set(row, TextAreaRunes.concat(lines.get(row), repeatSpaces(spaces)));
                    spaces = 0;
                    word.clear();
                } else {
                    lines.set(row, TextAreaRunes.concat(lines.get(row), word.toArray()));
                    lines.set(row, TextAreaRunes.concat(lines.get(row), repeatSpaces(spaces)));
                    spaces = 0;
                    word.clear();
                }
            } else if (!word.isEmpty()) {
                int lastCharWidth = TextAreaRunes.cellWidth(new int[]{word.last()});
                int wordWidth = TextAreaRunes.cellWidth(word.toArray());
                if (wordWidth + lastCharWidth > width) {
                    if (lines.get(row).length > 0) {
                        row++;
                        lines.add(new int[0]);
                    }
                    lines.set(row, TextAreaRunes.concat(lines.get(row), word.toArray()));
                    word.clear();
                }
            }
        }

        int currentLineWidth = TextAreaRunes.cellWidth(lines.get(row));
        int wordWidth = TextAreaRunes.cellWidth(word.toArray());
        if (currentLineWidth + wordWidth + spaces >= width) {
            lines.add(word.toArray());
            row++;
            spaces++;
            lines.set(row, TextAreaRunes.concat(lines.get(row), repeatSpaces(spaces)));
        } else {
            lines.set(row, TextAreaRunes.concat(lines.get(row), word.toArray()));
            spaces++;
            lines.set(row, TextAreaRunes.concat(lines.get(row), repeatSpaces(spaces)));
        }

        return lines;
    }

    /**
     * Returns a run of space runes.
     *
     * @param count number of spaces
     * @return space runes
     */
    private static int[] repeatSpaces(int count) {
        if (count <= 0) {
            return new int[0];
        }
        int[] spaces = new int[count];
        Arrays.fill(spaces, ' ');
        return spaces;
    }

    /**
     * Simple growable int accumulator.
     */
    private static final class IntAccumulator {
        private int[] buffer = new int[8];
        private int size;

        /**
         * Appends a value.
         *
         * @param value value to append
         */
        void add(int value) {
            if (size >= buffer.length) {
                buffer = Arrays.copyOf(buffer, buffer.length * 2);
            }
            buffer[size++] = value;
        }

        /**
         * Returns whether the accumulator is empty.
         *
         * @return true if empty
         */
        boolean isEmpty() {
            return size == 0;
        }

        /**
         * Returns the last appended value.
         *
         * @return last value
         */
        int last() {
            return buffer[size - 1];
        }

        /**
         * Clears the accumulator.
         */
        void clear() {
            size = 0;
        }

        /**
         * Returns the accumulated values.
         *
         * @return array of values
         */
        int[] toArray() {
            return Arrays.copyOf(buffer, size);
        }
    }
}
