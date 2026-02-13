package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border;

/**
 * Border glyph set for Bubble Tea-compatible rendering.
 *
 * @param top top border glyph
 * @param bottom bottom border glyph
 * @param left left border glyph
 * @param right right border glyph
 * @param topLeft top-left corner glyph
 * @param topRight top-right corner glyph
 * @param bottomLeft bottom-left corner glyph
 * @param bottomRight bottom-right corner glyph
 * @param middleLeft middle-left glyph
 * @param middleRight middle-right glyph
 * @param middle middle glyph
 * @param middleTop middle-top glyph
 * @param middleBottom middle-bottom glyph
 * <p>
 * Lip Gloss: borders.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public record Border(
        String top,
        String bottom,
        String left,
        String right,
        String topLeft,
        String topRight,
        String bottomLeft,
        String bottomRight,
        String middleLeft,
        String middleRight,
        String middle,
        String middleTop,
        String middleBottom
) {
    /**
     * Handles to new for this component.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border} directly.
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.border.Border toNew() {
        return new com.williamcallahan.tui4j.compat.lipgloss.border.Border(
                top, bottom, left, right, topLeft, topRight, bottomLeft, bottomRight,
                middleLeft, middleRight, middle, middleTop, middleBottom
        );
    }

    /**
     * Handles from new for this component.
     *
     * @param b b
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border} directly.
     */
    @Deprecated(since = "0.3.0")
    public static Border fromNew(com.williamcallahan.tui4j.compat.lipgloss.border.Border b) {
        if (b == null) return null;
        return new Border(
                b.top(), b.bottom(), b.left(), b.right(), b.topLeft(), b.topRight(), b.bottomLeft(), b.bottomRight(),
                b.middleLeft(), b.middleRight(), b.middle(), b.middleTop(), b.middleBottom()
        );
    }

    /**
     * Returns the top size.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border#getTopSize()} instead.
     */
    @Deprecated(since = "0.3.0")
    public int getTopSize() { return toNew().getTopSize(); }
    /**
     * Returns the right size.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border#getRightSize()} instead.
     */
    @Deprecated(since = "0.3.0")
    public int getRightSize() { return toNew().getRightSize(); }
    /**
     * Returns the bottom size.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border#getBottomSize()} instead.
     */
    @Deprecated(since = "0.3.0")
    public int getBottomSize() { return toNew().getBottomSize(); }
    /**
     * Returns the left size.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border#getLeftSize()} instead.
     */
    @Deprecated(since = "0.3.0")
    public int getLeftSize() { return toNew().getLeftSize(); }

    /**
     * Handles apply borders for this component.
     *
     * @param string string
     * @param hasTop has top
     * @param hasRight has right
     * @param hasBottom has bottom
     * @param hasLeft has left
     * @param borderTopForeground border top foreground
     * @param borderRightForeground border right foreground
     * @param borderBottomForeground border bottom foreground
     * @param borderLeftForeground border left foreground
     * @param borderTopBackground border top background
     * @param borderRightBackground border right background
     * @param borderBottomBackground border bottom background
     * @param borderLeftBackground border left background
     * @param renderer renderer
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.border.Border#applyBorders} instead.
     */
    @Deprecated(since = "0.3.0")
    public String applyBorders(
        String string,
        boolean hasTop,
        boolean hasRight,
        boolean hasBottom,
        boolean hasLeft,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderTopForeground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderRightForeground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderBottomForeground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderLeftForeground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderTopBackground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderRightBackground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderBottomBackground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor borderLeftBackground,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer
    ) {
        return toNew().applyBorders(string, hasTop, hasRight, hasBottom, hasLeft,
                toNew(borderTopForeground),
                toNew(borderRightForeground),
                toNew(borderBottomForeground),
                toNew(borderLeftForeground),
                toNew(borderTopBackground),
                toNew(borderRightBackground),
                toNew(borderBottomBackground),
                toNew(borderLeftBackground),
                renderer == null ? null : renderer.toCanonical());
    }

    /**
     * Adapts a Bubble Tea terminal color to the canonical lipgloss terminal color interface.
     *
     * @param color bubbletea terminal color
     * @return canonical terminal color
     */
    private static com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor toNew(
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor color
    ) {
        if (color == null) {
            return null;
        }
        return new TerminalColorAdapter(color);
    }

    /**
     * Bridges legacy terminal colors to the canonical lipgloss color interface.
     */
    private static final class TerminalColorAdapter
        implements com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor {
        private final com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor legacy;

        /**
         * Creates an adapter for the legacy terminal color.
         *
         * @param legacy legacy terminal color
         */
        private TerminalColorAdapter(
            com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor legacy
        ) {
            this.legacy = legacy;
        }

        /**
         * Applies the legacy color as a background style.
         *
         * @param style base style
         * @param renderer renderer instance
         * @return updated style
         */
        @Override
        public org.jline.utils.AttributedStyle applyAsBackground(
            org.jline.utils.AttributedStyle style,
            com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
        ) {
            return legacy.applyAsBackground(style, renderer);
        }

        /**
         * Applies the legacy color as a foreground style.
         *
         * @param style base style
         * @param renderer renderer instance
         * @return updated style
         */
        @Override
        public org.jline.utils.AttributedStyle applyAsForeground(
            org.jline.utils.AttributedStyle style,
            com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
        ) {
            return legacy.applyAsForeground(style, renderer);
        }
    }
}
