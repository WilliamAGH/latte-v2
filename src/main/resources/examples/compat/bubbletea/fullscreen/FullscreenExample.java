package examples.compat.bubbletea.fullscreen;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.tick;

/**
 * Message used to drive the fullscreen countdown.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/fullscreen/FullscreenExample.java
 */
record TickMessage(LocalDateTime time) implements Message {}

/**
 * Demonstrates a fullscreen countdown that exits automatically.
 * Upstream: bubbletea/examples/fullscreen/main.go
 */
public class FullscreenExample implements Model {

    private int seconds;

    /**
     * Creates a model that exits after the given number of seconds.
     *
     * @param seconds countdown seconds
     */
    public FullscreenExample(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Starts the countdown tick.
     *
     * @return tick command
     */
    @Override
    public Command init() {
        return tick(Duration.ofSeconds(1), TickMessage::new);
    }

    /**
     * Decrements the timer and quits when it reaches zero.
     *
     * @param msg incoming message
     * @return next model state and command
     */
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

    /**
     * Renders the remaining countdown time.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "\n\n     Hi. This program will exit in %d seconds...".formatted(seconds);
    }

    /**
     * Runs the fullscreen countdown example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Model model = new FullscreenExample(5);
        new Program(model)
                .withAltScreen()
                .run();
    }
}
