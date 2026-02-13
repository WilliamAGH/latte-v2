package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;

/**
 * Pagination and selection-related public API for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
abstract class ListPublicApiPaging extends ListPublicApiFiltering {

    /** Shows/hides pagination.
     * @param showPagination whether to show pagination
     */
    public void setShowPagination(boolean showPagination) {
        this.showPagination = showPagination;
        updatePagination();
    }

    /** Returns whether pagination is shown.
     * @return whether pagination is shown
     */
    public boolean showPagination() {
        return showPagination;
    }

    /** Selects the item at the given absolute, zero-based index.
     * @param index absolute index
     * @return refresh command
     */
    public Command select(int index) {
        this.paginator.setPage(index / paginator.perPage());
        this.cursor = index % paginator.perPage();
        return fetchCurrentPageItems();
    }

    /** Resets selected index to the first item (cursor only). */
    public void resetSelected() {
        select(0);
    }

    /** Sets delegate and refreshes items.
     * @param itemDelegate item delegate
     * @return refresh command
     */
    public Command setItemDelegate(ItemDelegate itemDelegate) {
        this.itemDelegate = itemDelegate;
        return fetchCurrentPageItems();
    }

    /** Returns the selected item.
     * @return selected item or {@code null}
     */
    public Item selectedItem() {
        if (
            cursor < 0 ||
                currentPageItems.isEmpty() ||
                cursor >= currentPageItems.size()
        ) {
            return null;
        }
        return this.currentPageItems.get(this.cursor).item();
    }

    /** Returns items available to be shown.
     * @return items available to be shown
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

    /** Returns the absolute cursor index across the full list.
     * @return absolute cursor index
     */
    public int index() {
        return paginator.page() * paginator.perPage() + cursor;
    }

    /** Returns the cursor index within the current page.
     * @return cursor index
     */
    public int cursor() {
        return cursor;
    }

    /** Advances to the next page if available.
     * @return refresh command, or null
     */
    public Command nextPage() {
        if (!paginator.onLastPage()) {
            paginator.nextPage();
            cursor = 0;
            return fetchCurrentPageItems();
        }
        return Command.none();
    }

    /** Moves to the previous page if available.
     * @return refresh command, or null
     */
    public Command prevPage() {
        if (paginator.page() > 0) {
            paginator.prevPage();
            cursor = 0;
            return fetchCurrentPageItems();
        }
        return Command.none();
    }
}

