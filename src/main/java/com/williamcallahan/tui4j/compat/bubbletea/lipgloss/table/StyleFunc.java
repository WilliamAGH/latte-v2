package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.table;


/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.table.StyleFunc}.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: table/table.go.
 */
@Deprecated(since = "0.3.0")
@FunctionalInterface
public interface StyleFunc extends com.williamcallahan.tui4j.compat.lipgloss.table.StyleFunc {

    /**
     * Chooses the style for a table cell.
     *
     * @param row row index
     * @param col column index
     * @return style for the cell
     */
    @Deprecated(since = "0.3.0")
    @Override
    com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style apply(int row, int col);
}
