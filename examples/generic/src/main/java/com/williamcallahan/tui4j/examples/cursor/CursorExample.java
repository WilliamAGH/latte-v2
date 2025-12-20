package com.williamcallahan.tui4j.examples.cursor;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor.Cursor;

/**
 * Example program for cursor.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/cursor/CursorExample.java
 */
public class CursorExample implements Model {

    private final Cursor cursor;

    public CursorExample() {
        this.cursor = new Cursor();
        cursor.setChar(" ");
    }

    @Override
    public Command init() {
        return cursor.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }

        UpdateResult<? extends Model> updateResult = cursor.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        return "This cursor is not real... " + cursor.view();
    }

    public static void main(String[] args) {
        new Program(new CursorExample())
                .withAltScreen()
                .withReportFocus()
                .run();
    }
}
