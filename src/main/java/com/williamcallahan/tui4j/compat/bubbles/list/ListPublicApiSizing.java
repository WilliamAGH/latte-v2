package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.lipgloss.Size;

/**
 * Sizing-related public API for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
abstract class ListPublicApiSizing extends ListPublicApiPaging {

    /** Returns list width in columns.
     * @return width in columns
     */
    public int width() {
        return width;
    }

    /** Returns list height in rows.
     * @return height in rows
     */
    public int height() {
        return height;
    }

    /** Sets list dimensions and refreshes items.
     * @param width width
     * @param height height
     * @return refresh command
     */
    public Command setSize(int width, int height) {
        int promptWidth = Size.width(
            styles.title().render(filterInput.prompt())
        );

        this.width = width;
        this.height = height;
        this.help.setWidth(width);
        int inputWidth = Math.max(
            0,
            width - promptWidth - Size.width(ListViewRenderer.spinnerView(self()))
        );
        this.filterInput.setWidth(inputWidth);
        updatePagination();

        return fetchCurrentPageItems();
    }

    /** Sets list width.
     * @param width width
     * @return refresh command
     */
    public Command setWidth(int width) {
        return setSize(width, height);
    }

    /** Sets list height.
     * @param height height
     * @return refresh command
     */
    public Command setHeight(int height) {
        return setSize(width, height);
    }
}

