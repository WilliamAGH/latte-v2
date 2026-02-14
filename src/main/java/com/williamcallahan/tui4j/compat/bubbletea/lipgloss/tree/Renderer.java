package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.Renderer} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: tree/renderer.go.
 */
@Deprecated(since = "0.3.0")
public class Renderer extends com.williamcallahan.tui4j.compat.lipgloss.tree.Renderer {

    private final TreeStyle styleShim;

    /**
     * Creates Renderer to keep this component ready for use.
     */
    @Deprecated(since = "0.3.0")
    public Renderer() {
        super();
        this.styleShim = new TreeStyle(super.style());
    }

    /**
     * Handles render for this component.
     *
     * @param node node
     * @param root root
     * @param prefix prefix
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public String render(Node node, boolean root, String prefix) {
        return super.render(node, root, prefix);
    }

    /**
     * Handles style for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public TreeStyle style() {
        return styleShim;
    }

    /**
     * Updates the enumerator.
     *
     * @param enumerator enumerator
     */
    @Deprecated(since = "0.3.0")
    public void setEnumerator(TreeEnumerator enumerator) {
        super.setEnumerator(enumerator);
    }

    /**
     * Updates the indenter.
     *
     * @param indenter indenter
     */
    @Deprecated(since = "0.3.0")
    public void setIndenter(TreeIndenter indenter) {
        super.setIndenter(indenter);
    }

    /**
     * Updates the enumerator.
     *
     * @param enumerator enumerator
     */
    @Deprecated(since = "0.3.0")
    @Override
    public void setEnumerator(com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator enumerator) {
        super.setEnumerator(enumerator);
    }

    /**
     * Updates the indenter.
     *
     * @param indenter indenter
     */
    @Deprecated(since = "0.3.0")
    @Override
    public void setIndenter(com.williamcallahan.tui4j.compat.lipgloss.tree.TreeIndenter indenter) {
        super.setIndenter(indenter);
    }
}
