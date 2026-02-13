package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;


/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.TreeStyle} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: tree/tree.go.
 */
@Deprecated(since = "0.3.0")
public class TreeStyle extends com.williamcallahan.tui4j.compat.lipgloss.tree.TreeStyle {

    private final com.williamcallahan.tui4j.compat.lipgloss.tree.TreeStyle delegate;

    /**
     * Creates TreeStyle to keep this component ready for use.
     */
    @Deprecated(since = "0.3.0")
    public TreeStyle() {
        super();
        this.delegate = this;
        super.setEnumeratorFunction(
            (children, index) -> com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle()
                .paddingRight(1)
        );
        super.setItemFunction(
            (children, index) -> com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle()
        );
        super.setRootStyle(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle());
    }

    /**
     * Creates TreeStyle to keep this component ready for use.
     *
     * @param delegate delegate
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.TreeStyle} instead.
     */
    @Deprecated(since = "0.3.0")
    TreeStyle(com.williamcallahan.tui4j.compat.lipgloss.tree.TreeStyle delegate) {
        super();
        this.delegate = delegate;
        this.delegate.setEnumeratorFunction(
            (children, index) -> com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle()
                .paddingRight(1)
        );
        this.delegate.setItemFunction(
            (children, index) -> com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle()
        );
        this.delegate.setRootStyle(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style.newStyle());
    }

    /**
     * Handles root style for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public com.williamcallahan.tui4j.compat.lipgloss.Style rootStyle() {
        if (delegate == this) {
            return super.rootStyle();
        }
        return delegate.rootStyle();
    }

    /**
     * Handles enumerator function for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public StyleFunction enumeratorFunction() {
        if (delegate == this) {
            return (StyleFunction) super.enumeratorFunction();
        }
        return (StyleFunction) delegate.enumeratorFunction();
    }

    /**
     * Handles item function for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public StyleFunction itemFunction() {
        if (delegate == this) {
            return (StyleFunction) super.itemFunction();
        }
        return (StyleFunction) delegate.itemFunction();
    }

    /**
     * Updates the enumerator function.
     * <p>
     * Intentional overload for backward compatibility with the deprecated {@link StyleFunction} subtype.
     *
     * @param enumeratorFunction enumerator function
     */
    @Deprecated(since = "0.3.0")
    public void setEnumeratorFunction(StyleFunction enumeratorFunction) {
        setEnumeratorFunction((com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction) enumeratorFunction);
    }

    /**
     * Updates the enumerator function.
     *
     * @param enumeratorFunction enumerator function
     */
    @Deprecated(since = "0.3.0")
    @Override
    public void setEnumeratorFunction(com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction enumeratorFunction) {
        if (delegate == this) {
            super.setEnumeratorFunction(enumeratorFunction);
            return;
        }
        delegate.setEnumeratorFunction(enumeratorFunction);
    }

    /**
     * Updates the root style.
     * <p>
     * Intentional overload for backward compatibility with the deprecated Style subtype.
     *
     * @param rootStyle root style
     * @deprecated Deprecated in tui4j as of 0.3.0 because you should use {@link #setRootStyle(com.williamcallahan.tui4j.compat.lipgloss.Style)} instead.
     */
    @Deprecated(since = "0.3.0")
    public void setRootStyle(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style rootStyle) {
        setRootStyle((com.williamcallahan.tui4j.compat.lipgloss.Style) rootStyle);
    }

    /**
     * Updates the root style.
     *
     * @param rootStyle root style
     */
    @Deprecated(since = "0.3.0")
    @Override
    public void setRootStyle(com.williamcallahan.tui4j.compat.lipgloss.Style rootStyle) {
        if (delegate == this) {
            super.setRootStyle(rootStyle);
            return;
        }
        delegate.setRootStyle(rootStyle);
    }

    /**
     * Updates the item function.
     * <p>
     * Intentional overload for backward compatibility with the deprecated {@link StyleFunction} subtype.
     *
     * @param itemFunction item function
     */
    @Deprecated(since = "0.3.0")
    public void setItemFunction(StyleFunction itemFunction) {
        setItemFunction((com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction) itemFunction);
    }

    /**
     * Updates the item function.
     *
     * @param itemFunction item function
     */
    @Deprecated(since = "0.3.0")
    @Override
    public void setItemFunction(com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction itemFunction) {
        if (delegate == this) {
            super.setItemFunction(itemFunction);
            return;
        }
        delegate.setItemFunction(itemFunction);
    }
}
