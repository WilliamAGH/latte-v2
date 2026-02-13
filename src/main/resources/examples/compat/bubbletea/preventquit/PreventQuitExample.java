package examples.compat.bubbletea.preventquit;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.textarea.Textarea;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.cursor.Cursor;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Demonstrates a quit confirmation flow when there are unsaved changes.
 * Upstream: bubbletea/examples/prevent-quit/main.go
 */
public class PreventQuitExample implements Model {

    private static final Style CHOICE_STYLE = Style.newStyle().paddingLeft(1).foreground(Color.color("241"));
    private static final Style SAVE_TEXT_STYLE = Style.newStyle().foreground(Color.color("170"));

    private Textarea textarea;
    private Help help;
    private PreventQuitKeyMap keymap;
    private String saveText;
    private boolean hasChanges;
    private boolean quitting;

    /**
     * Configures the textarea and help bindings.
     */
    public PreventQuitExample() {
        this.textarea = new Textarea();
        textarea.setPlaceholder("Only the best words");
        textarea.focus();
        textarea.setWidth(40);
        textarea.setHeight(6);

        this.help = new Help();
        this.keymap = new PreventQuitKeyMap();
        this.saveText = "";
        this.hasChanges = false;
        this.quitting = false;
    }

    /**
     * Starts the blinking cursor for the textarea.
     *
     * @return cursor blink command
     */
    @Override
    public Command init() {
        return Cursor::blink;
    }

    /**
     * Routes updates to the editor or quit prompt state.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (quitting) {
            return updatePromptView(msg);
        }
        return updateTextView(msg);
    }

    /**
     * Updates text editing state and handles save/quit actions.
     *
     * @param msg incoming message
     * @return updated model and command
     */
    private UpdateResult<? extends Model> updateTextView(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            saveText = "";

            if (Binding.matches(keyPressMessage, keymap.save())) {
                saveText = "Changes saved!";
                hasChanges = false;
                return UpdateResult.from(this);
            }

            if (Binding.matches(keyPressMessage, keymap.quit())) {
                if (hasChanges) {
                    quitting = true;
                    return UpdateResult.from(this);
                }
                return UpdateResult.from(this, QuitMessage::new);
            }
        }

        String beforeValue = textarea.value();
        UpdateResult<? extends Model> taResult = textarea.update(msg);
        if (!beforeValue.equals(textarea.value())) {
            hasChanges = true;
        }
        return UpdateResult.from(this, taResult.command());
    }

    /**
     * Handles the quit confirmation prompt.
     *
     * @param msg incoming message
     * @return updated model and command
     */
    private UpdateResult<? extends Model> updatePromptView(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keymap.quit()) || keyPressMessage.key().equals("y")) {
                hasChanges = false;
                return UpdateResult.from(this, QuitMessage::new);
            }
            quitting = false;
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the editor or the quit confirmation prompt.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (quitting) {
            if (hasChanges) {
                String text = "You have unsaved changes. Quit without saving? [yn]";
                return CHOICE_STYLE.render(text);
            }
            return "Very important, thank you\n";
        }

        String helpView = help.render(keymap);

        return "\nType some important things.\n\n" +
                textarea.view() +
                "\n\n" +
                SAVE_TEXT_STYLE.render(saveText) +
                "\n " +
                helpView +
                "\n";
    }

    /**
     * Defines key bindings for save and quit actions.
     */
    private static final class PreventQuitKeyMap implements KeyMap {
        private final Binding save;
        private final Binding quit;

        /**
         * Initializes the save and quit bindings.
         */
        private PreventQuitKeyMap() {
            this.save = new Binding(
                    Binding.withKeys("ctrl+s"),
                    Binding.withHelp("ctrl+s", "save")
            );
            this.quit = new Binding(
                    Binding.withKeys("esc", "ctrl+c"),
                    Binding.withHelp("esc", "quit")
            );
        }

        /**
         * Returns the binding for saving.
         *
         * @return save binding
         */
        public Binding save() {
            return save;
        }

        /**
         * Returns the binding for quitting.
         *
         * @return quit binding
         */
        public Binding quit() {
            return quit;
        }

        /**
         * Provides the short help layout.
         *
         * @return short help bindings
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{save, quit};
        }

        /**
         * Provides the full help layout.
         *
         * @return full help bindings
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{save},
                    new Binding[]{quit}
            };
        }
    }

    /**
     * Runs the prevent-quit example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new PreventQuitExample()).run();
    }
}
