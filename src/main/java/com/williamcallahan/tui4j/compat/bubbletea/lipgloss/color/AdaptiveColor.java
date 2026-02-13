package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * Adaptive color that switches between light and dark variants.
 * <p>
 * Lipgloss: color.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * @since 0.3.0
 */
@Deprecated(since = "0.3.0")
public final class AdaptiveColor implements TerminalColor {
    private final com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor delegate;

    /**
     * Creates an adaptive color with light and dark values.
     *
     * @param light light color string
     * @param dark dark color string
     */
    @Deprecated(since = "0.3.0")
    public AdaptiveColor(String light, String dark) {
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor(light, dark);
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
     * @return canonical AdaptiveColor
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor toCanonical() {
        return delegate;
    }
}
