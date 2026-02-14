package examples.compat.bubbletea.realtime;

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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Example program for Realtime.
 */
public class RealtimeExample implements Model {

    private final Spinner spinner;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Random random = new Random();
    private int eventCount = 0;
    private boolean quitting = false;
    private Program program;

    /**
     * Creates RealtimeExample to keep example ready for use.
     */
    public RealtimeExample() {
        this.spinner = new Spinner(SpinnerType.DOT);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (running.get()) {
                try {
                    int delay = random.nextInt(901) + 100;
                    Thread.sleep(delay);
                    if (running.get() && program != null) {
                        program.send(RealtimeMessage.ACTIVITY);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        executor.shutdown();

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
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("q".equals(key) || "Q".equals(key)) {
                quitting = true;
                running.set(false);
                return new UpdateResult<>(this, QuitMessage::new);
            }
        }

        if (msg == RealtimeMessage.ACTIVITY) {
            eventCount++;
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
        String spinnerView = spinner.view();
        String content = String.format(
            "  %s Events received: %d",
            spinnerView,
            eventCount
        );

        if (quitting) {
            return content + "\n\n";
        }
        return content + "\n\n  Press q to exit\n\n";
    }

    /**
     * Updates the program.
     *
     * @param program program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        RealtimeExample model = new RealtimeExample();
        Program program = new Program(model);
        model.setProgram(program);
        program.run();
    }
}
