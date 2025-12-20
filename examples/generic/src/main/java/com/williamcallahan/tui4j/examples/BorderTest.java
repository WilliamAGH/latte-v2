package com.williamcallahan.tui4j.examples;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.term.TerminalInfo;

/**
 * Tests border.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/BorderTest.java
 */
public class BorderTest {

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
