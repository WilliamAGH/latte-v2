package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;


/**
 * Whitespace renderer for Bubble Tea-compatible layouts.
 * <p>
 * Lipgloss: whitespace.go.
 *
 * @since 0.3.0
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.Whitespace} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Whitespace {

    private final com.williamcallahan.tui4j.compat.lipgloss.Whitespace delegate;

    /**
     * Creates Whitespace to keep this component ready for use.
     *
     * @param style style
     */
    @Deprecated(since = "0.3.0")
    public Whitespace(Style style) {
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.Whitespace(style);
    }

    /**
     * Wraps a canonical whitespace instance for legacy callers.
     *
     * @param delegate canonical whitespace instance
     */
    private Whitespace(com.williamcallahan.tui4j.compat.lipgloss.Whitespace delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the canonical whitespace instance.
     *
     * @return canonical whitespace
     */
    @Deprecated(since = "0.3.0")
    com.williamcallahan.tui4j.compat.lipgloss.Whitespace toCanonical() {
        return delegate;
    }

    /**
     * Whitespace option hook for Bubble Tea compatibility.
     * <p>
     * Lipgloss: whitespace.go.
     *
     * @since 0.3.0
     */
    @Deprecated(since = "0.3.0")
    public interface WhitespaceOption {
        /**
         * Applies this option to the whitespace instance.
         *
         * @param whitespace whitespace instance
         * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption} instead.
         */
        @Deprecated(since = "0.3.0")
        void apply(Whitespace whitespace);
    }

    /**
     * Creates a whitespace foreground option.
     *
     * @param color foreground color
     * @return whitespace option
     */
    @Deprecated(since = "0.3.0")
    public static WhitespaceOption WithWhitespaceForeground(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor color) {
        return whitespace -> com.williamcallahan.tui4j.compat.lipgloss.Whitespace
            .WithWhitespaceForeground(adaptColor(color))
            .apply(whitespace.delegate);
    }

    /**
     * Creates a whitespace background option.
     *
     * @param color background color
     * @return whitespace option
     */
    @Deprecated(since = "0.3.0")
    public static WhitespaceOption WithWhitespaceBackground(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor color) {
        return whitespace -> com.williamcallahan.tui4j.compat.lipgloss.Whitespace
            .WithWhitespaceBackground(adaptColor(color))
            .apply(whitespace.delegate);
    }

    /**
     * Creates a whitespace character option.
     *
     * @param chars whitespace characters
     * @return whitespace option
     */
    @Deprecated(since = "0.3.0")
    public static WhitespaceOption WithWhitespaceChars(String chars) {
        return whitespace -> com.williamcallahan.tui4j.compat.lipgloss.Whitespace
            .WithWhitespaceChars(chars)
            .apply(whitespace.delegate);
    }

    /**
     * Constructs a whitespace renderer using the provided renderer and options.
     *
     * @param renderer renderer context
     * @param options whitespace options
     * @return whitespace instance
     */
    @Deprecated(since = "0.3.0")
    public static Whitespace newWhiteSpace(Renderer renderer, WhitespaceOption... options) {
        com.williamcallahan.tui4j.compat.lipgloss.Whitespace canonical =
            com.williamcallahan.tui4j.compat.lipgloss.Whitespace
                .newWhiteSpace(renderer.toCanonical(), toCanonicalOptions(options));
        return new Whitespace(canonical);
    }

    /**
     * Renders whitespace to the requested width.
     *
     * @param width width
     * @return rendered whitespace
     */
    @Deprecated(since = "0.3.0")
    public String render(int width) {
        return delegate.render(width);
    }

    /**
     * Converts legacy whitespace options to canonical options.
     *
     * @param options legacy whitespace options
     * @return canonical whitespace options
     */
    @Deprecated(since = "0.3.0")
    static com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption[] toCanonicalOptions(
        WhitespaceOption... options) {
        if (options == null || options.length == 0) {
            return new com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption[0];
        }
        com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption[] canonicalOptions =
            new com.williamcallahan.tui4j.compat.lipgloss.Whitespace.WhitespaceOption[options.length];
        for (int i = 0; i < options.length; i++) {
            WhitespaceOption option = options[i];
            canonicalOptions[i] = whitespace -> option.apply(new Whitespace(whitespace));
        }
        return canonicalOptions;
    }

    /**
     * Adapts a Bubble Tea terminal color to the canonical lipgloss terminal color interface.
     *
     * @param color bubbletea terminal color
     * @return canonical terminal color
     */
    private static com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor adaptColor(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor color) {
        if (color == null) {
            return null;
        }
        return new com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor() {
            /** {@inheritDoc} */
            @Override
            public org.jline.utils.AttributedStyle applyAsBackground(
                org.jline.utils.AttributedStyle style,
                com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
            ) {
                return color.applyAsBackground(style, renderer);
            }

            /** {@inheritDoc} */
            @Override
            public org.jline.utils.AttributedStyle applyAsForeground(
                org.jline.utils.AttributedStyle style,
                com.williamcallahan.tui4j.compat.lipgloss.Renderer renderer
            ) {
                return color.applyAsForeground(style, renderer);
            }
        };
    }
}
