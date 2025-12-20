package com.williamcallahan.tui4j.input;

import java.util.List;

/**
 * Exposes clickable or hoverable targets.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseTargetProvider.java
 */
public interface MouseTargetProvider {
    List<MouseTarget> mouseTargets();
}
