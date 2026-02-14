package examples.compat.bubbletea.progress.staticview;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.Progress;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Example program for Progress.
 */
public class ProgressStaticExample implements Model {

    private static final int PADDING = 2;
    private static final int MAX_WIDTH = 80;

    private final Progress progress;
    private double percent;
    private int width;

    /**
     * Creates ProgressStaticExample to keep example ready for use.
     */
    public ProgressStaticExample() {
        this.progress = new Progress()
                .withWidth(40)
                .withShowPercentage(true)
                .withDefaultGradient();
        this.percent = 0.0;
        this.width = 40;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return tickCmd();
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
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
            percent += 0.25;
            if (percent > 1.0) {
                percent = 1.0;
                return new UpdateResult<>(this, QuitMessage::new);
            }
            return new UpdateResult<>(this, tickCmd());
        }

        return UpdateResult.from(this);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" ".repeat(PADDING));
        sb.append(progress.viewAs(percent));
        sb.append("\n\n");
        sb.append(" ".repeat(PADDING));
        sb.append("Press any key to quit");
        return sb.toString();
    }

    /**
     * Handles tick cmd for example.
     *
     * @return result
     */
    private Command tickCmd() {
        return Command.tick(Duration.ofSeconds(1), time -> new TickMessage());
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new ProgressStaticExample()).run();
    }
}

/**
 * Support type for the Progress example.
 */
class TickMessage implements Message {
    private final LocalDateTime time;

    /**
     * Creates TickMessage to keep example ready for use.
     */
    public TickMessage() {
        this.time = LocalDateTime.now();
    }

    /**
     * Handles time for example.
     *
     * @return result
     */
    public LocalDateTime time() {
        return time;
    }
}
