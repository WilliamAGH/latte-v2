package examples.compat.bubbletea.suspend;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.ResumeMessage;

/**
 * Example program for Suspend.
 */
public class SuspendExample implements Model {
    private boolean quitting;
    private boolean suspending;

    /**
     * Creates SuspendExample to keep example ready for use.
     */
    public SuspendExample() {
        this.quitting = false;
        this.suspending = false;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof ResumeMessage) {
            suspending = false;
            return UpdateResult.from(this);
        }
        if (msg instanceof KeyPressMessage keyPressMessage) {
            switch (keyPressMessage.key()) {
                case "q", "esc":
                    quitting = true;
                    return new UpdateResult<>(this, QuitMessage::new);
                case "ctrl+c":
                    quitting = true;
                    return new UpdateResult<>(this, QuitMessage::new);
                case "ctrl+z":
                    suspending = true;
                    return new UpdateResult<>(this, com.williamcallahan.tui4j.compat.bubbletea.SuspendMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (suspending || quitting) {
            return "";
        }
        return "\nPress ctrl-z to suspend, ctrl+c to interrupt, q, or esc to exit\n";
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SuspendExample()).run();
    }
}
