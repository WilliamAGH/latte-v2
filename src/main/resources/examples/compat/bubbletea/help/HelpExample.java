package examples.compat.bubbletea.help;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;

/**
 * Demonstrates the help component with short and full modes.
 * Upstream: bubbletea/examples/help/main.go
 */
public class HelpExample implements Model {

    private final Help help;
    private final HelpKeyMap keys;
    private boolean showFullHelp;

    /**
     * Initializes the help component and key map.
     */
    public HelpExample() {
        this.help = new Help();
        this.keys = new HelpKeyMap();
        this.showFullHelp = false;
    }

    /**
     * Starts with no initial command.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Toggles help visibility and handles quit input.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.toggleFullHelp())) {
                showFullHelp = !showFullHelp;
                help.setShowAll(showFullHelp);
                return UpdateResult.from(this);
            }
            if (Binding.matches(keyPressMessage, keys.quit())) {
                return UpdateResult.from(this, Command.quit());
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders help content and usage instructions.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("This is a simple example demonstrating the help component.\n\n");
        sb.append("Press ").append(keys.toggleFullHelp().help().key()).append(" to toggle full help.\n\n");
        sb.append(help.render(keys));
        return sb.toString();
    }

    /**
     * Defines the key bindings and help layout for the demo.
     */
    private static final class HelpKeyMap implements KeyMap {
        private final Binding toggleFullHelp;
        private final Binding quit;

        /**
         * Initializes the help bindings.
         */
        private HelpKeyMap() {
            this.toggleFullHelp = new Binding(
                    Binding.withKeys("h", "?"),
                    Binding.withHelp("h/?", "toggle full help")
            );
            this.quit = new Binding(
                    Binding.withKeys("q", "ctrl+c"),
                    Binding.withHelp("q/ctrl+c", "quit")
            );
        }

        /**
         * Returns the binding used to toggle full help.
         *
         * @return toggle binding
         */
        public Binding toggleFullHelp() {
            return toggleFullHelp;
        }

        /**
         * Returns the binding used to quit the demo.
         *
         * @return quit binding
         */
        public Binding quit() {
            return quit;
        }

        /**
         * Provides the short help layout.
         *
         * @return short help bindings
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{
                    toggleFullHelp,
                    quit
            };
        }

        /**
         * Provides the full help layout.
         *
         * @return full help bindings
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{toggleFullHelp},
                    new Binding[]{quit}
            };
        }
    }

    /**
     * Runs the help example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new HelpExample()).run();
    }
}
