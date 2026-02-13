package com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput;

/**
 * Port of Bubbles echo mode.
 * Bubble Tea: bubbletea/examples/textinput/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.bubbles.textinput.EchoMode} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum EchoMode {

    /** Echoes input as typed. */
    @Deprecated(since = "0.3.0")
    EchoNormal,
    /** Masks input characters (password mode). */
    @Deprecated(since = "0.3.0")
    EchoPassword,
    /** Does not echo input. */
    @Deprecated(since = "0.3.0")
    EchoNone
}
