package com.williamcallahan.tui4j.examples.counter;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

/**
 * Example program for counter.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/counter/CounterExample.java
 */
public class CounterExample implements Model {

    private final int value;

    public CounterExample() {
        this(0);
    }

    public CounterExample(int value) {
        this.value = value;
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> new UpdateResult<>(this, () -> CounterMsg.INCREMENT);
                case "j", "J", "down" -> new UpdateResult<>(this, () -> CounterMsg.DECREMENT);
                case "d", "D" -> new UpdateResult<>(this, () -> CounterMsg.INCREMENT_LATER);
                case "q", "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        } else if (msg == CounterMsg.INCREMENT) {
            return UpdateResult.from(increment());
        } else if (msg == CounterMsg.DECREMENT) {
            return UpdateResult.from(decrement());
        } else if (msg == CounterMsg.INCREMENT_LATER) {
            return new UpdateResult<>(this, () -> {
                try {
                    Thread.sleep(1000); // Delay for 1 second
                    return CounterMsg.INCREMENT;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            });
        }
        return new UpdateResult<>(this, null);
    }

    public CounterExample increment() {
        return new CounterExample(value + 1);
    }

    public CounterExample decrement() {
        return new CounterExample(value - 1);
    }

    @Override
    public String view() {
        return """
                Counter Example
                ==============
                              \s
                Value: %d
                              \s
                Commands:
                ↑/k - Increment
                ↓/j - Decrement
                d - Delayed Increment
                q - Quit
               \s""".formatted(value);
    }

    public static void main(String[] args) {
        Program app = new Program(new CounterExample(0));
        app.run();
    }
}
