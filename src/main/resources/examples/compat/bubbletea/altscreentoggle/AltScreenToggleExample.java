package examples.compat.bubbletea.altscreentoggle;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.EnterAltScreenMessage;
import com.williamcallahan.tui4j.compat.bubbletea.ExitAltScreenMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Describes the active screen mode so the view can explain state changes.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/altscreentoggle/AltScreenToggleExample.java
 */
enum Mode {
    ALT_SCREEN(" altscreen mode "),
    INLINE(" inline mode ");

    private final String description;

    /**
     * Stores the label used by the UI for this mode.
     *
     * @param description user-facing description
     */
    Mode(String description) {
        this.description = description;
    }

    /**
     * Returns the user-facing label for the current mode.
     *
     * @return mode description
     */
    public String getDescription() {
        return description;
    }
}

/**
 * Example program toggling between inline and alternate screen modes.
 */
public class AltScreenToggleExample implements Model {

    private static final Style KEYWORD_STYLE = Style.newStyle()
        .foreground(Color.color("204"))
        .background(Color.color("235"));

    private static final Style HELP_STYLE = Style.newStyle().foreground(
        Color.color("241")
    );

    private boolean altScreen;
    private boolean quitting;

    /**
     * Starts with no initial command for the toggle demo.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Handles space to toggle modes and q to quit.
     *
     * @param msg incoming message
     * @return updated model and optional command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "q", "Q" -> {
                    quitting = true;
                    yield new UpdateResult<>(this, QuitMessage::new);
                }
                case " " -> {
                    Command cmd;
                    if (altScreen) {
                        cmd = ExitAltScreenMessage::new;
                    } else {
                        cmd = EnterAltScreenMessage::new;
                    }
                    altScreen = !altScreen;
                    yield new UpdateResult<>(this, cmd);
                }
                default -> new UpdateResult<>(this, null);
            };
        }

        return UpdateResult.from(this);
    }

    /**
     * Renders the current mode and help text.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (quitting) {
            return "Bye!\n";
        }

        Mode mode;
        if (altScreen) {
            mode = Mode.ALT_SCREEN;
        } else {
            mode = Mode.INLINE;
        }

        return (
            "\n\n  You're in %s\n\n\n".formatted(
                KEYWORD_STYLE.render(mode.getDescription())
            ) +
            HELP_STYLE.render("  space: switch modes • q: exit\n")
        );
    }

    /**
     * Runs the alternate screen toggle example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Program program = new Program(new AltScreenToggleExample());
        program.run();
    }
}
