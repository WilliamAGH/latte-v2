package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.lipgloss.Size;

/**
 * Pagination sizing and invariants for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
final class ListPaginationUpdater {

    private ListPaginationUpdater() {
    }

    /**
     * Recomputes pagination state (per-page, total pages, cursor mapping).
     *
     * @param list list model
     * @return {@code true} when a re-fetch is required to reflect the new sizing
     */
    static boolean updatePagination(List list) {
        int absoluteIndex = list.index();
        int previousPerPage = list.paginator.perPage();
        int previousPage = list.paginator.page();

        int availHeight = list.height;
        if (list.showTitle || (list.showFilter && list.filteringEnabled)) {
            availHeight -= Size.height(ListViewRenderer.titleView(list));
        }
        if (list.showStatusBar) {
            availHeight -= Size.height(ListViewRenderer.statusView(list));
        }
        if (list.showHelp) {
            availHeight -= Size.height(ListViewRenderer.helpView(list));
        }

        applyPagination(list, availHeight);

        // Only reserve a pagination row when pagination is visible.
        if (list.showPagination && list.paginator.totalPages() > 1) {
            availHeight -= Size.height(ListViewRenderer.paginationView(list));
            applyPagination(list, availHeight);
        }

        int perPage = list.paginator.perPage();
        int totalPages = list.paginator.totalPages();
        int newPage = Math.min(absoluteIndex / perPage, totalPages - 1);
        list.cursor = absoluteIndex % perPage;

        if (previousPage != newPage) {
            list.paginator.setPage(newPage);
        }

        list.updateKeybindings();

        return previousPerPage != perPage || previousPage != newPage;
    }

    private static void applyPagination(List list, int availHeight) {
        int perPage = perPageForHeight(list, availHeight);
        int totalPages = calculateTotalPages(list, perPage);
        list.paginator.setPerPage(perPage);
        list.paginator.setTotalPages(totalPages);
    }

    private static int perPageForHeight(List list, int availHeight) {
        return Math.max(
            1,
            availHeight / (list.itemDelegate.height() + list.itemDelegate.spacing())
        );
    }

    static int calculateTotalPages(List list, int perPage) {
        if (list.matchedItems <= 0) {
            return 1;
        }
        long pages = (list.matchedItems + perPage - 1) / perPage;
        return (int) Math.min(pages, Integer.MAX_VALUE);
    }
}

