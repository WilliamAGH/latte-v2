package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

/**
 * Dimension helpers that mirror Bubble Tea's lipgloss width/height helpers.
 * <p>
 * Lipgloss: size.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.Dimensions} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Dimensions {
    private final com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile profile;

    /**
     * Creates Dimensions to keep this component ready for use.
     *
     * @param profile profile
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Dimensions} instead.
     */
    @Deprecated(since = "0.3.0")
    public Dimensions(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile profile) {
        this.profile = toCanonical(profile);
    }

    /**
     * Creates Dimensions to keep this component ready for use.
     *
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Dimensions} instead.
     */
    @Deprecated(since = "0.3.0")
    public Dimensions() {
        this(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile.ANSI256);
    }

    /**
     * Handles width for this component.
     *
     * @param text text
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Size#width(String)} instead.
     */
    @Deprecated(since = "0.3.0")
    public int width(String text) {
        return com.williamcallahan.tui4j.compat.lipgloss.Size.width(text);
    }

    /**
     * Handles height for this component.
     *
     * @param text text
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Size#height(String)} instead.
     */
    @Deprecated(since = "0.3.0")
    public int height(String text) {
        return com.williamcallahan.tui4j.compat.lipgloss.Size.height(text);
    }

    /**
     * Handles with profile for this component.
     *
     * @param profile profile
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Dimensions} instead.
     */
    @Deprecated(since = "0.3.0")
    public Dimensions withProfile(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile profile) {
        return new Dimensions(profile);
    }

    /**
     * Handles profile for this component.
     *
     * @return result
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Dimensions} instead.
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile profile() {
        return profile;
    }

    private static com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile toCanonical(
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile profile
    ) {
        if (profile == null) {
            return null;
        }
        return com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile.valueOf(profile.name());
    }
}
