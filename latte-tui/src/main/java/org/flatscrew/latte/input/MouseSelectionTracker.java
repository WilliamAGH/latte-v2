package org.flatscrew.latte.input;

/**
 * Tracks whether a left-button mouse selection is active and emits state transitions.
 * Latte extension; no Bubble Tea equivalent.
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
}
