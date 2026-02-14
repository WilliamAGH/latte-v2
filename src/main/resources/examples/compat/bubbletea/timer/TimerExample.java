package examples.compat.bubbletea.timer;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.timer.StartStopMessage;
import com.williamcallahan.tui4j.compat.bubbles.timer.TickMessage;
import com.williamcallahan.tui4j.compat.bubbles.timer.Timer;
import com.williamcallahan.tui4j.compat.bubbles.timer.TimeoutMessage;

import java.time.Duration;

/**
 * Timer example mirroring Bubble Tea's timer sample.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/timer/TimerExample.java
 */
public class TimerExample implements Model {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_INTERVAL = Duration.ofMillis(1);

    private final Help help;
    private final TimerKeyMap keys;
    private Timer timer;
    private boolean quitting;

    /**
     * Creates TimerExample to keep example ready for use.
     */
    public TimerExample() {
        this.timer = new Timer(DEFAULT_TIMEOUT, DEFAULT_INTERVAL);
        this.help = new Help();
        this.keys = new TimerKeyMap();
        this.keys.start().setEnabled(false);
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
        if (msg instanceof TickMessage || msg instanceof StartStopMessage) {
            UpdateResult<Timer> updateResult = timer.update(msg);
            this.timer = updateResult.model();
            if (msg instanceof StartStopMessage) {
                keys.stop().setEnabled(timer.running());
                keys.start().setEnabled(!timer.running());
            }
            return UpdateResult.from(this, updateResult.command());
        }

        if (msg instanceof TimeoutMessage) {
            quitting = true;
            return UpdateResult.from(this, Command.quit());
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.quit())) {
                quitting = true;
                return UpdateResult.from(this, Command.quit());
            }
            if (Binding.matches(keyPressMessage, keys.reset())) {
                timer.setTimeout(DEFAULT_TIMEOUT);
                return UpdateResult.from(this);
            }
            if (Binding.matches(keyPressMessage, keys.start(), keys.stop())) {
                return UpdateResult.from(this, timer.toggle());
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
        String view = timer.view();
        if (timer.timedout()) {
            view = "All done!";
        }
        view += "\n";

        if (!quitting) {
            view = "Exiting in " + view;
            view += helpView();
        }
        return view;
    }

    /**
     * Handles help view for example.
     *
     * @return result
     */
    private String helpView() {
        return "\n" + help.render(keys);
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TimerExample()).run();
    }

    /**
     * Support type for the Timer example.
     */
    private static final class TimerKeyMap implements KeyMap {
        private final Binding start;
        private final Binding stop;
        private final Binding reset;
        private final Binding quit;

        /**
         * Creates TimerKeyMap to keep example ready for use.
         */
        private TimerKeyMap() {
            this.start = new Binding(
                    Binding.withKeys("s"),
                    Binding.withHelp("s", "start")
            );
            this.stop = new Binding(
                    Binding.withKeys("x"),
                    Binding.withHelp("x", "stop")
            );
            this.reset = new Binding(
                    Binding.withKeys("r"),
                    Binding.withHelp("r", "reset")
            );
            this.quit = new Binding(
                    Binding.withKeys("q", "ctrl+c"),
                    Binding.withHelp("q", "quit")
            );
        }

        /**
         * Handles start for example.
         *
         * @return result
         */
        private Binding start() {
            return start;
        }

        /**
         * Handles stop for example.
         *
         * @return result
         */
        private Binding stop() {
            return stop;
        }

        /**
         * Handles reset for example.
         *
         * @return result
         */
        private Binding reset() {
            return reset;
        }

        /**
         * Handles quit for example.
         *
         * @return result
         */
        private Binding quit() {
            return quit;
        }

        /**
         * Handles short help for example.
         *
         * @return result
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{start, stop, reset, quit};
        }

        /**
         * Handles full help for example.
         *
         * @return result
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{shortHelp()};
        }
    }
}
