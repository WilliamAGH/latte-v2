package org.flatscrew.latte.input;

/**
 * Selection state transition summary produced by {@link MouseSelectionTracker}.
 * Latte extension; no Bubble Tea equivalent.
 */
public record MouseSelectionUpdate(
        boolean selectionStarted,
        boolean selectionEnded,
        boolean selectionActive,
        MouseMessage selectionScrollUpdate
) {
}
