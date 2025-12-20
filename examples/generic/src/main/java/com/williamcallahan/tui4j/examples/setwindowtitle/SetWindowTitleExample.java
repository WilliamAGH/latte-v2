package com.williamcallahan.tui4j.examples.setwindowtitle;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

import static com.williamcallahan.tui4j.Command.setWidowTitle;

/**
 * Example program for set window title.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/setwindowtitle/SetWindowTitleExample.java
 */
public class SetWindowTitleExample implements Model {

    @Override
    public Command init() {
        return setWidowTitle("tui4j Example");
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "\nPress any key to quit.";
    }

    public static void main(String[] args) {
        new Program(new SetWindowTitleExample()).run();
    }
}
