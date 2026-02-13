package com.williamcallahan.tui4j.compat.lipgloss;

import org.jline.utils.AttributedCharSequence.ForceMode;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss padding rendering.
 * Upstream: lipgloss/style.go
 */
public class PaddingDecorator {

    /**
     * Creates a utility container for padding rendering.
     */
    public PaddingDecorator() {
    }

    /**
     * Applies padding around the provided input.
     *
     * @param input input content
     * @param topPadding top padding lines
     * @param rightPadding right padding columns
     * @param bottomPadding bottom padding lines
     * @param leftPadding left padding columns
     * @param attributedStyle optional style for padding spaces
     * @param renderer renderer for ANSI output
     * @return input content with padding applied
     */
    public static String applyPadding(
        String input,
        int topPadding,
        int rightPadding,
        int bottomPadding,
        int leftPadding,
        AttributedStyle attributedStyle,
        Renderer renderer
    ) {
        String padded = input;
        if (leftPadding > 0) {
            padded = padLeft(padded, leftPadding, attributedStyle, renderer);
        }
        if (rightPadding > 0) {
            padded = padRight(padded, rightPadding, attributedStyle, renderer);
        }
        if (topPadding > 0) {
            padded = "\n".repeat(topPadding) + padded;
        }
        if (bottomPadding > 0) {
            padded += "\n".repeat(bottomPadding);
        }
        return padded;
    }

    /**
     * Pads the input on the left.
     *
     * @param input input content
     * @param leftPadding number of columns to pad
     * @param attributedStyle optional style for padding spaces
     * @param renderer renderer for ANSI output
     * @return padded content
     */
    public static String padLeft(
        String input,
        int leftPadding,
        AttributedStyle attributedStyle,
        Renderer renderer
    ) {
        return pad(input, -leftPadding, attributedStyle, renderer);
    }

    /**
     * Pads the input on the right.
     *
     * @param input input content
     * @param rightPadding number of columns to pad
     * @param attributedStyle optional style for padding spaces
     * @param renderer renderer for ANSI output
     * @return padded content
     */
    public static String padRight(
        String input,
        int rightPadding,
        AttributedStyle attributedStyle,
        Renderer renderer
    ) {
        return pad(input, rightPadding, attributedStyle, renderer);
    }

    /**
     * Pads the input by the specified column count.
     *
     * @param str input content
     * @param n padding columns; negative pads left, positive pads right
     * @param attributedStyle optional style for padding spaces
     * @param renderer renderer for ANSI output
     * @return padded content
     */
    public static String pad(
        String str,
        int n,
        AttributedStyle attributedStyle,
        Renderer renderer
    ) {
        if (n == 0) {
            return str;
        }
        String padding = " ".repeat(Math.abs(n));

        if (attributedStyle != null) {
            padding = new AttributedString(padding, attributedStyle).toAnsi(
                renderer.colorProfile().colorsCount(),
                ForceMode.None
            );
        }

        StringBuilder b = new StringBuilder();
        String[] lines = str.split("\n", -1);

        for (int i = 0; i < lines.length; i++) {
            // pad right
            if (n > 0) {
                b.append(lines[i]);
                b.append(padding);
            }
            // pad left
            else {
                b.append(padding);
                b.append(lines[i]);
            }

            if (i != lines.length - 1) {
                b.append('\n');
            }
        }
        return b.toString();
    }
}
