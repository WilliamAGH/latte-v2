package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;

/**
 * Port of Bubbles keys.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class KeyMap implements com.williamcallahan.tui4j.compat.bubbles.help.KeyMap {

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
     * Creates default list key bindings.
     */
    public KeyMap() {
        // Browsing
        this.cursorUp = new Binding(Binding.withKeys("up", "k"), Binding.withHelp("↑/k", "up"));
        this.cursorDown = new Binding(Binding.withKeys("down", "j"), Binding.withHelp("↓/j", "down"));
        this.prevPage = new Binding(Binding.withKeys("left", "h", "pgup", "b", "u"),
                Binding.withHelp("←/h/pgup", "prev page"));
        this.nextPage = new Binding(Binding.withKeys("right", "l", "pgdown", "f", "d"),
                Binding.withHelp("→/l/pgdn", "next page"));
        this.goToStart = new Binding(Binding.withKeys("home", "g"), Binding.withHelp("g/home", "go to start"));
        this.goToEnd = new Binding(Binding.withKeys("end", "G"), Binding.withHelp("G/end", "go to end"));

        // Filtering
        this.filter = new Binding(Binding.withKeys("/"), Binding.withHelp("/", "filter"));
        this.clearFilter = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "clear filter"));
        this.cancelWhileFiltering = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "cancel"));
        this.acceptWhileFiltering = new Binding(
                Binding.withKeys("enter", "tab", "shift+tab", "ctrl+k", "up", "ctrl+j", "down"),
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
     * @return cursor-up binding
     */
    public Binding cursorUp() {
        return cursorUp;
    }

    /**
     * Returns the binding for moving the cursor down.
     *
     * @return cursor-down binding
     */
    public Binding cursorDown() {
        return cursorDown;
    }

    /**
     * Returns the binding for moving to the next page.
     *
     * @return next-page binding
     */
    public Binding nextPage() {
        return nextPage;
    }

    /**
     * Returns the binding for moving to the previous page.
     *
     * @return previous-page binding
     */
    public Binding prevPage() {
        return prevPage;
    }

    /**
     * Returns the binding for jumping to the first item.
     *
     * @return go-to-start binding
     */
    public Binding goToStart() {
        return goToStart;
    }

    /**
     * Returns the binding for jumping to the last item.
     *
     * @return go-to-end binding
     */
    public Binding goToEnd() {
        return goToEnd;
    }

    /**
     * Returns the binding for starting a filter.
     *
     * @return filter binding
     */
    public Binding filter() {
        return filter;
    }

    /**
     * Returns the binding for clearing the filter.
     *
     * @return clear-filter binding
     */
    public Binding clearFilter() {
        return clearFilter;
    }

    /**
     * Returns the binding for canceling filter input.
     *
     * @return cancel-filter binding
     */
    public Binding cancelWhileFiltering() {
        return cancelWhileFiltering;
    }

    /**
     * Returns the binding for accepting filter input.
     *
     * @return accept-filter binding
     */
    public Binding acceptWhileFiltering() {
        return acceptWhileFiltering;
    }

    /**
     * Returns the binding for showing full help.
     *
     * @return show-full-help binding
     */
    public Binding showFullHelp() {
        return showFullHelp;
    }

    /**
     * Returns the binding for closing full help.
     *
     * @return close-full-help binding
     */
    public Binding closeFullHelp() {
        return closeFullHelp;
    }

    /**
     * Returns the binding for quitting.
     *
     * @return quit binding
     */
    public Binding quit() {
        return quit;
    }

    /**
     * Returns the binding for forcing quit.
     *
     * @return force-quit binding
     */
    public Binding forceQuit() {
        return forceQuit;
    }

    /**
     * Returns the short help bindings.
     *
     * @return short help bindings
     */
    @Override
    public Binding[] shortHelp() {
        return new Binding[] { cursorUp, cursorDown };
    }

    /**
     * Returns the full help bindings.
     *
     * @return full help bindings
     */
    @Override
    public Binding[][] fullHelp() {
        return new Binding[][] {
                { cursorUp, cursorDown, prevPage, nextPage, goToStart, goToEnd },
                { filter, clearFilter, showFullHelp, closeFullHelp, quit, forceQuit }
        };
    }
}
