package com.williamcallahan.tui4j.input;

/**
 * Defines a mouse target region.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseTarget.java
 *
 * @param id stable identifier for the target
 * @param bounds target bounds in screen coordinates
 * @param zIndex overlap ordering, higher wins
 * @param cursor cursor hint for the target
 * @param hyperlink optional URL for hyperlink targets
 */
public record MouseTarget(
        String id,
        MouseBounds bounds,
        int zIndex,
        MouseCursor cursor,
        String hyperlink
) {
    public static MouseTarget click(String id, int left, int top, int width, int height) {
        return new MouseTarget(id, new MouseBounds(left, top, width, height), 0, MouseCursor.DEFAULT, null);
    }

    public static MouseTarget link(String id, String url, int left, int top, int width, int height) {
        return new MouseTarget(id, new MouseBounds(left, top, width, height), 0, MouseCursor.POINTER, url);
    }

    public boolean contains(int column, int row) {
        return bounds != null && bounds.contains(column, row);
    }
}
