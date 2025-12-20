package com.williamcallahan.tui4j.examples.terminfo;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

/**
 * Example program for term info.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/terminfo/TermInfoExample.java
 */
public class TermInfoExample implements Model {

    private final Style testStyle = Style.newStyle().foreground(
            new AdaptiveColor("#FF0000", "#00FF00")
    );

    @Override
    public Command init() {
        return QuitMessage::new;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return testStyle.render("Test test test");
    }

    public static void main(String[] args) {
        new Program(new TermInfoExample()).run();
    }
}
