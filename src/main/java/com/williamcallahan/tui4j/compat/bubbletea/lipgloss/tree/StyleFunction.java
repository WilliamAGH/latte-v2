package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;

/**
 * Port of Lip Gloss style function.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
@FunctionalInterface
public interface StyleFunction {

    Style apply(Children children, int index);
}
