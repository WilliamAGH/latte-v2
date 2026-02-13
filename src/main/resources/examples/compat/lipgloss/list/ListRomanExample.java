package examples.compat.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.List;
import com.williamcallahan.tui4j.compat.lipgloss.ListEnumerator;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Example program demonstrating lipgloss list with Roman numeral enumerator.
 * <p>
 * Shows a simple list with Roman numerals for each item.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/list/roman/main.go">lipgloss/examples/list</a>
 */
public class ListRomanExample {

    /**
     * Runs the example to render a list using Roman numeral enumeration.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style enumeratorStyle = Style.newStyle().foreground(Color.color("99")).marginRight(1);
        Style itemStyle = Style.newStyle().foreground(Color.color("255")).marginRight(1);

        List l = new List(
                "Glossier",
                "Claire's Boutique",
                "Nyx",
                "Mac",
                "Milk"
        ).enumerator(ListEnumerator.roman())
         .enumeratorStyle(enumeratorStyle)
         .itemStyleFunc((children, i) -> itemStyle);

        System.out.println(l);
    }
}
