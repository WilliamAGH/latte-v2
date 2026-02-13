package examples.compat.bubbletea.stopwatch;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.stopwatch.ResetMessage;
import com.williamcallahan.tui4j.compat.bubbles.stopwatch.StartStopMessage;
import com.williamcallahan.tui4j.compat.bubbles.stopwatch.Stopwatch;
import com.williamcallahan.tui4j.compat.bubbles.stopwatch.TickMessage;

import java.time.Duration;

/**
 * Stopwatch example mirroring Bubble Tea's stopwatch sample.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/stopwatch/StopwatchExample.java
 */
public class StopwatchExample implements Model {

    private static final Duration DEFAULT_INTERVAL = Duration.ofMillis(100);

    private final Help help;
    private final StopwatchKeyMap keys;
    private Stopwatch stopwatch;
    private boolean quitting;

    /**
     * Creates StopwatchExample to keep example ready for use.
     */
    public StopwatchExample() {
        this.stopwatch = new Stopwatch(DEFAULT_INTERVAL);
        this.help = new Help();
        this.keys = new StopwatchKeyMap();
        this.keys.stop().setEnabled(false);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return stopwatch.init();
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
            UpdateResult<Stopwatch> updateResult = stopwatch.update(msg);
            this.stopwatch = updateResult.model();
            if (msg instanceof StartStopMessage) {
                keys.stop().setEnabled(stopwatch.running());
                keys.start().setEnabled(!stopwatch.running());
            }
            return UpdateResult.from(this, updateResult.command());
        }

        if (msg instanceof ResetMessage) {
            UpdateResult<Stopwatch> updateResult = stopwatch.update(msg);
            this.stopwatch = updateResult.model();
            return UpdateResult.from(this, updateResult.command());
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.quit())) {
                quitting = true;
                return UpdateResult.from(this, Command.quit());
            }
            if (Binding.matches(keyPressMessage, keys.start())) {
                return update(new StartStopMessage(stopwatch.id(), true));
            }
            if (Binding.matches(keyPressMessage, keys.stop())) {
                return update(new StartStopMessage(stopwatch.id(), false));
            }
            if (Binding.matches(keyPressMessage, keys.reset())) {
                return update(new ResetMessage(stopwatch.id()));
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
        String view = stopwatch.view();
        view += "\n";

        if (!quitting) {
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
        new Program(new StopwatchExample()).run();
    }

    /**
     * Support type for the Stopwatch example.
     */
    private static final class StopwatchKeyMap implements KeyMap {
        private final Binding start;
        private final Binding stop;
        private final Binding reset;
        private final Binding quit;

        /**
         * Creates StopwatchKeyMap to keep example ready for use.
         */
        private StopwatchKeyMap() {
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
