package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedCharSequence;

/**
 * Port of Lip Gloss color profile.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum ColorProfile {
    /** TrueColor, 24-bit color profile. */
    @Deprecated(since = "0.3.0")
    TrueColor(AttributedCharSequence.TRUE_COLORS),
    /** ANSI256, 8-bit color profile. */
    @Deprecated(since = "0.3.0")
    ANSI256(256),
    /** ANSI, 4-bit color profile. */
    @Deprecated(since = "0.3.0")
    ANSI(16),
    /** Ascii, uncolored profile. */
    @Deprecated(since = "0.3.0")
    Ascii(1);

    private final int colorsCount;

    ColorProfile(int colorsCount) {
        this.colorsCount = colorsCount;
    }

    /**
     * Color creates a {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor} from a string. Valid inputs are hex colors, as well as
     * ANSI color codes (0-15, 16-255).
     *
     * @param color color string
     * @return terminal color, or null when parsing fails
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color(
        String color
    ) {
        if (color == null || color.isBlank()) {
            return null;
        }
        com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor terminalColor;

        if (color.startsWith("#")) {
            terminalColor =
                new com.williamcallahan.tui4j.compat.lipgloss.color.RGBColor(
                    color
                );
        } else {
            try {
                int colorCode = Integer.parseInt(color);
                if (colorCode < 16) {
                    terminalColor =
                        new com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor(
                            colorCode
                        );
                } else {
                    terminalColor =
                        new com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color(
                            colorCode
                        );
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return convertCanonical(terminalColor);
    }

    /**
     * Transforms given {@link TerminalColor} to a {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor}
     * supported within the {@link ColorProfile}.
     *
     * @param terminalColor terminal color
     * @return compatible terminal color
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor convert(
        TerminalColor terminalColor
    ) {
        if (terminalColor == null) {
            return null;
        }
        return convertCanonical(terminalColor);
    }

    private com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor convertCanonical(
        com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor terminalColor
    ) {
        if (this == Ascii) {
            return new com.williamcallahan.tui4j.compat.lipgloss.color.NoColor();
        }

        if (
            terminalColor instanceof
                com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor
        ) {
            return terminalColor;
        } else if (
            terminalColor instanceof
                com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color ansi256Color
        ) {
            if (this == ANSI) {
                return ansi256Color.toANSIColor();
            }
            return terminalColor;
        } else if (
            terminalColor instanceof
                com.williamcallahan.tui4j.compat.lipgloss.color.RGBColor rgbColor
        ) {
            if (this != TrueColor) {
                com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color ansi256Color =
                    rgbColor.toANSI256Color();
                if (this == ANSI) {
                    return ansi256Color.toANSIColor();
                }
                return ansi256Color;
            }
            return terminalColor;
        }
        return terminalColor;
    }

    /**
     * Returns the number of colors available for this profile.
     *
     * @return color count
     */
    @Deprecated(since = "0.3.0")
    public int colorsCount() {
        return colorsCount;
    }
}
