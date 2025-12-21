package com.williamcallahan.tui4j.examples.textinputs;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor.Cursor;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor.CursorMode;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput.EchoMode;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput.TextInput;

import java.util.LinkedList;
import java.util.List;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.batch;
import static com.williamcallahan.tui4j.compat.bubbletea.Command.quit;

/**
 * Example program for text inputs.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/textinputs/TextInputsExample.java
 */
public class TextInputsExample implements Model {

    private final Style focusedStyle = Style.newStyle().foreground(Color.color("205"));
    private final Style blurredStyle = Style.newStyle().foreground(Color.color("239"));
    private final Style cursorStyle = focusedStyle.copy();
    private final Style noStyle = Style.newStyle();
    private final Style helpStyle = blurredStyle.copy();
    private final Style cursorModeHelpStyle = Style.newStyle().foreground(Color.color("244"));

    private String focusedButton;
    private String blurredButton;
    private int focusIndex;
    private TextInput[] inputs;
    private CursorMode cursorMode = CursorMode.Blink;

    public TextInputsExample() {
        this.inputs = new TextInput[3];

        for (int i = 0; i < inputs.length; i++) {
            TextInput textInput = new TextInput();
            textInput.cursor().setStyle(cursorStyle.copy());
            textInput.setCharLimit(32);

            switch (i) {
                case 0:
                    textInput.setPlaceholder("Nickname");
                    textInput.focus();
                    textInput.setPromptStyle(focusedStyle.copy());
                    textInput.setTextStyle(focusedStyle.copy());
                    break;
                case 1:
                    textInput.setPlaceholder("Email");
                    textInput.setCharLimit(64);
                    break;
                case 2:
                    textInput.setPlaceholder("Password");
                    textInput.setEchoMode(EchoMode.EchoPassword);
                    textInput.setEchoCharacter('•');
                    break;
            }
            inputs[i] = textInput;
        }
    }

    @Override
    public Command init() {
        this.focusedButton = focusedStyle.render("[ Submit ]");
        this.blurredButton = "[ %s ]".formatted(blurredStyle.render("Submit"));

        return Cursor::blink;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "ctrl+r":
                    int cursorModeIndex = cursorMode.ordinal();
                    cursorModeIndex++;
                    if (cursorModeIndex > CursorMode.values().length - 1) {
                        cursorModeIndex = 0;
                    }
                    this.cursorMode = CursorMode.fromOrdinal(cursorModeIndex);

                    List<Command> commands = new LinkedList<>();
                    for (TextInput input : inputs) {
                        commands.add(input.cursor().setMode(this.cursorMode));
                    }
                    return UpdateResult.from(this, Command.batch(commands.toArray(new Command[0])));

                case "tab", "shift+tab", "enter", "up", "down":
                    if (key.equals("enter") && focusIndex == inputs.length) {
                        return UpdateResult.from(this, quit());
                    }

                    if ("up".equals(key) || "shift+tab".equals(key)) {
                        this.focusIndex--;
                    } else {
                        this.focusIndex++;
                    }

                    if (focusIndex > inputs.length) {
                        this.focusIndex = 0;
                    } else if (focusIndex < 0) {
                        this.focusIndex = inputs.length;
                    }

                    Command[] cmds = new Command[inputs.length];
                    for (int i = 0; i < inputs.length; i++) {
                        if (i == this.focusIndex) {
                            cmds[i] = inputs[i].focus();
                            inputs[i].setPromptStyle(focusedStyle.copy());
                            inputs[i].setTextStyle(focusedStyle.copy());
                            continue;
                        }
                        inputs[i].blur();
                        inputs[i].setPromptStyle(noStyle.copy());
                        inputs[i].setTextStyle(noStyle.copy());
                    }
                    return UpdateResult.from(this, batch(cmds));
            }
        }
        return UpdateResult.from(this, updateInputs(msg));
    }

    private Command updateInputs(Message msg) {
        Command[] commands = new Command[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            UpdateResult<? extends Model> updateResult = inputs[i].update(msg);
            commands[i] = updateResult.command();
        }
        return batch(commands);
    }

    @Override
    public String view() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputs.length; i++) {
            builder.append(inputs[i].view());
            if (i < inputs.length - 1) {
                builder.append('\n');
            }
        }

        String button = blurredButton;
        if (focusIndex == inputs.length) {
            button = focusedButton;
        }
        return builder
                .append("\n\n%s\n\n".formatted(button))
                .append(helpStyle.render("cursor mode is "))
                .append(cursorModeHelpStyle.render(cursorMode.name().toLowerCase()))
                .append(helpStyle.render(" (ctrl+r to change style)"))
                .toString();
    }

    public static void main(String[] args) {
        new Program(new TextInputsExample()).run();
    }
}
