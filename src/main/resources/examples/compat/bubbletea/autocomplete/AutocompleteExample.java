package examples.compat.bubbletea.autocomplete;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;


/**
 * Example program demonstrating text input suggestions with autocomplete keys.
 */
public class AutocompleteExample implements Model {

    private final TextInput textInput;
    private final Help help;
    private final AutocompleteKeyMap keys;
    private boolean quitting;

    /**
     * Configures the text input prompt, suggestions, and help bindings.
     */
    public AutocompleteExample() {
        this.textInput = new TextInput();
        textInput.setPlaceholder("repository");
        textInput.setPrompt("charmbracelet/");
        textInput.setShowSuggestions(true);
        textInput.setCharLimit(50);
        textInput.setWidth(20);
        textInput.focus();

        Style promptStyle = Style.newStyle().foreground(Color.color("63"));
        textInput.setPromptStyle(promptStyle);

        this.help = new Help();
        this.keys = new AutocompleteKeyMap();
        this.quitting = false;
    }

    /**
     * Seeds the suggestion list shown by the text input.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        String[] sampleRepos = {
            "bubbletea",
            "glow",
            "harmony",
            "kelp",
            "vhs",
            "glamour",
            "tree",
            "soft-serve",
            "wish",
            "neat"
        };
        textInput.setSuggestions(sampleRepos);
        return null;
    }

    /**
     * Handles quit and completion keys, then delegates to the text input.
     *
     * @param msg incoming message
     * @return updated model and optional command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.quit())) {
                quitting = true;
                return UpdateResult.from(this, Command.quit());
            }
            if (Binding.matches(keyPressMessage, keys.complete())) {
                if (!textInput.isEmpty()) {
                    String suggestion = textInput.currentSuggestion();
                    if (!suggestion.isEmpty()) {
                        textInput.setValue(suggestion);
                    }
                }
                return UpdateResult.from(this);
            }
        }

        UpdateResult<TextInput> result = textInput.update(msg);
        return UpdateResult.from(this, result.command());
    }

    /**
     * Renders the prompt, input field, and contextual help.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pick a Charm repo:\n\n");
        sb.append("  ").append(textInput.view()).append("\n\n");

        if (!quitting) {
            sb.append(help.render(keys));
        }

        return sb.toString();
    }

    /**
     * Runs the autocomplete example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new AutocompleteExample()).run();
    }

    /**
     * Defines key bindings used by the autocomplete demo.
     */
    private static final class AutocompleteKeyMap implements KeyMap {
        private final Binding complete;
        private final Binding next;
        private final Binding prev;
        private final Binding quit;

        /**
         * Initializes key bindings for completion, navigation, and quit.
         */
        private AutocompleteKeyMap() {
            this.complete = new Binding(
                    Binding.withKeys("tab"),
                    Binding.withHelp("tab", "complete")
            );
            this.next = new Binding(
                    Binding.withKeys("ctrl+n"),
                    Binding.withHelp("ctrl+n", "next")
            );
            this.prev = new Binding(
                    Binding.withKeys("ctrl+p"),
                    Binding.withHelp("ctrl+p", "prev")
            );
            this.quit = new Binding(
                    Binding.withKeys("esc", "ctrl+c"),
                    Binding.withHelp("esc", "quit")
            );
        }

        /**
         * Returns the completion binding used for suggestions.
         *
         * @return completion binding
         */
        private Binding complete() {
            return complete;
        }

        /**
         * Returns the quit binding for the demo.
         *
         * @return quit binding
         */
        private Binding quit() {
            return quit;
        }

        /**
         * Provides the short help layout for the help component.
         *
         * @return short help bindings
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{complete, next, prev, quit};
        }

        /**
         * Provides the full help layout for the help component.
         *
         * @return full help bindings
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{shortHelp()};
        }
    }
}
