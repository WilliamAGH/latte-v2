package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * RGBColor is a hex-encoded color, e.g. "#ff0000"
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class RGBColor implements TerminalColor, RGBSupplier {

    private final ColorApplyStrategy colorApplyStrategy;
    private final RGB rgb;

    public RGBColor(String hexValue) {
        this.colorApplyStrategy = new HexColorApplyStrategy(hexValue);
        this.rgb = RGB.fromHexString(hexValue);
    }

    public RGBColor(int r, int g, int b) {
        this.rgb = new RGB(r, g, b);
        this.colorApplyStrategy = rgb().asColorApplyStrategy();
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForForeground(style);
    }

    @Override
    public RGB rgb() {
        return rgb;
    }

    public ANSI256Color toANSI256Color() {
        return rgb.toANSI256Color();
    }
}
