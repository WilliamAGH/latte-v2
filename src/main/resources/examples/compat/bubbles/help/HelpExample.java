package examples.compat.bubbles.help;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Example program demonstrating help component.
 * <p>
 * Shows a help menu that toggles between short and full help views.
 *
 * @see <a href="https://github.com/charmbracelet/bubbletea/blob/main/examples/help/main.go">bubbletea/examples/help</a>
 */
public class HelpExample implements Model {

    private final Help help = new Help();
    private final Style inputStyle = Style.newStyle().foreground(Color.color("#FF75B7"));

    private final Binding upBinding = new Binding(Binding.withKeys("up", "k"), Binding.withHelp("↑/k", "move up"));
    private final Binding downBinding = new Binding(Binding.withKeys("down", "j"), Binding.withHelp("↓/j", "move down"));
    private final Binding leftBinding = new Binding(Binding.withKeys("left", "h"), Binding.withHelp("←/h", "move left"));
    private final Binding rightBinding = new Binding(Binding.withKeys("right", "l"), Binding.withHelp("→/l", "move right"));
    private final Binding helpBinding = new Binding(Binding.withKeys("?"), Binding.withHelp("?", "toggle help"));
    private final Binding quitBinding = new Binding(Binding.withKeys("q", "esc", "ctrl+c"), Binding.withHelp("q", "quit"));

    private final KeyMap keyMap = new KeyMap() {
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{upBinding, downBinding, leftBinding, rightBinding, helpBinding, quitBinding};
        }

        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                {upBinding, downBinding, leftBinding, rightBinding},
                {helpBinding, quitBinding}
            };
        }
    };

    private String lastKey;
    private boolean quitting;

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            help.setWidth(windowSizeMessage.width());
            return UpdateResult.from(this);
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();

            switch (key) {
                case "up", "k" -> {
                    lastKey = "↑";
                    return UpdateResult.from(this);
                }
                case "down", "j" -> {
                    lastKey = "↓";
                    return UpdateResult.from(this);
                }
                case "left", "h" -> {
                    lastKey = "←";
                    return UpdateResult.from(this);
                }
                case "right", "l" -> {
                    lastKey = "→";
                    return UpdateResult.from(this);
                }
                case "?" -> {
                    help.setShowAll(!help.showAll());
                    return UpdateResult.from(this);
                }
                case "q", "ctrl+c", "esc" -> {
                    quitting = true;
                    return UpdateResult.from(this, QuitMessage::new);
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
        if (quitting) {
            return "Bye!\n";
        }

        String status;
        if (lastKey == null || lastKey.isEmpty()) {
            status = "Waiting for input...";
        } else {
            status = "You chose: " + inputStyle.render(lastKey);
        }

        String helpView = help.render(keyMap);

        int newlines = (int) (8 - status.lines().count() - helpView.lines().count());
        String padding = "\n".repeat(Math.max(0, newlines));

        return "\n" + status + padding + helpView;
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new HelpExample()).run();
    }
}
