package examples.compat.x.ansi;

import com.williamcallahan.tui4j.compat.x.ansi.Cut;
import com.williamcallahan.tui4j.compat.x.ansi.StringWidth;
import com.williamcallahan.tui4j.compat.x.ansi.Strip;
import com.williamcallahan.tui4j.compat.x.ansi.Truncate;

/**
 * Example program demonstrating ANSI-aware string truncation.
 * <p>
 * Demonstrates Truncate.truncate() with various tail strings and widths.
 *
 * @see <a href="https://github.com/charmbracelet/x/blob/main/ansi/truncate.go">x/ansi/truncate.go</a>
 */
public class TruncateExample {

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        String longText = "This is a very long string that needs to be truncated to fit within a specific width while preserving ANSI escape codes.";
        String ansiText = "\u001B[31mThis is red text\u001B[0m that contains \u001B[1mbold\u001B[0m and \u001B[4munderlined\u001B[0m segments.";

        System.out.println("=== Basic Truncation ===");
        System.out.println(Truncate.truncate(longText, 40, "..."));

        System.out.println("\n=== Truncation with Different Tails ===");
        System.out.println(Truncate.truncate(longText, 40, " [...]"));
        System.out.println(Truncate.truncate(longText, 40, "…"));

        System.out.println("\n=== ANSI-Aware Truncation ===");
        String truncatedAnsi = Truncate.truncate(ansiText, 50, "...");
        System.out.println("Original: " + ansiText);
        System.out.println("Truncated: " + truncatedAnsi);
        System.out.println("Width: " + StringWidth.stringWidth(truncatedAnsi));

        System.out.println("\n=== Truncate from Left ===");
        System.out.println(Truncate.truncateLeft(longText, 50, "..."));

        System.out.println("\n=== Cut by Range ===");
        String cutText = Cut.cut(ansiText, 10, 30);
        System.out.println("Cut [10,30): " + cutText);

        System.out.println("\n=== Strip ANSI Codes ===");
        String stripped = Strip.strip(ansiText);
        System.out.println("Original: " + ansiText);
        System.out.println("Stripped: " + stripped);
    }
}
