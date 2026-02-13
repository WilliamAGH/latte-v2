package examples.compat.bubbletea.tutorials.basics;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tutorial example showing basic Bubble Tea interactions.
 * Upstream: bubbletea/tutorials/basics/main.go.
 */
public class BasicsTutorialExample implements Model {

    private static final String WINDOW_TITLE = "Grocery List";

    private final int cursor;
    private final List<String> choices;
    private final Set<Integer> selected;

    /**
     * Creates the tutorial example with default choices.
     */
    public BasicsTutorialExample() {
        this(0, defaultChoices(), new HashSet<>());
    }

    /**
     * Creates the tutorial example with explicit state.
     *
     * @param cursor current selection cursor
     * @param choices list of choices
     * @param selected set of selected choice indices
     */
    public BasicsTutorialExample(int cursor, List<String> choices, Set<Integer> selected) {
        this.cursor = cursor;
        this.choices = choices;
        this.selected = selected;
    }

    /**
     * Sets the window title on startup.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return Command.setWindowTitle(WINDOW_TITLE);
    }

    /**
     * Handles cursor movement, selection toggles, and quit keys.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "ctrl+c", "q" -> {
                    return new UpdateResult<>(this, QuitMessage::new);
                }
                case "up", "k" -> {
                    return UpdateResult.from(withCursor(Math.max(0, cursor - 1)));
                }
                case "down", "j" -> {
                    return UpdateResult.from(withCursor(Math.min(choices.size() - 1, cursor + 1)));
                }
                case "enter", " " -> {
                    return UpdateResult.from(withToggledSelection(cursor));
                }
                default -> {
                    return UpdateResult.from(this);
                }
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the list of choices with the cursor and selection state.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder builder = new StringBuilder();
        builder.append("What should we buy at the market?\n\n");

        for (int i = 0; i < choices.size(); i++) {
            String cursorIndicator = cursor == i ? ">" : " ";
            String checked = selected.contains(i) ? "x" : " ";
            builder.append(cursorIndicator)
                .append(" [")
                .append(checked)
                .append("] ")
                .append(choices.get(i))
                .append("\n");
        }

        builder.append("\nPress q to quit.\n");
        return builder.toString();
    }

    /**
     * Builds a default set of grocery choices.
     *
     * @return default choice list
     */
    private static List<String> defaultChoices() {
        List<String> choices = new ArrayList<>();
        choices.add("Buy carrots");
        choices.add("Buy celery");
        choices.add("Buy kohlrabi");
        return choices;
    }

    /**
     * Creates a new model with the provided cursor position.
     *
     * @param newCursor updated cursor position
     * @return updated model
     */
    private BasicsTutorialExample withCursor(int newCursor) {
        return new BasicsTutorialExample(newCursor, choices, new HashSet<>(selected));
    }

    /**
     * Creates a new model with the selection toggled at the given index.
     *
     * @param index index to toggle
     * @return updated model
     */
    private BasicsTutorialExample withToggledSelection(int index) {
        Set<Integer> nextSelected = new HashSet<>(selected);
        if (nextSelected.contains(index)) {
            nextSelected.remove(index);
        } else {
            nextSelected.add(index);
        }
        return new BasicsTutorialExample(cursor, choices, nextSelected);
    }

    /**
     * Runs the tutorial example.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new BasicsTutorialExample()).run();
    }
}
