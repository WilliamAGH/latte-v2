package com.williamcallahan.tui4j.ansi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * ANSI escape code enumeration.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/Code.java
 */
public enum Code {
    /** Enable focus reporting. */
    EnableFocusReporting("\u001b[?1004h"),
    /** Disable focus reporting. */
    DisableFocusReporting("\u001b[?1004l"),
    /** Enable mouse tracking. */
    EnableMouseNormalTracking("\u001b[?1000h"),
    /** Disable mouse tracking. */
    DisableMouseNormalTracking("\u001b[?1000l"),
    /** Enable mouse cell motion tracking. */
    EnableMouseCellMotion("\u001b[?1002h"),
    /** Disable mouse cell motion tracking. */
    DisableMouseCellMotion("\u001b[?1002l"),
    /** Enable mouse all motion tracking. */
    EnableMouseAllMotion("\u001b[?1003h"),
    /** Disable mouse all motion tracking. */
    DisableMouseAllMotion("\u001b[?1003l"),
    /** Enable SGR mouse reporting. */
    EnableMouseSgrExt("\u001b[?1006h"),
    /** Disable SGR mouse reporting. */
    DisableMouseSgrExt("\u001b[?1006l"),

    /** Enable bracketed paste mode. */
    EnableBracketedPaste("\u001b[?2004h"),
    /** Disable bracketed paste mode. */
    DisableBracketedPaste("\u001b[?2004l"),

    /**
     * Enable Kitty keyboard protocol (progressive enhancement mode 1).
     * This enables disambiguate escape codes, allowing terminals to report
     * modifier keys on Enter (Shift+Enter, Ctrl+Enter) via CSI-u sequences.
     * @see <a href="https://sw.kovidgoyal.net/kitty/keyboard-protocol/">Kitty Keyboard Protocol</a>
     */
    EnableKittyKeyboard("\u001b[>1u"),
    /** Disable Kitty keyboard protocol (pop keyboard mode). */
    DisableKittyKeyboard("\u001b[<u"),

    /** Set mouse cursor to text. */
    SetMouseTextCursor("\u001b]22;text\u001b\\"),
    /** Set mouse cursor to pointer. */
    SetMousePointerCursor("\u001b]22;pointer\u001b\\"),
    /** Reset mouse cursor. */
    ResetMouseCursor("\u001b]22;\u001b\\");

    private final String value;

    Code(String value) {
        this.value = value;
    }

    /**
     * Returns the ANSI escape sequence value.
     *
     * @return escape code value
     */
    public String value() {
        return value;
    }

    /**
     * Builds an OSC 52 escape sequence for clipboard copy.
     *
     * @param text text to copy
     * @return OSC 52 sequence
     */
    public static String copyToClipboard(String text) {
        String b64 = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        return "\u001b]52;c;" + b64 + "\u0007";
    }
}
