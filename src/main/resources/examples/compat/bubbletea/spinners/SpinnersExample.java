package examples.compat.bubbletea.spinners;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Example program for Spinners.
 */
public class SpinnersExample implements Model {

    private static final int TICK_INTERVAL_MS = 100;

    private final List<Spinner> spinners;
    private final Style spinnerStyle;
    private final Style nameStyle;
    private final Style containerStyle;
    private int tickTag;

    /**
     * Creates SpinnersExample to keep example ready for use.
     */
    public SpinnersExample() {
        this.spinners = new ArrayList<>();
        this.tickTag = 0;

        this.spinnerStyle = Style.newStyle()
                .foreground(Color.color("69"));

        this.nameStyle = Style.newStyle()
                .foreground(Color.color("252"));

        this.containerStyle = Style.newStyle()
                .paddingTop(1)
                .paddingBottom(1)
                .paddingLeft(2)
                .paddingRight(2);

        for (SpinnerType type : SpinnerType.values()) {
            Spinner spinner = new Spinner(type);
            spinner.setStyle(spinnerStyle.copy());
            spinners.add(spinner);
        }
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return this::tick;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if (key.equals("ctrl+c") || key.equals("q")) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
            return new UpdateResult<>(this, null);
        }

        if (msg instanceof TickMessage) {
            tickTag++;
            for (int i = 0; i < spinners.size(); i++) {
                Spinner spinner = spinners.get(i);
                UpdateResult<? extends Model> result = spinner.update(msg);
                spinners.set(i, (Spinner) result.model());
            }
            return new UpdateResult<>(this, Command.tick(Duration.ofMillis(TICK_INTERVAL_MS), this::createTickMessage));
        }

        return new UpdateResult<>(this, null);
    }

    /**
     * Creates tick message for example.
     *
     * @param time time
     * @return result
     */
    private Message createTickMessage(LocalDateTime time) {
        return new TickMessage(time, tickTag, 0);
    }

    /**
     * Handles tick for example.
     *
     * @return result
     */
    private Message tick() {
        return createTickMessage(LocalDateTime.now());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();

        int cols = 4;
        int rows = (int) Math.ceil((double) spinners.size() / cols);

        for (int row = 0; row < rows; row++) {
            StringBuilder rowBuilder = new StringBuilder();
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                if (index >= spinners.size()) {
                    break;
                }
                Spinner spinner = spinners.get(index);
                String name = SpinnerType.values()[index].name();
                String cell = nameStyle.render(centerString(name, 12)) + " " + spinner.view();
                rowBuilder.append(cell);
                if (col < cols - 1) {
                    rowBuilder.append("  ");
                }
            }
            sb.append(containerStyle.render(rowBuilder.toString())).append("\n");
        }

        sb.append("\n");
        sb.append(nameStyle.render("Press q or ctrl+c to quit\n"));

        return sb.toString();
    }

    /**
     * Handles center string for example.
     *
     * @param text text
     * @param width width
     * @return result
     */
    private String centerString(String text, int width) {
        int textWidth = stringWidth(text);
        int padding = (width - textWidth) / 2;
        int leftPad = padding;
        int rightPad = width - textWidth - leftPad;
        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    /**
     * Handles string width for example.
     *
     * @param s s
     * @return result
     */
    private int stringWidth(String s) {
        return com.williamcallahan.tui4j.ansi.TextWidth.measureCellWidth(s);
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new SpinnersExample()).run();
    }
}
