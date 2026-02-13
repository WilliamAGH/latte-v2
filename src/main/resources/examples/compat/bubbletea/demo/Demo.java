package examples.compat.bubbletea.demo;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Demonstrates a simple menu selection flow using Bubble Tea patterns.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/demo/Demo.java
 */
public class Demo implements Model {

    private final static Style SELECTION = Style.newStyle().foreground(Color.color("205"));
    private final static String[] CHOICES = {"Espresso", "Americano", "tui4j"};

    private int cursor;
    private String choice;

    /**
     * Starts with no initial command for the menu.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Handles navigation and selection keys.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> UpdateResult.from(this.moveUp());
                case "j", "J", "down" -> UpdateResult.from(this.moveDown());
                case "enter" -> UpdateResult.from(this.makeChoice(), QuitMessage::new);
                case "q", "Q" -> UpdateResult.from(this, QuitMessage::new);
                default -> UpdateResult.from(this);
            };
        }
        return UpdateResult.from(this);
    }

    /**
     * Stores the selected choice based on the cursor position.
     *
     * @return updated model
     */
    private Model makeChoice() {
        for (int index = 0; index < CHOICES.length; index++) {
            String choice = CHOICES[index];
            if (index == cursor) {
                this.choice = choice;
                return this;
            }
        }
        return this;
    }

    /**
     * Moves the cursor up with wraparound.
     *
     * @return updated model
     */
    private Model moveUp() {
        if (cursor - 1 < 0) {
            cursor = CHOICES.length - 1;
            return this;
        }
        cursor--;
        return this;
    }

    /**
     * Moves the cursor down with wraparound.
     *
     * @return updated model
     */
    private Model moveDown() {
        if (cursor + 1 >= CHOICES.length) {
            cursor = 0;
            return this;
        }
        cursor++;
        return this;
    }

    /**
     * Renders the menu with a visual cursor.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("What kind of Coffee would you like to order?\n\n");

        for (int index = 0; index < CHOICES.length; index++) {
            if (cursor == index) {
                buffer.append(SELECTION.render("[•]", CHOICES[index]));
            } else {
                buffer.append("[ ] ").append(CHOICES[index]);
            }
            buffer.append("\n");
        }
        buffer.append("\n(press q to quit)");
        return buffer.toString();
    }

    /**
     * Returns the selected choice after exiting the program.
     *
     * @return chosen menu item or null if none selected
     */
    public String getChoice() {
        return choice;
    }

    /**
     * Runs the menu example and prints the final selection.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Demo demoModel = new Demo();
        Program program = new Program(demoModel);
        program.run();

        if (demoModel.getChoice() == null) {
            return;
        }
        System.out.printf("\n---\nYou chose: %s!\n", demoModel.getChoice());
    }
}
