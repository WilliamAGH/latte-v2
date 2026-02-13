package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.align;

import com.williamcallahan.tui4j.compat.lipgloss.Position;
import org.jline.utils.AttributedStyle;

/**
 * Text alignment utilities.
 * <p>
 * Lipgloss: align.go
 *
 * @since 0.3.0
 * @see com.williamcallahan.tui4j.compat.lipgloss.align.AlignmentDecorator
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.align.AlignmentDecorator} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class AlignmentDecorator {

    /**
     * Prevents instantiation of the shim utility.
     */
    private AlignmentDecorator() {
    }

    /**
     * Vertically aligns text.
     *
     * @param input    text to align
     * @param position vertical alignment position
     * @param height   available height
     * @return aligned text
     */
    @Deprecated(since = "0.3.0")
    public static String alignTextVertical(String input, Position position, int height) {
        return com.williamcallahan.tui4j.compat.lipgloss.align.AlignmentDecorator
            .alignTextVertical(input, position, height);
    }

    /**
     * Horizontally aligns text.
     *
     * @param input           text to align
     * @param position        horizontal alignment position
     * @param width           available width
     * @param attributedStyle style to preserve for padding
     * @return aligned text
     */
    @Deprecated(since = "0.3.0")
    public static String alignTextHorizontal(
        String input,
        Position position,
        int width,
        AttributedStyle attributedStyle
    ) {
        return com.williamcallahan.tui4j.compat.lipgloss.align.AlignmentDecorator
            .alignTextHorizontal(input, position, width, attributedStyle);
    }
}
