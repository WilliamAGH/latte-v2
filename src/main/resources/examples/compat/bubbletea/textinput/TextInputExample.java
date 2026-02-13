package examples.compat.bubbletea.textinput;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases.KeyAlias;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;

/**
 * Example program for text input.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/textinput/TextInputExample.java
 */
public class TextInputExample implements Model {

    private TextInput textInput;

    /**
     * Creates TextInputExample to keep example ready for use.
     */
    public TextInputExample() {
        this.textInput = new TextInput();
        textInput.setPlaceholder("Pikachu");
        textInput.focus();
        textInput.setCharLimit(156);
        textInput.setWidth(20);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return TextInput::blink;
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
            if (KeyAliases.getKeyType(KeyAlias.KeyEnter) == keyPressMessage.type()
                    || KeyAliases.getKeyType(KeyAlias.KeyCtrlC) == keyPressMessage.type()
                    || KeyType.keyESC == keyPressMessage.type()) {
                return UpdateResult.from(this, QuitMessage::new);
            }
        }

        UpdateResult<? extends Model> updateResult = textInput.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "What’s your favorite Pokémon?\n\n%s\n\n%s\n".formatted(
                textInput.view(),
                "(esc to quit)"
        );
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TextInputExample()).run();
    }
}
