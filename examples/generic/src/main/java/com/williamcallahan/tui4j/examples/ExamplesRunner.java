package com.williamcallahan.tui4j.examples;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.examples.altscreentoggle.AltScreenToggleExample;
import com.williamcallahan.tui4j.examples.counter.CounterExample;
import com.williamcallahan.tui4j.examples.demo.Demo;
import com.williamcallahan.tui4j.examples.fullscreen.FullscreenExample;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

import java.util.List;

/**
 * Example program for examples runner.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/ExamplesRunner.java
 */
public class ExamplesRunner implements Model {

    private final List<Model> exampleModels;
    private int cursor;

    public ExamplesRunner(List<Model> exampleModels) {
        this.exampleModels = exampleModels;
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> new UpdateResult<>(this.moveUp(), null);
                case "j", "J", "down" -> new UpdateResult<>(this.moveDown(), null);
                case "enter" -> new UpdateResult<>(this.makeChoice(), null);
                case "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }
        return new UpdateResult<>(this, null);    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Pick an example:\n");
        buffer.append("~~~~~~~~~~~~~~~~\n\n");

        for (int index = 0; index < exampleModels.size(); index++) {
            if (cursor == index) {
                buffer.append("(•) ");
            } else {
                buffer.append("( ) ");

            }
            buffer.append(exampleModels.get(index).getClass().getSimpleName()).append("\n");
        }
        buffer.append("\n(press q to quit)");
        return buffer.toString();
    }

    private Model makeChoice() {
        for (int index = 0; index < exampleModels.size(); index++) {
            if (index == cursor) {
                return exampleModels.get(index);
            }
        }
        return this;
    }

    private Model moveUp() {
        if (cursor - 1 <= 0 ) {
            cursor = 0;
            return this;
        }
        cursor--;
        return this;
    }

    private Model moveDown() {
        if (cursor + 1 >= exampleModels.size()) {
            cursor = 0;
            return this;
        }
        cursor++;
        return this;
    }

    public static void main(String[] args) {
        new Program(new ExamplesRunner(List.of(
                new CounterExample(),
                new Demo(),
                new AltScreenToggleExample(),
                new FullscreenExample(5)
        ))).run();
    }
}