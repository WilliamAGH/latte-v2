package examples.compat.bubbletea.counter;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

/**
 * Demonstrates a counter with immediate and delayed commands.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/counter/CounterExample.java
 */
public class CounterExample implements Model {

    private final int value;

    /**
     * Starts the counter at zero.
     */
    public CounterExample() {
        this(0);
    }

    /**
     * Starts the counter at the provided value.
     *
     * @param value initial counter value
     */
    public CounterExample(int value) {
        this.value = value;
    }

    /**
     * Returns no initial command for this example.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Handles key input and counter messages, including a delayed increment.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> new UpdateResult<>(this, () ->
                    CounterMessage.INCREMENT
                );
                case "j", "J", "down" -> new UpdateResult<>(this, () ->
                    CounterMessage.DECREMENT
                );
                case "d", "D" -> new UpdateResult<>(this, () ->
                    CounterMessage.INCREMENT_LATER
                );
                case "q", "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        } else if (msg == CounterMessage.INCREMENT) {
            return UpdateResult.from(increment());
        } else if (msg == CounterMessage.DECREMENT) {
            return UpdateResult.from(decrement());
        } else if (msg == CounterMessage.INCREMENT_LATER) {
            return new UpdateResult<>(this, () -> {
                try {
                    Thread.sleep(1000); // Delay for 1 second
                    return CounterMessage.INCREMENT;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            });
        }
        return new UpdateResult<>(this, null);
    }

    /**
     * Produces a new model with the value incremented.
     *
     * @return updated model
     */
    public CounterExample increment() {
        return new CounterExample(value + 1);
    }

    /**
     * Produces a new model with the value decremented.
     *
     * @return updated model
     */
    public CounterExample decrement() {
        return new CounterExample(value - 1);
    }

    /**
     * Renders instructions and the current counter value.
     *
     * @return rendered view
     */
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

    /**
     * Runs the counter example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Program app = new Program(new CounterExample(0));
        app.run();
    }
}
