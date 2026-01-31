package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.bubbles.cursor.Cursor;
import com.williamcallahan.tui4j.compat.bubbles.runeutil.Sanitizer;
import com.williamcallahan.tui4j.compat.bubbles.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Mutable textarea state holder.
 * <p>
 * Derived from {@code bubbles/textarea/textarea.go} Model.
 */
final class TextAreaState {

    String prompt;
    String placeholder;
    boolean showLineNumbers;
    char endOfBufferCharacter;
    TextAreaKeyMap keyMap;
    TextAreaStyle focusedStyle;
    TextAreaStyle blurredStyle;
    TextAreaStyle style;
    Cursor cursor;
    int charLimit;
    int maxHeight;
    int maxWidth;
    IntFunction<String> promptFunc;
    int promptWidth;
    int width;
    int height;
    List<int[]> value;
    boolean focus;
    int col;
    int row;
    int lastCharOffset;
    Sanitizer sanitizer;
    int lineNumberWidth;
    Viewport viewport;
    TextAreaWrapCache wrapCache;
    Exception error;

    /**
     * Creates a textarea state with default settings.
     */
    TextAreaState() {
        TextAreaStyle[] styles = TextAreaDefaults.defaultStyles();
        this.prompt = TextAreaDefaults.defaultPrompt();
        this.placeholder = "";
        this.showLineNumbers = true;
        this.endOfBufferCharacter = ' ';
        this.keyMap = new TextAreaKeyMap();
        this.focusedStyle = styles[0];
        this.blurredStyle = styles[1];
        this.style = blurredStyle;
        this.cursor = new Cursor();
        this.charLimit = TextAreaDefaults.DEFAULT_CHAR_LIMIT;
        this.maxHeight = TextAreaDefaults.DEFAULT_MAX_HEIGHT;
        this.maxWidth = TextAreaDefaults.DEFAULT_MAX_WIDTH;
        this.promptWidth = TextWidth.measureCellWidth(prompt);
        this.width = TextAreaDefaults.DEFAULT_WIDTH;
        this.height = TextAreaDefaults.DEFAULT_HEIGHT;
        this.value = new ArrayList<>();
        this.value.add(new int[0]);
        this.focus = false;
        this.col = 0;
        this.row = 0;
        this.lastCharOffset = 0;
        this.sanitizer = new Sanitizer(Sanitizer.replaceTabs("    "));
        this.lineNumberWidth = TextAreaDefaults.DEFAULT_LINE_NUMBER_WIDTH;
        this.viewport = new Viewport();
        this.wrapCache = new TextAreaWrapCache(TextAreaDefaults.MAX_LINES);
    }
}
