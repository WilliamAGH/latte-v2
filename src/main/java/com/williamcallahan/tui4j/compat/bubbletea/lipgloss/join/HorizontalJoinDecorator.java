package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.join;

import com.williamcallahan.tui4j.compat.lipgloss.Position;

/**
 * Utility for joining strings horizontally.
 * <p>
 * Lipgloss: join.go
 *
 * @since 0.3.0
 * @see com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class HorizontalJoinDecorator {

    /**
     * Prevents instantiation of the shim utility.
     */
    private HorizontalJoinDecorator() {
    }

    /**
     * Joins blocks horizontally with the specified vertical alignment.
     *
     * @param position vertical alignment position
     * @param blocks   blocks to join
     * @return joined block string
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator#joinHorizontal(Position, String...)} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String joinHorizontal(Position position, String... blocks) {
        return com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator
            .joinHorizontal(position, blocks);
    }
}
