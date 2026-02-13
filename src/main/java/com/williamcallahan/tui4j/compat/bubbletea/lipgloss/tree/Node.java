package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Node} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: tree/tree.go.
 */
@Deprecated(since = "0.3.0")
public interface Node extends com.williamcallahan.tui4j.compat.lipgloss.tree.Node {
    /**
     * Handles value for this component.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Node#value()} instead.
     */
    @Deprecated(since = "0.3.0")
    @Override
    String value();

    /**
     * Handles children for this component.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Node#children()} instead.
     */
    @Deprecated(since = "0.3.0")
    @Override
    Children children();
}
