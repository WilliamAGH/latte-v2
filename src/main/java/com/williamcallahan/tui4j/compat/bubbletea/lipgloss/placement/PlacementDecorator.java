package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.placement;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Whitespace.WhitespaceOption;

import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer.defaultRenderer;

/**
 * Port of Lip Gloss placement decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class PlacementDecorator {

    public static String place(int width, int height, Position hPos, Position vPos, String input, WhitespaceOption... options) {
        return defaultRenderer().place(width, height, hPos, vPos, input, options);
    }

    public static String placeHorizontal(int width, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeHorizontal(width, position, input, options);
    }

    public static String placeVertical(int height, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeVertical(height, position, input, options);
    }
}
