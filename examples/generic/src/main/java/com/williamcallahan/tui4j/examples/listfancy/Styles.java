package com.williamcallahan.tui4j.examples.listfancy;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;

import java.util.function.Function;

/**
 * Example program for styles.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/Styles.java
 */
public class Styles {

    public static Style appStyle = Style.newStyle().padding(1, 2);

    public static Style titleStyle = Style.newStyle()
            .foreground(Color.color("#FFFDF5"))
            .background(Color.color("#25A065"))
            .padding(0, 1);

    public static Function<String[], String> statusMessageStyle = Style.newStyle()
            .foreground(new AdaptiveColor("#04B575", "#04B575"))::render;
}
