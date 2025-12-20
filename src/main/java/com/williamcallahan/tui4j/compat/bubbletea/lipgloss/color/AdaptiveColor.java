package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss adaptive color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class AdaptiveColor implements TerminalColor {

    private final Color light;
    private final Color dark;

    public AdaptiveColor(String light, String dark) {
        this.light = Color.color(light);
        this.dark = Color.color(dark);
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        if (renderer.hasDarkBackground()) {
            return dark.applyAsBackground(style, renderer);
        }
        return light.applyAsBackground(style, renderer);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        if (renderer.hasDarkBackground()) {
            return dark.applyAsForeground(style, renderer);
        }
        return light.applyAsForeground(style, renderer);
    }
}
