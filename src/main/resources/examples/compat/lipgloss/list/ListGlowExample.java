package examples.compat.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.List;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;

/**
 * Example program demonstrating lipgloss list with document entries and timestamps.
 * <p>
 * Shows a file list with timestamps using faint style for time strings.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/list/glow/main.go">lipgloss/examples/list</a>
 */
public class ListGlowExample {

    private static final int SELECTED = 1;

    /**
     * Holds example data to demonstrate list item rendering.
     */
    private record Document(String name, String time) {
        /**
         * Formats the document with a faint timestamp to highlight list styling.
         *
         * @return formatted list item text
         */
        @Override
        public String toString() {
            return name + "\n" + Style.newStyle().faint(true).render(time);
        }
    }

    private static final Document[] DOCS = {
            new Document("README.md", "2 minutes ago"),
            new Document("Example.md", "1 hour ago"),
            new Document("secrets.md", "1 week ago")
    };

    /**
     * Runs the example to render a list with a highlighted selection.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style baseStyle = Style.newStyle().marginBottom(1).marginLeft(1);
        String dimColor = "250";
        String highlightColor = "#EE6FF8";

        StyleFunction itemStyleFunc = (items, i) -> {
            if (SELECTED == i) {
                return baseStyle.foreground(Color.color(highlightColor));
            }
            return baseStyle.foreground(Color.color(dimColor));
        };

        StyleFunction enumStyleFunc = (items, i) -> {
            if (SELECTED == i) {
                return Style.newStyle().foreground(Color.color(highlightColor));
            }
            return Style.newStyle().foreground(Color.color(dimColor));
        };

        List l = new List()
                .enumerator((children, index) -> SELECTED == index ? "│\n│" : " ")
                .enumeratorStyleFunc(enumStyleFunc)
                .itemStyleFunc(itemStyleFunc);

        for (Document doc : DOCS) {
            l.item(doc);
        }

        System.out.println();
        System.out.println(l);
    }
}
