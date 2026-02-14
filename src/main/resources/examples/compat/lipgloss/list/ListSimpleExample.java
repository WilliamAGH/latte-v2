package examples.compat.lipgloss.list;

import com.williamcallahan.tui4j.compat.lipgloss.List;
import com.williamcallahan.tui4j.compat.lipgloss.ListEnumerator;

/**
 * Example program demonstrating lipgloss list component.
 * <p>
 * Shows a simple nested list with Roman numeral enumerator for the nested portion.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/list/simple/main.go">lipgloss/examples/list</a>
 */
public class ListSimpleExample {

    /**
     * Runs the example to render a nested list with Roman numeral enumeration.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        List l = new List(
                "A",
                "B",
                "C",
                new List(
                        "D",
                        "E",
                        "F"
                ).enumerator(ListEnumerator.roman()),
                "G"
        );

        System.out.println(l);
    }
}
