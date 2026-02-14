package examples.compat.bubbletea.mouse;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

/**
 * Demonstrates mouse motion, selection, and hover handling.
 * Upstream: bubbletea/examples/mouse/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/mouse/MouseExample.java
 */
public class MouseExample implements Model {

    private static final int BOX_INNER_WIDTH = 20;
    private static final int BOX_INNER_HEIGHT = 5;
    private static final int BOX_LEFT = 2;
    private static final int HEADER_LINES = 9;

    private MouseMode mode = MouseMode.AllMotion;
    private MouseMessage lastMouse;
    private String lastKey = "(none)";
    private boolean selecting;
    private boolean hasSelection;
    private int selectionStartCol;
    private int selectionStartRow;
    private int selectionEndCol;
    private int selectionEndRow;

    /**
     * Tracks the active mouse reporting mode.
     */
    private enum MouseMode {
        AllMotion("all motion"),
        CellMotion("cell motion"),
        Disabled("disabled");

        private final String label;

        /**
         * Stores the human-readable label for the mode.
         *
         * @param label mode label
         */
        MouseMode(String label) {
            this.label = label;
        }
    }

    /**
     * Starts with no initial command for the mouse demo.
     *
     * @return null to indicate no startup command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Tracks mouse events, selection, and toggles mouse modes via keys.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof MouseMessage mouseMessage) {
            lastMouse = mouseMessage;
            if (mouseMessage.getAction() == MouseAction.MouseActionPress
                    && mouseMessage.getButton() == MouseButton.MouseButtonLeft) {
                selecting = true;
                hasSelection = true;
                selectionStartCol = mouseMessage.column();
                selectionStartRow = mouseMessage.row();
                selectionEndCol = mouseMessage.column();
                selectionEndRow = mouseMessage.row();
            } else if (mouseMessage.getAction() == MouseAction.MouseActionMotion
                    && selecting) {
                selectionEndCol = mouseMessage.column();
                selectionEndRow = mouseMessage.row();
            } else if (mouseMessage.getAction() == MouseAction.MouseActionRelease) {
                selecting = false;
            }
            return UpdateResult.from(this);
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            lastKey = keyPressMessage.key();
            if (keyPressMessage.key().equals("q")
                    || keyPressMessage.key().equals("Q")
                    || keyPressMessage.key().equals("ctrl+c")
                    || keyPressMessage.type() == KeyType.keyESC) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            switch (keyPressMessage.key()) {
                case "a", "A" -> {
                    mode = MouseMode.AllMotion;
                    return UpdateResult.from(this, Command.enableMouseAllMotion());
                }
                case "c", "C" -> {
                    mode = MouseMode.CellMotion;
                    return UpdateResult.from(this, Command.enableMouseCellMotion());
                }
                case "d", "D" -> {
                    mode = MouseMode.Disabled;
                    return UpdateResult.from(this, Command.disableMouse());
                }
                default -> {
                    return UpdateResult.from(this);
                }
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the mouse status, selection, and demo box.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder view = new StringBuilder();
        view.append("Mouse example (hover support demo)\n");
        view.append("Mode: ").append(mode.label).append("\n");
        view.append("Keys: a=all motion, c=cell motion, d=disable, q=quit\n");
        view.append("Last key: ").append(lastKey).append("\n");
        view.append("Last event: ").append(lastEventText()).append("\n");
        view.append("Selection: ").append(selectionText()).append("\n");
        view.append("Drag with left mouse to select; scroll while dragging to extend\n");
        view.append("Hover over text to see I-beam (terminal support required)\n");
        view.append("Hover over the box below (requires all motion)\n");

        int boxTop = HEADER_LINES;
        boolean inBox = isInBox(lastMouse, BOX_LEFT, boxTop);
        view.append(renderBox(inBox));

        return view.toString();
    }

    /**
     * Formats the last mouse event for display.
     *
     * @return event description
     */
    private String lastEventText() {
        if (lastMouse == null) {
            return "(none)";
        }
        return "(X: %d, Y: %d) %s".formatted(
                lastMouse.column(),
                lastMouse.row(),
                lastMouse.describe()
        );
    }

    /**
     * Formats the current selection bounds for display.
     *
     * @return selection description
     */
    private String selectionText() {
        if (!hasSelection) {
            return "(none)";
        }
        return "(%d,%d) -> (%d,%d)%s".formatted(
                selectionStartCol,
                selectionStartRow,
                selectionEndCol,
                selectionEndRow,
                selecting ? " (active)" : ""
        );
    }

    /**
     * Determines whether the last mouse event is within the demo box.
     *
     * @param mouse last mouse event
     * @param boxLeft left edge
     * @param boxTop top edge
     * @return true when the event falls inside the box
     */
    private boolean isInBox(MouseMessage mouse, int boxLeft, int boxTop) {
        if (mouse == null) {
            return false;
        }
        int boxWidth = BOX_INNER_WIDTH + 2;
        int boxHeight = BOX_INNER_HEIGHT + 2;

        int col = mouse.column();
        int row = mouse.row();
        return row >= boxTop
                && row < boxTop + boxHeight
                && col >= boxLeft
                && col < boxLeft + boxWidth;
    }

    /**
     * Renders the demo box, highlighting it when the mouse is inside.
     *
     * @param inBox whether the mouse is inside the box
     * @return rendered box
     */
    private String renderBox(boolean inBox) {
        String corner = inBox ? "#" : "+";
        String horizontal = inBox ? "#" : "-";
        String vertical = inBox ? "#" : "|";
        String fill = inBox ? "." : " ";
        String indent = " ".repeat(BOX_LEFT);

        StringBuilder box = new StringBuilder();
        String topBottom = corner + horizontal.repeat(BOX_INNER_WIDTH) + corner;
        box.append(indent).append(topBottom).append("\n");
        for (int i = 0; i < BOX_INNER_HEIGHT; i++) {
            box.append(indent)
                    .append(vertical)
                    .append(fill.repeat(BOX_INNER_WIDTH))
                    .append(vertical)
                    .append("\n");
        }
        box.append(indent).append(topBottom);
        return box.toString();
    }

    /**
     * Runs the mouse example with advanced mouse tracking enabled.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new MouseExample())
                .withMouseAllMotion()
                .withMouseSelectionExtendOnScroll()
                .withMouseSelectionCursor()
                .withMouseHoverTextCursor()
                .run();
    }
}
