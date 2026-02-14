package examples.compat.bubbletea.spinner;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;

/**
 * Example program for spinner.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/spinner/SpinnerExample.java
 */
public class SpinnerExample implements Model {

    private Model spinner;

    /**
     * Creates SpinnerExample to keep example ready for use.
     */
    public SpinnerExample() {
        this.spinner = new Spinner(SpinnerType.DOT).setStyle(Style.newStyle().foreground(Color.color("205")));
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return spinner.init();
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "q", "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }

        UpdateResult<? extends Model> updateResult = spinner.update(msg);
        spinner = updateResult.model();
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "%n%n   %s Loading forever...press q to quit%n%n".formatted(spinner.view());
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SpinnerExample()).run();
    }
}
