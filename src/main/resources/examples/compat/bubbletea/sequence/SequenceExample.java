package examples.compat.bubbletea.sequence;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.println;
import static com.williamcallahan.tui4j.compat.bubbletea.Command.sequence;

// TODO nested sequences are not supported yet!
/**
 * Example program for sequence.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/sequence/SequenceExample.java
 */
public class SequenceExample implements Model {

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return sequence(
                sequence(
                        println("A"),
                        println("B"),
                        println("C")
                ),
                println("X"),
                println("Z"),
                QuitMessage::new
        );
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
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
        return "";
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SequenceExample()).run();
    }
}
