package com.williamcallahan.tui4j.compat.bubbles.list;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.batch;

import com.williamcallahan.tui4j.compat.bubbletea.Command;

/**
 * Data fetch orchestration for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
final class ListDataFetcher {

    private ListDataFetcher() {
    }

    static Command fetchCurrentPageItems(List list, Runnable... postFetch) {
        list.fetchingItems = true;
        list.updateKeybindings();

        String filterValue =
            list.filterState == FilterState.Unfiltered ? "" : list.filterInput.value();

        return batch(
            updateFilter(list),
            list.startSpinner(),
            () -> new FetchedCurrentPageItems(
                list.dataSource.fetchItems(
                    list.paginator.page(),
                    list.paginator.perPage(),
                    filterValue
                ),
                postFetch
            )
        );
    }

    static Command updateFilter(List list) {
        if (list.fetchingItems) {
            list.filterInput.blur();
            return Command.none();
        }
        return list.filterInput.focus();
    }
}

