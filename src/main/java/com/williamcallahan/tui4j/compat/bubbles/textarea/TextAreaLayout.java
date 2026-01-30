package com.williamcallahan.tui4j.compat.bubbles.textarea;

/**
 * Layout calculations for textarea cursor and wrapping.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} line info logic.
 */
final class TextAreaLayout {

    /**
     * Returns information about the current line and cursor position.
     *
     * @param state textarea state
     * @return line info
     */
    TextAreaLineInfo lineInfo(TextAreaState state) {
        int[] current = state.value.get(state.row);
        var grid = state.wrapCache.wrap(current, state.width);

        int counter = 0;
        for (int i = 0; i < grid.size(); i++) {
            int[] line = grid.get(i);
            if (counter + line.length == state.col && i + 1 < grid.size()) {
                return new TextAreaLineInfo()
                        .charOffset(0)
                        .columnOffset(0)
                        .height(grid.size())
                        .rowOffset(i + 1)
                        .startColumn(state.col)
                        .width(grid.get(i + 1).length)
                        .charWidth(TextAreaRunes.cellWidth(line));
            }

            if (counter + line.length >= state.col) {
                int[] prefix = TextAreaRunes.slice(line, 0, Math.max(0, state.col - counter));
                int charOffset = TextAreaRunes.cellWidth(prefix);
                return new TextAreaLineInfo()
                        .charOffset(charOffset)
                        .columnOffset(state.col - counter)
                        .height(grid.size())
                        .rowOffset(i)
                        .startColumn(counter)
                        .width(line.length)
                        .charWidth(TextAreaRunes.cellWidth(line));
            }

            counter += line.length;
        }
        return new TextAreaLineInfo();
    }

    /**
     * Returns the wrapped line number the cursor is on.
     *
     * @param state textarea state
     * @return cursor line number
     */
    int cursorLineNumber(TextAreaState state) {
        int line = 0;
        for (int i = 0; i < state.row; i++) {
            line += state.wrapCache.wrap(state.value.get(i), state.width).size();
        }
        line += lineInfo(state).rowOffset();
        return line;
    }
}
