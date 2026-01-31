package com.williamcallahan.tui4j.compat.bubbles.textarea;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.cursor.Cursor;
import java.util.function.IntFunction;
/** Multi-line text editor bubble. Port of {@code bubbles/textarea/textarea.go}. */
public class Textarea implements Model {
    /** Styling configuration for the textarea. Port of {@code bubbles/textarea/textarea.go} Style. */
    public static class Style extends TextAreaStyle {
        /** Creates default textarea styles. */
        public Style() { super(); }
        /**
         * Creates styles from an existing TextAreaStyle.
         *
         * @param source source styles
         */
        public Style(TextAreaStyle source) {
            this();
            base(source.base());
            cursorLine(source.cursorLine());
            cursorLineNumber(source.cursorLineNumber());
            endOfBuffer(source.endOfBuffer());
            lineNumber(source.lineNumber());
            placeholder(source.placeholder());
            prompt(source.prompt());
            text(source.text());
        }
        /** {@inheritDoc} */
        @Override
        public Style base(com.williamcallahan.tui4j.compat.lipgloss.Style base) {
            super.base(base);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style cursorLine(com.williamcallahan.tui4j.compat.lipgloss.Style cursorLine) {
            super.cursorLine(cursorLine);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style cursorLineNumber(com.williamcallahan.tui4j.compat.lipgloss.Style cursorLineNumber) {
            super.cursorLineNumber(cursorLineNumber);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style endOfBuffer(com.williamcallahan.tui4j.compat.lipgloss.Style endOfBuffer) {
            super.endOfBuffer(endOfBuffer);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style lineNumber(com.williamcallahan.tui4j.compat.lipgloss.Style lineNumber) {
            super.lineNumber(lineNumber);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style placeholder(com.williamcallahan.tui4j.compat.lipgloss.Style placeholder) {
            super.placeholder(placeholder);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style prompt(com.williamcallahan.tui4j.compat.lipgloss.Style prompt) {
            super.prompt(prompt);
            return this;
        }
        /** {@inheritDoc} */
        @Override
        public Style text(com.williamcallahan.tui4j.compat.lipgloss.Style text) {
            super.text(text);
            return this;
        }
    }
    /** Key bindings for textarea actions. Port of {@code bubbles/textarea/textarea.go} KeyMap. */
    public static class KeyMap extends TextAreaKeyMap {
        /** Creates default key bindings. */
        public KeyMap() { super(); }
    }
    /** Line metadata for wrapped lines. Port of {@code bubbles/textarea/textarea.go} LineInfo. */
    public static class LineInfo extends TextAreaLineInfo {
        /** Creates empty line info. */
        public LineInfo() { super(); }
        /**
         * Creates line info with values.
         *
         * @param width line width in characters
         * @param charWidth line width in cells
         * @param height number of wrapped lines
         * @param startColumn starting column of the wrapped line
         * @param columnOffset cursor column within the wrapped line
         * @param rowOffset wrapped line index within the logical line
         * @param charOffset cursor position in cells from line start
         */
        public LineInfo(int width, int charWidth, int height, int startColumn,
                        int columnOffset, int rowOffset, int charOffset) {
            super(width, charWidth, height, startColumn, columnOffset, rowOffset, charOffset);
        }
        /** {@inheritDoc} */
        @Override
        public LineInfo width(int width) { super.width(width); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo charWidth(int charWidth) { super.charWidth(charWidth); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo height(int height) { super.height(height); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo startColumn(int startColumn) { super.startColumn(startColumn); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo columnOffset(int columnOffset) { super.columnOffset(columnOffset); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo rowOffset(int rowOffset) { super.rowOffset(rowOffset); return this; }
        /** {@inheritDoc} */
        @Override
        public LineInfo charOffset(int charOffset) { super.charOffset(charOffset); return this; }
    }
    private final TextAreaState state;
    private final TextAreaBuffer buffer;
    private final TextAreaLayout layout;
    private final TextAreaInputHandler inputHandler;
    private final TextAreaRenderer renderer;
    private final TextAreaSizing sizing;
    /** Creates a textarea with default settings. */
    public Textarea() {
        state = new TextAreaState();
        buffer = new TextAreaBuffer();
        layout = new TextAreaLayout();
        TextAreaNavigation navigation = new TextAreaNavigation(layout);
        inputHandler = new TextAreaInputHandler(buffer, navigation, layout);
        renderer = new TextAreaRenderer(layout);
        sizing = new TextAreaSizing();
        state.keyMap = new KeyMap();
        TextAreaStyle[] styles = TextAreaDefaults.defaultStyles();
        state.focusedStyle = new Style(styles[0]);
        state.blurredStyle = new Style(styles[1]);
        state.style = state.blurredStyle;
        sizing.setWidth(state, state.width);
        sizing.setHeight(state, state.height);
    }
    /** {@inheritDoc} */
    @Override
    public Command init() { return null; }
    /** {@inheritDoc} */
    @Override
    public UpdateResult<Textarea> update(Message msg) {
        return UpdateResult.from(this, inputHandler.update(state, msg));
    }
    /** {@inheritDoc} */
    @Override
    public String view() { return renderer.view(state); }
    /**
     * Returns line info.
     *
     * @return line info
     */
    public LineInfo lineInfo() {
        TextAreaLineInfo info = layout.lineInfo(state);
        return new LineInfo(info.width(), info.charWidth(), info.height(), info.startColumn(),
                info.columnOffset(), info.rowOffset(), info.charOffset());
    }
    /**
     * Returns text content.
     *
     * @return text content
     */
    public String value() {
        if (state.value.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < state.value.size(); i++) {
            builder.append(TextAreaRunes.toString(state.value.get(i)));
            if (i < state.value.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }
    /**
     * Sets the textarea content.
     *
     * @param value text to set
     */
    public void setValue(String value) { reset(); insertString(value); }
    /**
     * Inserts a string at the cursor.
     *
     * @param value string to insert
     */
    public void insertString(String value) { buffer.insertRunesFromUserInput(state, TextAreaRunes.toCodePoints(value)); }
    /**
     * Inserts a character at the cursor.
     *
     * @param value character to insert
     */
    public void insertRune(char value) { buffer.insertRunesFromUserInput(state, TextAreaRunes.toCodePoints(new char[]{value})); }
    /**
     * Returns the content length.
     *
     * @return length
     */
    public int length() { return buffer.length(state); }
    /**
     * Returns line count.
     *
     * @return line count
     */
    public int lineCount() { return state.value.size(); }
    /**
     * Returns the current line index.
     *
     * @return line index
     */
    public int line() { return state.row; }
    /** Focuses the textarea. */
    public void focus() {
        state.focus = true;
        state.style = state.focusedStyle;
        state.cursor.focus();
    }
    /** Removes focus from the textarea. */
    public void blur() {
        state.focus = false;
        state.style = state.blurredStyle;
        state.cursor.blur();
    }
    /**
     * Returns the focus state.
     *
     * @return true if focused
     */
    public boolean focused() { return state.focus; }
    /** Clears the textarea content and resets the cursor. */
    public void reset() { buffer.reset(state); state.viewport.gotoTop(); }
    /**
     * Sets the textarea width.
     *
     * @param width width in cells
     */
    public void setWidth(int width) { sizing.setWidth(state, width); }
    /**
     * Sets the textarea height.
     *
     * @param height height in lines
     */
    public void setHeight(int height) { sizing.setHeight(state, height); }
    /**
     * Sets the prompt string.
     *
     * @param prompt prompt to display
     */
    public void setPrompt(String prompt) { sizing.setPrompt(state, prompt); }
    /**
     * Sets a dynamic prompt function.
     *
     * @param promptWidth width of the prompt column
     * @param promptFunc prompt function
     */
    public void setPromptFunc(int promptWidth, IntFunction<String> promptFunc) {
        sizing.setPromptFunc(state, promptWidth, promptFunc);
    }
    /**
     * Sets the placeholder text.
     *
     * @param placeholder placeholder to display when empty
     */
    public void setPlaceholder(String placeholder) { state.placeholder = placeholder; }
    /**
     * Enables or disables line numbers.
     *
     * @param showLineNumbers true to show line numbers
     */
    public void setShowLineNumbers(boolean showLineNumbers) { state.showLineNumbers = showLineNumbers; }
    /**
     * Sets the character limit.
     *
     * @param charLimit maximum characters, 0 for unlimited
     */
    public void setCharLimit(int charLimit) { state.charLimit = charLimit; }
    /**
     * Sets the maximum height.
     *
     * @param maxHeight maximum height in lines
     */
    public void setMaxHeight(int maxHeight) { state.maxHeight = maxHeight; }
    /**
     * Sets the maximum width.
     *
     * @param maxWidth maximum width in cells
     */
    public void setMaxWidth(int maxWidth) { state.maxWidth = maxWidth; }
    /**
     * Sets the end-of-buffer character.
     *
     * @param endOfBufferCharacter character to display at end of buffer
     */
    public void setEndOfBufferCharacter(char endOfBufferCharacter) { state.endOfBufferCharacter = endOfBufferCharacter; }
    /**
     * Returns the current style.
     *
     * @return current style
     */
    public Style style() { return (Style) state.style; }
    /**
     * Returns the focused style.
     *
     * @return focused style
     */
    public Style focusedStyle() { return (Style) state.focusedStyle; }
    /**
     * Returns the blurred style.
     *
     * @return blurred style
     */
    public Style blurredStyle() { return (Style) state.blurredStyle; }
    /**
     * Returns the cursor.
     *
     * @return cursor
     */
    public Cursor cursor() { return state.cursor; }
    /**
     * Returns the textarea width.
     *
     * @return width in cells
     */
    public int width() { return state.width; }
    /**
     * Returns the textarea height.
     *
     * @return height in lines
     */
    public int height() { return state.height; }
}
