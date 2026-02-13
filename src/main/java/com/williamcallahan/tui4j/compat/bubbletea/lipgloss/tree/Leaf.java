package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Leaf node in a tree structure.
 * <p>
 * Lipgloss: tree/tree.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because of a compat refactor; use
 *             {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Leaf}.
 *             This transitional shim preserves backward compatibility and will be removed
 *             in a future release. Migrate to the canonical location.
 * @since 0.3.0
 * @see com.williamcallahan.tui4j.compat.lipgloss.tree.Leaf
 */
@Deprecated(since = "0.3.0")
public class Leaf extends com.williamcallahan.tui4j.compat.lipgloss.tree.Leaf {

    /**
     * Creates a Leaf with the given string value.
     *
     * @param value the leaf value
     */
    @Deprecated(since = "0.3.0")
    public Leaf(String value) {
        super(value);
    }
}
