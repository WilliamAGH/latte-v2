package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

/**
 * RGB color representation with float channels (0-1).
 * <p>
 * Lipgloss: color.go.
 *
 * @param r red channel (0-1)
 * @param g green channel (0-1)
 * @param b blue channel (0-1)
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.RGB} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public record RGB(float r, float g, float b) {

    /**
     * Converts to canonical RGB.
     *
     * @return canonical RGB
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.RGB toCanonical() {
        return new com.williamcallahan.tui4j.compat.lipgloss.color.RGB(r, g, b);
    }

    /**
     * Creates from canonical RGB.
     *
     * @param canonical the canonical RGB
     * @return deprecated RGB shim
     */
    @Deprecated(since = "0.3.0")
    public static RGB fromCanonical(com.williamcallahan.tui4j.compat.lipgloss.color.RGB canonical) {
        return new RGB(canonical.r(), canonical.g(), canonical.b());
    }

    /**
     * Creates a black RGB color.
     *
     * @return black RGB
     */
    @Deprecated(since = "0.3.0")
    public static RGB black() {
        return new RGB(0, 0, 0);
    }

    /**
     * Creates an RGB color from a hex string.
     *
     * @param hexValue the hex color value (e.g., "#FF0000" or "FF0000")
     * @return the RGB color
     */
    @Deprecated(since = "0.3.0")
    public static RGB fromHexString(String hexValue) {
        var canonical = com.williamcallahan.tui4j.compat.lipgloss.color.RGB.fromHexString(hexValue);
        return fromCanonical(canonical);
    }

    /**
     * Converts this RGB to the nearest ANSI 256 color.
     *
     * @return the ANSI 256 color
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color toANSI256Color() {
        return toCanonical().toANSI256Color();
    }

    /**
     * Converts this RGB to HSL.
     *
     * @return the HSL representation
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.HSL toHSL() {
        return toCanonical().toHSL();
    }

    /**
     * Calculates the HSLuv distance to another RGB value.
     *
     * @param other other RGB value
     * @return distance
     */
    @Deprecated(since = "0.3.0")
    public float distanceHSLuv(RGB other) {
        return toCanonical().distanceHSLuv(other.toCanonical());
    }

    /**
     * Creates a color apply strategy for this RGB value.
     *
     * @return color apply strategy
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ColorApplyStrategy asColorApplyStrategy() {
        return toCanonical().asColorApplyStrategy();
    }

    /**
     * Converts this RGB to an integer representation.
     *
     * @return integer RGB value
     */
    @Deprecated(since = "0.3.0")
    public int toInt() {
        return toCanonical().toInt();
    }

    /**
     * Returns the string representation of this RGB value.
     *
     * @return formatted RGB string
     */
    @Deprecated(since = "0.3.0")
    @Override
    public String toString() {
        return toCanonical().toString();
    }
}
