package examples.compat.lipgloss.table;

import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;

/**
 * Example program demonstrating lipgloss table with multi-language content.
 * <p>
 * Shows a table with language names, formal/informal forms, and sample text
 * including international characters (Chinese, Japanese, Arabic, Russian, Spanish).
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/table/languages/main.go">lipgloss/examples/table</a>
 */
public class TableLanguagesExample {

    private static final String PURPLE = "99";
    private static final String GRAY = "245";
    private static final String LIGHT_GRAY = "241";

    private static final String[][] ROWS = {
            {"Chinese", "您好", "你好"},
            {"Japanese", "こんにちは", "やあ"},
            {"Arabic", "أهلين", "أهلا"},
            {"Russian", "Здравствуйте", "Привет"},
            {"Spanish", "Hola", "¿Qué tal?"},
    };

    /**
     * Runs the example to render a multilingual table with styled rows.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style headerStyle = Style.newStyle()
                .foreground(Color.color(PURPLE))
                .bold(true)
                .alignHorizontal(Position.Center);

        Style cellStyle = Style.newStyle().padding(0, 1).width(14);
        Style oddRowStyle = cellStyle.foreground(Color.color(GRAY));
        Style evenRowStyle = cellStyle.foreground(Color.color(LIGHT_GRAY));
        Style borderStyle = Style.newStyle().foreground(Color.color(PURPLE));

        Table t = Table.create()
                .border(StandardBorder.ThickBorder)
                .borderStyle(borderStyle)
                .styleFunc((row, col) -> {
                    Style style;
                    if (row == Table.HEADER_ROW) {
                        return headerStyle;
                    }

                    style = row % 2 == 0 ? evenRowStyle : oddRowStyle;

                    if (col == 1) {
                        return style.width(22);
                    }

                    if (row < ROWS.length && "Arabic".equals(ROWS[row][0]) && col != 0) {
                        return style.alignHorizontal(Position.Right);
                    }

                    return style;
                })
                .headers("LANGUAGE", "FORMAL", "INFORMAL");

        for (String[] row : ROWS) {
            t.rows(row);
        }

        t.row("English", "You look absolutely fabulous.", "How's it going?");

        System.out.println(t);
    }
}
