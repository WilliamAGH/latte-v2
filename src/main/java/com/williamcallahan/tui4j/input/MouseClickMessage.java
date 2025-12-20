package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

/**
 * Synthesized click message from mouse press/release.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseClickMessage.java
 *
 * @param column column where the click occurred
 * @param row row where the click occurred
 * @param button mouse button associated with the click
 * @param shift whether shift was held
 * @param alt whether alt was held
 * @param ctrl whether ctrl was held
 * @param clickCount click chain count at the same cell
 * @param target resolved target under the click, if any
 * @param source originating mouse release message
 */
public record MouseClickMessage(
        int column,
        int row,
        MouseButton button,
        boolean shift,
        boolean alt,
        boolean ctrl,
        int clickCount,
        MouseTarget target,
        MouseMessage source
) implements Message {
}
