package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

/**
 * Port of Lip Gloss filter.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class Filter implements Children {

    public interface FilterFunction {
        boolean filter(int index);
    }

    private final Children data;
    private FilterFunction filterFunction = index -> true;

    public Filter(Children data) {
        this.data = data;
    }

    @Override
    public Node at(int index) {
        int j = 0;
        for (int i = 0; i < data.length(); i++) {
            if (filterFunction.filter(i)) {
                if (j == index) {
                    return data.at(i);
                }
                j++;
            }
        }

        return null;
    }

    public Filter filter(FilterFunction filterFunction) {
        this.filterFunction = filterFunction;
        return this;
    }

    @Override
    public Children remove(int index) {
        return null;
    }

    @Override
    public Children append(Node child) {
        return null;
    }

    @Override
    public int length() {
        int j = 0;
        for (int i = 0; i < data.length(); i++) {
            if (filterFunction.filter(i)) {
                j++;
            }
        }
        return j;
    }
}
