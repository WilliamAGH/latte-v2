package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor;

/**
 * Styles used by the {@link List} component.
 * <p>
 * Upstream: bubbles/list/list.go
 */
public class Styles {

    /**
     * Creates an empty styles container.
     */
    public Styles() {}

    /**
     * Returns a default styles instance aligned with upstream defaults.
     *
     * @return default list styles
     */
    public static Styles defaultStyles() {
        TerminalColor verySubduedColor = new AdaptiveColor("#DDDADA", "#3C3C3C");
        TerminalColor subduedColor = new AdaptiveColor("#9B9B9B", "#5C5C5C");

        Styles defaultStyles = new Styles();

        defaultStyles.titleBar = Style.newStyle().padding(0, 0, 1, 2);
        defaultStyles.title = Style.newStyle()
            .background(Color.color("62"))
            .foreground(Color.color("230"))
            .padding(0, 1);

        defaultStyles.spinner = Style.newStyle()
            .foreground(new AdaptiveColor("#8E8E8E", "#747373"));

        defaultStyles.filterPrompt = Style.newStyle()
            .foreground(new AdaptiveColor("#04B575", "#ECFD65"));

        defaultStyles.filterCursor = Style.newStyle()
            .foreground(new AdaptiveColor("#EE6FF8", "#EE6FF8"));

        defaultStyles.defaultFilterCharacterMatch = Style.newStyle()
            .underline(true);

        defaultStyles.statusBar = Style.newStyle()
            .foreground(new AdaptiveColor("#A49FA5", "#777777"))
            .padding(0, 0, 1, 2);

        defaultStyles.statusEmpty = Style.newStyle()
            .foreground(subduedColor);

        defaultStyles.statusBarActiveFilter = Style.newStyle()
            .foreground(new AdaptiveColor("#1a1a1a", "#dddddd"));

        defaultStyles.statusBarFilterCount = Style.newStyle()
            .foreground(verySubduedColor);

        defaultStyles.noItems = Style.newStyle()
            .foreground(new AdaptiveColor("#909090", "#626262"));

        defaultStyles.arabicPagination = Style.newStyle()
            .foreground(subduedColor);

        defaultStyles.paginationStyle = Style.newStyle()
            .paddingLeft(2);

        defaultStyles.helpStyle = Style.newStyle()
            .padding(1, 0, 0, 2);

        defaultStyles.activePaginationDot = Style.newStyle()
            .foreground(new AdaptiveColor("#847A85", "#979797"))
            .setString(BULLET);

        defaultStyles.inactivePaginationDot = Style.newStyle()
            .foreground(verySubduedColor)
            .setString(BULLET);

        defaultStyles.dividerDot = Style.newStyle()
            .foreground(verySubduedColor)
            .setString(" " + BULLET + " ");

        return defaultStyles;
    }

    private static final String BULLET = "•";

    private Style titleBar;
    private Style title;
    private Style spinner;
    private Style filterPrompt;
    private Style filterCursor;
    private Style defaultFilterCharacterMatch;
    private Style statusBar;
    private Style statusEmpty;
    private Style statusBarActiveFilter;
    private Style statusBarFilterCount;
    private Style noItems;
    private Style paginationStyle;
    private Style helpStyle;
    private Style activePaginationDot;
    private Style inactivePaginationDot;
    private Style arabicPagination;
    private Style dividerDot;

    /** Returns the title bar style.
     * @return title bar style
     */
    public Style titleBar() {
        return titleBar;
    }

    /** Sets the title bar style.
     * @param titleBar title bar style
     */
    public void setTitleBar(Style titleBar) {
        this.titleBar = titleBar;
    }

    /** Returns the title style.
     * @return title style
     */
    public Style title() {
        return title;
    }

    /** Sets the title style.
     * @param title title style
     */
    public void setTitle(Style title) {
        this.title = title;
    }

    /** Returns the spinner style.
     * @return spinner style
     */
    public Style spinner() {
        return spinner;
    }

    /** Sets the spinner style.
     * @param spinner spinner style
     */
    public void setSpinner(Style spinner) {
        this.spinner = spinner;
    }

    /** Returns the filter prompt style.
     * @return filter prompt style
     */
    public Style filterPrompt() {
        return filterPrompt;
    }

    /** Sets the filter prompt style.
     * @param filterPrompt filter prompt style
     */
    public void setFilterPrompt(Style filterPrompt) {
        this.filterPrompt = filterPrompt;
    }

    /** Returns the filter cursor style.
     * @return filter cursor style
     */
    public Style filterCursor() {
        return filterCursor;
    }

    /** Sets the filter cursor style.
     * @param filterCursor filter cursor style
     */
    public void setFilterCursor(Style filterCursor) {
        this.filterCursor = filterCursor;
    }

    /** Returns the match style for the filter.
     * @return style for matching characters
     */
    public Style defaultFilterCharacterMatch() {
        return defaultFilterCharacterMatch;
    }

    /** Sets the match style for the filter.
     * @param defaultFilterCharacterMatch style for matching characters
     */
    public void setDefaultFilterCharacterMatch(Style defaultFilterCharacterMatch) {
        this.defaultFilterCharacterMatch = defaultFilterCharacterMatch;
    }

    /** Returns the status bar style.
     * @return status bar style
     */
    public Style statusBar() {
        return statusBar;
    }

    /** Sets the status bar style.
     * @param statusBar status bar style
     */
    public void setStatusBar(Style statusBar) {
        this.statusBar = statusBar;
    }

    /** Returns the empty-state status style.
     * @return empty-state style
     */
    public Style statusEmpty() {
        return statusEmpty;
    }

    /** Sets the empty-state status style.
     * @param statusEmpty empty-state style
     */
    public void setStatusEmpty(Style statusEmpty) {
        this.statusEmpty = statusEmpty;
    }

    /** Returns the active-filter status style.
     * @return active-filter status style
     */
    public Style statusBarActiveFilter() {
        return statusBarActiveFilter;
    }

    /** Sets the active-filter status style.
     * @param statusBarActiveFilter active-filter status style
     */
    public void setStatusBarActiveFilter(Style statusBarActiveFilter) {
        this.statusBarActiveFilter = statusBarActiveFilter;
    }

    /** Returns the filter-count status style.
     * @return filter-count status style
     */
    public Style statusBarFilterCount() {
        return statusBarFilterCount;
    }

    /** Sets the filter-count status style.
     * @param statusBarFilterCount filter-count status style
     */
    public void setStatusBarFilterCount(Style statusBarFilterCount) {
        this.statusBarFilterCount = statusBarFilterCount;
    }

    /** Returns the empty-list style.
     * @return empty list style
     */
    public Style noItems() {
        return noItems;
    }

    /** Sets the empty-list style.
     * @param noItems empty list style
     */
    public void setNoItems(Style noItems) {
        this.noItems = noItems;
    }

    /** Returns the pagination style.
     * @return pagination section style
     */
    public Style paginationStyle() {
        return paginationStyle;
    }

    /** Sets the pagination style.
     * @param paginationStyle pagination section style
     */
    public void setPaginationStyle(Style paginationStyle) {
        this.paginationStyle = paginationStyle;
    }

    /** Returns the help style.
     * @return help section style
     */
    public Style helpStyle() {
        return helpStyle;
    }

    /** Sets the help style.
     * @param helpStyle help section style
     */
    public void setHelpStyle(Style helpStyle) {
        this.helpStyle = helpStyle;
    }

    /** Returns the active-dot style.
     * @return active pagination dot style
     */
    public Style activePaginationDot() {
        return activePaginationDot;
    }

    /** Sets the active-dot style.
     * @param activePaginationDot active pagination dot style
     */
    public void setActivePaginationDot(Style activePaginationDot) {
        this.activePaginationDot = activePaginationDot;
    }

    /** Returns the inactive-dot style.
     * @return inactive pagination dot style
     */
    public Style inactivePaginationDot() {
        return inactivePaginationDot;
    }

    /** Sets the inactive-dot style.
     * @param inactivePaginationDot inactive pagination dot style
     */
    public void setInactivePaginationDot(Style inactivePaginationDot) {
        this.inactivePaginationDot = inactivePaginationDot;
    }

    /** Returns the Arabic pagination style.
     * @return style for Arabic pagination mode
     */
    public Style arabicPagination() {
        return arabicPagination;
    }

    /** Sets the Arabic pagination style.
     * @param arabicPagination style for Arabic pagination mode
     */
    public void setArabicPagination(Style arabicPagination) {
        this.arabicPagination = arabicPagination;
    }

    /** Returns the divider-dot style.
     * @return divider dot style
     */
    public Style dividerDot() {
        return dividerDot;
    }

    /** Sets the divider-dot style.
     * @param dividerDot divider dot style
     */
    public void setDividerDot(Style dividerDot) {
        this.dividerDot = dividerDot;
    }
}
