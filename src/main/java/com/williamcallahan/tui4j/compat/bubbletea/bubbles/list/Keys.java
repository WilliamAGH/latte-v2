package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

/**
 * Port of Bubbles keys.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class Keys {

    private final Binding cursorUp;
    private final Binding cursorDown;
    private final Binding nextPage;
    private final Binding prevPage;
    private final Binding goToStart;
    private final Binding goToEnd;
    private final Binding filter;
    private final Binding clearFilter;
    private final Binding cancelWhileFiltering;
    private final Binding acceptWhileFiltering;
    private final Binding showFullHelp;
    private final Binding closeFullHelp;
    private final Binding quit;
    private final Binding forceQuit;

    public Keys() {
        // Browsing
        this.cursorUp = new Binding(Binding.withKeys("up", "k"), Binding.withHelp("↑/k", "up"));
        this.cursorDown = new Binding(Binding.withKeys("down", "j"), Binding.withHelp("↓/j", "down"));
        this.prevPage = new Binding(Binding.withKeys("left", "h", "pgup", "b", "u"), Binding.withHelp("←/h/pgup", "prev page"));
        this.nextPage = new Binding(Binding.withKeys("right", "l", "pgdown", "f", "d"), Binding.withHelp("→/l/pgdn", "next page"));
        this.goToStart = new Binding(Binding.withKeys("home", "g"), Binding.withHelp("g/home", "go to start"));
        this.goToEnd = new Binding(Binding.withKeys("end", "G"), Binding.withHelp("G/end", "go to end"));

        // Filtering
        this.filter = new Binding(Binding.withKeys("/"), Binding.withHelp("/", "filter"));
        this.clearFilter = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "clear filter"));
        this.cancelWhileFiltering = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "cancel"));
        this.acceptWhileFiltering = new Binding(Binding.withKeys("enter", "tab", "shift+tab", "ctrl+k", "up", "ctrl+j", "down"),
                Binding.withHelp("enter", "apply filter"));

        // Help
        this.showFullHelp = new Binding(Binding.withKeys("?"), Binding.withHelp("?", "more"));
        this.closeFullHelp = new Binding(Binding.withKeys("?"), Binding.withHelp("?", "close help"));

        // Quitting
        this.quit = new Binding(Binding.withKeys("q", "esc"), Binding.withHelp("q", "quit"));
        this.forceQuit = new Binding(Binding.withKeys("ctrl+c"));
    }

    public Binding cursorUp() {
        return cursorUp;
    }

    public Binding cursorDown() {
        return cursorDown;
    }

    public Binding nextPage() {
        return nextPage;
    }

    public Binding prevPage() {
        return prevPage;
    }

    public Binding goToStart() {
        return goToStart;
    }

    public Binding goToEnd() {
        return goToEnd;
    }

    public Binding filter() {
        return filter;
    }

    public Binding clearFilter() {
        return clearFilter;
    }

    public Binding cancelWhileFiltering() {
        return cancelWhileFiltering;
    }

    public Binding acceptWhileFiltering() {
        return acceptWhileFiltering;
    }

    public Binding showFullHelp() {
        return showFullHelp;
    }

    public Binding closeFullHelp() {
        return closeFullHelp;
    }

    public Binding quit() {
        return quit;
    }

    public Binding forceQuit() {
        return forceQuit;
    }
}
