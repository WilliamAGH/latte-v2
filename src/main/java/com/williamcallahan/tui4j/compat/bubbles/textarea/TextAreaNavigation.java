package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.ibm.icu.lang.UCharacter;
import com.williamcallahan.tui4j.ansi.TextWidth;

/**
 * Cursor navigation and word transformations.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} navigation logic.
 */
final class TextAreaNavigation {

    private final TextAreaLayout layout;

    /**
     * Creates a navigation helper.
     *
     * @param layout layout calculator
     */
    TextAreaNavigation(TextAreaLayout layout) {
        this.layout = layout;
    }

    /**
     * Moves the cursor to the given column.
     *
     * @param state textarea state
     * @param column target column
     */
    void setCursor(TextAreaState state, int column) {
        int[] line = state.value.get(state.row);
        state.col = clamp(column, 0, line.length);
        state.lastCharOffset = 0;
    }

    /**
     * Moves the cursor to the start of the line.
     *
     * @param state textarea state
     */
    void cursorStart(TextAreaState state) {
        setCursor(state, 0);
    }

    /**
     * Moves the cursor to the end of the line.
     *
     * @param state textarea state
     */
    void cursorEnd(TextAreaState state) {
        setCursor(state, state.value.get(state.row).length);
    }

    /**
     * Moves the cursor down one visual line.
     *
     * @param state textarea state
     */
    void cursorDown(TextAreaState state) {
        TextAreaLineInfo li = layout.lineInfo(state);
        int charOffset = Math.max(state.lastCharOffset, li.charOffset());
        state.lastCharOffset = charOffset;

        if (li.rowOffset() + 1 >= li.height() && state.row < state.value.size() - 1) {
            state.row++;
            state.col = 0;
        } else {
            int trailingSpace = 2;
            int rowLen = state.value.get(state.row).length;
            state.col = rowLen == 0 ? 0 : Math.max(0, Math.min(li.startColumn() + li.width() + trailingSpace, rowLen - 1));
        }

        TextAreaLineInfo nli = layout.lineInfo(state);
        state.col = nli.startColumn();

        if (nli.width() <= 0) {
            return;
        }

        int offset = 0;
        while (offset < charOffset) {
            int[] line = state.value.get(state.row);
            if (state.row >= state.value.size() || state.col >= line.length || offset >= nli.charWidth() - 1) {
                break;
            }
            offset += TextWidth.measureCellWidth(TextAreaRunes.toString(new int[]{line[state.col]}));
            state.col++;
        }
    }

    /**
     * Moves the cursor up one visual line.
     *
     * @param state textarea state
     */
    void cursorUp(TextAreaState state) {
        TextAreaLineInfo li = layout.lineInfo(state);
        int charOffset = Math.max(state.lastCharOffset, li.charOffset());
        state.lastCharOffset = charOffset;

        if (li.rowOffset() <= 0 && state.row > 0) {
            state.row--;
            state.col = state.value.get(state.row).length;
        } else {
            int trailingSpace = 2;
            state.col = Math.max(0, li.startColumn() - trailingSpace);
        }

        TextAreaLineInfo nli = layout.lineInfo(state);
        state.col = nli.startColumn();

        if (nli.width() <= 0) {
            return;
        }

        int offset = 0;
        while (offset < charOffset) {
            int[] line = state.value.get(state.row);
            if (state.col >= line.length || offset >= nli.charWidth() - 1) {
                break;
            }
            offset += TextWidth.measureCellWidth(TextAreaRunes.toString(new int[]{line[state.col]}));
            state.col++;
        }
    }

    /**
     * Moves the cursor one character to the right.
     *
     * @param state textarea state
     */
    void characterRight(TextAreaState state) {
        int[] line = state.value.get(state.row);
        if (state.col < line.length) {
            setCursor(state, state.col + 1);
        } else if (state.row < state.value.size() - 1) {
            state.row++;
            cursorStart(state);
        }
    }

    /**
     * Moves the cursor one character to the left.
     *
     * @param state textarea state
     * @param insideLine whether to stay inside the previous line
     */
    void characterLeft(TextAreaState state, boolean insideLine) {
        if (state.col == 0 && state.row != 0) {
            state.row--;
            cursorEnd(state);
            if (!insideLine) {
                return;
            }
        }
        if (state.col > 0) {
            setCursor(state, state.col - 1);
        }
    }

    /**
     * Moves the cursor one word to the left.
     *
     * @param state textarea state
     */
    void wordLeft(TextAreaState state) {
        while (true) {
            if (state.row == 0 && state.col == 0) {
                break;
            }
            characterLeft(state, true);
            int[] line = state.value.get(state.row);
            if (state.col < line.length && !UCharacter.isWhitespace(line[state.col])) {
                break;
            }
        }

        while (state.col > 0) {
            int[] line = state.value.get(state.row);
            if (UCharacter.isWhitespace(line[state.col - 1])) {
                break;
            }
            setCursor(state, state.col - 1);
        }
    }

    /**
     * Moves the cursor one word to the right.
     *
     * @param state textarea state
     */
    void wordRight(TextAreaState state) {
        doWordRight(state, (charIdx, pos) -> {});
    }

    /**
     * Uppercases the word to the right of the cursor.
     *
     * @param state textarea state
     */
    void uppercaseRight(TextAreaState state) {
        doWordRight(state, (charIdx, pos) -> {
            int[] line = state.value.get(state.row);
            line[pos] = Character.toUpperCase(line[pos]);
        });
    }

    /**
     * Lowercases the word to the right of the cursor.
     *
     * @param state textarea state
     */
    void lowercaseRight(TextAreaState state) {
        doWordRight(state, (charIdx, pos) -> {
            int[] line = state.value.get(state.row);
            line[pos] = Character.toLowerCase(line[pos]);
        });
    }

    /**
     * Capitalizes the word to the right of the cursor.
     *
     * @param state textarea state
     */
    void capitalizeRight(TextAreaState state) {
        doWordRight(state, (charIdx, pos) -> {
            if (charIdx == 0) {
                int[] line = state.value.get(state.row);
                line[pos] = Character.toTitleCase(line[pos]);
            }
        });
    }

    /**
     * Moves the cursor to the start of the input.
     *
     * @param state textarea state
     */
    void moveToBegin(TextAreaState state) {
        state.row = 0;
        setCursor(state, 0);
    }

    /**
     * Moves the cursor to the end of the input.
     *
     * @param state textarea state
     */
    void moveToEnd(TextAreaState state) {
        state.row = state.value.size() - 1;
        setCursor(state, state.value.get(state.row).length);
    }

    /**
     * Applies a word operation while moving right.
     *
     * @param state textarea state
     * @param consumer word consumer
     */
    private void doWordRight(TextAreaState state, WordConsumer consumer) {
        int[] line = state.value.get(state.row);
        while (state.col >= line.length || UCharacter.isWhitespace(line[state.col])) {
            if (state.row == state.value.size() - 1 && state.col == line.length) {
                break;
            }
            characterRight(state);
            line = state.value.get(state.row);
        }

        int charIdx = 0;
        while (state.col < line.length && !UCharacter.isWhitespace(line[state.col])) {
            consumer.accept(charIdx, state.col);
            setCursor(state, state.col + 1);
            charIdx++;
        }
    }

    /**
     * Word operation consumer.
     */
    @FunctionalInterface
    interface WordConsumer {

        /**
         * Accepts a word character position.
         *
         * @param charIndex index within the word
         * @param pos position within the line
         */
        void accept(int charIndex, int pos);
    }

    /**
     * Clamps a value to the inclusive range.
     *
     * @param value value to clamp
     * @param low minimum value
     * @param high maximum value
     * @return clamped value
     */
    private static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(high, value));
    }
}
