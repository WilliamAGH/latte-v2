package examples.compat.bubbletea.focusblur;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.BlurMessage;
import com.williamcallahan.tui4j.compat.bubbletea.FocusMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Demonstrates focus/blur reporting events.
 * Upstream: bubbletea/examples/focus-blur/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/focusblur/FocusBlurExample.java
 */
public class FocusBlurExample implements Model {

    private boolean focused = true;
    private boolean reporting = true;

    /**
     * Starts with no initial command for the focus demo.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Tracks focus events and toggles reporting state.
     *
     * @param msg incoming message
     * @return next model state and command
     */
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

    /**
     * Renders the focus status and help text.
     *
     * @return rendered view
     */
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

    /**
     * Runs the focus/blur example with focus reporting enabled.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new FocusBlurExample())
                .withReportFocus()
                .run();
    }
}
