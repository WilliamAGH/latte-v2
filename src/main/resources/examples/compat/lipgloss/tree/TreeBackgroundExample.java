package examples.compat.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Example program demonstrating lipgloss tree with header styling.
 * <p>
 * Shows a table of contents tree with styled headers and items.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/tree/background/main.go">lipgloss/examples/tree</a>
 */
public class TreeBackgroundExample {

    private static final Style ENUMERATOR_STYLE = Style.newStyle()
            .background(Color.color("0"))
            .padding(0, 1);

    private static final Style HEADER_ITEM_STYLE = Style.newStyle()
            .background(Color.color("#ee6ff8"))
            .foreground(Color.color("#ecfe65"))
            .bold(true)
            .padding(0, 1);

    private static final Style ITEM_STYLE = HEADER_ITEM_STYLE.background(Color.color("0"));

    /**
     * Runs the example to render a tree with styled headers and enumerators.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Tree t = Tree.withRoot("# Table of Contents")
                .rootStyle(HEADER_ITEM_STYLE)
                .itemStyle(ITEM_STYLE)
                .enumeratorStyle(ENUMERATOR_STYLE)
                .child(
                        Tree.withRoot("## Chapter 1")
                                .child("Chapter 1.1", "Chapter 1.2"),
                        Tree.withRoot("## Chapter 2")
                                .child("Chapter 2.1", "Chapter 2.2")
                );

        System.out.println(t);
    }
}
