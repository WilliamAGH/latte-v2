package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.padding;

import org.jline.utils.AttributedStyle;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: style.go.
 */
@Deprecated(since = "0.3.0")
public class PaddingDecorator extends com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator {

    /**
     * Creates a padding decorator instance for legacy compatibility.
     *
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator} instead.
     */
    @Deprecated(since = "0.3.0")
    public PaddingDecorator() {
        // Deprecated shim constructor required for backward compatibility
    }

    /**
     * Applies padding to all sides.
     *
     * @param input           input string
     * @param topPadding      top padding
     * @param rightPadding    right padding
     * @param bottomPadding   bottom padding
     * @param leftPadding     left padding
     * @param attributedStyle style to preserve for padding
     * @param renderer        renderer to use
     * @return padded string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator#applyPadding} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String applyPadding(
        String input,
        int topPadding,
        int rightPadding,
        int bottomPadding,
        int leftPadding,
        AttributedStyle attributedStyle,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator.applyPadding(
            input,
            topPadding,
            rightPadding,
            bottomPadding,
            leftPadding,
            attributedStyle,
            renderer == null ? null : renderer.toCanonical()
        );
    }

    /**
     * Applies left padding.
     *
     * @param input           input string
     * @param leftPadding     left padding
     * @param attributedStyle style to preserve for padding
     * @param renderer        renderer to use
     * @return padded string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator#padLeft} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String padLeft(
        String input,
        int leftPadding,
        AttributedStyle attributedStyle,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator.padLeft(
            input,
            leftPadding,
            attributedStyle,
            renderer == null ? null : renderer.toCanonical()
        );
    }

    /**
     * Applies right padding.
     *
     * @param input           input string
     * @param rightPadding    right padding
     * @param attributedStyle style to preserve for padding
     * @param renderer        renderer to use
     * @return padded string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator#padRight} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String padRight(
        String input,
        int rightPadding,
        AttributedStyle attributedStyle,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator.padRight(
            input,
            rightPadding,
            attributedStyle,
            renderer == null ? null : renderer.toCanonical()
        );
    }

    /**
     * Applies equal padding on both sides.
     *
     * @param str             input string
     * @param n               padding size
     * @param attributedStyle style to preserve for padding
     * @param renderer        renderer to use
     * @return padded string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator#pad} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String pad(
        String str,
        int n,
        AttributedStyle attributedStyle,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.PaddingDecorator.pad(
            str,
            n,
            attributedStyle,
            renderer == null ? null : renderer.toCanonical()
        );
    }
}
