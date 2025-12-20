package com.williamcallahan.tui4j.term;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;

/**
 * Represents terminal capability and background info.
 * tui4j: src/main/java/com/williamcallahan/tui4j/term/TerminalInfo.java
 */
public record TerminalInfo(boolean tty, TerminalColor backgroundColor) {

    private static TerminalInfoProvider infoProvider;

    public static void provide(TerminalInfoProvider infoProvider) {
        TerminalInfo.infoProvider = infoProvider;
    }

    public static TerminalInfo get() {
        return infoProvider.provide();
    }
}