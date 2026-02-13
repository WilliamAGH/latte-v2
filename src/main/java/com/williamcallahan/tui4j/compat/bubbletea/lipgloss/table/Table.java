package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.table;

/**
 * Table component for rendering tabular data.
 * <p>
 * Lipgloss: table/table.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because of a compat refactor; use
 * {@link com.williamcallahan.tui4j.compat.lipgloss.table.Table}.
 * This transitional shim preserves backward compatibility and will be removed
 * in a future release. Migrate to the canonical location.
 * @since 0.3.0
 * @see com.williamcallahan.tui4j.compat.lipgloss.table.Table
 * Lip Gloss: table/table.go.
 */
@Deprecated(since = "0.3.0")
public class Table extends com.williamcallahan.tui4j.compat.lipgloss.table.Table {

    /**
     * Creates a Table with the given renderer.
     *
     * @param renderer the renderer
     */
    @Deprecated(since = "0.3.0")
    public Table(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer renderer) {
        super(renderer.toCanonical());
    }

    /**
     * Creates a Table with the default renderer.
     *
     * @return the new Table
     */
    @Deprecated(since = "0.3.0")
    public static Table create() {
        return new Table(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer.defaultRenderer());
    }
}
