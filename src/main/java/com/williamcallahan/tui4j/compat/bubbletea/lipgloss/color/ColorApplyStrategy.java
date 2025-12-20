package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss color apply strategy.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public interface ColorApplyStrategy {
    AttributedStyle applyForForeground(AttributedStyle attributedStyle);
    AttributedStyle applyForBackground(AttributedStyle attributedStyle);
}

class ColorCodeApplyStrategy implements ColorApplyStrategy {

    private final int colorCode;

    ColorCodeApplyStrategy(int colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return attributedStyle.foreground(colorCode);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return attributedStyle.background(colorCode);
    }
}

class RGBAApplyStrategy implements ColorApplyStrategy {

    private final int r;
    private final int g;
    private final int b;

    public RGBAApplyStrategy(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGBAApplyStrategy(RGB rgb) {
        this.r = (int) (rgb.r() * 255);
        this.g = (int) (rgb.g() * 255);
        this.b = (int) (rgb.b() * 255);
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return attributedStyle.foreground(r, g, b);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return attributedStyle.background(r, g, b);
    }
}

class HexColorApplyStrategy implements ColorApplyStrategy {

    private final ColorApplyStrategy rgbaApplyStrategy;

    public HexColorApplyStrategy(String hexValue) {
        this.rgbaApplyStrategy = RGB.fromHexString(hexValue).asColorApplyStrategy();
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return rgbaApplyStrategy.applyForForeground(attributedStyle);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return rgbaApplyStrategy.applyForBackground(attributedStyle);
    }
}