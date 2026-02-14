package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.jline.utils.AttributedStyle;

/**
 * Terminal color application contract for the Bubble Tea-compatible API surface.
 * <p>
 * Lipgloss: color.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * @since 0.3.0
 */
@Deprecated(since = "0.3.0")
public interface TerminalColor extends com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor {
    /**
     * Applies this color as a background to the given style.
     *
     * @param style style to update
     * @param renderer renderer context
     * @return updated style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor} instead.
     */
    @Deprecated(since = "0.3.0")
    default AttributedStyle applyAsBackground(AttributedStyle style,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer) {
        if (renderer == null) {
            return applyAsBackground(style, (com.williamcallahan.tui4j.compat.lipgloss.Renderer) null);
        }
        return applyAsBackground(style, renderer.toCanonical());
    }

    /**
     * Applies this color as a foreground to the given style.
     *
     * @param style style to update
     * @param renderer renderer context
     * @return updated style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor} instead.
     */
    @Deprecated(since = "0.3.0")
    default AttributedStyle applyAsForeground(AttributedStyle style,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer) {
        if (renderer == null) {
            return applyAsForeground(style, (com.williamcallahan.tui4j.compat.lipgloss.Renderer) null);
        }
        return applyAsForeground(style, renderer.toCanonical());
    }

    /**
     * Returns the canonical terminal color representation for this shim.
     *
     * @return canonical terminal color
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor} directly.
     */
    @Deprecated(since = "0.3.0")
    default com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor toCanonical() {
        return this;
    }
}
