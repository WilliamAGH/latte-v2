package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * Port of Lip Gloss ansi 256 color.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class ANSI256Color implements TerminalColor, RGBSupplier {

    private final ColorCodeApplyStrategy applyStrategy;
    private final int colorCode;

    public ANSI256Color(int colorCode) {
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

    public ANSIColor toANSIColor() {
        int ansiColorCode = 0;
        float minDistance = Float.MAX_VALUE;
        RGB rgb = RGB.fromHexString(ANSIColors.ANSI_HEX[colorCode]);

        for (int colorIndex = 0; colorIndex <= 15; colorIndex++) {
            RGB candidate = RGB.fromHexString(ANSIColors.ANSI_HEX[colorIndex]);
            float distance = rgb.distanceHSLuv(candidate);

            if (distance < minDistance) {
                minDistance = distance;
                ansiColorCode = colorIndex;
            }
        }
        return new ANSIColor(ansiColorCode);
    }
}