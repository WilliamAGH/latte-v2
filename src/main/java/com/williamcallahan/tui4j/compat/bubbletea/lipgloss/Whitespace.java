package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;


import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;

/**
 * Port of Lip Gloss whitespace.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class Whitespace {
    private Style style;
    private String chars;

    public Whitespace(Style style) {
        this.style = style;
    }

    public interface WhitespaceOption {
        void apply(Whitespace whitespace);
    }

    public static WhitespaceOption WithWhitespaceForeground(TerminalColor color) {
        return whitespace -> whitespace.style = whitespace.style.foreground(color);
    }

    public static WhitespaceOption WithWhitespaceBackground(TerminalColor color) {
        return whitespace -> whitespace.style = whitespace.style.background(color);
    }

    public static WhitespaceOption WithWhitespaceChars(String chars) {
        return whitespace -> whitespace.chars = chars;
    }

    public static Whitespace newWhiteSpace(Renderer renderer, WhitespaceOption... options) {
        Whitespace whitespace = new Whitespace(renderer.newStyle());
        for (WhitespaceOption option : options) {
            option.apply(whitespace);
        }
        return whitespace;
    }

    public String render(int width) {
        if (chars == null || chars.isEmpty()) {
            chars = " ";
        }

        char[] runes = chars.toCharArray();
        int j = 0;

        StringBuilder builder = new StringBuilder();
        // Cycle through runes and print them into the whitespace.
        for (int i = 0; i < width;) {
            builder.append(runes[j]);
            j++;
            if (j >= runes.length) {
                j = 0;
            }
            i += TextWidth.measureCellWidth(String.valueOf(runes[j]));
        }

        int shorty = width - TextWidth.measureCellWidth(builder.toString());
        if (shorty > 0) {
            builder.append(" ".repeat(shorty));
        }

        return style.render(builder.toString());
    }

}
