package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

/**
 * Tracks mouse selection state.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseSelectionTracker.java
 */
public final class MouseSelectionTracker {
    private boolean selecting;
    private int lastColumn;
    private int lastRow;

    /**
     * Update selection state from the latest mouse event.
     */
    public MouseSelectionUpdate update(MouseMessage message) {
        boolean wasSelecting = selecting;

        if (message.getAction() == MouseAction.MouseActionPress
                && message.getButton() == MouseButton.MouseButtonLeft) {
            selecting = true;
        } else if (message.getAction() == MouseAction.MouseActionRelease) {
            selecting = false;
        } else if (selecting
                && message.getAction() == MouseAction.MouseActionMotion
                && message.getButton() == MouseButton.MouseButtonNone) {
            selecting = false;
        }

        lastColumn = message.column();
        lastRow = message.row();

        MouseMessage selectionScrollUpdate = null;
        if (message.isWheel() && selecting) {
            selectionScrollUpdate = new MouseMessage(
                    lastColumn,
                    lastRow,
                    message.isShift(),
                    message.isAlt(),
                    message.isCtrl(),
                    MouseAction.MouseActionMotion,
                    MouseButton.MouseButtonLeft
            );
        }

        return new MouseSelectionUpdate(
                !wasSelecting && selecting,
                wasSelecting && !selecting,
                selecting,
                selectionScrollUpdate
        );
    }

    public boolean isSelecting() {
        return selecting;
    }

    public int lastColumn() {
        return lastColumn;
    }

    public int lastRow() {
        return lastRow;
    }
}
