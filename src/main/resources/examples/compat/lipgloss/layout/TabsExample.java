package examples.compat.lipgloss.layout;

import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Size;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Example program demonstrating lipgloss tabs with active/inactive states.
 * <p>
 * Shows a tabbed interface where different tabs can be selected.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/layout/main.go">lipgloss/examples/layout</a>
 * <p>
 * Lipgloss: examples/layout/main.go.
 */
public class TabsExample {

    private static final String HIGHLIGHT = "#874BFD";

    private static final Border ACTIVE_TAB_BORDER = new Border(
            "─", " ", "│", "│",
            "╭", "╮", "┘", "└",
            "", "", "", "", ""
    );

    private static final Border TAB_BORDER = new Border(
            "─", "─", "│", "│",
            "╭", "╮", "┴", "┴",
            "", "", "", "", ""
    );

    private static final int WIDTH = 96;

    /**
     * Runs the example to render a tabbed layout with active and inactive styles.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style tab = Style.newStyle()
                .border(TAB_BORDER, true)
                .borderForeground(Color.color(HIGHLIGHT))
                .padding(0, 1);

        Style activeTab = tab.border(ACTIVE_TAB_BORDER, true);

        Style tabGap = tab.borderTop(false).borderLeft(false).borderRight(false);

        String activeRow = Join.joinHorizontal(
                Position.Bottom,
                activeTab.render("Lip Gloss"),
                tab.render("Blush"),
                tab.render("Eye Shadow"),
                tab.render("Mascara"),
                tab.render("Foundation")
        );

        String gap = tabGap.render(" ".repeat(Math.max(0, WIDTH - Size.width(activeRow) - 2)));

        System.out.println(
                Join.joinHorizontal(Position.Bottom, activeRow, gap) + "\n\n"
        );
    }
}
