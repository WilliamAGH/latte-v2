package com.williamcallahan.tui4j.examples.focusblur;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.BlurMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.FocusMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

/**
 * Example program for focus blur.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/focusblur/FocusBlurExample.java
 */
public class FocusBlurExample implements Model {

    private boolean focused = true;
    private boolean reporting = true;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof FocusMessage) {
            this.focused = true;
        } else if (msg instanceof BlurMessage) {
            this.focused = false;
        } else if (msg instanceof KeyPressMessage keyPress) {

            switch (keyPress.key()) {
                case "t":
                    this.reporting = !this.reporting;
                    break;
                case "q":
                    return UpdateResult.from(this, QuitMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder("Hi. Focus report is currently ");
        if (this.reporting) {
            buffer.append("enabled");
        } else {
            buffer.append("disabled");
        }
        buffer.append(".\n\n");

        if (reporting) {
            if (focused) {
                buffer.append("This program is currently focused!");
            } else {
                buffer.append("This program is currently blurred!");
            }
        }

        buffer.append("\n\nTo quit sooner press ctrl-c or q, or t to toggle focus reporting...\n");

        return buffer.toString();
    }

    public static void main(String[] args) {
        new Program(new FocusBlurExample())
                .withReportFocus()
                .run();
    }
}
