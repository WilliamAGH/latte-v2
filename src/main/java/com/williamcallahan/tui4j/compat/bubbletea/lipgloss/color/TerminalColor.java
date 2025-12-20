package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss terminal color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public interface TerminalColor {
    AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer);
    AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer);
}
