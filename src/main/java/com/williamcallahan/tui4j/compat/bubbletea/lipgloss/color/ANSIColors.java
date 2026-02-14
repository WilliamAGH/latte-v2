package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

/**
 * ANSI 16/256 color lookup table for the Bubble Tea-compatible API surface.
 * <p>
 * Lipgloss: color.go
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColors} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public final class ANSIColors {

    /**
     * ANSI 16-color and 256-color hex palette.
     */
    @Deprecated(since = "0.3.0")
    public static final String[] ANSI_HEX = com.williamcallahan.tui4j.compat.lipgloss.color.ANSIColors.ANSI_HEX.clone();

    /**
     * Prevents instantiation of the deprecated shim.
     */
    private ANSIColors() {
    }
}
