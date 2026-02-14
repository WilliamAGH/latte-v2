package examples.compat.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;

/**
 * Example program demonstrating lipgloss tree component.
 * <p>
 * Shows a simple operating system hierarchy as a tree.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/tree/simple/main.go">lipgloss/examples/tree</a>
 */
public class TreeSimpleExample {

    /**
     * Runs the example to render a simple tree hierarchy.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Tree t = Tree.withRoot(".")
                .child(
                        Tree.withRoot("macOS")
                                .child(
                                        "Linux",
                                        "NixOS",
                                        "Arch Linux (btw)",
                                        "Void Linux"
                                ),
                        Tree.withRoot("BSD")
                                .child(
                                        "FreeBSD",
                                        "OpenBSD"
                                )
                );

        System.out.println(t);
    }
}
