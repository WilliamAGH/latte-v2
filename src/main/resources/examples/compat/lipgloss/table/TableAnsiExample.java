package examples.compat.lipgloss.table;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;

/**
 * Example program demonstrating lipgloss table with simple rows.
 * <p>
 * Shows a simple table with rendered string cells.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/table/ansi/main.go">lipgloss/examples/table</a>
 */
public class TableAnsiExample {

    /**
     * Dims a value to show ANSI color styling inside table cells.
     *
     * @param value text to style
     * @return styled text
     */
    private static String dim(String value) {
        return Style.newStyle().foreground(Color.color("240")).render(value);
    }

    /**
     * Runs the example to render a minimal ANSI-styled table.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Table t = Table.create()
                .row("Bubble Tea", dim("Milky"))
                .row("Milk Tea", dim("Also milky"))
                .row("Actual milk", dim("Milky as well"));

        System.out.println(t.render());
    }
}
