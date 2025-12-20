package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests text lines.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class TextLinesTest {

    private static Stream<Arguments> textLineTestCases() {
        return Stream.of(
                Arguments.of("Hello\nWorld", 5),
                Arguments.of("A\nLonger line", 11),
                Arguments.of("Short\nVery long line\nEnd", 14),
                Arguments.of("", 0),
                Arguments.of("Single line", 11),
                Arguments.of("\n\n\n", 0),
                Arguments.of("Test    \nSpaces   ", 9),
                Arguments.of("Line1\n\nLine2", 5),
                Arguments.of("\u001B[31mRed\u001B[0m", 3),
                Arguments.of("\u001B[1mBold\u001B[0m\n\u001B[4mUnderline\u001B[0m", 9),
                Arguments.of("\u001B[31;1mMultiple\u001B[0m\nFormats", 8),
                Arguments.of("\u001B[38;5;201mPink\u001B[0m", 4),
                Arguments.of("\u001B[48;2;255;0;0mRGB\u001B[0m", 3),
                Arguments.of("Normal\n\u001B[31mRed\n\u001B[32mGreen", 6),
                Arguments.of("\u001B[31m\u001B[1m\u001B[4mAll\u001B[0m", 3),
                Arguments.of("\u001B[31mIncomplete format\nAnd this is something else", 26)
        );
    }

    @ParameterizedTest
    @MethodSource("textLineTestCases")
    void testWidestLineLength(String input, int expectedWidth) {
        TextLines textLines = TextLines.fromText(input);
        assertEquals(expectedWidth, textLines.widestLineLength(),
                "Widest line length mismatch for input: " + input);
    }
}