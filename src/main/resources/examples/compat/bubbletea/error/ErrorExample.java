package examples.compat.bubbletea.error;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

/**
 * Demonstrates how program failures surface as errors.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/error/ErrorExample.java
 */
public class ErrorExample implements Model {

    /**
     * Starts with a command that throws to illustrate error handling.
     *
     * @return command that fails immediately
     */
    @Override
    public Command init() {
        return this::failingCommand;
    }

    /**
     * Throws a runtime exception to simulate a failing command.
     *
     * @return never returns normally
     */
    private Message failingCommand() {
        throw new RuntimeException("Error!");
    }

    /**
     * Leaves the model unchanged after the failure.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    /**
     * Renders a placeholder view once the program is running.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "Error?";
    }

    /**
     * Runs the error example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ErrorExample()).run();
    }
}
