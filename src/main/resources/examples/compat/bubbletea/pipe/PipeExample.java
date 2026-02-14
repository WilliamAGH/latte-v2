package examples.compat.bubbletea.pipe;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases.KeyAlias;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Demonstrates reading piped input into a text input field.
 * Upstream: bubbletea/examples/pipe/main.go
 */
public class PipeExample implements Model {

    private final TextInput textInput;

    /**
     * Seeds the input field with piped content.
     *
     * @param initialValue initial text
     */
    public PipeExample(String initialValue) {
        this.textInput = new TextInput();
        textInput.setPlaceholder("Type something...");
        textInput.setValue(initialValue);
        textInput.setWidth(48);
        textInput.focus();
    }

    /**
     * Starts the blinking cursor for the input.
     *
     * @return blink command
     */
    @Override
    public Command init() {
        return TextInput::blink;
    }

    /**
     * Handles quit keys and forwards updates to the input.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage
                && (KeyAliases.getKeyType(KeyAlias.KeyEnter) == keyPressMessage.type()
                    || KeyAliases.getKeyType(KeyAlias.KeyCtrlC) == keyPressMessage.type()
                    || KeyType.keyESC == keyPressMessage.type())) {
            return UpdateResult.from(this, QuitMessage::new);
        }

        UpdateResult<? extends Model> updateResult = textInput.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the piped text with exit instructions.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return String.format("%nYou piped in: %s%n%nPress ^C, Esc, or Enter to exit", textInput.view());
    }

    /**
     * Runs the pipe example, reading input from stdin when piped.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        String pipedInput = readPipedInput();

        if (pipedInput == null || pipedInput.isEmpty()) {
            System.out.println("Try piping in some text. Example:");
            System.out.println("  echo 'Hello, World!' | java -jar tui4j-examples.jar");
            System.exit(1);
            return;
        }

        new Program(new PipeExample(pipedInput.trim())).run();
    }

    /**
     * Reads piped input from stdin when no console is attached.
     *
     * @return piped content or null when not piped
     */
    private static String readPipedInput() {
        if (System.console() != null) {
            return null; // console present = not piped
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // don't close - Program needs System.in
        try {
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
        } catch (IOException e) {
            System.err.println("Error reading piped input: " + e.getMessage());
            return null;
        }
        return sb.toString();
    }
}
