package examples.compat.bubbletea.creditcardform;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.textinput.TextInput;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.quit;

/**
 * Demonstrates a multi-field credit card form with focus navigation.
 * Upstream: bubbletea/examples/credit-card-form/main.go
 */
public class CreditCardFormExample implements Model {

    private static final int CCN = 0;
    private static final int EXP = 1;
    private static final int CVV = 2;

    private static final String COLOR_INPUT = "#FF06B7";
    private static final String COLOR_CONTINUE = "#767676";

    private final Style inputStyle = Style.newStyle().foreground(Color.color(COLOR_INPUT));
    private final Style continueStyle = Style.newStyle().foreground(Color.color(COLOR_CONTINUE));

    private final TextInput[] inputs;
    private int focused;
    private String focusedButton;
    private String blurredButton;

    /**
     * Configures the input fields and sets initial focus.
     */
    public CreditCardFormExample() {
        this.inputs = new TextInput[3];

        TextInput ccnInput = new TextInput();
        ccnInput.setPlaceholder("4505 **** **** 1234");
        ccnInput.focus();
        ccnInput.setCharLimit(20);
        ccnInput.setWidth(30);
        ccnInput.setPrompt("");
        inputs[CCN] = ccnInput;

        TextInput expInput = new TextInput();
        expInput.setPlaceholder("MM/YY ");
        expInput.setCharLimit(5);
        expInput.setWidth(5);
        expInput.setPrompt("");
        inputs[EXP] = expInput;

        TextInput cvvInput = new TextInput();
        cvvInput.setPlaceholder("XXX");
        cvvInput.setCharLimit(3);
        cvvInput.setWidth(5);
        cvvInput.setPrompt("");
        inputs[CVV] = cvvInput;

        this.focused = CCN;
    }

    /**
     * Prepares button styles and starts the blinking cursor.
     *
     * @return blink command
     */
    @Override
    public Command init() {
        this.focusedButton = continueStyle.copy().bold(true).render("[ Continue -> ]");
        this.blurredButton = continueStyle.render("[ Continue -> ]");
        return TextInput::blink;
    }

    /**
     * Handles focus traversal and forwards input to each field.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "enter":
                    if (focused == inputs.length) {
                        return UpdateResult.from(this, quit());
                    }
                    nextInput();
                    break;
                case "ctrl+c", "esc":
                    return UpdateResult.from(this, QuitMessage::new);
                case "shift+tab", "ctrl+p":
                    prevInput();
                    break;
                case "tab", "ctrl+n":
                    nextInput();
                    break;
            }
        }

        Command[] cmds = new Command[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            UpdateResult<? extends Model> result = inputs[i].update(msg);
            cmds[i] = result.command();
        }
        return UpdateResult.from(this, Command.batch(cmds));
    }

    /**
     * Moves focus to the next input field.
     */
    private void nextInput() {
        focused = (focused + 1) % (inputs.length + 1);
        applyFocus();
    }

    /**
     * Moves focus to the previous input field.
     */
    private void prevInput() {
        focused = (focused - 1 + inputs.length + 1) % (inputs.length + 1);
        applyFocus();
    }

    private void applyFocus() {
        for (int i = 0; i < inputs.length; i++) {
            if (i == focused) {
                inputs[i].focus();
            } else {
                inputs[i].blur();
            }
        }
    }

    /**
     * Renders the form fields and the continue button.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder builder = new StringBuilder();
        builder.append(" Total: $21.50:\n\n");
        builder.append(inputStyle.width(30).render("Card Number")).append("\n");
        builder.append(inputs[CCN].view()).append("\n\n");
        builder.append(inputStyle.width(6).render("EXP")).append("  ");
        builder.append(inputStyle.width(6).render("CVV")).append("\n");
        builder.append(inputs[EXP].view()).append("  ");
        builder.append(inputs[CVV].view()).append("\n\n");

        String button = blurredButton;
        if (focused == inputs.length) {
            button = focusedButton;
        }
        builder.append(button).append("\n");

        return builder.toString();
    }

    /**
     * Runs the credit card form example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new CreditCardFormExample()).run();
    }
}
