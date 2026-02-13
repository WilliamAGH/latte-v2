package examples.compat.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.lipgloss.tree.TreeEnumerator;

/**
 * Example program demonstrating lipgloss tree with expandable/collapsible directories.
 * <p>
 * Shows a directory tree with toggles for expanding/collapsing branches.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/tree/toggle/main.go">lipgloss/examples/tree</a>
 */
public class TreeToggleExample {

    /**
     * Bundles the styles used to render the toggleable tree.
     */
    private static class Styles {
        Style base;
        Style block;
        Style enumerator;
        Style dir;
        Style toggle;
        Style file;

        /**
         * Builds the demo style palette to keep rendering consistent.
         */
        Styles() {
            this.base = Style.newStyle().background(Color.color("57")).foreground(Color.color("225"));
            this.block = base.padding(1, 3).margin(1, 3).width(40);
            this.enumerator = base.foreground(Color.color("212")).paddingRight(1);
            this.dir = base.inline(true);
            this.toggle = base.foreground(Color.color("207")).paddingRight(1);
            this.file = base;
        }
    }

    /**
     * Represents a directory entry with open/closed state for toggled rendering.
     */
    private static class Dir {
        String name;
        boolean open;
        Styles styles;

        /**
         * Creates a directory entry with its current toggle state.
         *
         * @param name directory name to render
         * @param open whether the directory is expanded
         * @param styles palette used for rendering
         */
        Dir(String name, boolean open, Styles styles) {
            this.name = name;
            this.open = open;
            this.styles = styles;
        }

        /**
         * Formats the directory label with a toggle indicator.
         *
         * @return rendered directory label
         */
        @Override
        public String toString() {
            String t = styles.toggle.render(name);
            String n = styles.dir.render(name);
            return open ? t + "▼" + n : t + "▶" + n;
        }
    }

    /**
     * Represents a file entry rendered using the file style.
     */
    private static class File {
        String name;
        Styles styles;

        /**
         * Creates a file entry with its rendering styles.
         *
         * @param name file name to render
         * @param styles palette used for rendering
         */
        File(String name, Styles styles) {
            this.name = name;
            this.styles = styles;
        }

        /**
         * Formats the file label for display.
         *
         * @return rendered file label
         */
        @Override
        public String toString() {
            return styles.file.render(name);
        }
    }

    /**
     * Runs the example to render a toggleable directory tree.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Styles s = new Styles();

        Tree t = Tree.withRoot(new Dir("~/charm", true, s))
                .enumerator(new TreeEnumerator.RounderEnumerator())
                .enumeratorStyle(s.enumerator)
                .child(
                        new Dir("ayman", false, s),
                        Tree.withRoot(new Dir("bash", true, s))
                                .child(
                                        Tree.withRoot(new Dir("tools", true, s))
                                                .child(new File("zsh", s), new File("doom-emacs", s))
                                ),
                        Tree.withRoot(new Dir("carlos", true, s))
                                .child(
                                        Tree.withRoot(new Dir("emotes", true, s))
                                                .child(new File("chefkiss.png", s), new File("kekw.png", s))
                                ),
                        new Dir("maas", false, s)
                );

        System.out.println(s.block.render(t.toString()));
    }
}
