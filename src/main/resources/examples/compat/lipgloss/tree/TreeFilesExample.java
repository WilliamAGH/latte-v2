package examples.compat.lipgloss.tree;

import com.williamcallahan.tui4j.compat.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Example program demonstrating lipgloss tree from file system.
 * <p>
 * Creates a tree representation of the current directory, recursively scanning
 * directories and skipping dotfiles.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/tree/files/main.go">lipgloss/examples/tree</a>
 */
public class TreeFilesExample {

    /**
     * Walks the filesystem to populate the tree with non-hidden entries.
     *
     * @param root tree to populate
     * @param path starting path
     * @throws IOException when the directory cannot be read
     */
    private static void addBranches(Tree root, String path) throws IOException {
        File dir = new File(path);

        File[] items = dir.listFiles();
        if (items == null) {
            return;
        }

        for (File item : items) {
            if (item.isDirectory()) {
                String name = item.getName();

                if (name.startsWith(".")) {
                    continue;
                }

                Tree branch = Tree.withRoot(name);
                root.child(branch);

                String branchPath = Paths.get(path, name).toString();
                addBranches(branch, branchPath);
            } else {
                String name = item.getName();

                if (name.startsWith(".")) {
                    continue;
                }

                root.child(name);
            }
        }
    }

    /**
     * Runs the example to render the current directory as a styled tree.
     *
     * @param args ignored
     * @throws IOException when filesystem traversal fails
     */
    public static void main(String[] args) throws IOException {
        Style enumeratorStyle = Style.newStyle().foreground(Color.color("240")).paddingRight(1);
        Style rootStyle = Style.newStyle().foreground(Color.color("99")).bold(true).paddingRight(1);
        Style itemStyle = Style.newStyle().foreground(Color.color("99"));

        String pwd = Paths.get(".").toAbsolutePath().normalize().toString();

        Tree t = Tree.withRoot(pwd)
                .enumeratorStyle(enumeratorStyle)
                .rootStyle(rootStyle)
                .itemStyle(itemStyle);

        addBranches(t, ".");

        System.out.println(t);
    }
}
