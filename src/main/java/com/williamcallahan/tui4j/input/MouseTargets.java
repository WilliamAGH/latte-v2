package com.williamcallahan.tui4j.input;

import java.util.Comparator;
import java.util.List;

/**
 * Collection of mouse target regions.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseTargets.java
 */
public final class MouseTargets {
    private MouseTargets() {
    }

    public static MouseTarget hitTest(List<MouseTarget> targets, int column, int row) {
        if (targets == null || targets.isEmpty()) {
            return null;
        }
        return targets.stream()
                .filter(target -> target != null && target.contains(column, row))
                .max(Comparator.comparingInt(MouseTarget::zIndex))
                .orElse(null);
    }
}
