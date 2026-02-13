package examples.compat.lipgloss.table;

import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;

/**
 * Example program demonstrating lipgloss table with chess board.
 * <p>
 * Shows a chess board using Unicode chess pieces.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/table/chess/main.go">lipgloss/examples/table</a>
 */
public class TableChessExample {

    private static final String[][] BOARD = {
            {"♜", "♞", "♛", "♚", "♝", "♟", "♞"},
            {"♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"},
            {"♖", "♘", "♗", "♔", "♗", "♘", "♖"},
    };

    /**
     * Runs the example to render a chessboard with labeled ranks and files.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style labelStyle = Style.newStyle().foreground(Color.color("241"));

        Table t = Table.create()
                .border(StandardBorder.NormalBorder)
                .borderRow(true)
                .borderColumn(true)
                .rows(BOARD)
                .styleFunc((row, col) -> Style.newStyle().padding(0, 1));

        String ranks = labelStyle.render(String.join("   ", "A", "B", "C", "D", "E", "F", "G", "H  "));
        String files = labelStyle.render(String.join("\n\n ", " 1", "2", "3", "4", "5", "6", "7", "8 "));

        System.out.println(
                Style.newStyle()
                        .alignHorizontal(Position.Right)
                        .render(String.join("\n", files, t.render(), ranks))
        );
    }
}
