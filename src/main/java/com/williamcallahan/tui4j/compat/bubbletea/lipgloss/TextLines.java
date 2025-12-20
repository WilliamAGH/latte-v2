package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import com.williamcallahan.tui4j.ansi.TextWidth;

/**
 * Port of Lip Gloss text lines.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class TextLines {

    private int widestLineLength;
    private String[] lines;

    public static TextLines fromText(String text) {
        return new TextLines(text);
    }

    private TextLines(String text) {
        readTextLines(text);
    }

    private void readTextLines(String text) {
        this.lines = text.split("\n", -1);

        for (String line : lines) {
            int width = TextWidth.measureCellWidth(line);
            if (widestLineLength < width) {
                widestLineLength = width;
            }
        }
    }

    public int widestLineLength() {
        return widestLineLength;
    }

    public String[] lines() {
        return lines;
    }
}
