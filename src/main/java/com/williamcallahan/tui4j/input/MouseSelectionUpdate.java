package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

/**tui4j
 * Represents a selection update from a mouse event.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseSelectionUpdate.java
 */
public record MouseSelectionUpdate(
        boolean selectionStarted,
        boolean selectionEnded,
        boolean selectionActive,
        MouseMessage selectionScrollUpdate
) {
}
