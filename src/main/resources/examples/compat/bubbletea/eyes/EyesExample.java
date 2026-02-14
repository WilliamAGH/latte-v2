package examples.compat.bubbletea.eyes;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Blinking eyes animation example.
 * Upstream: bubbletea/examples/eyes/main.go.
 */
public class EyesExample implements Model {

    private static final int EYE_WIDTH = 15;
    private static final int EYE_HEIGHT = 12;
    private static final int EYE_SPACING = 40;
    private static final int BLINK_FRAMES = 20;
    private static final int OPEN_TIME_MIN_MS = 1000;
    private static final int OPEN_TIME_MAX_MS = 4000;

    private static final String EYE_CHAR = "●";
    private static final String BG_CHAR = " ";

    private static final Style EYE_STYLE = Style.newStyle()
        .foreground(Color.color("#F0F0F0"));

    private static final Random RANDOM = new Random();

    private int width;
    private int height;
    private final int[] eyePositions;
    private int eyeY;
    private boolean isBlinking;
    private int blinkState;
    private LocalDateTime lastBlink;
    private Duration openTime;

    /**
     * Creates a new eye animation model with defaults.
     */
    public EyesExample() {
        this.width = 80;
        this.height = 24;
        this.eyePositions = new int[2];
        this.isBlinking = false;
        this.blinkState = 0;
        this.lastBlink = LocalDateTime.now();
        this.openTime = randomOpenTime();
        updateEyePositions();
    }

    /**
     * Starts the animation tick loop.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return tickCommand();
    }

    /**
     * Handles window resize, quit keys, and blink animation steps.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("ctrl+c".equals(key) || "esc".equals(key)) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
        } else if (msg instanceof WindowSizeMessage windowSize) {
            this.width = windowSize.width();
            this.height = windowSize.height();
            updateEyePositions();
        } else if (msg instanceof TickMessage) {
            handleBlink();
        }

        return new UpdateResult<>(this, tickCommand());
    }

    /**
     * Renders the blinking eyes.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        String[][] canvas = buildCanvas();
        int currentHeight = currentEyeHeight();
        for (int i = 0; i < 2; i++) {
            drawEllipse(canvas, eyePositions[i], eyeY, EYE_WIDTH, currentHeight);
        }

        StringBuilder output = new StringBuilder();
        for (String[] row : canvas) {
            for (String cell : row) {
                output.append(cell);
            }
            output.append("\n");
        }

        return EYE_STYLE.render(output.toString());
    }

    /**
     * Updates cached eye positions based on the current size.
     */
    private void updateEyePositions() {
        int startX = (width - EYE_SPACING) / 2;
        eyeY = height / 2;
        eyePositions[0] = startX;
        eyePositions[1] = startX + EYE_SPACING;
    }

    /**
     * Builds an empty canvas filled with background characters.
     *
     * @return fresh canvas grid
     */
    private String[][] buildCanvas() {
        int safeHeight = Math.max(1, height);
        int safeWidth = Math.max(1, width);
        String[][] canvas = new String[safeHeight][safeWidth];
        for (int y = 0; y < safeHeight; y++) {
            for (int x = 0; x < safeWidth; x++) {
                canvas[y][x] = BG_CHAR;
            }
        }
        return canvas;
    }

    /**
     * Computes the current eye height based on the blink state.
     *
     * @return eye height for this frame
     */
    private int currentEyeHeight() {
        if (!isBlinking) {
            return EYE_HEIGHT;
        }

        double blinkProgress;
        if (blinkState < BLINK_FRAMES / 2) {
            blinkProgress = (double) blinkState / (BLINK_FRAMES / 2);
            blinkProgress = 1.0 - (blinkProgress * blinkProgress);
        } else {
            blinkProgress = (double) (blinkState - BLINK_FRAMES / 2) / (BLINK_FRAMES / 2);
            blinkProgress = blinkProgress * (2.0 - blinkProgress);
        }

        return Math.max(1, (int) Math.round(EYE_HEIGHT * blinkProgress));
    }

    /**
     * Advances the blink animation state.
     */
    private void handleBlink() {
        LocalDateTime now = LocalDateTime.now();
        if (!isBlinking && Duration.between(lastBlink, now).compareTo(openTime) >= 0) {
            isBlinking = true;
            blinkState = 0;
        }

        if (isBlinking) {
            blinkState++;
            if (blinkState >= BLINK_FRAMES) {
                isBlinking = false;
                lastBlink = now;
                openTime = randomOpenTime();
                if (RANDOM.nextInt(10) == 0) {
                    openTime = Duration.ofMillis(300);
                }
            }
        }
    }

    /**
     * Returns a randomized open time between blinks.
     *
     * @return randomized open duration
     */
    private Duration randomOpenTime() {
        int range = OPEN_TIME_MAX_MS - OPEN_TIME_MIN_MS;
        int next = RANDOM.nextInt(range) + OPEN_TIME_MIN_MS;
        return Duration.ofMillis(next);
    }

    /**
     * Draws an ellipse on the canvas.
     *
     * @param canvas canvas grid
     * @param x0 center x
     * @param y0 center y
     * @param rx radius x
     * @param ry radius y
     */
    private void drawEllipse(String[][] canvas, int x0, int y0, int rx, int ry) {
        if (ry <= 0 || rx <= 0) {
            return;
        }
        for (int y = -ry; y <= ry; y++) {
            double normalizedY = (double) y / ry;
            int widthAtY = (int) Math.round(rx * Math.sqrt(1.0 - (normalizedY * normalizedY)));
            for (int x = -widthAtY; x <= widthAtY; x++) {
                int canvasX = x0 + x;
                int canvasY = y0 + y;
                if (canvasY >= 0 && canvasY < canvas.length
                    && canvasX >= 0 && canvasX < canvas[0].length) {
                    canvas[canvasY][canvasX] = EYE_CHAR;
                }
            }
        }
    }

    /**
     * Builds the next tick command for the animation.
     *
     * @return tick command
     */
    private Command tickCommand() {
        return Command.tick(Duration.ofMillis(50), TickMessage::new);
    }

    /**
     * Tick message used to drive animation updates.
     *
     * @param time tick time
     */
    private record TickMessage(LocalDateTime time) implements Message {
    }

    /**
     * Runs the eyes example in alternate screen mode.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new EyesExample()).withAltScreen().run();
    }
}
