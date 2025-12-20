package com.williamcallahan.tui4j.compat.bubbletea.input;

/**
 * Mouse button values.
 * Bubble Tea: bubbletea/mouse.go
 */
public enum MouseButton {
    MouseButtonNone("none"),

    // left button
    MouseButtonLeft("left"),

    // middle button (pressing the scroll wheel)
    MouseButtonMiddle("middle"),

    // right button
    MouseButtonRight("right"),

    // turn scroll wheel up
    MouseButtonWheelUp("wheel up"),

    // turn scroll wheel down
    MouseButtonWheelDown("wheel down"),

    // push scroll wheel left
    MouseButtonWheelLeft("wheel left"),

    // push scroll wheel right
    MouseButtonWheelRight("wheel right"),

    // 4th button (aka browser backward button)
    MouseButtonBackward("backward"),

    // 5th button (aka browser forward button)
    MouseButtonForward("forward"),

    // 10?
    MouseButton10("button 10"),

    // 11?
    MouseButton11("button 11");

    private final String buttonName;

    MouseButton(String buttonName) {
        this.buttonName = buttonName;
    }

    public String buttonName() {
        return buttonName;
    }

    @Override
    public String toString() {
        return buttonName;
    }
}
