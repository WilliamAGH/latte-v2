package examples.compat.bubbletea.split;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.textarea.Textarea;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases.KeyAlias;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.util.ArrayList;
import java.util.List;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.batch;

/**
 * Example program for Split.
 */
public class SplitEditorsExample implements Model {

    private static final int INITIAL_INPUTS = 2;
    private static final int MAX_INPUTS = 6;
    private static final int MIN_INPUTS = 1;
    private static final int HELP_HEIGHT = 5;

    private static final Style CURSOR_STYLE = Style.newStyle()
            .foreground(new AdaptiveColor("212", "212"));

    private static final Style CURSOR_LINE_STYLE = Style.newStyle()
            .background(new AdaptiveColor("57", "57"))
            .foreground(new AdaptiveColor("230", "230"));

    private static final Style PLACEHOLDER_STYLE = Style.newStyle()
            .foreground(new AdaptiveColor("238", "238"));

    private static final Style END_OF_BUFFER_STYLE = Style.newStyle()
            .foreground(new AdaptiveColor("235", "235"));

    private static final Style FOCUSED_PLACEHOLDER_STYLE = Style.newStyle()
            .foreground(new AdaptiveColor("99", "99"));

    private static final Style FOCUSED_BORDER_STYLE = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(new AdaptiveColor("238", "238"));

    private static final Style BLURRED_BORDER_STYLE = Style.newStyle()
            .border(StandardBorder.HiddenBorder);

    private int width;
    private int height;
    private List<Textarea> inputs;
    private int focus;
    private boolean enableAdd;
    private boolean enableRemove;

    /**
     * Creates SplitEditorsExample to keep example ready for use.
     */
    public SplitEditorsExample() {
        this.inputs = new ArrayList<>();
        this.width = 80;
        this.height = 24;
        this.focus = 0;

        for (int i = 0; i < INITIAL_INPUTS; i++) {
            inputs.add(createTextarea());
        }

        inputs.get(focus).focus();
        updateKeybindings();
        sizeInputs();
    }

    /**
     * Creates textarea for example.
     *
     * @return result
     */
    private Textarea createTextarea() {
        Textarea t = new Textarea();
        t.setPrompt("");
        t.setPlaceholder("Type something");
        t.setShowLineNumbers(true);
        t.cursor().setStyle(CURSOR_STYLE);
        t.focusedStyle().placeholder(FOCUSED_PLACEHOLDER_STYLE);
        t.blurredStyle().placeholder(PLACEHOLDER_STYLE);
        t.focusedStyle().cursorLine(CURSOR_LINE_STYLE);
        t.focusedStyle().base(FOCUSED_BORDER_STYLE);
        t.blurredStyle().base(BLURRED_BORDER_STYLE);
        t.focusedStyle().endOfBuffer(END_OF_BUFFER_STYLE);
        t.blurredStyle().endOfBuffer(END_OF_BUFFER_STYLE);
        t.blur();
        return t;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return inputs.get(0).cursor().blinkCommand();
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        List<Command> commands = new ArrayList<>();

        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            KeyType keyType = keyPressMessage.type();

            if (KeyAliases.getKeyType(KeyAlias.KeyEsc) == keyType
                    || KeyAliases.getKeyType(KeyAlias.KeyCtrlC) == keyType) {
                for (Textarea input : inputs) {
                    input.blur();
                }
                return UpdateResult.from(this, QuitMessage::new);
            }

            if ("tab".equals(key)) {
                inputs.get(focus).blur();
                focus++;
                if (focus > inputs.size() - 1) {
                    focus = 0;
                }
                inputs.get(focus).focus();
                return UpdateResult.from(this);
            }

            if ("shift+tab".equals(key)) {
                inputs.get(focus).blur();
                focus--;
                if (focus < 0) {
                    focus = inputs.size() - 1;
                }
                inputs.get(focus).focus();
                return UpdateResult.from(this);
            }

            if (KeyAliases.getKeyType(KeyAlias.KeyCtrlN) == keyType
                    && inputs.size() < MAX_INPUTS) {
                inputs.add(createTextarea());
                inputs.get(focus).blur();
                focus = inputs.size() - 1;
                inputs.get(focus).focus();
                updateKeybindings();
                sizeInputs();
                return UpdateResult.from(this);
            }

            if (KeyAliases.getKeyType(KeyAlias.KeyCtrlW) == keyType
                    && inputs.size() > MIN_INPUTS) {
                inputs.get(focus).blur();
                inputs.remove(inputs.size() - 1);
                if (focus > inputs.size() - 1) {
                    focus = inputs.size() - 1;
                }
                inputs.get(focus).focus();
                updateKeybindings();
                sizeInputs();
                return UpdateResult.from(this);
            }
        }

        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.width = windowSizeMessage.width();
            this.height = windowSizeMessage.height();
            sizeInputs();
        }

        updateKeybindings();

        for (int i = 0; i < inputs.size(); i++) {
            UpdateResult<? extends Model> result = inputs.get(i).update(msg);
            commands.add(result.command());
        }

        return UpdateResult.from(this, batch(commands.toArray(new Command[0])));
    }

    /**
     * Handles size inputs for example.
     */
    private void sizeInputs() {
        int editorWidth = Math.max(1, width / inputs.size());
        int editorHeight = Math.max(1, height - HELP_HEIGHT);
        for (Textarea input : inputs) {
            input.setWidth(editorWidth);
            input.setHeight(editorHeight);
        }
    }

    /**
     * Handles update keybindings for example.
     */
    private void updateKeybindings() {
        this.enableAdd = inputs.size() < MAX_INPUTS;
        this.enableRemove = inputs.size() > MIN_INPUTS;
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();

        List<String> views = new ArrayList<>();
        for (Textarea input : inputs) {
            views.add(input.view());
        }
        sb.append(joinHorizontal(views));

        sb.append("\n\n");
        sb.append(helpView());

        return sb.toString();
    }

    /**
     * Handles join horizontal for example.
     *
     * @param views views
     * @return result
     */
    private String joinHorizontal(List<String> views) {
        if (views.isEmpty()) {
            return "";
        }
        if (views.size() == 1) {
            return views.get(0);
        }

        String[][] lines = new String[views.size()][];
        for (int i = 0; i < views.size(); i++) {
            lines[i] = views.get(i).split("\n", -1);
        }

        int maxLines = 0;
        for (String[] viewLines : lines) {
            maxLines = Math.max(maxLines, viewLines.length);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            for (int j = 0; j < lines.length; j++) {
                if (j > 0) {
                    sb.append("");
                }
                if (i < lines[j].length) {
                    sb.append(lines[j][i]);
                } else {
                    sb.append(" ".repeat(width / inputs.size()));
                }
            }
            if (i < maxLines - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Handles help view for example.
     *
     * @return result
     */
    private String helpView() {
        StringBuilder sb = new StringBuilder();

        Style helpStyle = Style.newStyle().foreground(new AdaptiveColor("245", "7"));
        Style keyStyle = Style.newStyle().foreground(new AdaptiveColor("250", "15"));
        Style descStyle = Style.newStyle().foreground(new AdaptiveColor("240", "11"));

        sb.append(helpStyle.render("[" + keyStyle.render("tab") + descStyle.render("] next editor  ")));
        sb.append(helpStyle.render("[" + keyStyle.render("shift+tab") + descStyle.render("] prev editor  ")));
        if (enableAdd) {
            sb.append(helpStyle.render("[" + keyStyle.render("ctrl+n") + descStyle.render("] add editor  ")));
        } else {
            sb.append(helpStyle.render("[" + keyStyle.render("ctrl+n") + descStyle.render("] add editor (disabled)  ")));
        }
        if (enableRemove) {
            sb.append(helpStyle.render("[" + keyStyle.render("ctrl+w") + descStyle.render("] remove editor  ")));
        } else {
            sb.append(helpStyle.render("[" + keyStyle.render("ctrl+w") + descStyle.render("] remove editor (disabled)  ")));
        }
        sb.append(helpStyle.render("[" + keyStyle.render("esc") + descStyle.render("] quit  ")));

        return sb.toString();
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SplitEditorsExample()).run();
    }

    /**
     * Support type for the Split example.
     */
    public static class WindowSizeMessage implements Message {
        private final int width;
        private final int height;

        /**
         * Creates WindowSizeMessage to keep example ready for use.
         *
         * @param width width
         * @param height height
         */
        public WindowSizeMessage(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Handles width for example.
         *
         * @return result
         */
        public int width() {
            return width;
        }

        /**
         * Handles height for example.
         *
         * @return result
         */
        public int height() {
            return height;
        }
    }
}
