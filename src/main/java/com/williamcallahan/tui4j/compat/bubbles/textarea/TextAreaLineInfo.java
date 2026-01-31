package com.williamcallahan.tui4j.compat.bubbles.textarea;

/**
 * Information about the current line and cursor position.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} LineInfo.
 */
public class TextAreaLineInfo {

    private int width;
    private int charWidth;
    private int height;
    private int startColumn;
    private int columnOffset;
    private int rowOffset;
    private int charOffset;

    /**
     * Creates an empty line info.
     */
    public TextAreaLineInfo() {
    }

    /**
     * Creates line info with the specified values.
     *
     * @param width line width in characters
     * @param charWidth line width in cells
     * @param height number of wrapped lines
     * @param startColumn starting column of the wrapped line
     * @param columnOffset cursor column within the wrapped line
     * @param rowOffset wrapped line index within the logical line
     * @param charOffset cursor position in cells from line start
     */
    public TextAreaLineInfo(int width, int charWidth, int height, int startColumn,
                            int columnOffset, int rowOffset, int charOffset) {
        this.width = width;
        this.charWidth = charWidth;
        this.height = height;
        this.startColumn = startColumn;
        this.columnOffset = columnOffset;
        this.rowOffset = rowOffset;
        this.charOffset = charOffset;
    }

    /**
     * Returns the line width in characters.
     *
     * @return width in characters
     */
    public int width() {
        return width;
    }

    /**
     * Returns the line width in cells.
     *
     * @return width in cells
     */
    public int charWidth() {
        return charWidth;
    }

    /**
     * Returns the number of wrapped lines.
     *
     * @return wrapped line count
     */
    public int height() {
        return height;
    }

    /**
     * Returns the starting column of the wrapped line.
     *
     * @return start column
     */
    public int startColumn() {
        return startColumn;
    }

    /**
     * Returns the cursor column within the wrapped line.
     *
     * @return column offset
     */
    public int columnOffset() {
        return columnOffset;
    }

    /**
     * Returns the wrapped line index within the logical line.
     *
     * @return row offset
     */
    public int rowOffset() {
        return rowOffset;
    }

    /**
     * Returns the cursor position in cells from line start.
     *
     * @return character offset
     */
    public int charOffset() {
        return charOffset;
    }

    /**
     * Sets the line width in characters.
     *
     * @param width width in characters
     * @return this line info
     */
    public TextAreaLineInfo width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the line width in cells.
     *
     * @param charWidth width in cells
     * @return this line info
     */
    public TextAreaLineInfo charWidth(int charWidth) {
        this.charWidth = charWidth;
        return this;
    }

    /**
     * Sets the number of wrapped lines.
     *
     * @param height wrapped line count
     * @return this line info
     */
    public TextAreaLineInfo height(int height) {
        this.height = height;
        return this;
    }

    /**
     * Sets the starting column of the wrapped line.
     *
     * @param startColumn start column
     * @return this line info
     */
    public TextAreaLineInfo startColumn(int startColumn) {
        this.startColumn = startColumn;
        return this;
    }

    /**
     * Sets the cursor column within the wrapped line.
     *
     * @param columnOffset column offset
     * @return this line info
     */
    public TextAreaLineInfo columnOffset(int columnOffset) {
        this.columnOffset = columnOffset;
        return this;
    }

    /**
     * Sets the wrapped line index within the logical line.
     *
     * @param rowOffset row offset
     * @return this line info
     */
    public TextAreaLineInfo rowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
        return this;
    }

    /**
     * Sets the cursor position in cells from line start.
     *
     * @param charOffset character offset
     * @return this line info
     */
    public TextAreaLineInfo charOffset(int charOffset) {
        this.charOffset = charOffset;
        return this;
    }
}
