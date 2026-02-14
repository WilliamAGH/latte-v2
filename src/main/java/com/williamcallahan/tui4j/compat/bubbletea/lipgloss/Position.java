package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.Position} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: position.go.
 */
@Deprecated(since = "0.3.0")
public final class Position {
    private final com.williamcallahan.tui4j.compat.lipgloss.Position delegate;

    /** Top-aligned position. */
    @Deprecated(since = "0.3.0")
    public static final Position Top = new Position(com.williamcallahan.tui4j.compat.lipgloss.Position.Top);
    /** Bottom-aligned position. */
    @Deprecated(since = "0.3.0")
    public static final Position Bottom = new Position(com.williamcallahan.tui4j.compat.lipgloss.Position.Bottom);
    /** Center-aligned position. */
    @Deprecated(since = "0.3.0")
    public static final Position Center = new Position(com.williamcallahan.tui4j.compat.lipgloss.Position.Center);
    /** Left-aligned position. */
    @Deprecated(since = "0.3.0")
    public static final Position Left = new Position(com.williamcallahan.tui4j.compat.lipgloss.Position.Left);
    /** Right-aligned position. */
    @Deprecated(since = "0.3.0")
    public static final Position Right = new Position(com.williamcallahan.tui4j.compat.lipgloss.Position.Right);

    /**
     * Creates Position with value.
     *
     * @param value position value (0.0-1.0)
     */
    @Deprecated(since = "0.3.0")
    public Position(double value) {
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.Position(value);
    }

    private Position(com.williamcallahan.tui4j.compat.lipgloss.Position delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the position value (0.0-1.0).
     *
     * @return position value
     */
    @Deprecated(since = "0.3.0")
    public double value() {
        return delegate.value();
    }

    /**
     * Returns the canonical delegate.
     *
     * @return canonical Position
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.Position toNew() {
        return delegate;
    }

    /**
     * Creates a shim from the canonical type.
     *
     * @param p canonical Position
     * @return deprecated Position shim
     */
    @Deprecated(since = "0.3.0")
    public static Position fromNew(com.williamcallahan.tui4j.compat.lipgloss.Position p) {
        if (p == null) {
            return null;
        }
        return new Position(p);
    }
}
