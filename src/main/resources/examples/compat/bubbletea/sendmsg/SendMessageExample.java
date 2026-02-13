package examples.compat.bubbletea.sendmsg;

import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;
import com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Example program for Sendmsg.
 */
public class SendMessageExample implements Model {

    private static final int MAX_RESULTS = 5;

    private final Spinner spinner;
    private final List<SendMessage> results;
    private boolean quitting;
    private Program program;

    /**
     * Creates SendMessageExample to keep example ready for use.
     */
    public SendMessageExample() {
        this.spinner = new Spinner(SpinnerType.DOT);
        this.results = new ArrayList<>(MAX_RESULTS);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return spinner.init();
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
            quitting = true;
            return new UpdateResult<>(this, QuitMessage::new);
        }

        if (msg instanceof SendMessage result) {
            results.add(result);
            while (results.size() > MAX_RESULTS) {
                results.remove(0);
            }
            return new UpdateResult<>(this, null);
        }

        if (msg instanceof TickMessage) {
            UpdateResult<Spinner> result = spinner.update(msg);
            return UpdateResult.from(this, result.command());
        }

        return new UpdateResult<>(this, null);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();

        if (quitting) {
            sb.append("That's all for today!\n\n");
        } else {
            sb.append(spinner.view()).append(" Eating food...\n\n");
        }

        for (int i = 0; i < results.size(); i++) {
            SendMessage result = results.get(i);
            sb.append(result.toString()).append("\n");
        }

        if (!quitting) {
            sb.append("\nPress any key to exit");
        }

        return sb.toString();
    }

    /**
     * Updates the program.
     *
     * @param program program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    private static final String[] FOODS = {
        "an apple",
        "a pear",
        "a gherkin",
        "a party gherkin",
        "a kohlrabi",
        "some spaghetti",
        "tacos",
        "a currywurst",
        "some curry",
        "a sandwich",
        "some peanut butter",
        "some cashews",
        "some ramen",
    };

    private static final Random RANDOM = new Random();

    /**
     * Handles random food for example.
     *
     * @return result
     */
    private static String randomFood() {
        return FOODS[RANDOM.nextInt(FOODS.length)];
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        SendMessageExample model = new SendMessageExample();
        Program program = new Program(model);
        model.setProgram(program);

        new Thread(() -> {
            boolean started = false;
            while (model.program != null) {
                if (!model.program.isRunning()) {
                    if (started) {
                        break;
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }
                started = true;
                try {
                    int delayMs = RANDOM.nextInt(900) + 100;
                    Thread.sleep(delayMs);
                    if (model.program.isRunning()) {
                        model.program.send(
                            new SendMessage(
                                Duration.ofMillis(delayMs),
                                randomFood()
                            )
                        );
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        })
            .start();

        program.run();
    }
}
