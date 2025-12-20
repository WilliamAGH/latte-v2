package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.padding;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss padding decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class PaddingDecorator {

    public static String applyPadding(String input,
                                      int topPadding,
                                      int rightPadding,
                                      int bottomPadding,
                                      int leftPadding,
                                      AttributedStyle attributedStyle,
                                      Renderer renderer) {
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

    public static String padLeft(String input, int leftPadding, AttributedStyle attributedStyle, Renderer renderer) {
        return pad(input, -leftPadding, attributedStyle, renderer);
    }

    public static String padRight(String input, int rightPadding, AttributedStyle attributedStyle, Renderer renderer) {
        return pad(input, rightPadding, attributedStyle, renderer);
    }

    public static String pad(String str, int n, AttributedStyle attributedStyle, Renderer renderer) {
        if (n == 0) {
            return str;
        }
        String padding = " ".repeat(Math.abs(n));

        if (attributedStyle != null) {
            padding = new AttributedString(padding, attributedStyle).toAnsi();
        }

        StringBuilder b = new StringBuilder();
        String[] lines = str.split("\n");

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
