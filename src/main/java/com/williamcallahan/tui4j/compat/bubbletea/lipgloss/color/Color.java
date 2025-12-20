package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class Color implements TerminalColor {

    public static Color color(String color) {
        return new Color(color);
    }

    private final String color;

    private Color(String color) {
        this.color = color;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return renderer
                .colorProfile()
                .color(color)
                .applyAsBackground(style, renderer);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return renderer
                .colorProfile()
                .color(color)
                .applyAsForeground(style, renderer);
    }
}
