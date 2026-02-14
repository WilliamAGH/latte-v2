package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.table;

import java.util.List;

/**
 * String-backed table data storage.
 * <p>
 * Lipgloss: table/rows.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because of a compat refactor; use
 *             {@link com.williamcallahan.tui4j.compat.lipgloss.table.StringData}.
 *             This transitional shim preserves backward compatibility and will be removed
 *             in a future release. Migrate to the canonical location.
 */
@Deprecated(since = "0.3.0")
public class StringData extends Data {
    private final com.williamcallahan.tui4j.compat.lipgloss.table.StringData delegate;
    private final String rawData;

    /**
     * Creates empty StringData.
     */
    @Deprecated(since = "0.3.0")
    public StringData() {
        super(new String[0], new String[0][0]);
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.table.StringData();
        this.rawData = null;
    }

    /**
     * Creates StringData with the given data string.
     *
     * @param data data
     */
    @Deprecated(since = "0.3.0")
    public StringData(String data) {
        super(new String[0], new String[0][0]);
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.table.StringData();
        this.rawData = data;
        if (data != null) {
            String[] lines = data.split("\n", -1);
            for (String line : lines) {
                this.delegate.append(line);
            }
        }
    }

    /**
     * Creates StringData with the given rows.
     *
     * @param rows the rows
     */
    @Deprecated(since = "0.3.0")
    public StringData(List<List<String>> rows) {
        super(new String[0], new String[0][0]);
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.table.StringData(rows);
        this.rawData = null;
    }

    /**
     * Creates StringData with the given rows.
     *
     * @param rows the rows
     */
    @Deprecated(since = "0.3.0")
    public StringData(String[]... rows) {
        super(new String[0], new String[0][0]);
        this.delegate = new com.williamcallahan.tui4j.compat.lipgloss.table.StringData(rows);
        this.rawData = null;
    }

    /**
     * Handles data for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public String data() {
        return rawData == null ? "" : rawData;
    }

    /**
     * Handles append for this component.
     *
     * @param row row
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public StringData append(String... row) {
        delegate.append(row);
        return this;
    }

    /**
     * Handles item for this component.
     *
     * @param row row
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public StringData item(String... row) {
        delegate.item(row);
        return this;
    }

    /**
     * Handles clear for this component.
     *
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public StringData clear() {
        delegate.clear();
        return this;
    }

    /**
     * Returns the cell content at the requested coordinates.
     *
     * @param row  row index
     * @param cell cell index
     * @return cell contents
     */
    @Deprecated(since = "0.3.0")
    @Override
    public String at(int row, int cell) {
        return delegate.at(row, cell);
    }

    /**
     * Returns the number of rows in the data set.
     *
     * @return row count
     */
    @Deprecated(since = "0.3.0")
    @Override
    public int rows() {
        return delegate.rows();
    }

    /**
     * Returns the number of columns in the data set.
     *
     * @return column count
     */
    @Deprecated(since = "0.3.0")
    @Override
    public int columns() {
        return delegate.columns();
    }

    /**
     * Returns the header labels for the data set.
     *
     * @return header labels
     */
    @Deprecated(since = "0.3.0")
    @Override
    public String[] headers() {
        return new String[0];
    }

    /**
     * Returns all rows in the data set.
     *
     * @return row data
     */
    @Deprecated(since = "0.3.0")
    @Override
    public String[][] getRows() {
        return new String[0][0];
    }
}
