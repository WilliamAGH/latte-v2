package com.williamcallahan.tui4j.compat.lipgloss.table;

import com.williamcallahan.tui4j.ansi.TextWrapper;
import com.williamcallahan.tui4j.compat.lipgloss.Size;
import com.williamcallahan.tui4j.compat.lipgloss.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Resizes table columns and rows to match Lip Gloss table sizing behavior.
 * <p>
 * Port of github.com/charmbracelet/lipgloss/table/resizing.go.
 */
final class TableResizer {

    private final int tableWidth;
    private final List<String> headers;
    private final String[][] allRows;
    private final List<ResizerColumn> columns = new ArrayList<>();

    private int[] rowHeights;
    private boolean wrap;
    private boolean borderColumn;
    private int[][] yPaddings;

    /**
     * Creates a new resizer for the provided table configuration.
     *
     * @param tableWidth table width constraint
     * @param tableHeight table height constraint
     * @param headers header row values
     * @param rows data rows
     */
    TableResizer(int tableWidth, int tableHeight, List<String> headers, String[][] rows) {
        this.tableWidth = tableWidth;
        this.headers = headers == null ? List.of() : headers;

        if (this.headers.isEmpty()) {
            this.allRows = rows;
        } else {
            this.allRows = prependHeaders(this.headers, rows);
        }

        buildColumns();
    }

    /**
     * Sets whether content wrapping is enabled.
     *
     * @param wrap wrap flag
     */
    void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * Sets whether column borders are enabled.
     *
     * @param borderColumn border column flag
     */
    void setBorderColumn(boolean borderColumn) {
        this.borderColumn = borderColumn;
    }

    /**
     * Applies style-driven padding and fixed sizing rules to the resizer.
     *
     * @param styleFunc style function for cells
     * @param hasHeaders whether the table has headers
     */
    void applyStyles(StyleFunc styleFunc, boolean hasHeaders) {
        rowHeights = defaultRowHeights();
        yPaddings = new int[allRows.length][];

        for (int i = 0; i < allRows.length; i++) {
            String[] row = allRows[i];
            yPaddings[i] = new int[row.length];

            for (int j = 0; j < row.length; j++) {
                ResizerColumn column = columns.get(j);
                int rowIndex = hasHeaders ? i - 1 : i;
                Style style = styleFunc.apply(rowIndex, j);

                int totalHorizontalPadding = style.getHorizontalMargins() + style.getHorizontalPadding();
                column.xPadding = Math.max(column.xPadding, totalHorizontalPadding);
                column.fixedWidth = Math.max(column.fixedWidth, style.getWidth());

                rowHeights[i] = Math.max(rowHeights[i], style.getHeight());

                int totalVerticalPadding = style.getVerticalMargins() + style.getVerticalPadding();
                yPaddings[i][j] = totalVerticalPadding;
            }
        }
    }

    /**
     * Returns optimized column widths and row heights based on the table size.
     *
     * @return resize result
     */
    ResizeResult optimizedWidths() {
        int effectiveTableWidth = tableWidth;
        if (effectiveTableWidth <= 0) {
            effectiveTableWidth = detectTableWidth();
        }

        if (maxTotal() <= effectiveTableWidth) {
            return expandTableWidth(effectiveTableWidth);
        }
        return shrinkTableWidth(effectiveTableWidth);
    }

    /**
     * Converts table data to a rectangular matrix.
     *
     * @param data table data
     * @return matrix of row data
     */
    static String[][] dataToMatrix(Data data) {
        int numRows = data.rows();
        int numCols = data.columns();
        String[][] rows = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rows[i][j] = data.at(i, j);
            }
        }
        return rows;
    }

    /**
     * Builds column metadata from the current rows.
     */
    private void buildColumns() {
        for (String[] row : allRows) {
            for (int i = 0; i < row.length; i++) {
                int cellLen = Size.width(row[i]);

                if (columns.size() <= i) {
                    columns.add(new ResizerColumn(i, cellLen));
                    continue;
                }

                ResizerColumn column = columns.get(i);
                column.widths.add(cellLen);
                column.min = Math.min(column.min, cellLen);
                column.max = Math.max(column.max, cellLen);
            }
        }

        for (ResizerColumn column : columns) {
            int[] widths = column.widths.stream().mapToInt(Integer::intValue).toArray();
            column.median = median(widths);
        }
    }

    /**
     * Expands column widths to fill the available table width.
     *
     * @param effectiveTableWidth resolved table width
     * @return resize result
     */
    private ResizeResult expandTableWidth(int effectiveTableWidth) {
        int[] colWidths = maxColumnWidths();

        for (; ; ) {
            int totalWidth = sum(colWidths) + totalHorizontalBorder();
            if (totalWidth >= effectiveTableWidth) {
                break;
            }

            int shorterColumnIndex = 0;
            int shorterColumnWidth = Integer.MAX_VALUE;

            for (int j = 0; j < colWidths.length; j++) {
                if (colWidths[j] == columns.get(j).fixedWidth) {
                    continue;
                }
                if (colWidths[j] < shorterColumnWidth) {
                    shorterColumnWidth = colWidths[j];
                    shorterColumnIndex = j;
                }
            }

            colWidths[shorterColumnIndex]++;
        }

        int[] heights = expandRowHeights(colWidths);
        return new ResizeResult(colWidths, heights);
    }

    /**
     * Shrinks column widths to fit the available table width.
     *
     * @param effectiveTableWidth resolved table width
     * @return resize result
     */
    private ResizeResult shrinkTableWidth(int effectiveTableWidth) {
        int[] colWidths = maxColumnWidths();

        shrinkBiggestColumns(colWidths, effectiveTableWidth, true);
        shrinkToMedian(colWidths, effectiveTableWidth);
        shrinkBiggestColumns(colWidths, effectiveTableWidth, false);

        int[] heights = expandRowHeights(colWidths);
        return new ResizeResult(colWidths, heights);
    }

    /**
     * Shrinks the widest columns until the table fits.
     *
     * @param colWidths current column widths
     * @param effectiveTableWidth resolved table width
     * @param veryBigOnly whether to shrink only very large columns first
     */
    private void shrinkBiggestColumns(int[] colWidths, int effectiveTableWidth, boolean veryBigOnly) {
        for (; ; ) {
            int totalWidth = sum(colWidths) + totalHorizontalBorder();
            if (totalWidth <= effectiveTableWidth) {
                break;
            }

            int bigColumnIndex = Integer.MIN_VALUE;
            int bigColumnWidth = Integer.MIN_VALUE;

            for (int j = 0; j < colWidths.length; j++) {
                if (colWidths[j] == columns.get(j).fixedWidth) {
                    continue;
                }
                if (veryBigOnly) {
                    if (colWidths[j] >= (effectiveTableWidth / 2) && colWidths[j] > bigColumnWidth) {
                        bigColumnWidth = colWidths[j];
                        bigColumnIndex = j;
                    }
                } else if (colWidths[j] > bigColumnWidth) {
                    bigColumnWidth = colWidths[j];
                    bigColumnIndex = j;
                }
            }

            if (bigColumnIndex < 0 || colWidths[bigColumnIndex] == 0) {
                break;
            }
            colWidths[bigColumnIndex]--;
        }
    }

    /**
     * Shrinks columns based on distance from the median width.
     *
     * @param colWidths current column widths
     * @param effectiveTableWidth resolved table width
     */
    private void shrinkToMedian(int[] colWidths, int effectiveTableWidth) {
        for (; ; ) {
            int totalWidth = sum(colWidths) + totalHorizontalBorder();
            if (totalWidth <= effectiveTableWidth) {
                break;
            }

            int biggestDiffToMedian = Integer.MIN_VALUE;
            int biggestDiffToMedianIndex = Integer.MIN_VALUE;

            for (int j = 0; j < colWidths.length; j++) {
                if (colWidths[j] == columns.get(j).fixedWidth) {
                    continue;
                }
                int diffToMedian = colWidths[j] - columns.get(j).median;
                if (diffToMedian > 0 && diffToMedian > biggestDiffToMedian) {
                    biggestDiffToMedian = diffToMedian;
                    biggestDiffToMedianIndex = j;
                }
            }

            if (biggestDiffToMedianIndex <= 0 || colWidths[biggestDiffToMedianIndex] == 0) {
                break;
            }
            colWidths[biggestDiffToMedianIndex]--;
        }
    }

    /**
     * Expands row heights based on wrapped content.
     *
     * @param colWidths column widths to wrap against
     * @return row heights
     */
    private int[] expandRowHeights(int[] colWidths) {
        int[] heights = defaultRowHeights();
        if (!wrap) {
            return heights;
        }

        boolean hasHeaders = !headers.isEmpty();
        for (int i = 0; i < allRows.length; i++) {
            String[] row = allRows[i];
            for (int j = 0; j < row.length; j++) {
                if (hasHeaders && i == 0) {
                    continue;
                }
                int height = detectContentHeight(row[j], colWidths[j] - xPaddingForCol(j)) + xPaddingForCell(i, j);
                if (height > heights[i]) {
                    heights[i] = height;
                }
            }
        }
        return heights;
    }

    /**
     * Returns default row heights based on any fixed height values.
     *
     * @return default row heights
     */
    private int[] defaultRowHeights() {
        int[] heights = new int[allRows.length];
        for (int i = 0; i < heights.length; i++) {
            if (rowHeights != null && i < rowHeights.length) {
                heights[i] = rowHeights[i];
            }
            if (heights[i] < 1) {
                heights[i] = 1;
            }
        }
        return heights;
    }

    /**
     * Returns the maximum widths for each column.
     *
     * @return maximum column widths
     */
    private int[] maxColumnWidths() {
        int[] maxColumnWidths = new int[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            ResizerColumn column = columns.get(i);
            if (column.fixedWidth > 0) {
                maxColumnWidths[i] = column.fixedWidth;
            } else {
                maxColumnWidths[i] = column.max + xPaddingForCol(column.index);
            }
        }
        return maxColumnWidths;
    }

    /**
     * Returns the maximum character count across all columns.
     *
     * @return maximum character count
     */
    private int maxCharCount() {
        int count = 0;
        for (int j = 0; j < columns.size(); j++) {
            ResizerColumn column = columns.get(j);
            if (column.fixedWidth > 0) {
                count += column.fixedWidth - xPaddingForCol(column.index);
            } else {
                count += column.max;
            }
        }
        return count;
    }

    /**
     * Returns the maximum total width for the current column widths.
     *
     * @return maximum total width
     */
    private int maxTotal() {
        int maxTotal = 0;
        for (int j = 0; j < columns.size(); j++) {
            ResizerColumn column = columns.get(j);
            if (column.fixedWidth > 0) {
                maxTotal += column.fixedWidth;
            } else {
                maxTotal += column.max + xPaddingForCol(j);
            }
        }
        return maxTotal;
    }

    /**
     * Returns total horizontal padding across all columns.
     *
     * @return total horizontal padding
     */
    private int totalHorizontalPadding() {
        int totalPadding = 0;
        for (ResizerColumn column : columns) {
            totalPadding += column.xPadding;
        }
        return totalPadding;
    }

    /**
     * Returns horizontal padding for a column index.
     *
     * @param j column index
     * @return horizontal padding
     */
    private int xPaddingForCol(int j) {
        if (j >= columns.size()) {
            return 0;
        }
        return columns.get(j).xPadding;
    }

    /**
     * Returns vertical padding for a specific cell.
     *
     * @param i row index
     * @param j column index
     * @return vertical padding
     */
    private int xPaddingForCell(int i, int j) {
        if (yPaddings == null || i >= yPaddings.length || j >= yPaddings[i].length) {
            return 0;
        }
        return yPaddings[i][j];
    }

    /**
     * Returns total horizontal border width contributed by column separators.
     *
     * @return total horizontal border width
     */
    private int totalHorizontalBorder() {
        return (columnCount() * borderPerCell()) + extraBorder();
    }

    /**
     * Returns the number of border columns per cell.
     *
     * @return border count per cell
     */
    private int borderPerCell() {
        return borderColumn ? 1 : 0;
    }

    /**
     * Returns the extra trailing border width.
     *
     * @return extra border width
     */
    private int extraBorder() {
        return borderColumn ? 1 : 0;
    }

    /**
     * Detects the width of the table when no explicit width is set.
     *
     * @return detected table width
     */
    private int detectTableWidth() {
        return maxCharCount() + totalHorizontalPadding() + totalHorizontalBorder();
    }

    /**
     * Detects content height for a single cell given a wrap width.
     *
     * @param content cell content
     * @param width wrap width
     * @return detected content height
     */
    private int detectContentHeight(String content, int width) {
        if (width == 0) {
            return 1;
        }

        int height = 0;
        String normalized = content.replace("\r\n", "\n");
        String[] lines = normalized.split("\n", -1);
        TextWrapper wrapper = new TextWrapper();
        for (String line : lines) {
            String wrapped = wrapper.wrap(line, width);
            height += Size.height(wrapped);
        }
        return height;
    }

    /**
     * Returns the number of columns in the resizer.
     *
     * @return column count
     */
    private int columnCount() {
        return columns.size();
    }

    /**
     * Sums an array of integers.
     *
     * @param values values to sum
     * @return sum of values
     */
    private static int sum(int[] values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }

    /**
     * Returns the median value of an array of integers.
     *
     * @param values values to analyze
     * @return median value
     */
    private static int median(int[] values) {
        if (values.length == 0) {
            return 0;
        }
        Arrays.sort(values);
        if (values.length % 2 == 0) {
            int half = values.length / 2;
            return (values[half - 1] + values[half]) / 2;
        }
        return values[values.length / 2];
    }

    /**
     * Prepends headers to a matrix of rows.
     *
     * @param headers header values
     * @param rows data rows
     * @return combined rows including the header row
     */
    private static String[][] prependHeaders(List<String> headers, String[][] rows) {
        String[][] allRows = new String[rows.length + 1][];
        allRows[0] = headers.toArray(new String[0]);
        System.arraycopy(rows, 0, allRows, 1, rows.length);
        return allRows;
    }

    /**
     * Resizer column metadata.
     */
    private static final class ResizerColumn {
        private final int index;
        private int min;
        private int max;
        private int median;
        private final List<Integer> widths = new ArrayList<>();
        private int xPadding;
        private int fixedWidth;

        /**
         * Creates a column metadata container.
         *
         * @param index column index
         * @param cellLen initial cell width
         */
        private ResizerColumn(int index, int cellLen) {
            this.index = index;
            this.min = cellLen;
            this.max = cellLen;
            this.median = cellLen;
        }
    }

    /**
     * Result for resized columns and row heights.
     */
    static final class ResizeResult {
        private final int[] columnWidths;
        private final int[] rowHeights;

        /**
         * Creates a resize result.
         *
         * @param columnWidths column widths
         * @param rowHeights row heights
         */
        ResizeResult(int[] columnWidths, int[] rowHeights) {
            this.columnWidths = columnWidths;
            this.rowHeights = rowHeights;
        }

        /**
         * Returns the column widths.
         *
         * @return column widths
         */
        int[] columnWidths() {
            return columnWidths;
        }

        /**
         * Returns the row heights.
         *
         * @return row heights
         */
        int[] rowHeights() {
            return rowHeights;
        }
    }
}
