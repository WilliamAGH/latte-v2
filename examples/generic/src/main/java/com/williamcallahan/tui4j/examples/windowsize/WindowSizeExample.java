package com.williamcallahan.tui4j.examples.windowsize;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.message.CheckWindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.printf;

/**
 * Example program for window size.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/windowsize/WindowSizeExample.java
 */
public class WindowSizeExample implements Model {
    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key().equals("q")) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this, CheckWindowSizeMessage::new);
        }
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            return UpdateResult.from(this, printf("%dx%d", windowSizeMessage.width(), windowSizeMessage.height()));
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "When you're done press q to quit. Press any other key to query the window-size.\n";
    }

    public static void main(String[] args) {
        new Program(new WindowSizeExample()).run();
    }
}
