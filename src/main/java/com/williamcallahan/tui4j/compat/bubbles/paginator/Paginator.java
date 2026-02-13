package com.williamcallahan.tui4j.compat.bubbles.paginator;

import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;

/**
 * Port of the Bubble Tea paginator component.
 * Upstream: bubbles/paginator/paginator.go
 * <p>
 * Indexing contract: {@code page} is zero-based for internal math while rendered
 * Arabic output is one-based ({@code page + 1}) for humans.
 * <p>
 * Policy references for compat ports: AGENTS.md [JD1a], [JD1b], [FS1g].
 */
public class Paginator {

    /**
     * Port of the paginator option hook used during construction.
     * Upstream: bubbles/paginator/paginator.go
     */
    public interface Option {

        /**
         * Creates an option that sets total pages.
         *
         * @param totalPages total pages
         * @return option instance
         */
        static Option withTotalPages(int totalPages) {
            return paginator -> paginator.totalPages = totalPages;
        }

        /**
         * Creates an option that sets items per page.
         *
         * @param perPage items per page
         * @return option instance
         */
        static Option withPerPage(int perPage) {
            return paginator -> paginator.perPage = perPage;
        }

        /**
         * Applies this option to the paginator.
         *
         * @param paginator paginator to mutate
         */
        void apply(Paginator paginator);
    }

    private Type type;
    private int page;
    private int perPage;
    private int totalPages;
    private String activeDot;
    private String inactiveDot;
    private String arabicFormat;

    private KeyMap keyMap;

    /**
     * Creates a paginator with optional configuration.
     *
     * @param options construction options
     */
    public Paginator(Option... options) {
        this.type = Type.Arabic;
        this.page = 0;
        this.perPage = 1;
        this.totalPages = 1;
        this.keyMap = KeyMap.defaultKeyMap();
        this.activeDot = "•";
        this.inactiveDot = "○";
        this.arabicFormat = "%d/%d";

        for (Option option : options) {
            option.apply(this);
        }
    }

    /**
     * Sets the paginator type.
     *
     * @param type paginator type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns how many items appear on the current page.
     *
     * @param totalItems total items in the list
     * @return items on the current page
     */
    public int itemsOnPage(int totalItems) {
        if (totalItems < 1) {
            return 0;
        }
        Bounds sliceBounds = getSliceBounds(totalItems);
        return sliceBounds.end() - sliceBounds.start();
    }

    /**
     * Returns the slice bounds for the current page.
     *
     * @param length total items length
     * @return bounds for slicing
     */
    public Bounds getSliceBounds(int length) {
        int start = page * perPage;
        int end = Math.min(page * perPage + perPage, length);
        return new Bounds(start, end);
    }

    /**
     * Moves to the previous page when possible.
     */
    public void prevPage() {
        if (page > 0) {
            page--;
        }
    }

    /**
     * Moves to the next page when possible.
     */
    public void nextPage() {
        if (!onLastPage()) {
            page++;
        }
    }

    /**
     * Returns whether the paginator is on the last page.
     *
     * This compares a zero-based page index to {@code totalPages - 1}.
     *
     * @return {@code true} when on the last page
     */
    public boolean onLastPage() {
        return page == totalPages - 1;
    }

    /**
     * Returns whether the paginator is on the first page.
     *
     * The first page index is always {@code 0}.
     *
     * @return {@code true} when on the first page
     */
    public boolean onFirstPage() {
        return page == 0;
    }

    /**
     * Updates the paginator from an incoming message.
     *
     * @param msg input message
     * @return updated paginator
     */
    public Paginator update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keyMap.nextPage())) {
                nextPage();
            } else if (Binding.matches(keyPressMessage, keyMap.prevPage())) {
                prevPage();
            }
        }
        return this;
    }

    /**
     * Renders the paginator view.
     *
     * @return rendered paginator
     */
    public String view() {
        if (type == Type.Dots) {
            return dotsView();
        }
        return arabicView();
    }

    private String dotsView() {
        StringBuilder view = new StringBuilder();
        for (int i = 0; i < totalPages; i++) {
            if (i == page) {
                view.append(activeDot);
                continue;
            }
            view.append(inactiveDot);
        }
        return view.toString();
    }

    private String arabicView() {
        return String.format(arabicFormat, page + 1, totalPages);
    }

    /**
     * Returns the total number of pages.
     *
     * This value is a count, not a zero-based index.
     *
     * @return total page count
     */
    public int totalPages() {
        return totalPages;
    }

    /**
     * Returns the items per page.
     *
     * @return items per page
     */
    public int perPage() {
        return perPage;
    }

    /**
     * Sets the items per page.
     *
     * @param perPage items per page
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     * Sets the current page.
     *
     * Page indexes are zero-based.
     *
     * @param page zero-based page index
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Returns the current page.
     *
     * Page indexes are zero-based.
     *
     * @return zero-based page index
     */
    public int page() {
        return page;
    }

    /**
     * Sets the total number of pages.
     *
     * This value is a count, not a zero-based index.
     *
     * @param totalPages total page count
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Computes and sets total pages from item count.
     *
     * @param items total items
     * @return computed total pages
     */
    public int setTotalPagesFromItemsSize(int items) {
        if (items < 1) {
            return totalPages;
        }
        int n = items / perPage;
        if (items % perPage > 0) {
            n++;
        }
        this.totalPages = n;
        return n;
    }

    /**
     * Sets the active dot used in dot view.
     *
     * @param activeDot active dot string
     */
    public void setActiveDot(String activeDot) {
        this.activeDot = activeDot;
    }

    /**
     * Sets the inactive dot used in dot view.
     *
     * @param inactiveDot inactive dot string
     */
    public void setInactiveDot(String inactiveDot) {
        this.inactiveDot = inactiveDot;
    }
}
