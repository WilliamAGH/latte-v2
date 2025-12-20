package com.williamcallahan.tui4j.compat.bubbletea.bubbles.key;

/**
 * Port of Bubbles help.
 * Bubble Tea: bubbletea/examples/help/main.go
 */
public record Help(String key, String desc) {

    public Help() {
        this("", "");
    }
}
