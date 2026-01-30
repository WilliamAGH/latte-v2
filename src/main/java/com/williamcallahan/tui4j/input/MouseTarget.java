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
    /**
     * Creates a click target with default cursor and no hyperlink.
     *
     * @param id target id
     * @param left left column
     * @param top top row
     * @param width width in columns
     * @param height height in rows
     * @return mouse target
     */
    public static MouseTarget click(String id, int left, int top, int width, int height) {
        return new MouseTarget(id, new MouseBounds(left, top, width, height), 0, MouseCursor.DEFAULT, null);
    }


    /**
     * Creates a button target with pointer cursor and no hyperlink.
     *
     * @param id target id
     * @param left left column
     * @param top top row
     * @param width width in columns
     * @param height height in rows
     * @return mouse target
     */
    public static MouseTarget button(String id, int left, int top, int width, int height) {
        return new MouseTarget(id, new MouseBounds(left, top, width, height), 0, MouseCursor.POINTER, null);
    }
    /**
     * Creates a link target with pointer cursor.
     *
     * @param id target id
     * @param url hyperlink target
     * @param left left column
     * @param top top row
     * @param width width in columns
     * @param height height in rows
     * @return mouse target
     */
    public static MouseTarget link(String id, String url, int left, int top, int width, int height) {
        return new MouseTarget(id, new MouseBounds(left, top, width, height), 0, MouseCursor.POINTER, url);
    }

    /**
     * Returns whether the given position is inside this target.
     *
     * @param column column to test
     * @param row row to test
     * @return {@code true} when inside the target
     */
    public boolean contains(int column, int row) {
        return bounds != null && bounds.contains(column, row);
    }
}
