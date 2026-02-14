package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * No-op color implementation for Bubble Tea-compatible rendering.
 * <p>
 * Lipgloss: color.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.NoColor}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * @since 0.3.0
 */
@Deprecated(since = "0.3.0")
public final class NoColor implements TerminalColor {
    private final com.williamcallahan.tui4j.compat.lipgloss.color.NoColor delegate;

    /**
     * Creates NoColor.
     */
    @Deprecated(since = "0.3.0")
    public NoColor() {
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.color.NoColor();
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
     * @return canonical NoColor
     */
    @Deprecated(since = "0.3.0")
    @Override
    public com.williamcallahan.tui4j.compat.lipgloss.color.NoColor toCanonical() {
        return delegate;
    }
}
