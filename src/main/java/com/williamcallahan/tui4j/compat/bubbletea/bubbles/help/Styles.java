package com.williamcallahan.tui4j.compat.bubbletea.bubbles.help;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;

/**
 * Port of Bubbles styles.
 * Bubble Tea: bubbletea/examples/help/main.go
 */
public class Styles {

    private Style ellipsis;

    // Styling for the short help
    private Style shortKey;
    private Style shortDesc;
    private Style shortSeparator;

    // Styling for the full help
    private Style fullKey;
    private Style fullDesc;
    private Style fullSeparator;

    public Styles() {
        Style keyStyle = Style.newStyle().foreground(new AdaptiveColor("#909090", "#626262"));
        Style descStyle = Style.newStyle().foreground(new AdaptiveColor("#B2B2B2", "#4A4A4A"));
        Style sepStyle = Style.newStyle().foreground(new AdaptiveColor("#DDDADA", "#3C3C3C"));

        this.shortKey = keyStyle.copy();
        this.shortDesc = descStyle.copy();
        this.shortSeparator = sepStyle.copy();
        this.ellipsis = sepStyle.copy();
        this.fullKey = keyStyle.copy();
        this.fullDesc = descStyle.copy();
        this.fullSeparator = sepStyle.copy();
    }

    public Style getShortSeparator() {
        return shortSeparator;
    }

    public Style getShortKey() {
        return shortKey;
    }

    public Style getShortDesc() {
        return shortDesc;
    }

    public Style getFullSeparator() {
        return fullSeparator;
    }

    public Style getFullKey() {
        return fullKey;
    }

    public Style getFullDesc() {
        return fullDesc;
    }

    public Style getEllipsis() {
        return ellipsis;
    }
}
