package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.placement;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: style.go.
 */
@Deprecated(since = "0.3.0")
public class PlacementDecorator extends com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator {

    /**
     * Creates a placement decorator instance for legacy compatibility.
     */
    @Deprecated(since = "0.3.0")
    public PlacementDecorator() {
        // Deprecated shim constructor required for backward compatibility
    }

    /**
     * Places text within a rectangle using legacy positions.
     *
     * @param width   width in cells
     * @param height  height in rows
     * @param hPos    horizontal position
     * @param vPos    vertical position
     * @param input   input text
     * @param options whitespace options
     * @return placed text
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String place(
        int width,
        int height,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position hPos,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position vPos,
        String input,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Whitespace.WhitespaceOption... options
    ) {
        return com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer
            .defaultRenderer()
            .place(width, height, hPos.toNew(), vPos.toNew(), input, options);
    }

    /**
     * Places text horizontally within a width using legacy positions.
     *
     * @param width    width in cells
     * @param position horizontal position
     * @param input    input text
     * @param options  whitespace options
     * @return placed text
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String placeHorizontal(
        int width,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position position,
        String input,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Whitespace.WhitespaceOption... options
    ) {
        return com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer
            .defaultRenderer()
            .placeHorizontal(width, position.toNew(), input, options);
    }

    /**
     * Places text vertically within a height using legacy positions.
     *
     * @param height   height in rows
     * @param position vertical position
     * @param input    input text
     * @param options  whitespace options
     * @return placed text
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator} instead.
     */
    @Deprecated(since = "0.3.0")
    public static String placeVertical(
        int height,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position position,
        String input,
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Whitespace.WhitespaceOption... options
    ) {
        return com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer
            .defaultRenderer()
            .placeVertical(height, position.toNew(), input, options);
    }
}
