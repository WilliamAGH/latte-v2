package examples.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import examples.compat.bubbletea.altscreentoggle.AltScreenToggleExample;
import examples.compat.bubbletea.counter.CounterExample;
import examples.compat.bubbletea.demo.Demo;
import examples.compat.bubbletea.exec.ExecExample;
import examples.compat.bubbletea.fullscreen.FullscreenExample;
import examples.compat.bubbletea.split.SplitEditorsExample;
import examples.compat.bubbletea.tuidemoncombo.DaemonComboExample;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.util.List;

/**
 * Provides an interactive chooser to launch example models.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/ExamplesRunner.java
 */
public class ExamplesRunner implements Model {

    private final List<Model> exampleModels;
    private int cursor;

    /**
     * Creates a runner with the available example models.
     *
     * @param exampleModels models to list for selection
     */
    public ExamplesRunner(List<Model> exampleModels) {
        this.exampleModels = exampleModels;
    }

    /**
     * Starts with no initial command for the chooser.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Handles navigation and selection keys to choose an example.
     *
     * @param msg incoming message
     * @return updated model and optional command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> new UpdateResult<>(this.moveUp(), null);
                case "j", "J", "down" -> new UpdateResult<>(this.moveDown(), null);
                case "enter" -> new UpdateResult<>(this.makeChoice(), null);
                case "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }
        return new UpdateResult<>(this, null);    }

    /**
     * Renders the list of available examples with a cursor indicator.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Pick an example:\n");
        buffer.append("~~~~~~~~~~~~~~~~\n\n");

        for (int index = 0; index < exampleModels.size(); index++) {
            if (cursor == index) {
                buffer.append("(•) ");
            } else {
                buffer.append("( ) ");

            }
            buffer.append(exampleModels.get(index).getClass().getSimpleName()).append("\n");
        }
        buffer.append("\n(press q to quit)");
        return buffer.toString();
    }

    /**
     * Returns the currently selected model to hand off to the program.
     *
     * @return selected model or this instance if none is selected
     */
    private Model makeChoice() {
        for (int index = 0; index < exampleModels.size(); index++) {
            if (index == cursor) {
                return exampleModels.get(index);
            }
        }
        return this;
    }

    /**
     * Moves the cursor up, clamping to the first item.
     *
     * @return updated model
     */
    private Model moveUp() {
        if (cursor - 1 <= 0 ) {
            cursor = 0;
            return this;
        }
        cursor--;
        return this;
    }

    /**
     * Moves the cursor down, wrapping to the top at the end.
     *
     * @return updated model
     */
    private Model moveDown() {
        if (cursor + 1 >= exampleModels.size()) {
            cursor = 0;
            return this;
        }
        cursor++;
        return this;
    }

    /**
     * Launches the examples runner with a default selection list.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ExamplesRunner(List.of(
                new CounterExample(),
                new Demo(),
                new AltScreenToggleExample(),
                new FullscreenExample(5),
                new ExecExample(),
                new SplitEditorsExample(),
                new DaemonComboExample()
        ))).run();
    }
}
