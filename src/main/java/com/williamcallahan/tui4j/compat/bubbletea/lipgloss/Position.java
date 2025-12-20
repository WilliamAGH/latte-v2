package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

/**
 * Port of Lip Gloss position.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public final class Position {
    public static final Position Top = new Position(0.0);
    public static final Position Bottom = new Position(1.0);
    public static final Position Center = new Position(0.5);
    public static final Position Left = new Position(0.0);
    public static final Position Right = new Position(1.0);

    private final double value;

    public Position(double value) {
        this.value = value;
    }

    public double value() {
        return Math.min(1, Math.max(0, this.value));
    }
}
