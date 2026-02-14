package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.join;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: join.go.
 */
@Deprecated(since = "0.3.0")
public class VerticalJoinDecorator extends com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator {

    /**
     * Prevents instantiation of the shim utility.
     */
    private VerticalJoinDecorator() {
    }

    /**
     * Joins blocks vertically using the legacy position type.
     *
     * @param position position to align within the widest block
     * @param blocks   blocks to join
     * @return joined block string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator#joinVertical} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String joinVertical(
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position position,
        String... blocks
    ) {
        return joinVertical(position.toNew(), blocks);
    }

    /**
     * Joins blocks vertically using the canonical position type.
     *
     * @param position position to align within the widest block
     * @param blocks blocks to join
     * @return joined block string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator#joinVertical} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String joinVertical(com.williamcallahan.tui4j.compat.lipgloss.Position position, String... blocks) {
        return com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator
            .joinVertical(position, blocks);
    }
}
