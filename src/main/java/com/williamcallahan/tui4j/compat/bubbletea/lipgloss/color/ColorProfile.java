package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedCharSequence;

/**
 * Port of Lip Gloss color profile.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public enum ColorProfile {
    // TrueColor, 24-bit color profile
    TrueColor(AttributedCharSequence.TRUE_COLORS),
    // ANSI256, 8-bit color profile
    ANSI256(256),
    // ANSI, 4-bit color profile
    ANSI(16),
    // Ascii, uncolored profile
    Ascii(1);

    private final int colorsCount;

    ColorProfile(int colorsCount) {
        this.colorsCount = colorsCount;
    }

    /**
     * Color creates a {@link TerminalColor} from a string. Valid inputs are hex colors, as well as
     * ANSI color codes (0-15, 16-255).
     */
    public TerminalColor color(String color) {
        if (color == null || color.isBlank()) {
            return null;
        }
        TerminalColor terminalColor;

        if (color.startsWith("#")) {
            terminalColor = new RGBColor(color);
        } else {
            try {
                int colorCode = Integer.parseInt(color);
                if (colorCode < 16) {
                    terminalColor = new ANSIColor(colorCode);
                } else {
                    terminalColor = new ANSI256Color(colorCode);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return convert(terminalColor);
    }

    /**
     * Transforms given {@link TerminalColor} to a {@link TerminalColor} supported within the {@link ColorProfile}.
     */
    public TerminalColor convert(TerminalColor terminalColor) {
        if (this == Ascii) {
            return new NoColor();
        }

        if (terminalColor instanceof ANSIColor) {
            return terminalColor;
        } else if (terminalColor instanceof ANSI256Color ansi256Color) {
            if (this == ANSI) {
                return ansi256Color.toANSIColor();
            }
            return terminalColor;
        } else if (terminalColor instanceof RGBColor rgbColor) {
            if (this != TrueColor) {
                ANSI256Color ansi256Color = rgbColor.toANSI256Color();
                if (this == ANSI) {
                    return ansi256Color.toANSIColor();
                }
                return ansi256Color;
            }
            return terminalColor;
        }
        return terminalColor;
    }

    public int colorsCount() {
        return colorsCount;
    }
}
