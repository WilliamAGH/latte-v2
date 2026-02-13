package examples.compat.bubbletea.result;

import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import java.util.List;

/**
 * Example demonstrating how to retrieve a final model after exit.
 * Upstream: bubbletea/examples/result/main.go.
 */
public class ResultExample implements Model {

    private static final List<String> CHOICES = List.of("Taro", "Coffee", "Lychee");

    private final int cursor;
    private final String choice;

    /**
     * Creates a result example with the default cursor.
     */
    public ResultExample() {
        this(0, "");
    }

    /**
     * Creates a result example with explicit state.
     *
     * @param cursor current cursor position
     * @param choice selected choice
     */
    public ResultExample(int cursor, String choice) {
        this.cursor = cursor;
        this.choice = choice;
    }

    /**
     * No initial command for this example.
     *
     * @return null
     */
    @Override
    public com.williamcallahan.tui4j.compat.bubbletea.Command init() {
        return null;
    }

    /**
     * Handles cursor movement, selection, and quit keys.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "ctrl+c", "q", "esc" -> {
                    return UpdateResult.from(this, QuitMessage::new);
                }
                case "enter" -> {
                    return UpdateResult.from(new ResultExample(cursor, CHOICES.get(cursor)), QuitMessage::new);
                }
                case "down", "j" -> {
                    int next = (cursor + 1) % CHOICES.size();
                    return UpdateResult.from(new ResultExample(next, choice));
                }
                case "up", "k" -> {
                    int next = cursor - 1;
                    if (next < 0) {
                        next = CHOICES.size() - 1;
                    }
                    return UpdateResult.from(new ResultExample(next, choice));
                }
                default -> {
                    return UpdateResult.from(this);
                }
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the choice list and cursor.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder builder = new StringBuilder();
        builder.append("What kind of Bubble Tea would you like to order?\n\n");

        for (int i = 0; i < CHOICES.size(); i++) {
            builder.append(cursor == i ? "(•) " : "( ) ");
            builder.append(CHOICES.get(i)).append("\n");
        }

        builder.append("\n(press q to quit)\n");
        return builder.toString();
    }

    /**
     * Returns the selected choice, if any.
     *
     * @return selected choice
     */
    public String getChoice() {
        return choice;
    }

    /**
     * Runs the example and prints the final choice.
     *
     * @param args args
     */
    public static void main(String[] args) {
        Program program = new Program(new ResultExample());
        Model finalModel = program.runWithFinalModel();
        if (finalModel instanceof ResultExample resultExample && !resultExample.getChoice().isBlank()) {
            System.out.printf("%n---%nYou chose %s!%n", resultExample.getChoice());
        }
    }
}
