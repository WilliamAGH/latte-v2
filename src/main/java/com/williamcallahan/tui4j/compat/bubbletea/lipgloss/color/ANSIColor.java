package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * ANSI 16-color representation for the Bubble Tea-compatible API surface.
 * <p>
 * Lipgloss: color.go.
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class ANSIColor implements TerminalColor, RGBSupplier {

    private final com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor delegate;

    /**
     * Creates an ANSI color from a 16-color code.
     *
     * @param colorCode ANSI color code (0-15)
     */
    @Deprecated(since = "0.3.0")
    public ANSIColor(int colorCode) {
        this.delegate =
            new com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor(
                colorCode
            );
    }

    /**
     * Applies this ANSI color as a background.
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
     * Applies this ANSI color as a background using the canonical renderer.
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
     * Applies this ANSI color as a foreground.
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
     * Applies this ANSI color as a foreground using the canonical renderer.
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
     * Returns the RGB approximation for this ANSI color.
     *
     * @return RGB value
     */
    @Deprecated(since = "0.3.0")
    @Override
    public com.williamcallahan.tui4j.compat.lipgloss.color.RGB rgb() {
        return delegate.rgb();
    }

    /**
     * Returns the ANSI 16-color code.
     *
     * @return color code
     */
    @Deprecated(since = "0.3.0")
    public int value() {
        return delegate.value();
    }

    /**
     * Returns the canonical ANSI color delegate.
     *
     * @return canonical ANSI color
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColor toCanonical() {
        return delegate;
    }
}
