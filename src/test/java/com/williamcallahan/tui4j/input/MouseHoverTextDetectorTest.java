package com.williamcallahan.tui4j.input;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests mouse hover text detector.
 * tui4j: src/test/java/com/williamcallahan/tui4j/input/MouseHoverTextDetectorTest.java
 */
class MouseHoverTextDetectorTest {

    @Test
    void test_ShouldDetectTextCells() {
        MouseHoverTextDetector detector = new MouseHoverTextDetector();

        assertThat(detector.isHoveringText("Hello", 0, 0)).isTrue();
        assertThat(detector.isHoveringText("Hello", 4, 0)).isTrue();
        assertThat(detector.isHoveringText("Hello", 5, 0)).isFalse();
    }

    @Test
    void test_ShouldIgnoreWhitespaceCells() {
        MouseHoverTextDetector detector = new MouseHoverTextDetector();

        assertThat(detector.isHoveringText("   ", 0, 0)).isFalse();
        assertThat(detector.isHoveringText(" a ", 0, 0)).isFalse();
        assertThat(detector.isHoveringText(" a ", 1, 0)).isTrue();
        assertThat(detector.isHoveringText(" a ", 2, 0)).isFalse();
    }

    @Test
    void test_ShouldIgnoreAnsiSequences() {
        MouseHoverTextDetector detector = new MouseHoverTextDetector();
        String view = "\u001b[31mHi\u001b[0m";

        assertThat(detector.isHoveringText(view, 0, 0)).isTrue();
        assertThat(detector.isHoveringText(view, 1, 0)).isTrue();
        assertThat(detector.isHoveringText(view, 2, 0)).isFalse();
    }

    @Test
    void test_ShouldHandleMultipleLines() {
        MouseHoverTextDetector detector = new MouseHoverTextDetector();
        String view = "foo\nbar";

        assertThat(detector.isHoveringText(view, 0, 0)).isTrue();
        assertThat(detector.isHoveringText(view, 0, 1)).isTrue();
        assertThat(detector.isHoveringText(view, 0, 2)).isFalse();
    }
}
