package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.x.ansi.GraphemeCluster;
import com.williamcallahan.tui4j.compat.x.ansi.Method;
import com.williamcallahan.tui4j.compat.x.ansi.HardWrap;
import com.williamcallahan.tui4j.compat.x.ansi.WordWrap;

import java.util.List;

/**
 * Renders the textarea view.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} view logic.
 */
final class TextAreaRenderer {

    private final TextAreaLayout layout;

    /**
     * Creates a renderer with layout dependencies.
     *
     * @param layout layout calculator
     */
    TextAreaRenderer(TextAreaLayout layout) {
        this.layout = layout;
    }

    /**
     * Renders the textarea view.
     *
     * @param state textarea state
     * @return rendered view
     */
    String view(TextAreaState state) {
        if (value(state).isEmpty() && state.row == 0 && state.col == 0 && !state.placeholder.isEmpty()) {
            return placeholderView(state);
        }

        state.cursor.setTextStyle(state.style.computedCursorLine());

        StringBuilder builder = new StringBuilder();
        int displayLine = 0;
        int widestLineNumber = 0;
        TextAreaLineInfo lineInfo = layout.lineInfo(state);

        for (int l = 0; l < state.value.size(); l++) {
            int[] line = state.value.get(l);
            List<int[]> wrappedLines = state.wrapCache.wrap(line, state.width);

            var lineStyle = (state.row == l) ? state.style.computedCursorLine() : state.style.computedText();

            for (int wl = 0; wl < wrappedLines.size(); wl++) {
                int[] wrappedLine = wrappedLines.get(wl);
                String prompt = getPromptString(state, displayLine);
                prompt = state.style.computedPrompt().render(prompt);
                builder.append(lineStyle.render(prompt));
                displayLine++;

                String ln = "";
                if (state.showLineNumbers) {
                    if (wl == 0) {
                        if (state.row == l) {
                            ln = lineStyle.render(state.style.computedCursorLineNumber()
                                    .render(formatLineNumber(state, l + 1)));
                        } else {
                            ln = lineStyle.render(state.style.computedLineNumber()
                                    .render(formatLineNumber(state, l + 1)));
                        }
                    } else {
                        if (state.row == l) {
                            ln = lineStyle.render(state.style.computedCursorLineNumber()
                                    .render(formatLineNumber(state, " ")));
                        } else {
                            ln = lineStyle.render(state.style.computedLineNumber()
                                    .render(formatLineNumber(state, " ")));
                        }
                    }
                    builder.append(ln);
                }

                int lnWidth = TextWidth.measureCellWidth(ln);
                if (lnWidth > widestLineNumber) {
                    widestLineNumber = lnWidth;
                }

                int strWidth = TextAreaRunes.cellWidth(wrappedLine);
                int padding = state.width - strWidth;
                if (strWidth > state.width && wrappedLine.length > 0) {
                    if (wrappedLine[wrappedLine.length - 1] == ' ') {
                        wrappedLine = TextAreaRunes.slice(wrappedLine, 0, wrappedLine.length - 1);
                    }
                    padding -= state.width - strWidth;
                }

                if (state.row == l && lineInfo.rowOffset() == wl) {
                    int[] beforeCursor = TextAreaRunes.slice(wrappedLine, 0, lineInfo.columnOffset());
                    builder.append(lineStyle.render(TextAreaRunes.toString(beforeCursor)));
                    if (state.col >= line.length && lineInfo.charOffset() >= state.width) {
                        state.cursor.setChar(" ");
                        builder.append(state.cursor.view());
                    } else {
                        String cursorChar = " ";  // Default to space when cursor at end of line
                        if (lineInfo.columnOffset() < wrappedLine.length) {
                            cursorChar = TextAreaRunes.toString(new int[]{wrappedLine[lineInfo.columnOffset()]});
                        }
                        state.cursor.setChar(cursorChar);
                        builder.append(lineStyle.render(state.cursor.view()));

                        if (lineInfo.columnOffset() + 1 < wrappedLine.length) {
                            builder.append(lineStyle.render(TextAreaRunes.toString(
                                    TextAreaRunes.slice(wrappedLine, lineInfo.columnOffset() + 1, wrappedLine.length))));
                        }
                    }
                } else {
                    builder.append(lineStyle.render(TextAreaRunes.toString(wrappedLine)));
                }

                builder.append(lineStyle.render(" ".repeat(Math.max(0, padding))));
                builder.append('\n');
            }
        }

        for (int i = 0; i < state.height; i++) {
            String prompt = getPromptString(state, displayLine);
            prompt = state.style.computedPrompt().render(prompt);
            builder.append(prompt);
            displayLine++;

            String leftGutter = String.valueOf(state.endOfBufferCharacter);
            int leftGutterWidth = TextWidth.measureCellWidth(leftGutter);
            int rightGapWidth = state.width - leftGutterWidth + widestLineNumber;
            String rightGap = " ".repeat(Math.max(0, rightGapWidth));
            builder.append(state.style.computedEndOfBuffer().render(leftGutter + rightGap));
            builder.append('\n');
        }

        state.viewport.setContent(builder.toString());
        ensureCursorVisible(state);
        return state.style.base().render(state.viewport.view());
    }

    /**
     * Renders the placeholder view.
     *
     * @param state textarea state
     * @return placeholder view
     */
    String placeholderView(TextAreaState state) {
        StringBuilder builder = new StringBuilder();
        String placeholder = state.placeholder;
        String wordWrapped = WordWrap.wordWrap(placeholder, state.width, "");
        String hardWrapped = HardWrap.hardWrap(wordWrapped, state.width, true);
        String trimmed = hardWrapped.trim();
        String[] lines = trimmed.isEmpty() ? new String[0] : trimmed.split("\n", -1);

        for (int i = 0; i < state.height; i++) {
            var lineStyle = state.style.computedPlaceholder();
            var lineNumberStyle = state.style.computedLineNumber();
            if (lines.length > i) {
                lineStyle = state.style.computedCursorLine();
                lineNumberStyle = state.style.computedCursorLineNumber();
            }

            String prompt = getPromptString(state, i);
            prompt = state.style.computedPrompt().render(prompt);
            builder.append(lineStyle.render(prompt));

            if (state.showLineNumbers) {
                String ln = "";
                switch (i) {
                    case 0 -> {
                        ln = String.valueOf(i + 1);
                        builder.append(lineStyle.render(lineNumberStyle.render(formatLineNumber(state, ln))));
                    }
                    default -> {
                        if (lines.length > i) {
                            builder.append(lineStyle.render(lineNumberStyle.render(formatLineNumber(state, " "))));
                        }
                    }
                }
            }

            if (i == 0 && lines.length > 0) {
                state.cursor.setTextStyle(state.style.computedPlaceholder());
                GraphemeCluster.StringResult result = GraphemeCluster.getFirstGraphemeClusterString(lines[0], Method.GRAPHEME_WIDTH);
                String cluster = result.cluster();
                String rest = lines[0].substring(cluster.length());
                state.cursor.setChar(cluster);
                builder.append(lineStyle.render(state.cursor.view()));
                builder.append(lineStyle.render(state.style.computedPlaceholder().render(rest)));
            } else if (lines.length > i) {
                int lineWidth = TextWidth.measureCellWidth(lines[i]);
                int padding = Math.max(0, state.width - lineWidth);
                builder.append(lineStyle.render(state.style.computedPlaceholder()
                        .render(lines[i] + " ".repeat(padding))));
            } else {
                builder.append(state.style.computedEndOfBuffer()
                        .render(String.valueOf(state.endOfBufferCharacter)));
            }

            builder.append('\n');
        }

        state.viewport.setContent(builder.toString());
        return state.style.base().render(state.viewport.view());
    }

    /**
     * Returns the prompt for the given display line.
     *
     * @param state textarea state
     * @param displayLine display line index
     * @return prompt string
     */
    private static String getPromptString(TextAreaState state, int displayLine) {
        String prompt = state.prompt;
        if (state.promptFunc == null) {
            return prompt;
        }
        prompt = state.promptFunc.apply(displayLine);
        int promptWidth = TextWidth.measureCellWidth(prompt);
        if (promptWidth < state.promptWidth) {
            prompt = " ".repeat(state.promptWidth - promptWidth) + prompt;
        }
        return prompt;
    }

    /**
     * Formats a line number with padding for display.
     *
     * @param state textarea state
     * @param value line number value
     * @return formatted line number
     */
    private static String formatLineNumber(TextAreaState state, Object value) {
        int digits = String.valueOf(state.maxHeight).length();
        return String.format(" %" + digits + "s ", value);
    }

    /**
     * Returns the textarea value as a string.
     *
     * @param state textarea state
     * @return text value
     */
    private static String value(TextAreaState state) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < state.value.size(); i++) {
            builder.append(TextAreaRunes.toString(state.value.get(i)));
            if (i < state.value.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    /**
     * Ensures the cursor line is visible in the viewport.
     *
     * @param state textarea state
     */
    private void ensureCursorVisible(TextAreaState state) {
        int cursorRow = layout.cursorLineNumber(state);
        int minimum = state.viewport.getYOffset();
        int maximum = minimum + state.viewport.getHeight() - 1;
        if (cursorRow < minimum) {
            state.viewport.scrollUp(minimum - cursorRow);
        } else if (cursorRow > maximum) {
            state.viewport.scrollDown(cursorRow - maximum);
        }
    }
}
