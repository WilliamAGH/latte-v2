package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests mouse message.
 * tui4j: src/test/java/com/williamcallahan/tui4j/input/MouseMessageTest.java
 */
class MouseMessageTest {

    @Test
    void testSgrReleasePreservesMotion() {
        MouseMessage message = MouseMessage.parseSGRMouseEvent(32, 10, 20, true);

        assertThat(message.getAction()).isEqualTo(MouseAction.MouseActionMotion);
        assertThat(message.getButton()).isEqualTo(MouseButton.MouseButtonLeft);
    }

    @Test
    void testSgrReleaseSetsReleaseForNonMotion() {
        MouseMessage message = MouseMessage.parseSGRMouseEvent(0, 10, 20, true);

        assertThat(message.getAction()).isEqualTo(MouseAction.MouseActionRelease);
        assertThat(message.getButton()).isEqualTo(MouseButton.MouseButtonNone);
    }
}
