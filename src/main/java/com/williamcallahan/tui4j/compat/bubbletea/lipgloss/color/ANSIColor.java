package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss ansi color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class ANSIColor implements TerminalColor, RGBSupplier {

    private final ColorApplyStrategy applyStrategy;
    private final int colorCode;

    // ANSIColor is a color (0-15) as defined by the ANSI Standard.
    public ANSIColor(int colorCode) {
        this.applyStrategy = new ColorCodeApplyStrategy(colorCode);
        this.colorCode = colorCode;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return applyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return applyStrategy.applyForForeground(style);
    }

    public RGB rgb() {
        return RGB.fromHexString(ANSIColors.ANSI_HEX[colorCode]);
    }
}
