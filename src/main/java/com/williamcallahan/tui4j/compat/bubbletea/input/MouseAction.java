package com.williamcallahan.tui4j.compat.bubbletea.input;

/**
 * Mouse action values.
 * Bubble Tea: bubbletea/mouse.go
 */
public enum MouseAction {
    MouseActionPress("press"),
    MouseActionRelease("release"),
    MouseActionMotion("motion");

    private final String value;

    MouseAction(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
