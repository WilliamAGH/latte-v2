package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * Simple color wrapper for Bubble Tea-compatible rendering.
 * <p>
 * Lipgloss: color.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.Color}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * @since 0.3.0
 */
@Deprecated(since = "0.3.0")
public final class Color implements TerminalColor {
    private final com.williamcallahan.tui4j.compat.lipgloss.color.Color delegate;

    /**
     * Creates a color from a string representation.
     *
     * @param color color string
     * @return color instance
     */
    @Deprecated(since = "0.3.0")
    public static Color color(String color) {
        return new Color(color);
    }

    private Color(String color) {
        this.delegate = com.williamcallahan.tui4j.compat.lipgloss.color.Color.color(color);
    }

    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style,
            com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer) {
        return delegate.applyAsBackground(style, renderer);
    }

    @Deprecated(since = "0.3.0")
    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style,
            com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer) {
        return delegate.applyAsForeground(style, renderer);
    }

    /**
     * Returns the canonical delegate.
     *
     * @return canonical Color
     */
    @Deprecated(since = "0.3.0")
    @Override
    public com.williamcallahan.tui4j.compat.lipgloss.color.Color toCanonical() {
        return delegate;
    }
}
