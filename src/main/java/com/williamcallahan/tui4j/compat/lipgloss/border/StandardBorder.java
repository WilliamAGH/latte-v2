package com.williamcallahan.tui4j.compat.lipgloss.border;

/**
 * Port of Lip Gloss standard border.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class StandardBorder {

    /** Border with no visible lines. */
    public static final Border NoBorder = new Border("", "", "", "", "", "", "", "", "", "", "", "", "");
    /** Single-line box border. */
    public static final Border NormalBorder = new Border("─", "─", "│", "│", "┌", "┐", "└", "┘", "├", "┤", "┼", "┬", "┴");
    /** Rounded corner border. */
    public static final Border RoundedBorder = new Border("─", "─", "│", "│", "╭", "╮", "╰", "╯", "├", "┤", "┼", "┬", "┴");
    /** Solid block border. */
    public static final Border BlockBorder = new Border("█", "█", "█", "█", "█", "█", "█", "█", "█", "█", "█", "█", "█");
    /** Outer half-block border. */
    public static final Border OuterHalfBlockBorder = new Border("▀", "▄", "▌", "▐", "▛", "▜", "▙", "▟", "", "", "", "", "");
    /** Inner half-block border. */
    public static final Border InnerHalfBlockBorder = new Border("▄", "▀", "▐", "▌", "▗", "▖", "▝", "▘", "", "", "", "", "");
    /** Thick line border. */
    public static final Border ThickBorder = new Border("━", "━", "┃", "┃", "┏", "┓", "┗", "┛", "┣", "┫", "╋", "┳", "┻");
    /** Double line border. */
    public static final Border DoubleBorder = new Border("═", "═", "║", "║", "╔", "╗", "╚", "╝", "╠", "╣", "╬", "╦", "╩");
    /** Hidden border (spaces). */
    public static final Border HiddenBorder = new Border(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
    /** Markdown table border. */
    public static final Border MarkdownBorder = new Border("-", "-", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|", "|");
    /** ASCII line border. */
    public static final Border ASCIIBorder = new Border("-", "-", "|", "|", "+", "+", "+", "+", "+", "+", "+", "+", "+");

    /**
     * Creates a standard border container.
     */
    public StandardBorder() {
        // Public constructor needed by deprecated subclass shim in bubbletea.lipgloss.border
    }
}
