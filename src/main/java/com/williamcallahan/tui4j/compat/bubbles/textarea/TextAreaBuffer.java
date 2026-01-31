package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.ibm.icu.lang.UCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * Text buffer mutations for textarea.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} buffer logic.
 *
 * @see <a href="https://github.com/charmbracelet/bubbles/blob/master/textarea/textarea.go">bubbles/textarea/textarea.go</a>
 */
final class TextAreaBuffer {

    /**
     * Inserts runes at the current cursor position.
     *
     * @param state textarea state
     * @param runes runes to insert
     */
    void insertRunesFromUserInput(TextAreaState state, int[] runes) {
        int[] sanitized = sanitize(state, runes);

        if (state.charLimit > 0) {
            int availSpace = state.charLimit - length(state);
            if (availSpace <= 0) {
                return;
            }
            if (availSpace < sanitized.length) {
                sanitized = TextAreaRunes.slice(sanitized, 0, availSpace);
            }
        }

        List<int[]> lines = splitLines(sanitized);

        int allowedHeight = Math.max(0, TextAreaDefaults.MAX_LINES - state.value.size() + 1);
        while (lines.size() > allowedHeight) {
            lines.remove(lines.size() - 1);
        }

        if (lines.isEmpty()) {
            return;
        }

        int[] currentLine = state.value.get(state.row);
        int[] tail = TextAreaRunes.slice(currentLine, state.col, currentLine.length);

        int[] head = TextAreaRunes.slice(currentLine, 0, state.col);
        int[] newCurrentLine = TextAreaRunes.concat(head, lines.get(0));
        state.value.set(state.row, newCurrentLine);
        state.col += lines.get(0).length;

        if (lines.size() > 1) {
            int originalRow = state.row;
            int numExtraLines = lines.size() - 1;

            List<int[]> newValue = new ArrayList<>(state.value.size() + numExtraLines);
            newValue.addAll(state.value.subList(0, originalRow + 1));

            for (int i = 1; i < lines.size(); i++) {
                state.row++;
                newValue.add(lines.get(i));
            }

            newValue.addAll(state.value.subList(originalRow + 1, state.value.size()));
            state.value = newValue;
            state.col = lines.get(lines.size() - 1).length;
        }

        int[] finalLine = TextAreaRunes.concat(state.value.get(state.row), tail);
        state.value.set(state.row, finalLine);
        setCursor(state, state.col);
    }

    /**
     * Deletes all text before the cursor.
     *
     * @param state textarea state
     */
    void deleteBeforeCursor(TextAreaState state) {
        int[] line = state.value.get(state.row);
        state.value.set(state.row, TextAreaRunes.slice(line, state.col, line.length));
        setCursor(state, 0);
    }

    /**
     * Deletes all text after the cursor.
     *
     * @param state textarea state
     */
    void deleteAfterCursor(TextAreaState state) {
        int[] line = state.value.get(state.row);
        state.value.set(state.row, TextAreaRunes.slice(line, 0, state.col));
        setCursor(state, state.value.get(state.row).length);
    }

    /**
     * Deletes the word to the left of the cursor.
     *
     * @param state textarea state
     */
    void deleteWordLeft(TextAreaState state) {
        int[] line = state.value.get(state.row);
        if (state.col == 0 || line.length == 0) {
            return;
        }

        int oldCol = state.col;
        setCursor(state, state.col - 1);
        while (state.col > 0 && UCharacter.isWhitespace(line[state.col])) {
            setCursor(state, state.col - 1);
        }

        while (state.col > 0) {
            if (!UCharacter.isWhitespace(line[state.col])) {
                setCursor(state, state.col - 1);
            } else {
                setCursor(state, state.col + 1);
                break;
            }
        }

        int[] newLine = TextAreaRunes.concat(
                TextAreaRunes.slice(line, 0, state.col),
                TextAreaRunes.slice(line, oldCol, line.length));
        state.value.set(state.row, newLine);
    }

    /**
     * Deletes the word to the right of the cursor.
     *
     * @param state textarea state
     */
    void deleteWordRight(TextAreaState state) {
        int[] line = state.value.get(state.row);
        if (state.col >= line.length || line.length == 0) {
            return;
        }

        int oldCol = state.col;

        while (state.col < line.length && UCharacter.isWhitespace(line[state.col])) {
            setCursor(state, state.col + 1);
        }

        while (state.col < line.length) {
            if (!UCharacter.isWhitespace(line[state.col])) {
                setCursor(state, state.col + 1);
            } else {
                break;
            }
        }

        int[] newLine = TextAreaRunes.concat(
                TextAreaRunes.slice(line, 0, oldCol),
                TextAreaRunes.slice(line, state.col, line.length));
        state.value.set(state.row, newLine);
        setCursor(state, oldCol);
    }

    /**
     * Deletes the character before the cursor.
     *
     * @param state textarea state
     */
    void deleteCharacterBackward(TextAreaState state) {
        int[] line = state.value.get(state.row);
        setCursor(state, clamp(state.col, 0, line.length));
        if (state.col <= 0) {
            mergeLineAbove(state, state.row);
            return;
        }
        if (line.length > 0) {
            int[] newLine = TextAreaRunes.removeRange(line, Math.max(0, state.col - 1), state.col);
            state.value.set(state.row, newLine);
            if (state.col > 0) {
                setCursor(state, state.col - 1);
            }
        }
    }

    /**
     * Deletes the character after the cursor.
     *
     * @param state textarea state
     */
    void deleteCharacterForward(TextAreaState state) {
        int[] line = state.value.get(state.row);
        if (line.length > 0 && state.col < line.length) {
            int[] newLine = TextAreaRunes.removeRange(line, state.col, Math.min(line.length, state.col + 1));
            state.value.set(state.row, newLine);
        }
        if (state.col >= state.value.get(state.row).length) {
            mergeLineBelow(state, state.row);
        }
    }

    /**
     * Splits the current line at the cursor.
     *
     * @param state textarea state
     * @param splitRow row to split
     * @param splitCol column to split
     */
    void splitLine(TextAreaState state, int splitRow, int splitCol) {
        int[] line = state.value.get(splitRow);
        int[] head = TextAreaRunes.slice(line, 0, splitCol);
        int[] tail = TextAreaRunes.slice(line, splitCol, line.length);

        state.value.add(splitRow + 1, tail);
        state.value.set(splitRow, head);

        state.col = 0;
        state.row++;
    }

    /**
     * Merges the current line with the line below.
     *
     * @param state textarea state
     * @param mergeRow row to merge
     */
    void mergeLineBelow(TextAreaState state, int mergeRow) {
        if (mergeRow >= state.value.size() - 1) {
            return;
        }

        int[] newLine = TextAreaRunes.concat(state.value.get(mergeRow), state.value.get(mergeRow + 1));
        state.value.set(mergeRow, newLine);
        state.value.remove(mergeRow + 1);
    }

    /**
     * Merges the current line with the line above.
     *
     * @param state textarea state
     * @param mergeRow row to merge
     */
    void mergeLineAbove(TextAreaState state, int mergeRow) {
        if (mergeRow <= 0) {
            return;
        }

        state.col = state.value.get(mergeRow - 1).length;
        state.row = mergeRow - 1;

        int[] newLine = TextAreaRunes.concat(state.value.get(mergeRow - 1), state.value.get(mergeRow));
        state.value.set(mergeRow - 1, newLine);
        state.value.remove(mergeRow);
    }

    /**
     * Transposes the rune at the cursor with the one before it.
     *
     * @param state textarea state
     */
    void transposeLeft(TextAreaState state) {
        int[] line = state.value.get(state.row);
        if (state.col == 0 || line.length < 2) {
            return;
        }
        if (state.col >= line.length) {
            setCursor(state, state.col - 1);
        }
        int temp = line[state.col - 1];
        line[state.col - 1] = line[state.col];
        line[state.col] = temp;
        if (state.col < line.length) {
            setCursor(state, state.col + 1);
        }
    }

    /**
     * Returns the current content length in cells.
     *
     * @param state textarea state
     * @return length in cells
     */
    int length(TextAreaState state) {
        int length = 0;
        for (int[] row : state.value) {
            length += TextAreaRunes.cellWidth(row);
        }
        return length + state.value.size() - 1;
    }

    /**
     * Resets the buffer to an empty line.
     *
     * @param state textarea state
     */
    void reset(TextAreaState state) {
        state.value = new ArrayList<>();
        state.value.add(new int[0]);
        state.col = 0;
        state.row = 0;
        setCursor(state, 0);
    }

    /** Clamps a value to the inclusive range. */
    private static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(high, value));
    }

    /** Sets the cursor column and resets the cached offset. */
    private static void setCursor(TextAreaState state, int column) {
        int[] line = state.value.get(state.row);
        state.col = clamp(column, 0, line.length);
        state.lastCharOffset = 0;
    }

    /** Sanitizes runes using the configured sanitizer. */
    private static int[] sanitize(TextAreaState state, int[] runes) {
        String raw = TextAreaRunes.toString(runes);
        char[] sanitized = state.sanitizer.sanitize(raw.toCharArray());
        return TextAreaRunes.toCodePoints(sanitized);
    }

    /** Splits runes into newline-separated line arrays. */
    private static List<int[]> splitLines(int[] runes) {
        List<int[]> lines = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < runes.length; i++) {
            if (runes[i] == '\n') {
                lines.add(TextAreaRunes.slice(runes, start, i));
                start = i + 1;
            }
        }
        if (start <= runes.length) {
            lines.add(TextAreaRunes.slice(runes, start, runes.length));
        }
        return lines;
    }
}
