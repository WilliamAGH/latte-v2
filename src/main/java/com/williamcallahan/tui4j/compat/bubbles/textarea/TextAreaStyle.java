package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.compat.lipgloss.Style;

/**
 * Styling configuration for the textarea.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} Style.
 */
public class TextAreaStyle {

    private Style base;
    private Style cursorLine;
    private Style cursorLineNumber;
    private Style endOfBuffer;
    private Style lineNumber;
    private Style placeholder;
    private Style prompt;
    private Style text;

    /**
     * Creates default textarea styles.
     */
    public TextAreaStyle() {
        this.base = Style.newStyle();
        this.cursorLine = Style.newStyle();
        this.cursorLineNumber = Style.newStyle();
        this.endOfBuffer = Style.newStyle();
        this.lineNumber = Style.newStyle();
        this.placeholder = Style.newStyle();
        this.prompt = Style.newStyle();
        this.text = Style.newStyle();
    }

    /**
     * Sets the base style.
     *
     * @param base base style
     * @return this style
     */
    public TextAreaStyle base(Style base) {
        this.base = base;
        return this;
    }

    /**
     * Sets the cursor line style.
     *
     * @param cursorLine cursor line style
     * @return this style
     */
    public TextAreaStyle cursorLine(Style cursorLine) {
        this.cursorLine = cursorLine;
        return this;
    }

    /**
     * Sets the cursor line number style.
     *
     * @param cursorLineNumber cursor line number style
     * @return this style
     */
    public TextAreaStyle cursorLineNumber(Style cursorLineNumber) {
        this.cursorLineNumber = cursorLineNumber;
        return this;
    }

    /**
     * Sets the end of buffer style.
     *
     * @param endOfBuffer end of buffer style
     * @return this style
     */
    public TextAreaStyle endOfBuffer(Style endOfBuffer) {
        this.endOfBuffer = endOfBuffer;
        return this;
    }

    /**
     * Sets the line number style.
     *
     * @param lineNumber line number style
     * @return this style
     */
    public TextAreaStyle lineNumber(Style lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    /**
     * Sets the placeholder style.
     *
     * @param placeholder placeholder style
     * @return this style
     */
    public TextAreaStyle placeholder(Style placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * Sets the prompt style.
     *
     * @param prompt prompt style
     * @return this style
     */
    public TextAreaStyle prompt(Style prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * Sets the text style.
     *
     * @param text text style
     * @return this style
     */
    public TextAreaStyle text(Style text) {
        this.text = text;
        return this;
    }

    /**
     * Returns the base style.
     *
     * @return base style
     */
    public Style base() {
        return base;
    }

    /**
     * Returns the cursor line style.
     *
     * @return cursor line style
     */
    public Style cursorLine() {
        return cursorLine;
    }

    /**
     * Returns the cursor line number style.
     *
     * @return cursor line number style
     */
    public Style cursorLineNumber() {
        return cursorLineNumber;
    }

    /**
     * Returns the end of buffer style.
     *
     * @return end of buffer style
     */
    public Style endOfBuffer() {
        return endOfBuffer;
    }

    /**
     * Returns the line number style.
     *
     * @return line number style
     */
    public Style lineNumber() {
        return lineNumber;
    }

    /**
     * Returns the placeholder style.
     *
     * @return placeholder style
     */
    public Style placeholder() {
        return placeholder;
    }

    /**
     * Returns the prompt style.
     *
     * @return prompt style
     */
    public Style prompt() {
        return prompt;
    }

    /**
     * Returns the text style.
     *
     * @return text style
     */
    public Style text() {
        return text;
    }

    /**
     * Returns the computed cursor line style with inheritance.
     *
     * @return computed cursor line style
     */
    public Style computedCursorLine() {
        return cursorLine.inherit(base).inline(true);
    }

    /**
     * Returns the computed cursor line number style with inheritance.
     *
     * @return computed cursor line number style
     */
    public Style computedCursorLineNumber() {
        return cursorLineNumber.inherit(cursorLine).inherit(base).inline(true);
    }

    /**
     * Returns the computed end of buffer style with inheritance.
     *
     * @return computed end of buffer style
     */
    public Style computedEndOfBuffer() {
        return endOfBuffer.inherit(base).inline(true);
    }

    /**
     * Returns the computed line number style with inheritance.
     *
     * @return computed line number style
     */
    public Style computedLineNumber() {
        return lineNumber.inherit(base).inline(true);
    }

    /**
     * Returns the computed placeholder style with inheritance.
     *
     * @return computed placeholder style
     */
    public Style computedPlaceholder() {
        return placeholder.inherit(base).inline(true);
    }

    /**
     * Returns the computed prompt style with inheritance.
     *
     * @return computed prompt style
     */
    public Style computedPrompt() {
        return prompt.inherit(base).inline(true);
    }

    /**
     * Returns the computed text style with inheritance.
     *
     * @return computed text style
     */
    public Style computedText() {
        return text.inherit(base).inline(true);
    }
}
