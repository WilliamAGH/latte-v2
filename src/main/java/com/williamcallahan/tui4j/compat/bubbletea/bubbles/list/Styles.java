package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;

/**
 * Port of Bubbles styles.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class Styles {

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

        defaultStyles.defaultFilterCharacterMatch = Style.newStyle().underline(true);

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

    public Style titleBar() {
        return titleBar;
    }

    public void setTitleBar(Style titleBar) {
        this.titleBar = titleBar;
    }

    public Style title() {
        return title;
    }

    public void setTitle(Style title) {
        this.title = title;
    }

    public Style spinner() {
        return spinner;
    }

    public void setSpinner(Style spinner) {
        this.spinner = spinner;
    }

    public Style filterPrompt() {
        return filterPrompt;
    }

    public void setFilterPrompt(Style filterPrompt) {
        this.filterPrompt = filterPrompt;
    }

    public Style filterCursor() {
        return filterCursor;
    }

    public void setFilterCursor(Style filterCursor) {
        this.filterCursor = filterCursor;
    }

    public Style defaultFilterCharacterMatch() {
        return defaultFilterCharacterMatch;
    }

    public void setDefaultFilterCharacterMatch(Style defaultFilterCharacterMatch) {
        this.defaultFilterCharacterMatch = defaultFilterCharacterMatch;
    }

    public Style statusBar() {
        return statusBar;
    }

    public void setStatusBar(Style statusBar) {
        this.statusBar = statusBar;
    }

    public Style statusEmpty() {
        return statusEmpty;
    }

    public void setStatusEmpty(Style statusEmpty) {
        this.statusEmpty = statusEmpty;
    }

    public Style statusBarActiveFilter() {
        return statusBarActiveFilter;
    }

    public void setStatusBarActiveFilter(Style statusBarActiveFilter) {
        this.statusBarActiveFilter = statusBarActiveFilter;
    }

    public Style statusBarFilterCount() {
        return statusBarFilterCount;
    }

    public void setStatusBarFilterCount(Style statusBarFilterCount) {
        this.statusBarFilterCount = statusBarFilterCount;
    }

    public Style noItems() {
        return noItems;
    }

    public void setNoItems(Style noItems) {
        this.noItems = noItems;
    }

    public Style paginationStyle() {
        return paginationStyle;
    }

    public void setPaginationStyle(Style paginationStyle) {
        this.paginationStyle = paginationStyle;
    }

    public Style helpStyle() {
        return helpStyle;
    }

    public void setHelpStyle(Style helpStyle) {
        this.helpStyle = helpStyle;
    }

    public Style activePaginationDot() {
        return activePaginationDot;
    }

    public void setActivePaginationDot(Style activePaginationDot) {
        this.activePaginationDot = activePaginationDot;
    }

    public Style inactivePaginationDot() {
        return inactivePaginationDot;
    }

    public void setInactivePaginationDot(Style inactivePaginationDot) {
        this.inactivePaginationDot = inactivePaginationDot;
    }

    public Style arabicPagination() {
        return arabicPagination;
    }

    public void setArabicPagination(Style arabicPagination) {
        this.arabicPagination = arabicPagination;
    }

    public Style dividerDot() {
        return dividerDot;
    }

    public void setDividerDot(Style dividerDot) {
        this.dividerDot = dividerDot;
    }
}
