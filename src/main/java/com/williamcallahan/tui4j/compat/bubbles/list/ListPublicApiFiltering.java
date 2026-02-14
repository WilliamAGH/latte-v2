package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import java.util.stream.Stream;

/**
 * Filtering and visibility-related public API for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
abstract class ListPublicApiFiltering extends ListBase {

    /** Returns the current data source.
     * @return current data source
     */
    public ListDataSource dataSource() {
        return dataSource;
    }

    /** Refreshes the current page.
     * @param postRefresh callbacks to run after refresh
     * @return refresh command
     */
    public Command refresh(Runnable... postRefresh) {
        return fetchCurrentPageItems(
            Stream.concat(
                Stream.of(postRefresh),
                Stream.of(() -> ListUpdateHandler.keepCursorInBounds(self()))
            ).toArray(Runnable[]::new)
        );
    }

    /** Sets whether filtering requires accept.
     * @param filterOnAcceptOnly whether filtering requires accept
     */
    public void setFilterOnAcceptOnly(boolean filterOnAcceptOnly) {
        this.filterOnAcceptOnly = filterOnAcceptOnly;
    }

    /** Enables/disables filtering; disabling resets filtering.
     * @param filteringEnabled whether filtering is enabled
     * @return reset-filter command, or null
     */
    public Command setFilteringEnabled(boolean filteringEnabled) {
        this.filteringEnabled = filteringEnabled;
        if (!filteringEnabled) {
            return resetFiltering();
        }
        return Command.none();
    }

    /** Returns whether filtering is enabled.
     * @return whether filtering is enabled
     */
    public boolean filteringEnabled() {
        return filteringEnabled;
    }

    /** Sets the list title.
     * @param title new title text
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Shows/hides the title and refreshes items.
     * @param showTitle whether to show the title
     * @return refresh command
     */
    public Command setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return fetchCurrentPageItems();
    }

    /** Sets filter text and refreshes items.
     * @param filter filter text
     * @return refresh command
     */
    public Command setFilterText(String filter) {
        this.filterState = FilterState.Filtering;
        this.filterInput.setValue(filter);
        return fetchCurrentPageItems();
    }

    /** Sets filter state and refreshes items.
     * @param filterState new filter state
     * @return refresh command
     */
    public Command setFilterState(FilterState filterState) {
        this.paginator.setPage(0);
        this.cursor = 0;
        this.filterInput.cursorEnd();
        this.filterInput.focus();
        this.filterState = filterState;

        return fetchCurrentPageItems();
    }

    /** Returns whether the title is shown.
     * @return whether title is shown
     */
    public boolean showTitle() {
        return showTitle;
    }

    /** Shows/hides filter input.
     * @param showFilter whether to show filter input
     */
    public void setShowFilter(boolean showFilter) {
        this.showFilter = showFilter;
        updatePagination();
    }

    /** Returns whether filter input is shown.
     * @return whether filter input is shown
     */
    public boolean showFilter() {
        return showFilter;
    }

    /** Shows/hides status bar.
     * @param showStatusBar whether to show status bar
     */
    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
        updatePagination();
    }

    /** Returns whether status bar is shown.
     * @return whether status bar is shown
     */
    public boolean showStatusBar() {
        return showStatusBar;
    }

    /** Sets the singular and plural names used in the status bar.
     * @param singular singular name
     * @param plural plural name
     */
    public void setStatusBarItemName(String singular, String plural) {
        this.itemNameSingular = singular;
        this.itemNamePlural = plural;
    }

    /** Returns the singular item name.
     * @return singular item name
     */
    public String itemNameSingular() {
        return itemNameSingular;
    }

    /** Returns the plural item name.
     * @return plural item name
     */
    public String itemNamePlural() {
        return itemNamePlural;
    }

    /** Shows/hides help and refreshes items.
     * @param showHelp whether to show help
     * @return refresh command
     */
    public Command setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
        return fetchCurrentPageItems();
    }

    /** Returns whether help is shown.
     * @return whether help is shown
     */
    public boolean showHelp() {
        return showHelp;
    }

    /** Returns the current filter state.
     * @return current filter state
     */
    public FilterState filterState() {
        return filterState;
    }

    /** Returns the current filter value.
     * @return current filter value
     */
    public String filterValue() {
        return filterInput.value();
    }

    /** Returns whether filter input is active.
     * @return whether filter input is active
     */
    public boolean settingFilter() {
        return this.filterState == FilterState.Filtering;
    }

    /** Returns whether a filter is applied.
     * @return whether a filter is applied
     */
    public boolean isFiltered() {
        return this.filterState == FilterState.FilterApplied;
    }

    /** Resets filtering to unfiltered state.
     * @return refresh command, or null
     */
    protected Command resetFiltering() {
        if (filterState == FilterState.Unfiltered) {
            return Command.none();
        }

        this.filterState = FilterState.Unfiltered;
        this.filterInput.reset();

        return fetchCurrentPageItems();
    }
}

