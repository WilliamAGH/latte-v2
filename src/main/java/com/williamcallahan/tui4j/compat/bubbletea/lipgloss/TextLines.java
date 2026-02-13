package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

/**
 * Text-line measurement helpers for Bubble Tea-compatible rendering.
 * <p>
 * Lipgloss: ranges.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.lipgloss.TextLines} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class TextLines {
    private final com.williamcallahan.tui4j.compat.lipgloss.TextLines delegate;

    /**
     * Wraps the canonical text lines helper.
     *
     * @param delegate canonical text lines helper
     */
    private TextLines(com.williamcallahan.tui4j.compat.lipgloss.TextLines delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates a text-lines helper from raw text.
     *
     * @param text text to split
     * @return text-lines helper
     */
    @Deprecated(since = "0.3.0")
    public static TextLines fromText(String text) {
        return new TextLines(com.williamcallahan.tui4j.compat.lipgloss.TextLines.fromText(text));
    }

    /**
     * Returns the width of the widest line.
     *
     * @return widest line length
     */
    @Deprecated(since = "0.3.0")
    public int widestLineLength() {
        return delegate.widestLineLength();
    }

    /**
     * Returns the text split into lines.
     *
     * @return lines
     */
    @Deprecated(since = "0.3.0")
    public String[] lines() {
        return delegate.lines();
    }
}
