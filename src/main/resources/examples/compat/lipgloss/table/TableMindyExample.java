package examples.compat.lipgloss.table;

import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Example program demonstrating lipgloss table with color swatches.
 * <p>
 * Creates a large table showing color swatches organized by rows with alternating
 * label/swatch layout.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/table/mindy/main.go">lipgloss/examples/table</a>
 */
public class TableMindyExample {

    private static final int ROW_LENGTH = 12;

    /**
     * Builds a row of label/swatch pairs for a contiguous color range.
     *
     * @param start first color code to include
     * @param end last color code to include
     * @return row data for the table
     */
    private static String[] makeRow(int start, int end) {
        java.util.List<String> row = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            row.add(String.valueOf(i));
            row.add("");
        }
        while (row.size() < ROW_LENGTH) {
            row.add("");
        }
        return row.toArray(new String[0]);
    }

    /**
     * Creates an empty spacer row to separate color groups.
     *
     * @return spacer row data
     */
    private static String[] makeEmptyRow() {
        return makeRow(0, -1);
    }

    /**
     * Runs the example to render a palette grid with labeled color swatches.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style labelStyle = Style.newStyle().width(3).alignHorizontal(Position.Right);
        Style swatchStyle = Style.newStyle().width(6);

        List<String[]> data = new ArrayList<>();

        for (int i = 0; i < 13; i += 8) {
            data.add(makeRow(i, i + 5));
        }
        data.add(makeEmptyRow());

        for (int i = 6; i < 15; i += 8) {
            data.add(makeRow(i, i + 1));
        }
        data.add(makeEmptyRow());

        for (int i = 16; i < 231; i += 6) {
            data.add(makeRow(i, i + 5));
        }
        data.add(makeEmptyRow());

        for (int i = 232; i < 256; i += 6) {
            data.add(makeRow(i, i + 5));
        }

        Table t = Table.create()
                .border(StandardBorder.HiddenBorder)
                .rows(data.toArray(new String[0][]))
                .styleFunc((row, col) -> {
                    if (row < 0 || row >= data.size()) {
                        return labelStyle;
                    }
                    String colorCode = data.get(row)[col - (col % 2)];

                    if (col % 2 == 0) {
                        return labelStyle.foreground(Color.color(colorCode));
                    } else {
                        return swatchStyle.background(Color.color(colorCode));
                    }
                });

        System.out.println(t);
    }
}
