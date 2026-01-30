package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.compat.lipgloss.Borders;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Default values for textarea configuration.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} defaults.
 */
final class TextAreaDefaults {

    static final int MIN_HEIGHT = 1;
    static final int DEFAULT_HEIGHT = 6;
    static final int DEFAULT_WIDTH = 40;
    static final int DEFAULT_CHAR_LIMIT = 0;
    static final int DEFAULT_MAX_HEIGHT = 99;
    static final int DEFAULT_MAX_WIDTH = 500;
    static final int MAX_LINES = 10000;
    static final int DEFAULT_LINE_NUMBER_WIDTH = 4;

    /**
     * Prevents instantiation.
     */
    private TextAreaDefaults() {
    }

    /**
     * Returns the default prompt string.
     *
     * @return prompt string
     */
    static String defaultPrompt() {
        return Borders.thickBorder().left() + " ";
    }

    /**
     * Returns the default style set for focused and blurred states.
     *
     * @return array of [focused, blurred] styles
     */
    static TextAreaStyle[] defaultStyles() {
        TextAreaStyle focused = new TextAreaStyle();
        focused.base(Style.newStyle());
        focused.cursorLine(Style.newStyle().background(new AdaptiveColor("255", "0")));
        focused.cursorLineNumber(Style.newStyle().foreground(new AdaptiveColor("240", "240")));
        focused.endOfBuffer(Style.newStyle().foreground(new AdaptiveColor("254", "0")));
        focused.lineNumber(Style.newStyle().foreground(new AdaptiveColor("249", "7")));
        focused.placeholder(Style.newStyle().foreground(Color.color("240")));
        focused.prompt(Style.newStyle().foreground(Color.color("7")));
        focused.text(Style.newStyle());

        TextAreaStyle blurred = new TextAreaStyle();
        blurred.base(Style.newStyle());
        blurred.cursorLine(Style.newStyle().foreground(new AdaptiveColor("245", "7")));
        blurred.cursorLineNumber(Style.newStyle().foreground(new AdaptiveColor("249", "7")));
        blurred.endOfBuffer(Style.newStyle().foreground(new AdaptiveColor("254", "0")));
        blurred.lineNumber(Style.newStyle().foreground(new AdaptiveColor("249", "7")));
        blurred.placeholder(Style.newStyle().foreground(Color.color("240")));
        blurred.prompt(Style.newStyle().foreground(Color.color("7")));
        blurred.text(Style.newStyle().foreground(new AdaptiveColor("245", "7")));

        return new TextAreaStyle[]{focused, blurred};
    }
}
