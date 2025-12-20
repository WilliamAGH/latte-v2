package com.williamcallahan.tui4j.ansi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * ANSI escape code enumeration.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/Code.java
 */
public enum Code {
    EnableFocusReporting("\u001b[?1004h"),
    DisableFocusReporting("\u001b[?1004l"),
    EnableMouseCellMotion("\u001b[?1002h"),
    DisableMouseCellMotion("\u001b[?1002l"),
    EnableMouseAllMotion("\u001b[?1003h"),
    DisableMouseAllMotion("\u001b[?1003l"),
    EnableMouseSgrExt("\u001b[?1006h"),
    DisableMouseSgrExt("\u001b[?1006l"),

    SetMouseTextCursor("\u001b]22;text\u001b\\"),
    SetMousePointerCursor("\u001b]22;pointer\u001b\\"),
    ResetMouseCursor("\u001b]22;\u001b\\");

    private final String value;

    Code(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static String copyToClipboard(String text) {
        String b64 = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        return "\u001b]52;c;" + b64 + "\u0007";
    }
}
