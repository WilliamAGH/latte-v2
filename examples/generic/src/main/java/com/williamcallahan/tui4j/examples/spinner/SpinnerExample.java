package com.williamcallahan.tui4j.examples.spinner;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner.SpinnerType;

/**
 * Example program for spinner.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/spinner/SpinnerExample.java
 */
public class SpinnerExample implements Model {

    private Model spinner;

    public SpinnerExample() {
        this.spinner = new Spinner(SpinnerType.DOT).setStyle(Style.newStyle().foreground(Color.color("205")));
    }

    @Override
    public Command init() {
        return spinner.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "q", "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }

        UpdateResult<? extends Model> updateResult = spinner.update(msg);
        spinner = updateResult.model();
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        return "\n\n   %s Loading forever...press q to quit\n\n".formatted(spinner.view());
    }

    public static void main(String[] args) {
        new Program(new SpinnerExample()).run();
    }
}
