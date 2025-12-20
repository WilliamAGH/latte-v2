package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss no color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class NoColor implements TerminalColor{

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return style;
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return style;
    }
}
