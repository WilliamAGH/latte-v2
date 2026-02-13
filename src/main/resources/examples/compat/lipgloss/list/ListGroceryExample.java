package examples.compat.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.List;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.tree.StyleFunction;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator;

/**
 * Example program demonstrating lipgloss list with custom enumerator and item styles.
 * <p>
 * Shows a grocery list with checkmarks for purchased items and strikethrough
 * styling for completed items.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/list/grocery/main.go">lipgloss/examples/list</a>
 */
public class ListGroceryExample {

    private static final String[] PURCHASED = {
            "Bananas", "Barley", "Cashews", "Coconut Milk", "Dill", "Fish Cake", "Leeks", "Papaya"
    };

    /**
     * Runs the example to render a grocery list with custom markers and styles.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style dimEnumStyle = Style.newStyle().foreground(Color.color("240")).marginRight(1);
        Style highlightedEnumStyle = Style.newStyle().foreground(Color.color("10")).marginRight(1);
        Style itemStyle = Style.newStyle().foreground(Color.color("255"));

        TreeEnumerator groceryEnumerator = (children, i) -> {
            String item = children.at(i).value();
            for (String purchased : PURCHASED) {
                if (purchased.equals(item)) {
                    return "✓";
                }
            }
            return "•";
        };

        StyleFunction enumStyleFunc = (children, i) -> {
            String item = children.at(i).value();
            for (String purchased : PURCHASED) {
                if (purchased.equals(item)) {
                    return highlightedEnumStyle;
                }
            }
            return dimEnumStyle;
        };

        StyleFunction itemStyleFunc = (children, i) -> {
            String item = children.at(i).value();
            for (String purchased : PURCHASED) {
                if (purchased.equals(item)) {
                    return itemStyle.faint(true);
                }
            }
            return itemStyle;
        };

        List l = new List(
                "Artichoke",
                "Baking Flour", "Bananas", "Barley", "Bean Sprouts",
                "Cashew Apple", "Cashews", "Coconut Milk", "Curry Paste", "Currywurst",
                "Dill", "Dragonfruit", "Dried Shrimp",
                "Eggs", "Fish Cake", "Furikake",
                "Jicama", "Kohlrabi", "Leeks", "Lentils", "Licorice Root"
        ).enumerator(groceryEnumerator)
         .enumeratorStyleFunc(enumStyleFunc)
         .itemStyleFunc(itemStyleFunc);

        System.out.println(l);
    }
}
