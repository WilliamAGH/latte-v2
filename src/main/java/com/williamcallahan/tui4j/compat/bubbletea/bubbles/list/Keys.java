package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

/**
 * Port of Bubbles keys.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved to the canonical TUI4J path; use {@link com.williamcallahan.tui4j.compat.bubbles.list.KeyMap} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
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

    /**
     * Creates the default list key bindings.
     */
    @Deprecated(since = "0.3.0")
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

    /**
     * Returns the binding for moving the cursor up.
     *
     * @return cursor up binding
     */
    @Deprecated(since = "0.3.0")
    public Binding cursorUp() {
        return cursorUp;
    }

    /**
     * Returns the binding for moving the cursor down.
     *
     * @return cursor down binding
     */
    @Deprecated(since = "0.3.0")
    public Binding cursorDown() {
        return cursorDown;
    }

    /**
     * Returns the binding for moving to the next page.
     *
     * @return next page binding
     */
    @Deprecated(since = "0.3.0")
    public Binding nextPage() {
        return nextPage;
    }

    /**
     * Returns the binding for moving to the previous page.
     *
     * @return previous page binding
     */
    @Deprecated(since = "0.3.0")
    public Binding prevPage() {
        return prevPage;
    }

    /**
     * Returns the binding for jumping to the start of the list.
     *
     * @return go to start binding
     */
    @Deprecated(since = "0.3.0")
    public Binding goToStart() {
        return goToStart;
    }

    /**
     * Returns the binding for jumping to the end of the list.
     *
     * @return go to end binding
     */
    @Deprecated(since = "0.3.0")
    public Binding goToEnd() {
        return goToEnd;
    }

    /**
     * Returns the binding for opening the filter prompt.
     *
     * @return filter binding
     */
    @Deprecated(since = "0.3.0")
    public Binding filter() {
        return filter;
    }

    /**
     * Returns the binding for clearing the filter.
     *
     * @return clear filter binding
     */
    @Deprecated(since = "0.3.0")
    public Binding clearFilter() {
        return clearFilter;
    }

    /**
     * Returns the binding for canceling filtering.
     *
     * @return cancel while filtering binding
     */
    @Deprecated(since = "0.3.0")
    public Binding cancelWhileFiltering() {
        return cancelWhileFiltering;
    }

    /**
     * Returns the binding for accepting a filter while filtering.
     *
     * @return accept while filtering binding
     */
    @Deprecated(since = "0.3.0")
    public Binding acceptWhileFiltering() {
        return acceptWhileFiltering;
    }

    /**
     * Returns the binding for showing full help.
     *
     * @return show full help binding
     */
    @Deprecated(since = "0.3.0")
    public Binding showFullHelp() {
        return showFullHelp;
    }

    /**
     * Returns the binding for closing full help.
     *
     * @return close full help binding
     */
    @Deprecated(since = "0.3.0")
    public Binding closeFullHelp() {
        return closeFullHelp;
    }

    /**
     * Returns the binding for quitting the list.
     *
     * @return quit binding
     */
    @Deprecated(since = "0.3.0")
    public Binding quit() {
        return quit;
    }

    /**
     * Returns the binding for forcing a quit.
     *
     * @return force quit binding
     */
    @Deprecated(since = "0.3.0")
    public Binding forceQuit() {
        return forceQuit;
    }
}
