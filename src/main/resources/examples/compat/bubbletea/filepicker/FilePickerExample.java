package examples.compat.bubbletea.filepicker;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.filepicker.FilePicker;
import com.williamcallahan.tui4j.compat.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Demonstrates the file picker component with help bindings.
 * Upstream: bubbletea/examples/file-picker/main.go
 */
public class FilePickerExample implements Model {

    private FilePicker filePicker;
    private final Help help;
    private final FilePickerKeyMap keys;
    private String selectedFile;
    private boolean quitting;
    private String errorMessage;

    /**
     * Configures the file picker and help bindings.
     */
    public FilePickerExample() {
        this.filePicker = new FilePicker();
        this.filePicker.setAllowedTypes(".mod", ".sum", ".go", ".txt", ".md");
        this.filePicker.setCurrentDirectory(System.getProperty("user.home"));
        this.filePicker.setShowPermissions(true);
        this.filePicker.setShowSize(true);

        this.help = new Help();
        this.keys = new FilePickerKeyMap();
        this.selectedFile = "";
        this.quitting = false;
        this.errorMessage = null;
    }

    /**
     * Initializes the file picker model.
     *
     * @return file picker init command
     */
    @Override
    public Command init() {
        return filePicker.init();
    }

    /**
     * Handles quit input and forwards updates to the file picker.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage
                && Binding.matches(keyPressMessage, keys.quit())) {
            quitting = true;
            return UpdateResult.from(this, Command.quit());
        }

        UpdateResult<FilePicker> result = filePicker.update(msg);
        filePicker = result.model();

        if (filePicker.didSelectFile(msg)) {
            selectedFile = filePicker.selectedPath();
        }

        return UpdateResult.from(this, result.command());
    }

    /**
     * Renders the picker UI with optional help and selection text.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  ");

        if (errorMessage != null) {
            Style errorStyle = Style.newStyle()
                    .foreground(Color.color("9"))
                    .italic(true);
            sb.append(errorStyle.render(errorMessage));
        } else if (selectedFile.isEmpty()) {
            sb.append("Pick a file:");
        } else {
            Style selectedStyle = filePicker.styles().selected();
            sb.append("Selected file: ").append(selectedStyle.render(selectedFile));
        }

        sb.append("\n\n");
        sb.append(filePicker.view());
        sb.append("\n");

        if (!quitting) {
            sb.append("\n");
            sb.append(help.render(keys));
        }

        return sb.toString();
    }

    /**
     * Runs the file picker example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new FilePickerExample()).run();
    }

    /**
     * Defines key bindings and help layouts for the file picker demo.
     */
    private static final class FilePickerKeyMap implements KeyMap {
        private final Binding up;
        private final Binding down;
        private final Binding home;
        private final Binding end;
        private final Binding pageUp;
        private final Binding pageDown;
        private final Binding open;
        private final Binding back;
        private final Binding select;
        private final Binding toggleHidden;
        private final Binding quit;

        /**
         * Initializes the key bindings used by the example.
         */
        private FilePickerKeyMap() {
            this.up = new Binding(
                    Binding.withKeys("up", "k"),
                    Binding.withHelp("↑/k", "up")
            );
            this.down = new Binding(
                    Binding.withKeys("down", "j"),
                    Binding.withHelp("↓/j", "down")
            );
            this.home = new Binding(
                    Binding.withKeys("home", "g"),
                    Binding.withHelp("g/home", "go to top")
            );
            this.end = new Binding(
                    Binding.withKeys("end", "G"),
                    Binding.withHelp("G/end", "go to bottom")
            );
            this.pageUp = new Binding(
                    Binding.withKeys("pgup", "ctrl+u"),
                    Binding.withHelp("pgup", "page up")
            );
            this.pageDown = new Binding(
                    Binding.withKeys("pgdown", "ctrl+d"),
                    Binding.withHelp("pgdn", "page down")
            );
            this.open = new Binding(
                    Binding.withKeys("right", "l", "enter"),
                    Binding.withHelp("→/l/enter", "open")
            );
            this.back = new Binding(
                    Binding.withKeys("left", "h", "backspace"),
                    Binding.withHelp("←/h/back", "back")
            );
            this.select = new Binding(
                    Binding.withKeys("ctrl+s"),
                    Binding.withHelp("ctrl+s", "select")
            );
            this.toggleHidden = new Binding(
                    Binding.withKeys("."),
                    Binding.withHelp(".", "toggle hidden files")
            );
            this.quit = new Binding(
                    Binding.withKeys("q", "esc", "ctrl+c"),
                    Binding.withHelp("q", "quit")
            );
        }

        /**
         * Returns the binding for quitting the demo.
         *
         * @return quit binding
         */
        private Binding quit() {
            return quit;
        }

        /**
         * Provides the condensed help bindings.
         *
         * @return short help bindings
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{open, back, select, toggleHidden, quit};
        }

        /**
         * Provides the full help layout for the demo.
         *
         * @return full help bindings
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{up, down, home, end},
                    new Binding[]{pageUp, pageDown},
                    new Binding[]{open, back, select},
                    new Binding[]{toggleHidden, quit}
            };
        }
    }
}
