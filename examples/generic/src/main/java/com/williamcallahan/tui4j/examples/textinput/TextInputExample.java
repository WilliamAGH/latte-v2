package com.williamcallahan.tui4j.examples.textinput;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases.KeyAlias;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput.TextInput;

/**
 * Example program for text input.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/textinput/TextInputExample.java
 */
public class TextInputExample implements Model {

    private TextInput textInput;

    public TextInputExample() {
        this.textInput = new TextInput();
        textInput.setPlaceholder("Pikachu");
        textInput.focus();
        textInput.setCharLimit(156);
        textInput.setWidth(20);
    }

    @Override
    public Command init() {
        return TextInput::blink;
    }

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

    @Override
    public String view() {
        return "What’s your favorite Pokémon?\n\n%s\n\n%s\n".formatted(
                textInput.view(),
                "(esc to quit)"
        );
    }

    public static void main(String[] args) {
        new Program(new TextInputExample()).run();
    }
}
