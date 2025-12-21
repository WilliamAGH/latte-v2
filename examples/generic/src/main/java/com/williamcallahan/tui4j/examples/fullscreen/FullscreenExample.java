package com.williamcallahan.tui4j.examples.fullscreen;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.tick;

/**
 * Example program for tick message.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/fullscreen/FullscreenExample.java
 */
record TickMessage(LocalDateTime time) implements Message {}

public class FullscreenExample implements Model {

    private int seconds;

    public FullscreenExample(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public Command init() {
        return tick(Duration.ofSeconds(1), TickMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            seconds--;
            if (seconds <= 0) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this, tick(Duration.ofSeconds(1), TickMessage::new));
        }
        return UpdateResult.from(this, null);
    }

    @Override
    public String view() {
        return "\n\n     Hi. This program will exit in %d seconds...".formatted(seconds);
    }

    public static void main(String[] args) {
        Model model = new FullscreenExample(5);
        new Program(model)
                .withAltScreen()
                .run();
    }
}
