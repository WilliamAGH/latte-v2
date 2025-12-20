package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.margin;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.TextLines;
import org.jline.utils.AttributedStyle;

import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.padding.PaddingDecorator.padLeft;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.padding.PaddingDecorator.padRight;

/**
 * Port of Lip Gloss margin decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class MarginDecorator {

    public static String applyMargins(String input,
                                      int topMargin,
                                      int rightMargin,
                                      int bottomMargin,
                                      int leftMargin,
                                      AttributedStyle attributedStyle,
                                      Renderer renderer) {
        String padded = input;
        if (leftMargin > 0) {
            padded = padLeft(padded, leftMargin, attributedStyle, renderer);
        }
        if (rightMargin > 0) {
            padded = padRight(padded, rightMargin, attributedStyle, renderer);
        }

        int width = TextLines.fromText(input).widestLineLength();
        String spaces = " ".repeat(width);
        if (topMargin > 0) {
            padded = (spaces + "\n").repeat(topMargin) + padded;
        }
        if (bottomMargin > 0) {
            padded += ("\n" + spaces).repeat(bottomMargin);
        }
        return padded;
    }
}