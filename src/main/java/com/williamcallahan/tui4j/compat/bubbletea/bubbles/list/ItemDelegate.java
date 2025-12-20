package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

/**
 * Port of Bubbles item delegate.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public interface ItemDelegate {

    interface UpdateFunction {
        Command update(Message msg, List listModel);
    }

    interface ShortHelpFunc {
        Binding[] get();
    }

    interface FullHelpFunc {
        Binding[][] get();
    }

    void render(StringBuilder output, List list, int index, FilteredItem filteredItem);
    int height();
    int spacing();
    Command update(Message msg, List listModel);
}
