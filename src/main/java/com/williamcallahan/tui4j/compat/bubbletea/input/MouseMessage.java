package com.williamcallahan.tui4j.compat.bubbletea.input;

import com.williamcallahan.tui4j.Message;

/**
 * Mouse input event data.
 * Bubble Tea: bubbletea/mouse.go
 */
public class MouseMessage implements Message {
    private final int x;
    private final int y;
    private final boolean shift;
    private final boolean alt;
    private final boolean ctrl;
    private final MouseAction action;
    private final MouseButton button;

    private static final int X10_MOUSE_BYTE_OFFSET = 32;

    public MouseMessage(int x, int y, boolean shift, boolean alt, boolean ctrl,
                        MouseAction action, MouseButton button) {
        this.x = x;
        this.y = y;
        this.shift = shift;
        this.alt = alt;
        this.ctrl = ctrl;
        this.action = action;
        this.button = button;
    }

    public int column() {
        return x;
    }

    public int row() {
        return y;
    }

    public boolean isShift() {
        return shift;
    }

    public boolean isAlt() {
        return alt;
    }

    public boolean isCtrl() {
        return ctrl;
    }

    public MouseAction getAction() {
        return action;
    }

    public MouseButton getButton() {
        return button;
    }

    public boolean isWheel() {
        return button == MouseButton.MouseButtonWheelUp ||
                button == MouseButton.MouseButtonWheelDown ||
                button == MouseButton.MouseButtonWheelLeft ||
                button == MouseButton.MouseButtonWheelRight;
    }

    @Override
    public String toString() {
        return String.format("MouseMessage(width=%d, height=%d, shift=%b, alt=%b, ctrl=%b, action=%s, button=%s)",
                x, y, shift, alt, ctrl, action, button);
    }

    public String describe() {
        StringBuilder s = new StringBuilder();

        // Add modifiers
        if (ctrl) {
            s.append("ctrl+");
        }
        if (alt) {
            s.append("alt+");
        }
        if (shift) {
            s.append("shift+");
        }

        // Handle different button/action combinations
        if (button == MouseButton.MouseButtonNone) {
            if (action == MouseAction.MouseActionMotion || action == MouseAction.MouseActionRelease) {
                s.append(action.value());
            } else {
                s.append("unknown");
            }
        } else if (isWheel()) {
            s.append(button.buttonName());
        } else {
            String btn = button.buttonName();
            if (!btn.isEmpty()) {
                s.append(btn);
            }
            String act = action.value();
            if (!act.isEmpty()) {
                s.append(" ").append(act);
            }
        }

        return s.toString();
    }

    public static MouseMessage parseX10MouseEvent(int col, int row, int button) {
        MouseEvent event = parseMouseButton(button, false);
        return new MouseMessage(
                col - 1,  // ✅ Subtract only 1 for zero-based indexing
                row - 1,
                event.shift,
                event.alt,
                event.ctrl,
                event.action,
                event.button
        );
    }

    public static MouseMessage parseSGRMouseEvent(int button, int col, int row, boolean release) {
        MouseEvent event = parseMouseButton(button, true);
        if (release && event.action != MouseAction.MouseActionMotion && !isWheelButton(event.button)) {
            event.action = MouseAction.MouseActionRelease;
            event.button = MouseButton.MouseButtonNone;
        }
        return new MouseMessage(
                col - 1,  // SGR already uses 1-based coordinates
                row - 1,
                event.shift,
                event.alt,
                event.ctrl,
                event.action,
                event.button
        );
    }

    private static class MouseEvent {
        boolean shift;
        boolean alt;
        boolean ctrl;
        MouseAction action = MouseAction.MouseActionPress; // Default action
        MouseButton button = MouseButton.MouseButtonNone;
    }

    private static MouseEvent parseMouseButton(int b, boolean isSGR) {
        MouseEvent m = new MouseEvent();
        int e = b & 0xFF;               // ✅ Treat b as unsigned
        if (!isSGR) {
            e = (e - X10_MOUSE_BYTE_OFFSET) & 0xFF;  // ✅ Wrap around correctly to stay in 0-255 range
        }

        final int BIT_SHIFT = 0b0000_0100;
        final int BIT_ALT = 0b0000_1000;
        final int BIT_CTRL = 0b0001_0000;
        final int BIT_MOTION = 0b0010_0000;
        final int BIT_WHEEL = 0b0100_0000;
        final int BIT_ADD = 0b1000_0000;
        final int BITS_MASK = 0b0000_0011;


        int buttonOffset = e & BITS_MASK;


        if ((e & BIT_ADD) != 0) {
            // Handles extra buttons: Backward, Forward
            m.button = MouseButton.values()[MouseButton.MouseButtonBackward.ordinal() + buttonOffset];
        } else if ((e & BIT_WHEEL) != 0) {
            // Handles mouse wheel: WheelUp, WheelDown
            m.button = MouseButton.values()[MouseButton.MouseButtonWheelUp.ordinal() + buttonOffset];
        } else {
            // Handles standard buttons: Left, Middle, Right
            if (buttonOffset == 0b11) { // X10 reports button release as 0b00000011 (3)
                m.action = MouseAction.MouseActionRelease;
                m.button = MouseButton.MouseButtonNone;
            } else {
                m.button = MouseButton.values()[MouseButton.MouseButtonLeft.ordinal() + buttonOffset];
                m.action = MouseAction.MouseActionPress; // Default to press if it's a button click
            }
        }


        // Motion bit doesn't get reported for wheel events
        if ((e & BIT_MOTION) != 0 && !isWheelButton(m.button)) {
            m.action = MouseAction.MouseActionMotion;
        } else if (m.action == null) {
            m.action = MouseAction.MouseActionPress; // Default to press if no other action is set
        }

        // Modifiers
        m.alt = (e & BIT_ALT) != 0;
        m.ctrl = (e & BIT_CTRL) != 0;
        m.shift = (e & BIT_SHIFT) != 0;

        return m;
    }

    private static boolean isWheelButton(MouseButton button) {
        return button == MouseButton.MouseButtonWheelUp ||
                button == MouseButton.MouseButtonWheelDown ||
                button == MouseButton.MouseButtonWheelLeft ||
                button == MouseButton.MouseButtonWheelRight;
    }
}
