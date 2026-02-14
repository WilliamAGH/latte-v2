package examples.compat.bubbletea.progress.animated;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.FrameMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.Progress;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Demonstrates an animated progress bar with timed increments.
 * Upstream: bubbletea/examples/progress-animated/main.go
 */
public class ProgressAnimatedExample implements Model {

    private static final int PADDING = 2;
    private static final int MAX_WIDTH = 80;

    private Progress progress;
    private int width;

    /**
     * Initializes the progress bar and default width.
     */
    public ProgressAnimatedExample() {
        this.progress = new Progress()
                .withShowPercentage(true)
                .withDefaultGradient();
        this.width = 40;
    }

    /**
     * Starts the periodic tick that drives progress updates.
     *
     * @return tick command
     */
    @Override
    public Command init() {
        return tickCmd();
    }

    /**
     * Handles resizing, ticks, and progress frame updates.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return new UpdateResult<>(this, QuitMessage::new);
        }

        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.width = windowSizeMessage.width() - PADDING * 2 - 4;
            if (this.width > MAX_WIDTH) {
                this.width = MAX_WIDTH;
            }
            progress.setWidth(this.width);
            return UpdateResult.from(this);
        }

        if (msg instanceof TickMessage) {
            if (progress.percent() >= 1.0) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
            Command cmd = progress.incrPercent(0.25);
            return new UpdateResult<>(this, Command.batch(tickCmd(), cmd));
        }

        if (msg instanceof FrameMessage) {
            UpdateResult<Progress> result = progress.update(msg);
            this.progress = result.model();
            return UpdateResult.from(this, result.command());
        }

        UpdateResult<Progress> result = progress.update(msg);
        this.progress = result.model();
        return UpdateResult.from(this, result.command());
    }

    /**
     * Renders the progress bar with padding and help text.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" ".repeat(PADDING));
        sb.append(progress.view());
        sb.append("\n\n");
        sb.append(" ".repeat(PADDING));
        sb.append("Press any key to quit");
        return sb.toString();
    }

    /**
     * Builds the periodic tick command used to increment progress.
     *
     * @return tick command
     */
    private Command tickCmd() {
        return Command.tick(Duration.ofSeconds(1), time -> new TickMessage());
    }

    /**
     * Runs the animated progress example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ProgressAnimatedExample()).run();
    }
}

/**
 * Message used to drive periodic progress updates.
 */
class TickMessage implements Message {
    private final LocalDateTime time;

    /**
     * Captures the current time for the tick message.
     */
    public TickMessage() {
        this.time = LocalDateTime.now();
    }

    /**
     * Returns the timestamp associated with the tick.
     *
     * @return tick time
     */
    public LocalDateTime time() {
        return time;
    }
}
