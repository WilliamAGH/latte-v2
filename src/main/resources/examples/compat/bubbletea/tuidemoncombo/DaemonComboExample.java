package examples.compat.bubbletea.tuidemoncombo;

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
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example program for Tuidemoncombo.
 */
public class DaemonComboExample implements Model {

    private static final Logger logger = Logger.getLogger(
        DaemonComboExample.class.getName()
    );

    private final Spinner spinner;
    private final Deque<Result> results;
    private final Random random;
    private boolean quitting;

    private static final int SHOW_LAST_RESULTS = 5;
    private static final Style HELP_STYLE = Style.newStyle().foreground(
        Color.color("241")
    );
    private static final Style MAIN_STYLE = Style.newStyle().marginLeft(1);

    /**
     * Creates DaemonComboExample to keep example ready for use.
     */
    public DaemonComboExample() {
        this.spinner = new Spinner(SpinnerType.DOT).setStyle(
            Style.newStyle().foreground(Color.color("206"))
        );
        this.results = new ArrayDeque<>(SHOW_LAST_RESULTS + 1);
        this.random = new Random();
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        logger.info("Starting work...");
        return Command.batch(spinner.init(), runPretendProcess());
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

        if (msg instanceof ProcessFinishedMessage processFinishedMessage) {
            Duration duration = processFinishedMessage.duration();
            Result res = new Result(randomEmoji(), duration);
            logger.info(res.emoji() + " Job finished in " + res.duration());

            results.addLast(res);
            if (results.size() > SHOW_LAST_RESULTS) {
                results.removeFirst();
            }

            return UpdateResult.from(this, runPretendProcess());
        }

        if (msg instanceof TickMessage) {
            UpdateResult<? extends Model> updateResult = spinner.update(msg);
            return UpdateResult.from(this, updateResult.command());
        }

        return UpdateResult.from(this, null);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb
            .append("\n")
            .append(spinner.view())
            .append(" Doing some work...\n\n");

        for (Result res : results) {
            sb
                .append(res.emoji())
                .append(" Job finished in ")
                .append(res.duration())
                .append("\n");
        }

        for (int i = results.size(); i < SHOW_LAST_RESULTS; i++) {
            sb.append("........................\n");
        }

        sb.append("\n");
        sb.append(HELP_STYLE.render("Press any key to exit\n"));

        if (quitting) {
            sb.append("\n");
        }

        return MAIN_STYLE.render(sb.toString());
    }

    /**
     * Handles run pretend process for example.
     *
     * @return result
     */
    private Command runPretendProcess() {
        return () -> {
            try {
                Duration pause = Duration.ofMillis(100 + random.nextInt(900));
                Thread.sleep(pause.toMillis());
                return new ProcessFinishedMessage(pause);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        };
    }

    /**
     * Handles random emoji for example.
     *
     * @return result
     */
    private String randomEmoji() {
        String[] emojis = {
            "🍦",
            "🧋",
            "🍡",
            "🤠",
            "👾",
            "😭",
            "🦊",
            "🐯",
            "🦆",
            "🥨",
            "🎏",
            "🍔",
            "🍒",
            "🍥",
            "🎮",
            "📦",
            "🦁",
            "🐶",
            "🐸",
            "🍕",
            "🥐",
            "🧲",
            "🚒",
            "🥇",
            "🏆",
            "🌽",
        };
        return emojis[random.nextInt(emojis.length)];
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        boolean daemonMode = Arrays.asList(args).contains("--daemon");

        if (daemonMode) {
            runDaemon();
        } else {
            runTUI();
        }
    }

    /**
     * Handles run tui for example.
     */
    private static void runTUI() {
        disableLoggingForTUI();
        Program program = new Program(new DaemonComboExample());
        try {
            program.run();
        } catch (Exception e) {
            System.err.println("Error starting program: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handles run daemon for example.
     */
    private static void runDaemon() {
        System.out.println("Running in daemon mode (headless)...");
        Random random = new Random();
        try {
            while (true) {
                Duration pause = Duration.ofMillis(100 + random.nextInt(900));
                Thread.sleep(pause.toMillis());
                String emoji = getRandomEmoji(random);
                System.out.println(emoji + " Job finished in " + pause);
            }
        } catch (InterruptedException e) {
            System.out.println("Daemon stopped.");
        }
    }

    /**
     * Returns the random emoji.
     *
     * @param random random
     * @return result
     */
    private static String getRandomEmoji(Random random) {
        String[] emojis = {
            "🍦",
            "🧋",
            "🍡",
            "🤠",
            "👾",
            "😭",
            "🦊",
            "🐯",
            "🦆",
            "🥨",
            "🎏",
            "🍔",
            "🍒",
            "🍥",
            "🎮",
            "📦",
            "🦁",
            "🐶",
            "🐸",
            "🍕",
            "🥐",
            "🧲",
            "🚒",
            "🥇",
            "🏆",
            "🌽",
        };
        return emojis[random.nextInt(emojis.length)];
    }

    /**
     * Handles disable logging for tui for example.
     */
    private static void disableLoggingForTUI() {
        Logger rootLogger = Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.WARNING);
        }
        Logger.getLogger(DaemonComboExample.class.getName()).setLevel(
            Level.INFO
        );
    }

    /**
     * Support type for the Tuidemoncombo example.
     */
    private record Result(String emoji, Duration duration) {}

    /**
     * Support type for the Tuidemoncombo example.
     */
    private record ProcessFinishedMessage(
        Duration duration
    ) implements Message {}
}
