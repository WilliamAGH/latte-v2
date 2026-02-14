package examples.compat.bubbletea.width;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Example program for width.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/width/WidthExample.java
 */
public class WidthExample implements Model {

    private final static Style limitedWidth = Style.newStyle().width(15);
    private final static Style red = Style.newStyle().foreground(Color.color("#ff0000"));
    private final static Style green = Style.newStyle().foreground(Color.color("#00ff00"));

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return QuitMessage::new;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return limitedWidth.render("This is a long text and it %s be wrapped after 15 %s, we will see how it presents ...".formatted(
                red.render("should"),
                green.render("characters")
        ));
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new WidthExample()).run();
    }
}
