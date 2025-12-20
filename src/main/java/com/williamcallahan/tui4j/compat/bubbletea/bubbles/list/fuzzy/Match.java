package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.fuzzy;

import java.util.List;

/**
 * Port of Bubbles match.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class Match {
    private String str;
    private int index;
    private List<Integer> matchedIndexes;
    private int score;

    public Match(String str, int index, List<Integer> matchedIndexes, int score) {
        this.str = str;
        this.index = index;
        this.matchedIndexes = matchedIndexes;
        this.score = score;
    }

    public String getStr() {
        return str;
    }

    public int getIndex() {
        return index;
    }

    public List<Integer> getMatchedIndexes() {
        return matchedIndexes;
    }

    public int getScore() {
        return score;
    }
}