package examples.compat.bubbletea.textarea;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.textarea.Textarea;

/**
 * Example program for Textarea.
 */
public class TextareaExample implements Model {

    private Textarea textarea;

    /**
     * Creates TextareaExample to keep example ready for use.
     */
    public TextareaExample() {
        this.textarea = new Textarea();
        textarea.setPlaceholder("Once upon a time...");
        textarea.focus();
    }

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
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (KeyType.keyESC == keyPressMessage.type()) {
                if (textarea.focused()) {
                    textarea.blur();
                }
            } else if (KeyType.keyETX == keyPressMessage.type()) {
                return UpdateResult.from(this, QuitMessage::new);
            } else {
                if (!textarea.focused()) {
                    textarea.focus();
                }
            }
        }

        UpdateResult<? extends Model> updateResult = textarea.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "Tell me a story.\n\n%s\n\n(ctrl+c to quit)\n".formatted(textarea.view());
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TextareaExample()).run();
    }
}
