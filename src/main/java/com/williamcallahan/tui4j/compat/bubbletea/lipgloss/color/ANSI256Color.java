package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * ANSI 256-color representation for the Bubble Tea-compatible API surface.
 * <p>
 * Lipgloss: color.go.
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class ANSI256Color implements TerminalColor, RGBSupplier {

    private final com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color delegate;

    /**
     * Creates an ANSI 256 color from a color code.
     *
     * @param colorCode ANSI 256 color code (0-255)
     */
    @Deprecated(since = "0.3.0")
    public ANSI256Color(int colorCode) {
        this.delegate =
            new com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color(
                colorCode
            );
    }

    /**
     * Applies this ANSI 256 color as a background.
     *
     * @param style style to update
     * @param renderer renderer context
     * @return updated style
     */
    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsBackground(
        AttributedStyle style,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return delegate.applyAsBackground(style, renderer.toCanonical());
    }

    /**
     * Applies this ANSI 256 color as a background using the canonical renderer.
     *
     * @param style style to update
     * @param renderer canonical renderer context
     * @return updated style
     */
    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsBackground(
        AttributedStyle style,
        com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
    ) {
        return delegate.applyAsBackground(style, renderer);
    }

    /**
     * Applies this ANSI 256 color as a foreground.
     *
     * @param style style to update
     * @param renderer renderer context
     * @return updated style
     */
    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsForeground(
        AttributedStyle style,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return delegate.applyAsForeground(style, renderer.toCanonical());
    }

    /**
     * Applies this ANSI 256 color as a foreground using the canonical renderer.
     *
     * @param style style to update
     * @param renderer canonical renderer context
     * @return updated style
     */
    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsForeground(
        AttributedStyle style,
        com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
    ) {
        return delegate.applyAsForeground(style, renderer);
    }

    /**
     * Returns the RGB approximation for this ANSI 256 color.
     *
     * @return RGB value
     */
    @Deprecated(since = "0.3.0")
    @Override
    public com.williamcallahan.tui4j.compat.lipgloss.color.RGB rgb() {
        return delegate.rgb();
    }

    /**
     * Converts this 256-color entry to the closest ANSI 16-color entry.
     *
     * @return closest ANSI color
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor toANSIColor() {
        return delegate.toANSIColor();
    }

    /**
     * Returns the ANSI 256-color code.
     *
     * @return color code
     */
    @Deprecated(since = "0.3.0")
    public int value() {
        return delegate.value();
    }

    /**
     * Returns the canonical ANSI 256 color delegate.
     *
     * @return canonical ANSI 256 color
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color toCanonical() {
        return delegate;
    }
}
