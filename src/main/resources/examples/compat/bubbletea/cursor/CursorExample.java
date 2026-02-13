package examples.compat.bubbletea.cursor;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.cursor.Cursor;

/**
 * Demonstrates the cursor component updating alongside the view.
 * Upstream: bubbletea/examples/cursor/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/cursor/CursorExample.java
 */
public class CursorExample implements Model {

    private final Cursor cursor;

    /**
     * Initializes the cursor with a blank character.
     */
    public CursorExample() {
        this.cursor = new Cursor();
        cursor.setChar(" ");
    }

    /**
     * Starts the cursor blinking behavior.
     *
     * @return cursor init command
     */
    @Override
    public Command init() {
        return cursor.init();
    }

    /**
     * Quits on keypress and otherwise forwards messages to the cursor.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }

        UpdateResult<? extends Model> updateResult = cursor.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the cursor view alongside a static label.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "This cursor is not real... " + cursor.view();
    }

    /**
     * Runs the cursor example with focus reporting.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new CursorExample())
                .withAltScreen()
                .withReportFocus()
                .run();
    }
}
