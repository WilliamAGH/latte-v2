package examples.compat.bubbletea.debounce;

import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.ANSI256Color;
import java.time.Duration;

/**
 * Demonstrates debouncing by quitting after a pause in key presses.
 * Upstream: bubbletea/examples/debounce/main.go
 */
public class DebounceExample implements Model {

    private static final Duration DEBOUNCE_DURATION = Duration.ofSeconds(1);

    private int tag;
    private Spinner spinner;

    /**
     * Initializes the tag counter and spinner.
     */
    public DebounceExample() {
        this.tag = 0;
        this.spinner = new Spinner(SpinnerType.DOT);
        this.spinner.setStyle(
            Style.newStyle().foreground(new ANSI256Color(240))
        );
    }

    /**
     * Starts with no initial command for the debounce demo.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Restarts the debounce timer on keypress and quits on the latest timeout.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            tag++;
            return UpdateResult.from(
                this,
                Command.batch(
                    Command.tick(DEBOUNCE_DURATION, __ -> new ExitMessage(tag)),
                    spinner::tick
                )
            );
        }

        if (msg instanceof ExitMessage exitMessage) {
            if (exitMessage.tag() == tag) {
                return UpdateResult.from(this, Command.quit());
            }
        }

        UpdateResult<Spinner> spinnerResult = spinner.update(msg);
        if (spinnerResult.command() != null) {
            spinner = spinnerResult.model();
            return UpdateResult.from(this, spinnerResult.command());
        }

        return UpdateResult.from(this);
    }

    /**
     * Renders the keypress count and debounce status.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        String status = spinner.view() + " Waiting for debounce...";
        return (
            "Key presses: " +
            tag +
            "\n" +
            status +
            "\n\nTo exit press any key, then wait for one second without pressing anything."
        );
    }

    /**
     * Runs the debounce example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new DebounceExample()).run();
    }

    /**
     * Message used to signal a debounce timeout.
     */
    private record ExitMessage(int tag) implements Message {}
}
