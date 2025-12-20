package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * Port of Bubbles filter function.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
@FunctionalInterface
public interface FilterFunction {

    Rank[] apply(String term, String[] targets);
}
