package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import java.util.List;

/**
 * Rendering and terminal mode control for Bubble Tea-compatible APIs.
 * <p>
 * Lipgloss: renderer.go.
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.Renderer} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Renderer {

    private static final Renderer DEFAULT_RENDERER =
        new Renderer(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.term.Output.defaultOutput());

    private final com.williamcallahan.tui4j.compat.lipgloss.Renderer delegate;

    /**
     * Returns the default renderer instance.
     *
     * @return default renderer
     */
    @Deprecated(since = "0.3.0")
    public static Renderer defaultRenderer() {
        return DEFAULT_RENDERER;
    }

    /**
     * Creates a renderer with the provided output.
     *
     * @param output output destination
     */
    @Deprecated(since = "0.3.0")
    public Renderer(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.term.Output output) {
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.Renderer(output.toCanonical());
    }

    private Renderer(com.williamcallahan.tui4j.compat.lipgloss.Renderer delegate) {
        this.delegate = delegate;
    }

    /**
     * Wraps a canonical renderer for legacy compatibility.
     *
     * @param renderer canonical renderer
     * @return bubbletea renderer wrapper
     */
    @Deprecated(since = "0.3.0")
    public static Renderer fromCanonical(com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer) {
        if (renderer == null) {
            return null;
        }
        return new Renderer(renderer);
    }

    /**
     * Returns the canonical renderer delegate.
     *
     * @return canonical renderer
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.Renderer toCanonical() {
        return delegate;
    }

    /**
     * Sets a custom environment for terminal capability detection.
     * <p>
     * Note: This method is a no-op as the canonical Renderer does not support custom environments.
     *
     * @param environment list of environment variables in "KEY=VALUE" format
     */
    @Deprecated(since = "0.3.0")
    public void setEnvironment(List<String> environment) {
        // No-op: canonical Renderer does not support setEnvironment
    }

    /**
     * Creates a new Bubble Tea-compatible style.
     *
     * @return new style
     */
    @Deprecated(since = "0.3.0")
    public Style newStyle() {
        return new Style(this);
    }

    /**
     * Reports whether the terminal background is dark.
     *
     * @return true when the background is dark
     */
    @Deprecated(since = "0.3.0")
    public boolean hasDarkBackground() {
        return delegate.hasDarkBackground();
    }

    /**
     * Returns the active color profile for this renderer.
     *
     * @return color profile
     */
    @Deprecated(since = "0.3.0")
    public com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile colorProfile() {
        return delegate.colorProfile();
    }

    /**
     * Sets the renderer's color profile.
     *
     * @param colorProfile color profile
     */
    @Deprecated(since = "0.3.0")
    public void setColorProfile(
        com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile colorProfile
    ) {
        delegate.setColorProfile(toCanonical(colorProfile));
    }

    /**
     * Overrides the background darkness detection.
     *
     * @param hasDarkBackground whether the background is dark
     */
    @Deprecated(since = "0.3.0")
    public void setHasDarkBackground(boolean hasDarkBackground) {
        delegate.setHasDarkBackground(hasDarkBackground);
    }

    /**
     * Places text within a given width and height with specified alignment.
     *
     * @param width width
     * @param height height
     * @param hPos horizontal position
     * @param vPos vertical position
     * @param input input text
     * @param options whitespace options
     * @return placed text
     */
    @Deprecated(since = "0.3.0")
    public String place(int width, int height, com.williamcallahan.tui4j.compat.lipgloss.Position hPos, com.williamcallahan.tui4j.compat.lipgloss.Position vPos, String input, Whitespace.WhitespaceOption... options) {
        return delegate.place(width, height, hPos, vPos, input, Whitespace.toCanonicalOptions(options));
    }

    /**
     * Places text vertically within a given height.
     *
     * @param height height
     * @param position vertical position
     * @param input input text
     * @param options whitespace options
     * @return placed text
     */
    @Deprecated(since = "0.3.0")
    public String placeVertical(int height, com.williamcallahan.tui4j.compat.lipgloss.Position position, String input, Whitespace.WhitespaceOption... options) {
        return delegate.placeVertical(height, position, input, Whitespace.toCanonicalOptions(options));
    }

    /**
     * Places text horizontally within a given width.
     *
     * @param width width
     * @param position horizontal position
     * @param input input text
     * @param options whitespace options
     * @return placed text
     */
    @Deprecated(since = "0.3.0")
    public String placeHorizontal(int width, com.williamcallahan.tui4j.compat.lipgloss.Position position, String input, Whitespace.WhitespaceOption... options) {
        return delegate.placeHorizontal(width, position, input, Whitespace.toCanonicalOptions(options));
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
