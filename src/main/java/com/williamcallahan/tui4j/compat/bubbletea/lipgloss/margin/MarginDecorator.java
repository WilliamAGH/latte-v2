package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.margin;

import org.jline.utils.AttributedStyle;

/**
 * Margin utilities for Bubble Tea-compatible layouts.
 * <p>
 * Lipgloss: style.go.
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.MarginDecorator} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class MarginDecorator {

    /**
     * Prevents instantiation of the deprecated shim.
     */
    private MarginDecorator() {
    }

    /**
     * Applies margins to all sides.
     *
     * @param input           input string
     * @param topMargin       top margin
     * @param rightMargin     right margin
     * @param bottomMargin    bottom margin
     * @param leftMargin      left margin
     * @param attributedStyle style to preserve for margins
     * @param renderer        renderer to use
     * @return margined string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.MarginDecorator#applyMargins} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String applyMargins(
        String input,
        int topMargin,
        int rightMargin,
        int bottomMargin,
        int leftMargin,
        AttributedStyle attributedStyle,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.MarginDecorator.applyMargins(
            input,
            topMargin,
            rightMargin,
            bottomMargin,
            leftMargin,
            attributedStyle,
            renderer == null ? null : renderer.toCanonical()
        );
    }
}
