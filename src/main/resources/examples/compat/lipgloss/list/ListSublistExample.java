package examples.compat.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.List;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;

/**
 * Example program demonstrating lipgloss list with nested structure.
 * <p>
 * Shows nested lists with custom styling for different sections.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/list/sublist/main.go">lipgloss/examples/list</a>
 */
public class ListSublistExample {

    private static final Style PURPLE = Style.newStyle().foreground(Color.color("99")).marginRight(1);
    private static final Style PINK = Style.newStyle().foreground(Color.color("212")).marginRight(1);
    private static final Style BASE = Style.newStyle().marginBottom(1).marginLeft(1);
    private static final String SPECIAL = "#43BF6D";

    /**
     * Runs the example to render nested lists with section-specific styling.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        StyleFunction checklistStyleFunc = (children, i) -> {
            Style st = BASE.copy();
            if (i == 1 || i == 2 || i == 4) {
                st = st.faint(true).foreground(new AdaptiveColor("#969B86", "#696969"));
            }
            return st;
        };

        StyleFunction enumStyleFunc = (children, i) -> {
            if (i == 1 || i == 2 || i == 4) {
                return Style.newStyle().foreground(Color.color(SPECIAL)).paddingRight(1);
            }
            return Style.newStyle().paddingRight(1);
        };

        String[][] items = {
                {"✓ Citrus Fruits to Try", "", "", ""},
                {"• Grapefruit", "", "", ""},
                {"• Yuzu", "", "", ""},
                {"• Citron", "", "", ""},
                {"• Kumquat", "", "", ""},
                {"• Pomelo", "", "", ""},
                {"✓ Actual Lip Gloss Vendors", "", "", ""},
                {"• Glossier", "", "", ""},
                {"• Claire's Boutique", "", "", ""},
                {"• Nyx", "", "", ""},
                {"• Mac", "", "", ""},
                {"• Milk", "", "", ""},
        };

        List citrusList = new List(
                new List((Object[]) items[0]),
                new List((Object[]) items[1])
        ).enumeratorStyleFunc(enumStyleFunc)
         .itemStyleFunc(checklistStyleFunc)
         .enumeratorStyle(PURPLE);

        List vendorsList = new List(
                new List((Object[]) items[8]),
                new List((Object[]) items[9])
        ).enumeratorStyleFunc(enumStyleFunc)
         .itemStyleFunc(checklistStyleFunc)
         .enumeratorStyle(PINK);

        List lipGlossList = new List()
                .enumeratorStyleFunc(enumStyleFunc)
                 .item("Lip Gloss")
                 .item("Lip Gloss")
                 .item("Lip Gloss")
                 .item("Lip Gloss");

        List mainList = new List()
                .enumeratorStyle(PURPLE)
                .item("Lip Gloss")
                .item("Blush")
                .item("Eye Shadow")
                .item("Mascara")
                .item("Foundation")
                .item(citrusList)
                .item(vendorsList)
                .item(lipGlossList)
                .item("List")
                .item("xoxo, Charm_™");

        System.out.println(mainList);
    }
}
