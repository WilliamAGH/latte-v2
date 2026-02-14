package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

/**
 * Supplies an RGB value for Bubble Tea-compatible color implementations.
 * <p>
 * Lipgloss: color.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.color.RGBSupplier} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public interface RGBSupplier {
    /**
     * Returns the RGB value for this color.
     *
     * @return RGB value
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.color.RGBSupplier#rgb()} instead.
     */
    @Deprecated(since = "0.3.0")
    com.williamcallahan.tui4j.compat.lipgloss.color.RGB rgb();
}
