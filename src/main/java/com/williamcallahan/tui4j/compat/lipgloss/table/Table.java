package com.williamcallahan.tui4j.compat.lipgloss.table;

import com.williamcallahan.tui4j.ansi.Truncate;
import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Size;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import java.util.ArrayList;
import java.util.List;

/**
 * Data table component.
 * <p>
 * Port of github.com/charmbracelet/lipgloss/table/table.go.
 * Renders structured data in a grid with customizable borders, headers, and cell styling.
 */
public class Table {

    /**
     * Marker row index used for header styling callbacks.
     */
    public static final int HEADER_ROW = -1;

    private static final StyleFunc DEFAULT_STYLES = (row, col) ->
        Style.newStyle();

    private StyleFunc styleFunc = DEFAULT_STYLES;
    private Border border;

    private boolean borderTop = true;
    private boolean borderBottom = true;
    private boolean borderLeft = true;
    private boolean borderRight = true;
    private boolean borderHeader = true;
    private boolean borderColumn = true;
    private boolean borderRow = false;

    private Style borderStyle;
    private List<String> headers = new ArrayList<>();
    private Data data;

    private int width = 0;
    private int height = 0;
    private boolean useManualHeight = false;
    private int offset = 0;
    private boolean wrap = true;

    private int[] widths;
    private int[] heights;

    /**
     * Creates a table with the specified renderer.
     *
     * @param renderer renderer for color profile detection (currently unused; retained for API stability [FS1g])
     */
    public Table(Renderer renderer) {
        this.border = createDefaultBorder();
        this.borderStyle = Style.newStyle();
        this.data = new StringData();
    }

    private Border createDefaultBorder() {
        return StandardBorder.RoundedBorder;
    }

    /**
     * Creates a table with the default renderer.
     *
     * @return new table instance
     */
    public static Table create() {
        return new Table(Renderer.defaultRenderer());
    }

    /**
     * Sets the style function for cell styling.
     *
     * @param styleFunc function that returns a style for each cell position
     * @return this table
     */
    public Table styleFunc(StyleFunc styleFunc) {
        this.styleFunc = styleFunc;
        return this;
    }

    /**
     * Returns the style for a cell at the specified position.
     *
     * @param row row index (-1 for header)
     * @param col column index
     * @return style for the cell
     */
    Style style(int row, int col) {
        if (styleFunc == null) {
            return Style.newStyle();
        }
        return styleFunc.apply(row, col);
    }

    /**
     * Sets the data source.
     *
     * @param data data provider
     * @return this table
     */
    public Table data(Data data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        this.data = data;
        return this;
    }

    /**
     * Appends rows to the table.
     *
     * @param rows rows to append
     * @return this table
     */
    public Table rows(String[]... rows) {
        if (data instanceof StringData stringData) {
            for (String[] row : rows) {
                stringData.append(row);
            }
        }
        return this;
    }

    /**
     * Appends a single row to the table.
     *
     * @param row row to append
     * @return this table
     */
    public Table row(String... row) {
        if (data instanceof StringData stringData) {
            stringData.append(row);
        }
        return this;
    }

    /**
     * Removes all rows from the table.
     *
     * @return this table
     */
    public Table clearRows() {
        if (data instanceof StringData stringData) {
            stringData.clear();
        } else {
            data = new StringData();
        }
        return this;
    }

    /**
     * Sets the header titles.
     *
     * @param headers header titles
     * @return this table
     */
    public Table headers(String... headers) {
        for (String header : headers) {
            this.headers.add(header);
        }
        return this;
    }

    /**
     * Sets the border style.
     *
     * @param border border definition
     * @return this table
     */
    public Table border(Border border) {
        this.border = border;
        return this;
    }

    /**
     * Enables or disables the top border.
     *
     * @param borderTop true to show top border
     * @return this table
     */
    public Table borderTop(boolean borderTop) {
        this.borderTop = borderTop;
        return this;
    }

    /**
     * Enables or disables the bottom border.
     *
     * @param borderBottom true to show bottom border
     * @return this table
     */
    public Table borderBottom(boolean borderBottom) {
        this.borderBottom = borderBottom;
        return this;
    }

    /**
     * Enables or disables the left border.
     *
     * @param borderLeft true to show left border
     * @return this table
     */
    public Table borderLeft(boolean borderLeft) {
        this.borderLeft = borderLeft;
        return this;
    }

    /**
     * Enables or disables the right border.
     *
     * @param borderRight true to show right border
     * @return this table
     */
    public Table borderRight(boolean borderRight) {
        this.borderRight = borderRight;
        return this;
    }

    /**
     * Enables or disables the header separator.
     *
     * @param borderHeader true to show header separator
     * @return this table
     */
    public Table borderHeader(boolean borderHeader) {
        this.borderHeader = borderHeader;
        return this;
    }

    /**
     * Enables or disables column separators.
     *
     * @param borderColumn true to show column separators
     * @return this table
     */
    public Table borderColumn(boolean borderColumn) {
        this.borderColumn = borderColumn;
        return this;
    }

    /**
     * Enables or disables row separators.
     *
     * @param borderRow true to show row separators
     * @return this table
     */
    public Table borderRow(boolean borderRow) {
        this.borderRow = borderRow;
        return this;
    }

    /**
     * Sets the style applied to border characters.
     *
     * @param borderStyle style for borders
     * @return this table
     */
    public Table borderStyle(Style borderStyle) {
        this.borderStyle = borderStyle;
        return this;
    }

    /**
     * Sets the table width.
     *
     * @param width width in cells
     * @return this table
     */
    public Table width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the table height.
     *
     * @param height height in lines
     * @return this table
     */
    public Table height(int height) {
        this.height = height;
        this.useManualHeight = true;
        return this;
    }

    /**
     * Sets the row offset for scrolling.
     *
     * @param offset number of rows to skip
     * @return this table
     */
    public Table offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Enables or disables text wrapping in cells.
     *
     * @param wrap true to wrap text
     * @return this table
     */
    public Table wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * Renders the table to a string.
     * Computes dimensions, applies borders, and renders headers and rows.
     *
     * @return rendered table
     */
    public String render() {
        boolean hasHeaders = !headers.isEmpty();
        boolean hasRows = data != null && data.rows() > 0;

        if (!hasHeaders && !hasRows) {
            return "";
        }

        int dataColumns = data != null ? data.columns() : 0;
        if (hasHeaders) {
            while (headers.size() < dataColumns) {
                headers.add("");
            }
        }

        resize();

        StringBuilder sb = new StringBuilder();

        if (borderTop) {
            sb.append(constructTopBorder()).append("\n");
        }

        if (hasHeaders) {
            sb.append(constructHeaders()).append("\n");
        }

        String bottom = "";
        if (borderBottom) {
            bottom = constructBottomBorder();
        }

        if (hasRows) {
            if (useManualHeight) {
                int topHeight = Size.height(sb.toString()) - 1;
                int availableLines = height - (topHeight + Size.height(bottom));

                if (availableLines > data.rows()) {
                    availableLines = data.rows();
                }
                sb.append(constructRows(availableLines));
            } else {
                for (int r = offset; r < data.rows(); r++) {
                    sb.append(constructRow(r, false));
                }
            }
        }

        sb.append(bottom);

        return Style.newStyle()
            .maxHeight(computeHeight())
            .maxWidth(width)
            .render(sb.toString());
    }

    private int computeHeight() {
        boolean hasHeaders = !headers.isEmpty();
        int sumHeights = 0;
        for (int h : heights) {
            sumHeights += h;
        }
        return (
            sumHeights -
            1 +
            boolToInt(hasHeaders) +
            boolToInt(borderTop) +
            boolToInt(borderBottom) +
            boolToInt(borderHeader) +
            data.rows() * boolToInt(borderRow)
        );
    }

    private int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    private String constructTopBorder() {
        StringBuilder s = new StringBuilder();
        if (borderLeft) {
            s.append(borderStyle.render(border.topLeft()));
        }
        for (int i = 0; i < widths.length; i++) {
            s.append(borderStyle.render(border.top().repeat(widths[i])));
            if (i < widths.length - 1 && borderColumn) {
                s.append(borderStyle.render(border.middleTop()));
            }
        }
        if (borderRight) {
            s.append(borderStyle.render(border.topRight()));
        }
        return s.toString();
    }

    private String constructBottomBorder() {
        StringBuilder s = new StringBuilder();
        if (borderLeft) {
            s.append(borderStyle.render(border.bottomLeft()));
        }
        for (int i = 0; i < widths.length; i++) {
            s.append(borderStyle.render(border.bottom().repeat(widths[i])));
            if (i < widths.length - 1 && borderColumn) {
                s.append(borderStyle.render(border.middleBottom()));
            }
        }
        if (borderRight) {
            s.append(borderStyle.render(border.bottomRight()));
        }
        return s.toString();
    }

    private String constructHeaders() {
        int headerHeight = heights[HEADER_ROW + 1];

        StringBuilder s = new StringBuilder();
        if (borderLeft) {
            s.append(borderStyle.render(border.left()));
        }
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            Style cellStyle = style(HEADER_ROW, i);

            if (!wrap) {
                header = truncateCell(header, HEADER_ROW, i);
            }

            s.append(
                cellStyle
                    .height(headerHeight - cellStyle.getVerticalMargins())
                    .maxHeight(headerHeight)
                    .width(widths[i] - cellStyle.getHorizontalMargins())
                    .maxWidth(widths[i])
                    .render(truncateCell(header, HEADER_ROW, i))
            );
            if (i < headers.size() - 1 && borderColumn) {
                s.append(borderStyle.render(border.left()));
            }
        }
        if (borderHeader) {
            if (borderRight) {
                s.append(borderStyle.render(border.right()));
            }
            s.append("\n");
            if (borderLeft) {
                s.append(borderStyle.render(border.middleLeft()));
            }
            for (int i = 0; i < headers.size(); i++) {
                s.append(borderStyle.render(border.top().repeat(widths[i])));
                if (i < headers.size() - 1 && borderColumn) {
                    s.append(borderStyle.render(border.middle()));
                }
            }
            if (borderRight) {
                s.append(borderStyle.render(border.middleRight()));
            }
        }
        if (borderRight && !borderHeader) {
            s.append(borderStyle.render(border.right()));
        }
        return s.toString();
    }

    private String constructRows(int availableLines) {
        StringBuilder sb = new StringBuilder();

        int offsetRowCount = data.rows() - offset;

        int rowsToRender = Math.max(availableLines, 1);

        boolean needsOverflow = rowsToRender < offsetRowCount;

        int rowIdx = offset;
        if (!needsOverflow) {
            rowIdx = Math.max(0, data.rows() - rowsToRender);
        }
        while (rowsToRender > 0 && rowIdx < data.rows()) {
            boolean isOverflow = needsOverflow && rowsToRender == 1;

            sb.append(constructRow(rowIdx, isOverflow));

            rowIdx++;
            rowsToRender--;
        }
        return sb.toString();
    }

    private String constructRow(int index, boolean isOverflow) {
        StringBuilder s = new StringBuilder();

        boolean hasHeaders = !headers.isEmpty();
        int rowHeight = heights[index + boolToInt(hasHeaders)];
        if (isOverflow) {
            rowHeight = 1;
        }

        List<String> cells = new ArrayList<>();
        String leftBorder = (borderStyle.render(border.left()) + "\n").repeat(
            rowHeight
        );
        if (borderLeft) {
            cells.add(leftBorder);
        }

        int numCols = data != null ? data.columns() : 0;
        for (int c = 0; c < numCols; c++) {
            String cell = isOverflow ? "…" : data.at(index, c);
            Style cellStyle = style(index, c);

            if (!wrap) {
                cell = truncateCell(cell, index, c);
            }

            int cellWidth = c < widths.length ? widths[c] : 0;
            String renderedCell = cellStyle
                .height(rowHeight - cellStyle.getVerticalMargins())
                .maxHeight(rowHeight)
                .width(cellWidth - cellStyle.getHorizontalMargins())
                .maxWidth(cellWidth)
                .render(cell);
            cells.add(renderedCell);

            if (c < numCols - 1 && borderColumn) {
                cells.add(leftBorder);
            }
        }

        if (borderRight) {
            String rightBorder = (
                borderStyle.render(border.right()) +
                "\n"
            ).repeat(rowHeight);
            cells.add(rightBorder);
        }

        trimCellNewlines(cells);

        s
            .append(
                Join.joinHorizontal(Position.Top, cells.toArray(new String[0]))
            )
            .append("\n");

        appendRowSeparator(s, index, isOverflow);

        return s.toString();
    }

    /**
     * Strips trailing newline characters from every cell string.
     * Matches Go's {@code strings.TrimRight(cell, "\n")} behavior.
     *
     * @param cells mutable cell list to trim in place
     */
    private void trimCellNewlines(List<String> cells) {
        // Match Go's strings.TrimRight(cell, "\n") - removes ALL trailing newlines
        for (int i = 0; i < cells.size(); i++) {
            String cell = cells.get(i);
            while (cell.endsWith("\n")) {
                cell = cell.substring(0, cell.length() - 1);
            }
            cells.set(i, cell);
        }
    }

    /**
     * Appends a horizontal row separator when border rows are enabled.
     *
     * @param s target builder
     * @param index current data row index
     * @param isOverflow true when rendering the overflow indicator row
     */
    private void appendRowSeparator(StringBuilder s, int index, boolean isOverflow) {
        if (!borderRow || data == null || index >= data.rows() - 1 || isOverflow) {
            return;
        }
        if (borderLeft) {
            s.append(borderStyle.render(border.middleLeft()));
        }
        for (int i = 0; i < widths.length; i++) {
            s.append(borderStyle.render(border.bottom().repeat(widths[i])));
            if (i < widths.length - 1 && borderColumn) {
                s.append(borderStyle.render(border.middle()));
            }
        }
        if (borderRight) {
            s.append(borderStyle.render(border.middleRight()));
        }
        s.append("\n");
    }

    private String truncateCell(String cell, int rowIndex, int colIndex) {
        boolean hasHeaders = !headers.isEmpty();
        int rowHeight = heights[rowIndex + boolToInt(hasHeaders)];
        int cellWidth = widths[colIndex];
        Style cellStyle = style(rowIndex, colIndex);

        int length =
            (cellWidth * rowHeight) -
            cellStyle.getHorizontalPadding() -
            cellStyle.getHorizontalMargins();
        return Truncate.truncate(cell, length, "…");
    }

    private void resize() {
        boolean hasHeaders = !headers.isEmpty();
        String[][] rows = TableResizer.dataToMatrix(data);
        TableResizer resizer = new TableResizer(width, height, headers, rows);
        resizer.setWrap(wrap);
        resizer.setBorderColumn(borderColumn);
        resizer.applyStyles(
            styleFunc == null ? DEFAULT_STYLES : styleFunc,
            hasHeaders
        );

        TableResizer.ResizeResult result = resizer.optimizedWidths();
        widths = result.columnWidths();
        heights = result.rowHeights();
    }

    @Override
    public String toString() {
        return render();
    }
}
