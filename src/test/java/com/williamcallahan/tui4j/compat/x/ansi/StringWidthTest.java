package com.williamcallahan.tui4j.compat.x.ansi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringWidthTest {

    @Test
    @DisplayName("Should parse C1 CSI sequence and ignore it")
    void testC1CsiSequence() {
        // \u009B is CSI (Control Sequence Introducer) in C1
        // equivalent to ESC [
        String input = "\u009B31mHello\u009B0m";

        // Expected: 5 (CSI 31 m is a color code, invisible)
        int width = StringWidth.stringWidth(input);

        assertThat(width).as("Width should be 5 for input with C1 CSI sequence").isEqualTo(5);
    }

    @Test
    @DisplayName("C1 CSI sequences should match 7-bit ESC sequences")
    void testC1CsiSequenceMatchesEsc() {
        String c1Input = "\u009B31mHello\u009B0m";
        String escInput = "\u001B[31mHello\u001B[0m";

        assertThat(StringWidth.stringWidth(c1Input))
                .as("C1 CSI width should match ESC CSI width")
                .isEqualTo(StringWidth.stringWidth(escInput));
    }
}
