package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

/**
 * Size helpers with optional color-profile context for Bubble Tea-compatible layouts.
 * <p>
 * Lipgloss: size.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.Size} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Size {
    private final com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile profile;

    /**
     * Creates Size to keep this component ready for use.
     */
    @Deprecated(since = "0.3.0")
    public Size() {
        this.profile = null;
    }

    /**
     * Creates a Size with the requested color profile.
     *
     * @param profile profile to attach
     */
    private Size(com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile profile) {
        this.profile = profile;
    }

    /**
     * Handles width for this component.
     *
     * @param str str
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public static int width(String str) {
        return com.williamcallahan.tui4j.compat.lipgloss.Size.width(str);
    }

    /**
     * Handles height for this component.
     *
     * @param str str
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public static int height(String str) {
        return com.williamcallahan.tui4j.compat.lipgloss.Size.height(str);
    }

    /**
     * Handles with profile for this component.
     *
     * @param profile profile
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public static Size withProfile(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile profile) {
        return new Size(toCanonical(profile));
    }

    /**
     * Handles profile for this component.
     *
     * @return result
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
