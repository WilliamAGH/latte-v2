package com.williamcallahan.tui4j.ansi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Tests text width.
 * tui4j: src/test/java/com/williamcallahan/tui4j/ansi/TextWidthTest.java
 */
class TextWidthTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("inputData")
    void test_ShouldHaveExpectedLength(String caseName, String input, int expectedWith) {
        Assertions.assertThat(TextWidth.measureCellWidth(input)).isEqualTo(expectedWith);
    }

    private static Stream<Arguments> inputData() {

        return Stream.of(
                Arguments.of("empty", "", 0),
                Arguments.of("ascii", "hello", 5),
                Arguments.of("emoji", "👋", 2),
                Arguments.of("wideemoji", "🫧", 2),
                Arguments.of("combining", "a\u0300", 1),
                Arguments.of("control", "\u001B[31mhello\u001B[0m", 5),
//                Arguments.of("csi8", "\u009B38;5;1mhello\u009Bm", 5),
//                Arguments.of("osc", "\u009d2;charmbracelet: ~/Source/bubbletea\u009c", 0),
                Arguments.of("controlemoji", "\u001B[31m👋\u001B[0m", 2),
                Arguments.of("oscwideemoji", "\u001b]2;title👨‍👩‍👦\u0007", 0),
//                Arguments.of("multiemojicsi", "👨‍👩‍👦\u009b38;5;1mhello\u009bm", 7),
//                Arguments.of("osc8eastasianlink", "\u009d8;id=1;https://example.com/\u009c打豆豆\u009d8;id=1;\u0007", 6),
                Arguments.of("dcsarabic", "\u001bP?123$pسلام\u001b\\اهلا", 4),
                Arguments.of("newline", "hello\nworld", 10),
                Arguments.of("tab", "hello\tworld", 10),
                Arguments.of("controlnewline", "\u001b[31mhello\u001b[0m\nworld", 10),
                Arguments.of("style", "\u001B[38;2;249;38;114mfoo", 3),
                Arguments.of("unicode", "\u001b[35m“box”\u001b[0m", 5),
                Arguments.of("just_unicode", "Claire’s Boutique", 17),
                Arguments.of("unclosed_ansi", "Hey, \u001b[7m\n猴", 7),
                Arguments.of("double_asian_runes", " 你\u001b[8m好.", 6),
                Arguments.of("text with link", "\033]8;;http://example.com\033\\This is a link\033]8;;\033\\", 14)
        );
    }
}