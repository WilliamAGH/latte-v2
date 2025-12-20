package com.williamcallahan.tui4j.examples.width;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

/**
 * Example program for width.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/width/WidthExample.java
 */
public class WidthExample implements Model {

    private final static Style limitedWidth = Style.newStyle().width(15);
    private final static Style red = Style.newStyle().foreground(Color.color("#ff0000"));
    private final static Style green = Style.newStyle().foreground(Color.color("#00ff00"));

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
        return limitedWidth.render("This is a long text and it %s be wrapped after 15 %s, we will see how it presents ...".formatted(
                red.render("should"),
                green.render("characters")
        ));
    }

    public static void main(String[] args) {
        new Program(new WidthExample()).run();
    }
}
