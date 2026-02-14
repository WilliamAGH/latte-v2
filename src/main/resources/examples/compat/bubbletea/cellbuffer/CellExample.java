package examples.compat.bubbletea.cellbuffer;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.harmonica.Spring;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import java.time.Duration;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

/**
 * Renders a spring-animated ellipse that follows mouse input.
 * Upstream: bubbletea/examples/cellbuffer/main.go
 */
public class CellExample implements Model {

    private static final double FPS = 60.0;
    private static final double FREQUENCY = 7.5;
    private static final double DAMPING = 0.15;
    private static final String ASTERISK = "*";

    private CellBuffer cells;
    private Spring spring;
    private double targetX;
    private double targetY;
    private double x;
    private double y;
    private double xVelocity;
    private double yVelocity;

    /**
     * Initializes the buffer and spring state for animation.
     */
    public CellExample() {
        this.cells = new CellBuffer();
        this.spring = Spring.newSpring(1.0 / FPS, FREQUENCY, DAMPING);
        this.targetX = 0;
        this.targetY = 0;
        this.x = 0;
        this.y = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
    }

    /**
     * Plots an ellipse into the buffer using a midpoint ellipse scan.
     *
     * @param cb buffer to draw into
     * @param xc ellipse center x
     * @param yc ellipse center y
     * @param rx x-radius
     * @param ry y-radius
     */
    private static void drawEllipse(CellBuffer cb, double xc, double yc, double rx, double ry) {
        double dx, dy, d1, d2;
        double x = 0;
        double y = ry;

        d1 = ry * ry - rx * rx * ry + 0.25 * rx * rx;
        dx = 2 * ry * ry * x;
        dy = 2 * rx * rx * y;

        while (dx < dy) {
            cb.set((int) (x + xc), (int) (y + yc));
            cb.set((int) (-x + xc), (int) (y + yc));
            cb.set((int) (x + xc), (int) (-y + yc));
            cb.set((int) (-x + xc), (int) (-y + yc));
            if (d1 < 0) {
                x++;
                dx = dx + (2 * ry * ry);
                d1 = d1 + dx + (ry * ry);
            } else {
                x++;
                y--;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d1 = d1 + dx - dy + (ry * ry);
            }
        }

        d2 = ((ry * ry) * ((x + 0.5) * (x + 0.5))) + ((rx * rx) * ((y - 1) * (y - 1))) - (rx * rx * ry * ry);

        while (y >= 0) {
            cb.set((int) (x + xc), (int) (y + yc));
            cb.set((int) (-x + xc), (int) (y + yc));
            cb.set((int) (x + xc), (int) (-y + yc));
            cb.set((int) (-x + xc), (int) (-y + yc));
            if (d2 > 0) {
                y--;
                dy = dy - (2 * rx * rx);
                d2 = d2 + (rx * rx) - dy;
            } else {
                y--;
                x++;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d2 = d2 + dx - dy + (rx * rx);
            }
        }
    }

    /**
     * Starts a frame ticker to drive animation at the target FPS.
     *
     * @return tick command for frame updates
     */
    @Override
    public Command init() {
        return Command.tick(Duration.ofNanos((long) (1_000_000_000.0 / FPS)), time -> new FrameMessage());
    }

    /**
     * Reacts to input, resizes the buffer, and advances animation frames.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("q".equals(key) || "Q".equals(key) || "ctrl+c".equals(key) || "esc".equals(key)) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
        }

        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            if (!cells.ready()) {
                targetX = windowSizeMessage.width() / 2.0;
                targetY = windowSizeMessage.height() / 2.0;
            }
            cells.init(windowSizeMessage.width(), windowSizeMessage.height());
            return UpdateResult.from(this);
        }

        if (msg instanceof MouseMessage mouseMessage) {
            if (!cells.ready()) {
                return UpdateResult.from(this);
            }
            targetX = mouseMessage.column();
            targetY = mouseMessage.row();
            return UpdateResult.from(this);
        }

        if (msg instanceof FrameMessage) {
            if (!cells.ready()) {
                return UpdateResult.from(this, Command.tick(Duration.ofNanos((long) (1_000_000_000.0 / FPS)), time -> new FrameMessage()));
            }

            cells.wipe();
            double[] xResult = spring.update(x, xVelocity, targetX);
            x = xResult[0];
            xVelocity = xResult[1];
            double[] yResult = spring.update(y, yVelocity, targetY);
            y = yResult[0];
            yVelocity = yResult[1];
            drawEllipse(cells, x, y, 16, 8);
            return UpdateResult.from(this, Command.tick(Duration.ofNanos((long) (1_000_000_000.0 / FPS)), time -> new FrameMessage()));
        }

        return UpdateResult.from(this);
    }

    /**
     * Renders the current buffer state for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return cells.toString();
    }

    /**
     * Runs the example in the alternate screen with mouse motion enabled.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        CellExample model = new CellExample();
        new Program(model)
                .withAltScreen()
                .withMouseCellMotion()
                .run();
    }

    /**
     * Minimal buffer for plotting animation frames in the example.
     */
    private static class CellBuffer {
        private String[] cells;
        private int stride;

        /**
         * Allocates the backing array for the current screen size.
         *
         * @param w width in columns
         * @param h height in rows
         */
        public void init(int w, int h) {
            if (w == 0) {
                return;
            }
            stride = w;
            cells = new String[w * h];
            wipe();
        }

        /**
         * Marks a cell with the current draw character when in bounds.
         *
         * @param x column index
         * @param y row index
         */
        public void set(int x, int y) {
            int i = y * stride + x;
            if (i > cells.length - 1 || x < 0 || y < 0 || x >= width() || y >= height()) {
                return;
            }
            cells[i] = ASTERISK;
        }

        /**
         * Clears the buffer between frames.
         */
        public void wipe() {
            for (int i = 0; i < cells.length; i++) {
                cells[i] = " ";
            }
        }

        /**
         * Returns the active buffer width.
         *
         * @return width in columns
         */
        public int width() {
            return stride;
        }

        /**
         * Returns the active buffer height.
         *
         * @return height in rows
         */
        public int height() {
            int h = cells.length / stride;
            if (cells.length % stride != 0) {
                h++;
            }
            return h;
        }

        /**
         * Reports whether the buffer has been initialized.
         *
         * @return true when ready to draw
         */
        public boolean ready() {
            return cells != null && cells.length > 0;
        }

        /**
         * Renders the buffer to a newline-delimited string.
         *
         * @return rendered buffer
         */
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < cells.length; i++) {
                if (i > 0 && i % stride == 0 && i < cells.length - 1) {
                    b.append('\n');
                }
                b.append(cells[i]);
            }
            return b.toString();
        }
    }

    /**
     * Message used to advance the animation frame.
     */
    private static class FrameMessage implements Message {
    }
}
