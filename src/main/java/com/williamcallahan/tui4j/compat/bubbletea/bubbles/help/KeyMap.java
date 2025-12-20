package com.williamcallahan.tui4j.compat.bubbletea.bubbles.help;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

/**
 * Port of Bubbles key map.
 * Bubble Tea: bubbletea/examples/help/main.go
 */
public interface KeyMap {

    Binding[] shortHelp();
    Binding[][] fullHelp();
}
