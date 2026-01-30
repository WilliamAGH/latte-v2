package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.ansi.TextWidth;

import java.util.Objects;
import java.util.function.IntFunction;

/**
 * Handles textarea sizing and prompt configuration.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} sizing logic.
 */
final class TextAreaSizing {

    /**
     * Sets the textarea width with prompt and line number accounting.
     *
     * @param state textarea state
     * @param width target width
     */
    void setWidth(TextAreaState state, int width) {
        if (state.promptFunc == null) {
            state.promptWidth = TextWidth.measureCellWidth(state.prompt);
        }

        int reservedOuter = state.style.base().getHorizontalFrameSize();
        int reservedInner = state.promptWidth;

        if (state.showLineNumbers) {
            reservedInner += state.lineNumberWidth;
        }

        int minWidth = reservedInner + reservedOuter + 1;
        int inputWidth = Math.max(width, minWidth);
        if (state.maxWidth > 0) {
            inputWidth = Math.min(inputWidth, state.maxWidth);
        }

        state.viewport.setWidth(inputWidth - reservedOuter);
        state.width = inputWidth - reservedOuter - reservedInner;
    }

    /**
     * Sets the textarea height with limits applied.
     *
     * @param state textarea state
     * @param height target height
     */
    void setHeight(TextAreaState state, int height) {
        if (state.maxHeight > 0) {
            state.height = clamp(height, TextAreaDefaults.MIN_HEIGHT, state.maxHeight);
            state.viewport.setHeight(state.height);
        } else {
            state.height = Math.max(height, TextAreaDefaults.MIN_HEIGHT);
            state.viewport.setHeight(state.height);
        }
    }

    /**
     * Updates the prompt string.
     *
     * @param state textarea state
     * @param prompt prompt string
     */
    void setPrompt(TextAreaState state, String prompt) {
        state.prompt = Objects.requireNonNull(prompt, "prompt");
        if (state.promptFunc == null) {
            state.promptWidth = TextWidth.measureCellWidth(prompt);
        }
    }

    /**
     * Sets a dynamic prompt function with a fixed width.
     *
     * @param state textarea state
     * @param promptWidth width of the prompt column
     * @param promptFunc prompt function
     */
    void setPromptFunc(TextAreaState state, int promptWidth, IntFunction<String> promptFunc) {
        state.promptFunc = Objects.requireNonNull(promptFunc, "promptFunc");
        state.promptWidth = promptWidth;
    }

    /**
     * Clamps a value to the inclusive range.
     *
     * @param value value to clamp
     * @param low minimum value
     * @param high maximum value
     * @return clamped value
     */
    private static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(high, value));
    }
}
