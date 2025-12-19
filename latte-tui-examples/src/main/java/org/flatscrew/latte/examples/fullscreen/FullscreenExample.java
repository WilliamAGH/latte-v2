package org.flatscrew.latte.examples.fullscreen;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.QuitMessage;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.flatscrew.latte.Command.tick;

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
