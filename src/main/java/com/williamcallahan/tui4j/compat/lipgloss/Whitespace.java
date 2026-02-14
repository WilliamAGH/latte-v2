package com.williamcallahan.tui4j.compat.lipgloss;


import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor;

/**
 * Port of Lip Gloss whitespace rendering.
 * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
 */
public final class Whitespace {

    private Style style;
    private String chars;

    /**
     * Creates a whitespace renderer with the specified style.
     *
     * @param style style to apply
     */
    public Whitespace(Style style) {
        this.style = style;
    }

    /**
     * Port of the whitespace option hook.
     * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
     */
    public interface WhitespaceOption {
        /**
         * Applies this option to the whitespace.
         *
         * @param whitespace whitespace to configure
         */
        void apply(Whitespace whitespace);
    }

    /**
     * Port of the whitespace foreground option.
     * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
     *
     * @param color foreground color
     * @return whitespace option
     */
    public static WhitespaceOption WithWhitespaceForeground(TerminalColor color) {
        return whitespace -> whitespace.style = whitespace.style.foreground(color);
    }

    /**
     * Port of the whitespace background option.
     * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
     *
     * @param color background color
     * @return whitespace option
     */
    public static WhitespaceOption WithWhitespaceBackground(TerminalColor color) {
        return whitespace -> whitespace.style = whitespace.style.background(color);
    }

    /**
     * Port of the whitespace character option.
     * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
     *
     * @param chars whitespace characters
     * @return whitespace option
     */
    public static WhitespaceOption WithWhitespaceChars(String chars) {
        return whitespace -> whitespace.chars = chars;
    }

    /**
     * Port of the whitespace constructor helper.
     * Upstream: github.com/charmbracelet/lipgloss (whitespace.go)
     *
     * @param renderer renderer to use
     * @param options whitespace options
     * @return whitespace renderer
     */
    public static Whitespace newWhiteSpace(Renderer renderer, WhitespaceOption... options) {
        Whitespace whitespace = new Whitespace(renderer.newStyle());
        for (WhitespaceOption option : options) {
            option.apply(whitespace);
        }
        return whitespace;
    }

    /**
     * Renders whitespace of the specified width.
     *
     * @param width width in cells
     * @return rendered whitespace
     */
    public String render(int width) {
        if (chars == null || chars.isEmpty()) {
            chars = " ";
        }

        int[] codePoints = chars.codePoints().toArray();
        int j = 0;
        StringBuilder builder = new StringBuilder();
        int zeroWidthCount = 0;
        int i = 0;
        while (i < width) {
            int cp = codePoints[j];
            String glyph = new String(Character.toChars(cp));
            builder.append(glyph);
            int w = TextWidth.measureCellWidth(glyph);
            if (w == 0) {
                // Prevent infinite loop on zero-width characters
                zeroWidthCount++;
                if (zeroWidthCount >= codePoints.length) {
                    break;
                }
            } else {
                zeroWidthCount = 0;
                i += w;
            }
            j = (j + 1) % codePoints.length;
        }

        int shorty = width - TextWidth.measureCellWidth(builder.toString());
        if (shorty > 0) {
            builder.append(" ".repeat(shorty));
        }

        return style.render(builder.toString());
    }

}
