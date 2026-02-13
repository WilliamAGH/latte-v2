package com.williamcallahan.tui4j.compat.lipgloss;

import com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption;

import static com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer;

/**
 * Port of Lip Gloss placement helpers.
 * Upstream: lipgloss/position.go
 */
public class PlacementDecorator {

    /**
     * Creates a utility container for placement helpers.
     */
    public PlacementDecorator() {
    }

    /**
     * Places the input within the given width and height.
     *
     * @param width available width
     * @param height available height
     * @param hPos horizontal position
     * @param vPos vertical position
     * @param input content to place
     * @param options whitespace options
     * @return positioned content
     */
    public static String place(int width, int height, Position hPos, Position vPos, String input, WhitespaceOption... options) {
        return defaultRenderer().place(width, height, hPos, vPos, input, options);
    }

    /**
     * Places the input horizontally within the given width.
     *
     * @param width available width
     * @param position horizontal position
     * @param input content to place
     * @param options whitespace options
     * @return positioned content
     */
    public static String placeHorizontal(int width, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeHorizontal(width, position, input, options);
    }

    /**
     * Places the input vertically within the given height.
     *
     * @param height available height
     * @param position vertical position
     * @param input content to place
     * @param options whitespace options
     * @return positioned content
     */
    public static String placeVertical(int height, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeVertical(height, position, input, options);
    }
}
