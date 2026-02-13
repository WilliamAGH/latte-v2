package examples.compat.bubbletea;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.term.TerminalInfo;

/**
 * Demonstrates border rendering for quick visual verification.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/BorderTest.java
 */
public class BorderTest {

    /**
     * Runs the border rendering smoke test to visualize frame styling.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        TerminalInfo.provide(() -> new TerminalInfo(true, new NoColor()));

        System.out.println(
                Style.newStyle()
                        .width(10)
                        .padding(5)
                        .margin(3)
                        .border(StandardBorder.RoundedBorder)
                        .borderBackground(Color.color("#ff0000"))
                        .background(Color.color("#ff0000"))
                        .render("This is a test of a frame")
        );
    }
}
