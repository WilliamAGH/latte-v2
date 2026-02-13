package examples.compat.bubbletea.tetris;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator;
import com.williamcallahan.tui4j.compat.lipgloss.join.VerticalJoinDecorator;
import com.williamcallahan.tui4j.compat.lipgloss.PlacementDecorator;
import com.williamcallahan.tui4j.message.CheckWindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;

/**
 * Example program for tetris game.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/tetris/TetrisGame.java
 */
public class TetrisGame implements Model {

    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;

    private final Grid grid;

    private WindowSizeMessage windowSizeMessage = new WindowSizeMessage(0, 0);

    private Style borderStyle = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(Color.color("63"));

    private Style gridStyle = borderStyle.copy();

    private Style scoreStyle = borderStyle.copy()
            .width(20)
            .align(Position.Center, Position.Center);

    private Style nextBlockStyle = borderStyle.copy()
            .width(20)
            .height(8)
            .align(Position.Center, Position.Center);

    private Style rightPanelStyle = Style.newStyle()
            .marginLeft(2);

    /**
     * Creates TetrisGame to keep example ready for use.
     *
     * @param score score
     */
    public TetrisGame(int score) {
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return Command.batch(grid.init(), CheckWindowSizeMessage::new);
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage sizeMsg) {
            this.windowSizeMessage = sizeMsg;
        } else if (msg instanceof GameOverMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        UpdateResult<? extends Model> updateResult = grid.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (grid.gameOver()) {
            return "Final score: %d".formatted(grid.score());
        }

        String grid = gridStyle.render(this.grid.view());

        String nextBlockPanel = nextBlockStyle.render(
                VerticalJoinDecorator.joinVertical(
                        Position.Center,
                        "Next block:\n\n",
                        this.grid.nextBlockPreview()
                )
        );

        String rightPanel = VerticalJoinDecorator.joinVertical(
                Position.Left,
                scoreStyle.render("Level:\n%d".formatted(this.grid.level())),
                scoreStyle.render("Score:\n%d".formatted(this.grid.score())),
                nextBlockPanel
        );

        String gameView = HorizontalJoinDecorator.joinHorizontal(
                Position.Top,
                grid,
                rightPanelStyle.render(rightPanel)
        );

        if (windowSizeMessage == null || windowSizeMessage.width() == 0) {
            return gameView;
        }

        return PlacementDecorator.placeHorizontal(
                windowSizeMessage.width(),
                Position.Center,
                gameView
        );
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TetrisGame(0))
//                .withAltScreen()
                .run();
    }

}
