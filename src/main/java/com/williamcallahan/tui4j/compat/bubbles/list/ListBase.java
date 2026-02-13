package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.paginator.Paginator;
import com.williamcallahan.tui4j.compat.bubbles.paginator.Type;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Shared state and internal helpers for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
abstract class ListBase {

    boolean showTitle;
    boolean showFilter;
    boolean showStatusBar;
    boolean showPagination;
    boolean showHelp;
    boolean filteringEnabled;

    String itemNameSingular;
    String itemNamePlural;

    String title;
    Styles styles;
    boolean infiniteScrolling;

    KeyMap keys;
    boolean disableQuitKeybindings;

    Supplier<Binding[]> additionalShortHelpKeyMap = () -> new Binding[0];
    Supplier<Binding[]> additionalFullHelpKeyMap = () -> new Binding[0];

    Spinner spinner;
    boolean showSpinner;
    int width;
    int height;
    Paginator paginator;
    int cursor;
    Help help;
    TextInput filterInput;
    FilterState filterState;
    boolean filterOnAcceptOnly;

    Duration statusMessageLifetime;
    String statusMessage;
    Timer statusMessageTimer;
    CompletableFuture<Message> statusMessageFuture;

    ListDataSource dataSource;
    boolean fetchingItems;
    long totalItems = 0;
    long matchedItems = 0;
    java.util.List<FilteredItem> currentPageItems;
    ItemDelegate itemDelegate;

    final List self() {
        return (List) this;
    }

    final void setup(
        ListDataSource dataSource,
        ItemDelegate delegate,
        int width,
        int height
    ) {
        this.dataSource = dataSource;
        this.currentPageItems = new ArrayList<>();
        this.filterState = FilterState.Unfiltered;
        this.itemDelegate = delegate;
        this.fetchingItems = false;

        this.width = width;
        this.height = height;
        this.title = "List";
        this.showTitle = true;
        this.showFilter = true;
        this.showStatusBar = true;
        this.showPagination = true;
        this.showHelp = true;
        this.itemNameSingular = "item";
        this.itemNamePlural = "items";
        this.filteringEnabled = true;
        this.keys = new KeyMap();
        this.styles = Styles.defaultStyles();
        this.statusMessageLifetime = Duration.ofSeconds(1);
        this.statusMessage = "";
        this.help = new Help();

        this.spinner = new Spinner(SpinnerType.LINE);
        spinner.setStyle(styles.spinner());

        this.filterInput = new TextInput();
        filterInput.setPrompt("Filter: ");
        filterInput.setPromptStyle(styles.filterPrompt());
        filterInput.cursor().setStyle(styles.filterCursor());
        filterInput.setCharLimit(64);
        filterInput.focus();

        this.paginator = new Paginator();
        paginator.setType(Type.Dots);
    }

    final Command fetchCurrentPageItems(Runnable... postFetch) {
        return ListDataFetcher.fetchCurrentPageItems(self(), postFetch);
    }

    final Command updateFilter() {
        return ListDataFetcher.updateFilter(self());
    }

    /** Updates keybindings based on the current list state. */
    protected final void updateKeybindings() {
        ListKeybindingsUpdater.updateKeybindings(self());
    }

    final boolean updatePagination() {
        return ListPaginationUpdater.updatePagination(self());
    }
}
