package com.williamcallahan.tui4j.compat.bubbles.list;

import static com.williamcallahan.tui4j.compat.bubbles.list.DefaultItemStyles.ELLIPSIS;
import static com.williamcallahan.tui4j.compat.bubbletea.Command.batch;

import com.williamcallahan.tui4j.ansi.Truncate;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.paginator.Paginator;
import com.williamcallahan.tui4j.compat.bubbles.paginator.Type;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Size;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Port of Bubbles list.
 * Upstream: bubbles/list/list.go
 * <p>
 * Policy references for compat ports: AGENTS.md [JD1a], [JD1b], [FS1g].
 */
public class List
    implements Model, com.williamcallahan.tui4j.compat.bubbles.help.KeyMap
{

    private boolean showTitle;
    private boolean showFilter;
    private boolean showStatusBar;
    private boolean showPagination;
    private boolean showHelp;
    private boolean filteringEnabled;

    private String itemNameSingular;
    private String itemNamePlural;

    private String title;
    private Styles styles;
    private boolean infiniteScrolling;

    private KeyMap keys;
    private boolean disableQuitKeybindings;

    private Supplier<Binding[]> additionalShortHelpKeyMap;
    private Supplier<Binding[]> additionalFullHelpKeyMap;

    private Spinner spinner;
    private boolean showSpinner;
    private int width;
    private int height;
    private Paginator paginator;
    private int cursor;
    private Help help;
    private TextInput filterInput;
    private FilterState filterState;
    private boolean filterOnAcceptOnly;

    private Duration statusMessageLifetime;
    private String statusMessage;
    private Timer statusMessageTimer;
    private CompletableFuture<Message> statusMessageFuture;

    private ListDataSource dataSource;
    private boolean fetchingItems;
    private long totalItems = 0;
    private long matchedItems = 0;
    private java.util.List<FilteredItem> currentPageItems;
    private ItemDelegate itemDelegate;

    /**
     * Creates a list backed by a static array of items.
     *
     * @param items list items
     * @param width list width
     * @param height list height
     */
    public List(Item[] items, int width, int height) {
        this(items, new DefaultDelegate(), width, height);
    }

    /**
     * Creates a list backed by a static array of items with a custom delegate.
     *
     * @param items list items
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     */
    public List(Item[] items, ItemDelegate delegate, int width, int height) {
        setup(new DefaultDataSource(this, items), delegate, width, height);
    }

    /**
     * Creates a list backed by a data source and delegate.
     *
     * @param dataSource list data source
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     */
    public List(
        ListDataSource dataSource,
        ItemDelegate delegate,
        int width,
        int height
    ) {
        setup(dataSource, delegate, width, height);
    }

    /**
     * Creates a list backed by a data source.
     *
     * @param dataSource list data source
     * @param width list width
     * @param height list height
     */
    public List(ListDataSource dataSource, int width, int height) {
        this(dataSource, new DefaultDelegate(), width, height);
    }

    private void setup(
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

    @Override
    public Command init() {
        paginator.setActiveDot(styles.activePaginationDot().render());
        paginator.setInactiveDot(styles.inactivePaginationDot().render());

        updatePagination();
        return fetchCurrentPageItems();
    }

    /**
     * Returns the current data source.
     *
     * @return list data source
     */
    public ListDataSource dataSource() {
        return dataSource;
    }

    /**
     * Refreshes the current page, optionally running callbacks after refresh.
     *
     * @param postRefresh callbacks to run after refresh
     * @return refresh command
     */
    public Command refresh(Runnable... postRefresh) {
        return fetchCurrentPageItems(
            Stream.concat(
                Stream.of(postRefresh),
                Stream.of(this::keepCursorInBounds)
            ).toArray(Runnable[]::new)
        );
    }

    private Command fetchCurrentPageItems(Runnable... postFetch) {
        this.fetchingItems = true;
        updateKeybindings();

        String filterValue =
            filterState == FilterState.Unfiltered ? "" : filterInput.value();

        return batch(updateFilter(), startSpinner(), () ->
            new FetchedCurrentPageItems(
                dataSource.fetchItems(
                    paginator.page(),
                    paginator.perPage(),
                    filterValue
                ),
                postFetch
            )
        );
    }

    private Command updateFilter() {
        if (fetchingItems) {
            filterInput.blur();
            return null;
        } else {
            return filterInput.focus();
        }
    }

    /**
     * Sets whether filtering only applies when accepted.
     *
     * @param filterOnAcceptOnly {@code true} to require accept
     */
    public void setFilterOnAcceptOnly(boolean filterOnAcceptOnly) {
        this.filterOnAcceptOnly = filterOnAcceptOnly;
    }

    /**
     * Enables or disables filtering.
     *
     * @param filteringEnabled {@code true} to enable filtering
     * @return command to reset filtering when disabling
     */
    public Command setFilteringEnabled(boolean filteringEnabled) {
        this.filteringEnabled = filteringEnabled;
        if (!filteringEnabled) {
            return resetFiltering();
        }
        return null;
    }

    /**
     * Returns whether filtering is enabled.
     *
     * @return {@code true} when filtering is enabled
     */
    public boolean filteringEnabled() {
        return filteringEnabled;
    }

    /**
     * Sets the list title.
     *
     * @param title title text
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets whether the title is shown.
     *
     * @param showTitle {@code true} to show the title
     * @return command to refresh items
     */
    public Command setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return fetchCurrentPageItems();
    }

    /**
     * Sets the filter text and refreshes items.
     *
     * @param filter filter text
     * @return command to refresh items
     */
    public Command setFilterText(String filter) {
        this.filterState = FilterState.Filtering;
        this.filterInput.setValue(filter);
        return fetchCurrentPageItems();
    }

    /**
     * Sets the filter state and refreshes items.
     *
     * @param filterState new filter state
     * @return command to refresh items
     */
    public Command setFilterState(FilterState filterState) {
        this.paginator.setPage(0);
        this.cursor = 0;
        this.filterInput.cursorEnd();
        this.filterInput.focus();
        this.filterState = filterState;

        return fetchCurrentPageItems();
    }

    /**
     * Returns whether the title is shown.
     *
     * @return {@code true} when the title is shown
     */
    public boolean showTitle() {
        return showTitle;
    }

    /**
     * Sets whether the filter input is shown.
     *
     * @param showFilter {@code true} to show the filter input
     */
    public void setShowFilter(boolean showFilter) {
        this.showFilter = showFilter;
        updatePagination();
    }

    /**
     * Returns whether the filter input is shown.
     *
     * @return {@code true} when the filter input is shown
     */
    public boolean showFilter() {
        return showFilter;
    }

    /**
     * Sets whether the status bar is shown.
     *
     * @param showStatusBar {@code true} to show the status bar
     */
    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
        updatePagination();
    }

    /**
     * Returns whether the status bar is shown.
     *
     * @return {@code true} when the status bar is shown
     */
    public boolean showStatusBar() {
        return showStatusBar;
    }

    /**
     * Sets the singular and plural names used in the status bar.
     *
     * @param singular singular item name
     * @param plural plural item name
     */
    public void setStatusBarItemName(String singular, String plural) {
        this.itemNameSingular = singular;
        this.itemNamePlural = plural;
    }

    /**
     * Returns the singular item name used in the status bar.
     *
     * @return singular item name
     */
    public String itemNameSingular() {
        return itemNameSingular;
    }

    /**
     * Returns the plural item name used in the status bar.
     *
     * @return plural item name
     */
    public String itemNamePlural() {
        return itemNamePlural;
    }

    /**
     * Sets whether pagination is shown.
     *
     * @param showPagination {@code true} to show pagination
     */
    public void setShowPagination(boolean showPagination) {
        this.showPagination = showPagination;
        updatePagination();
    }

    /**
     * Returns whether pagination is shown.
     *
     * @return {@code true} when pagination is shown
     */
    public boolean showPagination() {
        return showPagination;
    }

    /**
     * Sets whether the help view is shown.
     *
     * @param showHelp {@code true} to show help
     * @return command to refresh items
     */
    public Command setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
        return fetchCurrentPageItems();
    }

    /**
     * Returns whether the help view is shown.
     *
     * @return {@code true} when help is shown
     */
    public boolean showHelp() {
        return showHelp;
    }

    /**
     * Selects the item at the given index.
     *
     * The index is zero-based across the full visible sequence, not page-local.
     *
     * @param index zero-based absolute item index
     * @return command to refresh items
     */
    public Command select(int index) {
        this.paginator.setPage(index / paginator.perPage());
        this.cursor = index % paginator.perPage();
        return fetchCurrentPageItems();
    }

    /**
     * Resets the selected index to the first item.
     * <p>
     * Note: This only updates cursor state. Call {@link #select(int)} to run
     * the command that refreshes items.
     */
    public void resetSelected() {
        select(0);
    }

    /**
     * Sets the item delegate and refreshes items.
     *
     * @param itemDelegate item delegate
     * @return command to refresh items
     */
    public Command setItemDelegate(ItemDelegate itemDelegate) {
        this.itemDelegate = itemDelegate;
        return fetchCurrentPageItems();
    }

    /**
     * Returns the currently selected item, if any.
     *
     * @return selected item or {@code null}
     */
    public Item selectedItem() {
        if (cursor < 0 || currentPageItems.isEmpty() || cursor >= currentPageItems.size()) {
            return null;
        }
        return this.currentPageItems.get(this.cursor).item();
    }

    /**
     * Returns all items available to be shown, matching upstream Go behavior.
     * <p>
     * When unfiltered, returns all items. When filtering/filtered, returns all matching items.
     * Note: pagination is only applied for rendering, not for this method.
     *
     * @return all visible items
     */
    public java.util.List<FilteredItem> visibleItems() {
        String filterValue =
            filterState == FilterState.Unfiltered ? "" : filterInput.value();
        FetchedItems all = dataSource.fetchItems(
            0,
            Integer.MAX_VALUE,
            filterValue
        );
        return all.items();
    }

    /**
     * Returns the absolute index of the cursor within the full list.
     *
     * This index is zero-based and mirrors Bubble's {@code Model.Index()} math
     * ({@code page * perPage + cursor}) from {@code bubbles/list/list.go}.
     * <p>
     * Policy references for compat ports: AGENTS.md [JD1a], [JD1b], [FS1g].
     *
     * @return zero-based absolute cursor index
     */
    public int index() {
        return paginator.page() * paginator.perPage() + cursor;
    }

    /**
     * Returns the cursor position within the current page.
     *
     * Cursor indexes are zero-based within the active page.
     *
     * @return zero-based cursor index within the page
     */
    public int cursor() {
        return cursor;
    }

    /**
     * Advances to the next page if available.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command nextPage() {
        if (!paginator.onLastPage()) {
            paginator.nextPage();
            cursor = 0;
            return fetchCurrentPageItems();
        }
        return null;
    }

    /**
     * Moves to the previous page if available.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command prevPage() {
        if (paginator.page() > 0) {
            paginator.prevPage();
            cursor = 0;
            return fetchCurrentPageItems();
        }
        return null;
    }

    /**
     * Returns the current filter state.
     *
     * @return filter state
     */
    public FilterState filterState() {
        return filterState;
    }

    /**
     * Returns the current filter value.
     *
     * @return filter value
     */
    public String filterValue() {
        return filterInput.value();
    }

    /**
     * Returns whether the list is currently accepting filter input.
     *
     * @return {@code true} when filtering input is active
     */
    public boolean settingFilter() {
        return this.filterState == FilterState.Filtering;
    }

    /**
     * Returns whether a filter has been applied.
     *
     * @return {@code true} when filtered
     */
    public boolean isFiltered() {
        return this.filterState == FilterState.FilterApplied;
    }

    /**
     * Returns the list width.
     *
     * @return width in columns
     */
    public int width() {
        return width;
    }

    /**
     * Returns the list height.
     *
     * @return height in rows
     */
    public int height() {
        return height;
    }

    /**
     * Sets the spinner type used while fetching items.
     *
     * @param spinnerType spinner type
     */
    public void setSpinnerType(SpinnerType spinnerType) {
        spinner.setType(spinnerType);
    }

    /**
     * Starts the spinner tick loop.
     *
     * @return spinner tick command
     */
    public Command startSpinner() {
        this.showSpinner = true;
        return spinner::tick;
    }

    /**
     * Stops the spinner.
     */
    public void stopSpinner() {
        this.showSpinner = false;
    }

    /**
     * Disables quit keybindings for this list.
     */
    public void disableQuitKeybindings() {
        this.disableQuitKeybindings = true;
        keys.quit().setEnabled(false);
        keys.forceQuit().setEnabled(false);
    }

    /**
     * Sets a temporary status message.
     *
     * @param status status message text
     * @return command that clears the message after the lifetime
     */
    public Command newStatusMessage(String status) {
        this.statusMessage = status;

        // Cancel any pending status message future and timer
        if (statusMessageFuture != null && !statusMessageFuture.isDone()) {
            statusMessageFuture.cancel(true);
        }
        if (statusMessageTimer != null) {
            statusMessageTimer.cancel();
        }

        CompletableFuture<Message> future = new CompletableFuture<>();
        Timer timer = new Timer();
        statusMessageFuture = future;
        statusMessageTimer = timer;

        return () -> {
            if (future.isCancelled()) {
                timer.cancel();
                return null;
            }
            timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        future.complete(new StatusMessageTimeoutMessage());
                    }
                },
                statusMessageLifetime.toMillis()
            );

            try {
                return future.join();
            } catch (CancellationException e) {
                // Future was cancelled by a new status message or hideStatusMessage
                return null;
            } finally {
                timer.cancel();
                if (statusMessageTimer == timer) {
                    statusMessageTimer = null;
                }
            }
        };
    }

    /**
     * Sets the lifetime for status messages.
     *
     * @param statusMessageLifetime message lifetime
     */
    public void setStatusMessageLifetime(Duration statusMessageLifetime) {
        this.statusMessageLifetime = statusMessageLifetime;
    }

    /**
     * Sets the list dimensions and refreshes items.
     *
     * @param width list width
     * @param height list height
     * @return command to refresh items
     */
    public Command setSize(int width, int height) {
        int promptWidth = Size.width(
            styles.title().render(filterInput.prompt())
        );

        this.width = width;
        this.height = height;
        this.help.setWidth(width);
        int inputWidth = Math.max(
            0,
            width - promptWidth - Size.width(spinnerView())
        );
        this.filterInput.setWidth(inputWidth);
        updatePagination();

        return fetchCurrentPageItems();
    }

    /**
     * Sets the list width.
     *
     * @param width list width
     * @return command to refresh items
     */
    public Command setWidth(int width) {
        return setSize(width, height);
    }

    /**
     * Sets the list height.
     *
     * @param height list height
     * @return command to refresh items
     */
    public Command setHeight(int height) {
        return setSize(width, height);
    }

    /**
     * Resets filtering to the unfiltered state.
     *
     * @return command to refresh items, or {@code null} if unchanged
     */
    protected Command resetFiltering() {
        if (filterState == FilterState.Unfiltered) {
            return null;
        }

        this.filterState = FilterState.Unfiltered;
        this.filterInput.reset();

        return fetchCurrentPageItems();
    }

    /**
     * Updates enabled state of keybindings based on list state.
     */
    protected void updateKeybindings() {
        if (filterState == FilterState.Filtering || fetchingItems) {
            keys.cursorUp().setEnabled(false);
            keys.cursorDown().setEnabled(false);
            keys.nextPage().setEnabled(false);
            keys.prevPage().setEnabled(false);
            keys.goToStart().setEnabled(false);
            keys.goToEnd().setEnabled(false);
            keys.filter().setEnabled(false);
            keys.clearFilter().setEnabled(false);
            if (!fetchingItems) {
                keys.cancelWhileFiltering().setEnabled(true);
            }
            keys
                .acceptWhileFiltering()
                .setEnabled(!"".equals(filterInput.value()));
            keys.quit().setEnabled(false);
            keys.showFullHelp().setEnabled(false);
            keys.closeFullHelp().setEnabled(false);
        } else {
            boolean hasItems = totalItems != 0;
            keys.cursorUp().setEnabled(hasItems);
            keys.cursorDown().setEnabled(hasItems);
            keys.goToStart().setEnabled(hasItems);
            keys.goToEnd().setEnabled(hasItems);
            keys.filter().setEnabled(filteringEnabled && hasItems);

            boolean hasPages = paginator.totalPages() > 1;
            keys.nextPage().setEnabled(hasPages);
            keys.prevPage().setEnabled(hasPages);
            keys
                .clearFilter()
                .setEnabled(filterState == FilterState.FilterApplied);
            keys.cancelWhileFiltering().setEnabled(false);
            keys.acceptWhileFiltering().setEnabled(false);
            keys.quit().setEnabled(!disableQuitKeybindings);

            if (help.showAll()) {
                keys.showFullHelp().setEnabled(true);
                keys.closeFullHelp().setEnabled(true);
            } else {
                boolean minHelp = countEnabledBindings(fullHelp()) > 1;
                keys.showFullHelp().setEnabled(minHelp);
                keys.closeFullHelp().setEnabled(minHelp);
            }
        }
    }

    private void hideStatusMessage() {
        this.statusMessage = "";

        // Cancel the active timer if it's still running
        if (statusMessageFuture != null && !statusMessageFuture.isDone()) {
            statusMessageFuture.cancel(true);
        }
    }

    boolean updatePagination() {
        int index = index();
        int availHeight = this.height;

        if (showTitle || (showFilter && filteringEnabled)) {
            availHeight -= Size.height(titleView());
        }
        if (showStatusBar) {
            availHeight -= Size.height(statusView());
        }
        if (showPagination) {
            availHeight -= Size.height(paginationView());
        }
        if (showHelp) {
            availHeight -= Size.height(helpView());
        }

        int perPage = Math.max(
            1,
            availHeight / (itemDelegate.height() + itemDelegate.spacing())
        );

        paginator.setPerPage(perPage);
        int recalculatedTotalPages = calculateTotalPages(perPage);
        paginator.setTotalPages(recalculatedTotalPages);

        int newPage = Math.min(index / perPage, recalculatedTotalPages - 1);
        int newCursor = index % perPage;

        this.cursor = newCursor;

        if (paginator.page() != newPage) {
            paginator.setPage(newPage);
            updateKeybindings();
            return true;
        }

        updateKeybindings();
        return false;
    }

    /**
     * Calculates page count for the current matched-item set and page size.
     * <p>
     * Returns at least {@code 1} to preserve paginator invariants used by
     * {@code onFirstPage/onLastPage} checks. This matches Bubble's paginator
     * default/count semantics from {@code bubbles/paginator/paginator.go}.
     *
     * @param perPage items per page
     * @return computed total pages
     */
    private int calculateTotalPages(int perPage) {
        if (matchedItems <= 0) {
            return 1;
        }
        long pages = (matchedItems + perPage - 1) / perPage;
        return (int) Math.min(pages, Integer.MAX_VALUE);
    }

    @Override
    public UpdateResult<List> update(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.forceQuit())) {
                return UpdateResult.from(this, QuitMessage::new);
            }
        } else if (
            msg instanceof FetchedCurrentPageItems(FetchedItems fetchedItems, Runnable[] postFetchCallbacks)
        ) {
            stopSpinner();
            this.fetchingItems = false;

            this.currentPageItems = fetchedItems.items();
            this.matchedItems = fetchedItems.matchedItems();
            this.totalItems = fetchedItems.totalItems();

            updateKeybindings();

            for (Runnable runnable : postFetchCallbacks) {
                runnable.run();
            }

            // page changed thus need to re-fetch
            boolean forcedPageChange = updatePagination();
            if (forcedPageChange) {
                return UpdateResult.from(this, fetchCurrentPageItems());
            }

            return UpdateResult.from(this, updateFilter());
        } else if (msg instanceof TickMessage && showSpinner) {
            commands.add(spinner.update(msg).command());
        } else if (msg instanceof StatusMessageTimeoutMessage) {
            hideStatusMessage();
        }

        if (filterState == FilterState.Filtering) {
            commands.add(handleFiltering(msg));
        } else {
            commands.add(handleBrowsing(msg));
        }

        return UpdateResult.from(this, batch(commands));
    }

    private Command handleBrowsing(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.clearFilter())) {
                commands.add(resetFiltering());
            } else if (Binding.matches(keyPressMessage, keys.quit())) {
                return QuitMessage::new;
            } else if (Binding.matches(keyPressMessage, keys.cursorUp())) {
                commands.add(cursorUp());
            } else if (Binding.matches(keyPressMessage, keys.cursorDown())) {
                commands.add(cursorDown());
            } else if (Binding.matches(keyPressMessage, keys.prevPage())) {
                commands.add(cursorLeft());
            } else if (Binding.matches(keyPressMessage, keys.nextPage())) {
                commands.add(cursorRight());
            } else if (Binding.matches(keyPressMessage, keys.goToStart())) {
                commands.add(gotoStart());
            } else if (Binding.matches(keyPressMessage, keys.goToEnd())) {
                commands.add(gotoEnd());
            } else if (Binding.matches(keyPressMessage, keys.filter())) {
                hideStatusMessage();
                commands.add(TextInput::blink);

                if (!paginator.onFirstPage()) {
                    paginator.setPage(0);
                }

                this.filterState = FilterState.Filtering;
                filterInput.cursorEnd();
                filterInput.focus();
                updateKeybindings();

                commands.add(fetchCurrentPageItems(() -> this.cursor = 0));

                return batch(commands);
            } else if (
                Binding.matches(keyPressMessage, keys.showFullHelp()) ||
                Binding.matches(keyPressMessage, keys.closeFullHelp())
            ) {
                help.setShowAll(!help.showAll());
                updatePagination();

                commands.add(fetchCurrentPageItems());
            }
            commands.add(itemDelegate.update(msg, this));
        }
        return batch(commands);
    }

    private Command gotoStart() {
        if (paginator.onFirstPage()) {
            return null;
        }

        paginator.setPage(0);
        this.cursor = 0;
        return fetchCurrentPageItems();
    }

    private Command gotoEnd() {
        if (paginator.onLastPage()) {
            return null;
        }

        paginator.setPage(paginator.totalPages() - 1);
        return fetchCurrentPageItems(this::keepCursorInBounds);
    }

    private Command cursorLeft() {
        if (paginator.onFirstPage()) {
            return null;
        }
        paginator.prevPage();
        return fetchCurrentPageItems();
    }

    private Command cursorRight() {
        if (paginator.onLastPage()) {
            return null;
        }
        paginator.nextPage();
        return fetchCurrentPageItems(this::keepCursorInBounds);
    }

    private void keepCursorInBounds() {
        if (currentPageItems.isEmpty()) {
            this.cursor = 0;
            return;
        }
        this.cursor = Math.clamp(this.cursor, 0, this.currentPageItems.size() - 1);
    }

    /**
     * Moves the cursor up by one row, paging as needed.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command cursorUp() {
        if ((cursor - 1) > -1) {
            this.cursor--;
            return null;
        }

        if (paginator.page() != 0) {
            paginator.prevPage();
            return fetchCurrentPageItems(() ->
                this.cursor = Math.max(0, currentPageItems.size() - 1)
            );
        }

        if (!infiniteScrolling) {
            return null;
        }

        paginator.setPage(paginator.totalPages() - 1);
        return fetchCurrentPageItems(() ->
            cursor = Math.max(0, currentPageItems.size() - 1)
        );
    }

    /**
     * Moves the cursor down by one row, paging as needed.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command cursorDown() {
        if ((cursor + 1) < currentPageItems.size()) {
            this.cursor++;
            return null;
        }

        if (!paginator.onLastPage()) {
            paginator.nextPage();
            return fetchCurrentPageItems(() -> cursor = 0);
        }

        if (infiniteScrolling) {
            paginator.setPage(0);
            return fetchCurrentPageItems(() -> cursor = 0);
        }
        return null;
    }

    private Command handleFiltering(Message msg) {
        java.util.List<Command> commands = new LinkedList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.cancelWhileFiltering())) {
                resetFiltering();

                commands.add(
                    fetchCurrentPageItems(() -> {
                        keys.filter().setEnabled(true);
                        keys.clearFilter().setEnabled(false);
                    })
                );
            } else if (
                Binding.matches(keyPressMessage, keys.acceptWhileFiltering())
            ) {
                hideStatusMessage();

                if (totalItems > 0) {
                    if (this.matchedItems > 0) {
                        filterInput.blur();
                        this.filterState = FilterState.FilterApplied;
                        updateKeybindings();

                        if (filterInput.isEmpty()) {
                            commands.add(resetFiltering());
                        }
                    } else {
                        commands.add(resetFiltering());
                    }
                }
            }
        }

        String beforeChange = filterInput.value();
        UpdateResult<TextInput> updateResult = filterInput.update(msg);
        boolean filterChanged = !Objects.equals(
            beforeChange,
            updateResult.model().value()
        );
        this.filterInput = updateResult.model();
        commands.add(updateResult.command());

        if (filterChanged && !filterOnAcceptOnly) {
            commands.add(
                fetchCurrentPageItems(() -> {
                    keys
                        .acceptWhileFiltering()
                        .setEnabled(!filterInput.isEmpty());
                    updatePagination();
                })
            );
        }
        return batch(commands);
    }

    @Override
    public String view() {
        java.util.List<String> sections = new ArrayList<>();
        int availHeight = this.height;

        if (showTitle || (showFilter && filteringEnabled)) {
            String v = titleView();
            sections.add(v);
            availHeight -= Size.height(v);
        }

        if (showStatusBar) {
            String v = statusView();
            sections.add(v);
            availHeight -= Size.height(v);
        }

        String pagination = null;
        if (showPagination) {
            pagination = paginationView();
            availHeight -= Size.height(pagination);
        }

        String helpSection = null;
        if (showHelp) {
            helpSection = helpView();
            availHeight -= Size.height(helpSection);
        }

        String content = Style.newStyle()
            .height(availHeight)
            .render(populatedView());
        sections.add(content);

        if (showPagination) {
            sections.add(pagination);
        }

        if (showHelp) {
            sections.add(helpSection);
        }

        return VerticalJoinDecorator.joinVertical(
            Position.Left,
            sections.toArray(new String[0])
        );
    }

    private String titleView() {
        StringBuilder view = new StringBuilder();
        Style titleBarStyle = styles.titleBar().copy();
        String spinnerView = spinnerView();
        int spinnerWidth = Size.width(spinnerView);
        String spinnerLeftGap = " ";
        boolean spinnerOnLeft =
            titleBarStyle.leftPadding() >=
                spinnerWidth + Size.width(spinnerLeftGap) &&
            showSpinner;

        if (showSpinner && spinnerOnLeft) {
            view.append(spinnerView).append(spinnerLeftGap);
            int titleBarGap = titleBarStyle.leftPadding();
            titleBarStyle = titleBarStyle.paddingLeft(
                titleBarGap - spinnerWidth - Size.width(spinnerLeftGap)
            );
        }

        if (showFilter && filterState == FilterState.Filtering) {
            view.append(filterInput.view());
        } else if (showTitle) {
            view.append(styles.title().render(title));
            if (filterState != FilterState.Filtering) {
                view.append(" ").append(statusMessage);
                view = new StringBuilder(
                    Truncate.truncate(
                        view.toString(),
                        width - spinnerWidth,
                        ELLIPSIS
                    )
                );
            }
        }

        if (showSpinner && !spinnerOnLeft) {
            int availSpace =
                width - Size.width(styles.titleBar().render(view.toString()));
            if (availSpace > spinnerWidth) {
                view
                    .append(" ".repeat(availSpace - spinnerWidth))
                    .append(spinnerView);
            }
        }

        if (!view.isEmpty()) {
            return titleBarStyle.render(view.toString());
        }
        return view.toString();
    }

    private String statusView() {
        StringBuilder status = new StringBuilder();
        long matchedCount = this.matchedItems;

        String itemName = matchedCount == 1 ? itemNameSingular : itemNamePlural;

        String itemsDisplay = "%d %s".formatted(matchedCount, itemName);

        if (filterState == FilterState.Filtering) {
            if (matchedCount == 0) {
                status = new StringBuilder(
                    styles.statusEmpty().render("Nothing matched")
                );
            } else {
                status = new StringBuilder(itemsDisplay);
            }
        } else if (totalItems == 0) {
            status = new StringBuilder(
                styles.statusEmpty().render("No " + itemNamePlural)
            );
        } else {
            boolean filtered = filterState == FilterState.FilterApplied;

            if (filtered) {
                String f = filterInput.value().trim();
                f = Truncate.truncate(f, 10, ELLIPSIS);
                status.append("“%s” ".formatted(f));
            }

            status.append(itemsDisplay);
        }

        if (
            filterState == FilterState.Filtering ||
            filterState == FilterState.FilterApplied
        ) {
            long numFiltered = totalItems - matchedCount;
            if (numFiltered > 0) {
                status
                    .append(styles.dividerDot().render())
                    .append(
                        styles
                            .statusBarFilterCount()
                            .render("%d filtered".formatted(numFiltered))
                    );
            }
        }

        return styles.statusBar().render(status.toString());
    }

    private String paginationView() {
        Style style = styles.paginationStyle().copy();
        if (itemDelegate.spacing() == 0 && style.topMargin() == 0) {
            style = style.marginTop(1);
        }

        if (paginator.totalPages() < 2) {
            return style.render("");
        }

        paginator.setType(Type.Dots);
        String view = paginator.view();
        if (Size.width(view) > width) {
            paginator.setType(Type.Arabic);
            view = styles.arabicPagination().render(paginator.view());
        }

        return style.render(view);
    }

    private String populatedView() {
        StringBuilder b = new StringBuilder();

        if (this.matchedItems == 0) {
            if (filterState == FilterState.Filtering) {
                return "";
            }
            return styles.noItems().render("No " + itemNamePlural + ".");
        }

        for (int i = 0; i < this.currentPageItems.size(); i++) {
            itemDelegate.render(
                b,
                this,
                paginator.page() * paginator.perPage() + i,
                this.currentPageItems.get(i)
            );
            if (i != this.currentPageItems.size() - 1) {
                b.append("\n".repeat(itemDelegate.spacing() + 1));
            }
        }

        int itemsOnPage = this.currentPageItems.size();
        if (itemsOnPage < paginator.perPage()) {
            int emptyLines =
                (paginator.perPage() - itemsOnPage) *
                (itemDelegate.height() + itemDelegate.spacing());
            b.append("\n".repeat(emptyLines));
        }
        return b.toString();
    }

    private String helpView() {
        return styles.helpStyle().render(help.render(this));
    }

    private String spinnerView() {
        return spinner.view();
    }

    private int countEnabledBindings(Binding[][] groups) {
        int count = 0;
        for (Binding[] group : groups) {
            for (Binding binding : group) {
                if (binding.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Binding[] shortHelp() {
        java.util.List<Binding> kb = new LinkedList<>(
            Arrays.asList(keys.cursorUp(), keys.cursorDown())
        );

        boolean filtering = filterState == FilterState.Filtering;
        if (!filtering && itemDelegate instanceof KeyMap delegateKeyMap) {
            kb.addAll(Arrays.asList(delegateKeyMap.shortHelp()));
        }

        kb.addAll(
            Arrays.asList(
                keys.filter(),
                keys.clearFilter(),
                keys.acceptWhileFiltering(),
                keys.cancelWhileFiltering()
            )
        );

        if (!filtering && additionalShortHelpKeyMap != null) {
            kb.addAll(Arrays.asList(additionalShortHelpKeyMap.get()));
        }

        kb.addAll(Arrays.asList(keys.quit(), keys.showFullHelp()));

        return kb.toArray(new Binding[0]);
    }

    @Override
    public Binding[][] fullHelp() {
        java.util.List<Binding[]> kb = new LinkedList<>();
        kb.add(
            new Binding[] {
                keys.cursorUp(),
                keys.cursorDown(),
                keys.nextPage(),
                keys.prevPage(),
                keys.goToStart(),
                keys.goToEnd(),
            }
        );

        boolean filtering = filterState == FilterState.Filtering;
        if (!filtering && itemDelegate instanceof KeyMap delegateKeyMap) {
            kb.addAll(Arrays.asList(delegateKeyMap.fullHelp()));
        }

        java.util.List<Binding> listLevelBindings = new LinkedList<>(
            Arrays.asList(
                keys.filter(),
                keys.clearFilter(),
                keys.acceptWhileFiltering(),
                keys.cancelWhileFiltering()
            )
        );

        if (!filtering && additionalFullHelpKeyMap != null) {
            listLevelBindings.addAll(
                Arrays.asList(additionalFullHelpKeyMap.get())
            );
        }

        kb.add(listLevelBindings.toArray(new Binding[0]));
        kb.add(new Binding[] { keys.quit(), keys.closeFullHelp() });
        return kb.toArray(new Binding[0][]);
    }

    /**
     * Returns the current list styles.
     *
     * @return list styles
     */
    public Styles styles() {
        return styles;
    }

    /**
     * Sets additional short help key bindings.
     *
     * @param additionalShortHelpKeyMap supplier for extra bindings
     */
    public void setAdditionalShortHelpKeys(
        Supplier<Binding[]> additionalShortHelpKeyMap
    ) {
        this.additionalShortHelpKeyMap = additionalShortHelpKeyMap;
    }

    /**
     * Sets additional full help key bindings.
     *
     * @param additionalFullHelpKeyMap supplier for extra bindings
     */
    public void setAdditionalFullHelpKeys(
        Supplier<Binding[]> additionalFullHelpKeyMap
    ) {
        this.additionalFullHelpKeyMap = additionalFullHelpKeyMap;
    }
}
