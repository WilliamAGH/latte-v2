package com.williamcallahan.tui4j.ansi;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests truncate.
 * tui4j: src/test/java/com/williamcallahan/tui4j/ansi/TruncateTest.java
 */
class TruncateTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("truncateTestData")
    void test_ShouldTruncateText(String caseName, String input, String tail, int width, String expect) {
        // when
        String truncated = Truncate.truncate(input, width, tail);

        // then
        assertThat(truncated).isEqualTo(expect);
    }

    private static Stream<Arguments> truncateTestData() {
        return Stream.of(
                Arguments.of("empty", "", "", 0, ""),
                Arguments.of("equalascii", "one", ".", 3, "one"),
                Arguments.of("equalemoji", "on👋", ".", 3, "on."),
                Arguments.of("equalcontrolemoji", "one\u001B[0m", ".", 3, "one\u001B[0m"),
                Arguments.of("truncate_tail_greater", "foo", "...", 5, "foo"),
                Arguments.of("simple", "foobar", "", 3, "foo"),
                Arguments.of("passthrough", "foobar", "", 10, "foobar"),
                Arguments.of("ascii", "hello", "", 3, "hel"),
                Arguments.of("emoji", "👋", "", 2, "👋"),
                Arguments.of("wideemoji", "🫧", "", 2, "🫧"),
                Arguments.of("controlemoji", "\u001B[31mhello 👋abc\u001B[0m", "", 8, "\u001B[31mhello 👋\u001B[0m"),
                Arguments.of("osc8", "\u001B]8;;https://charm.sh\u001B\\Charmbracelet 🫧\u001B]8;;\u001B\\", "", 5, "\u001B]8;;https://charm.sh\u001B\\Charm\u001B]8;;\u001B\\"),
//                Arguments.of("osc8_8bit", "\u009d8;;https://charm.sh\u009cCharmbracelet 🫧\u009d8;;\u009c", "", 5, "\u009d8;;https://charm.sh\u009cCharm\u009d8;;\u009c"),
                Arguments.of("style_tail", "\u001B[38;5;219mHiya!", "…", 3, "\u001B[38;5;219mHi…"),
                Arguments.of("double_style_tail", "\u001B[38;5;219mHiya!\u001B[38;5;219mHello", "…", 7, "\u001B[38;5;219mHiya!\u001B[38;5;219mH…"),
                Arguments.of("noop", "\u001B[7m--", "", 2, "\u001B[7m--"),
                Arguments.of("double_width", "\u001B[38;2;249;38;114m你好\u001B[0m", "", 3, "\u001B[38;2;249;38;114m你\u001B[0m"),
                Arguments.of("double_width_rune", "你", "", 1, ""),
                Arguments.of("double_width_runes", "你好", "", 2, "你"),
                Arguments.of("spaces_only", "    ", "…", 2, " …"),
                Arguments.of("longer_tail", "foo", "...", 2, ""),
                Arguments.of("same_tail_width", "foo", "...", 3, "foo"),
                Arguments.of("same_tail_width_control", "\u001B[31mfoo\u001B[0m", "...", 3, "\u001B[31mfoo\u001B[0m"),
                Arguments.of("same_width", "foo", "", 3, "foo"),
                Arguments.of("truncate_with_tail", "foobar", ".", 4, "foo."),
                Arguments.of("style", "I really \u001B[38;2;249;38;114mlove\u001B[0m Go!", "", 8, "I really\u001B[38;2;249;38;114m\u001B[0m"),
                Arguments.of("dcs", "\u001BPq#0;2;0;0;0#1;2;100;100;0#2;2;0;100;0#1~~@@vv@@~~@@~~$#2??}}GG}}??}}??-#1!14@\u001B\\foobar", "…", 4, "\u001BPq#0;2;0;0;0#1;2;100;100;0#2;2;0;100;0#1~~@@vv@@~~@@~~$#2??}}GG}}??}}??-#1!14@\u001B\\foo…"),
                Arguments.of("emoji_tail", "\u001B[36mHello there!\u001B[m", "😃", 8, "\u001B[36mHello 😃\u001B[m"),
                Arguments.of("unicode", "\u001B[35mClaire's Boutique\u001B[0m", "", 8, "\u001B[35mClaire's\u001B[0m"),
                Arguments.of("wide_chars", "こんにちは", "…", 7, "こんに…"),
                Arguments.of("style_wide_chars", "\u001B[35mこんにちは\u001B[m", "…", 7, "\u001B[35mこんに…\u001B[m"),
                Arguments.of("osc8_lf", "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\\nสวัสดีสวัสดี\u001B]8;;\u001B\\", "…", 9, "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\\n…\u001B]8;;\u001B\\")
        );
    }
}