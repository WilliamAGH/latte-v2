package examples.compat.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.ListEnumerator;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;

/**
 * Example program demonstrating lipgloss tree with multiple vendors.
 * <p>
 * Shows a tree of lip gloss vendors with nested structure.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/tree/styles/main.go">lipgloss/examples/tree</a>
 */
public class TreeStylesExample {

    private static final Style PURPLE = Style.newStyle().foreground(Color.color("99")).marginRight(1);

    /**
     * Runs the example to render a tree with mixed enumerator styles.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Tree t = new Tree()
                .child("Glossier", "Claire's Boutique")
                .child(
                        Tree.withRoot("Nyx")
                                        .enumerator(ListEnumerator.roman())
                                        .child("Lip Gloss", "Foundation"),
                        "Mac", "Milk"
                )
                .enumeratorStyle(PURPLE);

        System.out.println(t);
    }
}
