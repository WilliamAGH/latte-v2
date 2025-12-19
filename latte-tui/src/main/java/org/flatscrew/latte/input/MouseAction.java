package org.flatscrew.latte.input;

/**
 * Mouse action values.
 * Bubble Tea: seeks to replicate charmbracelet/bubbletea/mouse.go MouseAction behavior.
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
