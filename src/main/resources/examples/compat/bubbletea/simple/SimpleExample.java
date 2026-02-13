package examples.compat.bubbletea.simple;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Example program for Simple.
 */
public class SimpleExample implements Model {
    private final int seconds;

    /**
     * Creates SimpleExample to keep example ready for use.
     */
    public SimpleExample() {
        this(5);
    }

    /**
     * Creates SimpleExample to keep example ready for use.
     *
     * @param seconds seconds
     */
    public SimpleExample(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return SimpleExample::tick;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if (key.equals("ctrl+c") || key.equals("q")) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
            if (key.equals("ctrl+z")) {
                return new UpdateResult<>(this, com.williamcallahan.tui4j.compat.bubbletea.SuspendMessage::new);
            }
            return new UpdateResult<>(this, null);
        }

        if (msg instanceof TickMessage) {
            if (seconds <= 1) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
            return new UpdateResult<>(new SimpleExample(seconds - 1), SimpleExample::tick);
        }

        return new UpdateResult<>(this, null);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return String.format("""
                Hi. This program will exit in %d seconds.

                To quit sooner press ctrl-c, or press ctrl-z to suspend...
                """, seconds);
    }

    /**
     * Support type for the Simple example.
     */
    private static class TickMessage implements Message {
    }

    /**
     * Handles tick for example.
     *
     * @return result
     */
    private static Message tick() {
        try {
            Thread.sleep(1000);
            return new TickMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SimpleExample()).run();
    }
}
