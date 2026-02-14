package examples.compat.bubbles.timer;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.timer.Timer;
import com.williamcallahan.tui4j.compat.bubbles.timer.TickMessage;
import com.williamcallahan.tui4j.compat.bubbles.timer.StartStopMessage;
import com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;

import java.time.Duration;

/**
 * Example program demonstrating timer component.
 * <p>
 * Shows a countdown timer that can be started, stopped, and reset.
 *
 * @see <a href="https://github.com/charmbracelet/bubbletea/blob/main/examples/timer/main.go">bubbletea/examples/timer</a>
 */
public class TimerExample implements Model {

    private final Timer timer;
    private final Help help = new Help();

    private final Binding startBinding = new Binding(Binding.withKeys("s"), Binding.withHelp("s", "start"));
    private final Binding stopBinding = new Binding(Binding.withKeys("s"), Binding.withHelp("s", "stop"));
    private final Binding resetBinding = new Binding(Binding.withKeys("r"), Binding.withHelp("r", "reset"));
    private final Binding quitBinding = new Binding(Binding.withKeys("q", "ctrl+c"), Binding.withHelp("q", "quit"));

    private final KeyMap keyMap = new KeyMap() {
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{startBinding, stopBinding, resetBinding, quitBinding};
        }

        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{{startBinding, stopBinding, resetBinding, quitBinding}};
        }
    };

    private boolean quitting;

    /**
     * Creates TimerExample to keep example ready for use.
     */
    public TimerExample() {
        this.timer = new Timer(Duration.ofSeconds(5), Duration.ofMillis(100));
        this.startBinding.setEnabled(false);
        this.stopBinding.setEnabled(true);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return timer.init();
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            Command cmd = timer.update(msg).command();
            return UpdateResult.from(this, cmd);
        }

        if (msg instanceof StartStopMessage) {
            UpdateResult<? extends Model> result = timer.update(msg);
            stopBinding.setEnabled(timer.running());
            startBinding.setEnabled(!timer.running());
            return UpdateResult.from(this, result.command());
        }

        if (msg instanceof TimeoutMessage) {
            quitting = true;
            return UpdateResult.from(this, QuitMessage::new);
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();

            switch (key) {
                case "q", "ctrl+c" -> {
                    quitting = true;
                    return UpdateResult.from(this, QuitMessage::new);
                }
                case "r" -> {
                    timer.setTimeout(Duration.ofSeconds(5));
                    return UpdateResult.from(this);
                }
                case "s" -> {
                    return UpdateResult.from(this, timer.toggle());
                }
                default -> {
                    return UpdateResult.from(this);
                }
            }
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
        String s = timer.view();

        if (timer.timedout()) {
            s = "All done!";
        }
        s += "\n";

        if (!quitting) {
            s += "Exiting in " + s;
            s += "\n\n" + help.render(keyMap);
        }

        return s;
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TimerExample()).run();
    }
}
