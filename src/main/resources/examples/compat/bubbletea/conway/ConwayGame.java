package examples.compat.bubbletea.conway;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.checkWindowSize;

/**
 * Wraps the Conway model to size it to the current terminal.
 * Upstream: bubbletea/examples/conway/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/conway/ConwayGame.java
 */
public class ConwayGame implements Model {
    private Conway conway;

    /**
     * Requests the terminal size before starting the simulation.
     *
     * @return window size command
     */
    @Override
    public Command init() {
        return checkWindowSize();
    }

    /**
     * Sizes the game on first window size and forwards subsequent updates.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.conway = Conway.createRandom(
                    windowSizeMessage.width(),
                    windowSizeMessage.height(),
                    0.5
            );
            return UpdateResult.from(this, conway.init());
        }
        if (conway != null) {
            UpdateResult<? extends Model> updateResult = conway.update(msg);
            return UpdateResult.from(this, updateResult.command());
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the Conway model once it is initialized.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (conway != null) {
            return conway.view();
        }
        return "";
    }

    /**
     * Runs the Conway example in the alternate screen.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ConwayGame())
                .withAltScreen()
                .run();
    }
}
