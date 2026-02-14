package com.williamcallahan.tui4j.compat.lipgloss.border;

/**
 * Resolved border edge and corner characters after applying visibility and fallback rules.
 * <p>
 * Extracts the character normalization logic from {@link Border#applyBorders} so that
 * the rendering method stays focused on assembling output.
 * <p>
 * Port of border character resolution logic from {@code lipgloss/border.go}.
 *
 * @param top top edge characters
 * @param bottom bottom edge characters
 * @param left left edge characters
 * @param right right edge characters
 * @param topLeft top-left corner character
 * @param topRight top-right corner character
 * @param bottomLeft bottom-left corner character
 * @param bottomRight bottom-right corner character
 */
record BorderCharacters(
        String top,
        String bottom,
        String left,
        String right,
        String topLeft,
        String topRight,
        String bottomLeft,
        String bottomRight
) {

    /**
     * Resolves border characters based on visibility flags, applying space fallbacks
     * for empty edge/corner characters and clearing corners where adjacent edges
     * are not visible.
     *
     * @param border source border definition
     * @param hasTop whether top edge is visible
     * @param hasRight whether right edge is visible
     * @param hasBottom whether bottom edge is visible
     * @param hasLeft whether left edge is visible
     * @return resolved border characters ready for rendering
     */
    static BorderCharacters resolve(Border border,
                                    boolean hasTop,
                                    boolean hasRight,
                                    boolean hasBottom,
                                    boolean hasLeft) {
        String left = hasLeft ? defaultIfBlank(border.left()) : border.left();
        String right = hasRight ? defaultIfBlank(border.right()) : border.right();

        return new BorderCharacters(
                border.top(),
                border.bottom(),
                left,
                right,
                resolveCorner(border.topLeft(), hasTop, hasLeft),
                resolveCorner(border.topRight(), hasTop, hasRight),
                resolveCorner(border.bottomLeft(), hasBottom, hasLeft),
                resolveCorner(border.bottomRight(), hasBottom, hasRight)
        );
    }

    /**
     * Resolves a corner character: visible only when both adjacent edges are present,
     * defaulting to a space if empty, and truncated to its first character.
     */
    private static String resolveCorner(String corner, boolean hasEdge1, boolean hasEdge2) {
        if (!hasEdge1 || !hasEdge2) {
            return "";
        }
        String resolved = defaultIfBlank(corner);
        return resolved.substring(0, 1);
    }

    /**
     * Returns a space when the input is null or empty; otherwise returns the input unchanged.
     */
    private static String defaultIfBlank(String value) {
        return (value == null || value.isEmpty()) ? " " : value;
    }
}
