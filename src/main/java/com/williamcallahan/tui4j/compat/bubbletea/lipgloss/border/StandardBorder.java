package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border;

/**
 * Port of Lip Gloss standard border.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class StandardBorder {

    public static Border NoBorder = new Border("", "", "", "", "", "", "", "", "", "", "", "", "");
    public static Border NormalBorder = new Border("─", "─", "│", "│", "┌", "┐", "└", "┘", "├", "┤", "┼", "┬", "┴");
    public static Border RoundedBorder = new Border("─", "─", "│", "│", "╭", "╮", "╰", "╯", "├", "┤", "┼", "┬", "┴");
    public static Border BlockBorder = new Border("█", "█", "█", "█", "█", "█", "█", "█", "", "", "", "", "");
    public static Border OuterHalfBlockBorder = new Border("▀", "▄", "▌", "▐", "▛", "▜", "▙", "▟", "", "", "", "", "");
    public static Border InnerHalfBlockBorder = new Border("▄", "▀", "▐", "▌", "▗", "▖", "▝", "▘", "", "", "", "", "");
    public static Border ThickBorder = new Border("━", "━", "┃", "┃", "┏", "┓", "┗", "┛", "┣", "┫", "╋", "┳", "┻");
    public static Border DoubleBorder = new Border("═", "═", "║", "║", "╔", "╗", "╚", "╝", "╠", "╣", "╬", "╦", "╩");
    public static Border HiddenBorder = new Border(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
}
