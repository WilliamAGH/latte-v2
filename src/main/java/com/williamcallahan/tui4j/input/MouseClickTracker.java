package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

/**
 * Tracks click sequences and counts.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseClickTracker.java
 */
public final class MouseClickTracker {
    private static final long CLICK_CHAIN_THRESHOLD_MS = 500;

    private int lastPressColumn = -1;
    private int lastPressRow = -1;
    private MouseButton lastPressButton = MouseButton.MouseButtonNone;
    private long lastClickAtMs = 0L;
    private int lastClickColumn = -1;
    private int lastClickRow = -1;
    private MouseButton lastClickButton = MouseButton.MouseButtonNone;
    private int lastClickCount = 0;

    public MouseClickMessage handle(MouseMessage message, MouseTarget target) {
        return handle(message, target, System.currentTimeMillis());
    }

    MouseClickMessage handle(MouseMessage message, MouseTarget target, long nowMs) {
        if (message == null || message.isWheel()) {
            return null;
        }
        if (message.getAction() == MouseAction.MouseActionPress
                && message.getButton() != MouseButton.MouseButtonNone) {
            lastPressColumn = message.column();
            lastPressRow = message.row();
            lastPressButton = message.getButton();
            return null;
        }
        if (message.getAction() == MouseAction.MouseActionRelease) {
            MouseClickMessage click = createClickIfMatches(message, target, nowMs);
            lastPressColumn = -1;
            lastPressRow = -1;
            lastPressButton = MouseButton.MouseButtonNone;
            return click;
        }
        return null;
    }

    private MouseClickMessage createClickIfMatches(MouseMessage message, MouseTarget target, long nowMs) {
        if (lastPressButton == MouseButton.MouseButtonNone) {
            return null;
        }
        if (message.column() != lastPressColumn || message.row() != lastPressRow) {
            return null;
        }
        int clickCount = calculateClickCount(nowMs, lastPressColumn, lastPressRow, lastPressButton);
        return new MouseClickMessage(
                message.column(),
                message.row(),
                lastPressButton,
                message.isShift(),
                message.isAlt(),
                message.isCtrl(),
                clickCount,
                target,
                message
        );
    }

    private int calculateClickCount(long nowMs, int column, int row, MouseButton button) {
        if (button == MouseButton.MouseButtonNone) {
            lastClickCount = 0;
            return 0;
        }
        boolean sameTarget = lastClickColumn == column
                && lastClickRow == row
                && lastClickButton == button;
        boolean withinThreshold = (nowMs - lastClickAtMs) <= CLICK_CHAIN_THRESHOLD_MS;
        int nextCount = (sameTarget && withinThreshold) ? lastClickCount + 1 : 1;
        lastClickAtMs = nowMs;
        lastClickColumn = column;
        lastClickRow = row;
        lastClickButton = button;
        lastClickCount = nextCount;
        return nextCount;
    }
}
