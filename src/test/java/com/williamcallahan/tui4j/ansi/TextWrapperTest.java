package com.williamcallahan.tui4j.ansi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests text wrapper.
 * tui4j: src/test/java/com/williamcallahan/tui4j/ansi/TextWrapperTest.java
 */
class TextWrapperTest {

    @Test
    void testSimpleWrapping() {
        String text = "the quick brown fox";
        String expected = "the quick\nbrown fox";
        assertEquals(expected, new TextWrapper().wrap(text, 10));
    }

    @Test
    void testLongerWrapping() {
        String text = "the quick brown foxxxxxxxxxxxxxxxx jumped over the lazy dog.";
        String expected = "the quick brown\nfoxxxxxxxxxxxxxx\nxx jumped over\nthe lazy dog.";
        assertEquals(expected, new TextWrapper().wrap(text, 16));
    }

    @Test
    void testReallyLongText() {
        String text = "This is a long text and it should be wrapped after 15 characters, we will see how it presents ...";
        String expected = "This is a long\ntext and it\nshould be\nwrapped after\n15 characters,\nwe will see how\nit presents ...";
        assertEquals(expected, new TextWrapper().wrap(text, 15));
    }

    @Test
    void testDoubleSpaces() {
        String text = "f  bar foobaz";
        String expected = "f  bar\nfoobaz";
        assertEquals(expected, new TextWrapper().wrap(text, 6));
    }

    @Test
    void testNewlinesInInput() {
        String text = "line1\nline2";
        String expected = "line1\nline2";
        assertEquals(expected, new TextWrapper().wrap(text, 10));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("wrapCases")
    void testWrap(String name, String input, int limit, String expected) {
        // when
        String result = new TextWrapper().wrap(input, limit);

        // then
        assertEquals(expected, result, "Test case '" + name + "' failed");
    }

    public static Stream<Arguments> wrapCases() {
        return Stream.of(
                Arguments.of("empty string", "", 0, ""),
                Arguments.of("simple", "I really \u001B[38;2;249;38;114mlove\u001B[0m Java!", 8,
                        "I really\n\u001B[38;2;249;38;114mlove\u001B[0m\nJava!"),
                Arguments.of("passthrough", "hello world ", 11, "hello world"),
                Arguments.of("asian", "こんにち", 7, "こんに\nち"),
                Arguments.of("emoji", "😃👰🏻‍♀️🫧", 2, "😃\n👰🏻‍♀️\n🫧"),
                Arguments.of("long style", "\u001B[38;2;249;38;114ma really long string\u001B[0m", 10,
                        "\u001B[38;2;249;38;114ma really\nlong\nstring\u001B[0m"),
                Arguments.of("long style nbsp", "\u001B[38;2;249;38;114ma really\u00a0long string\u001B[0m", 10,
                        "\u001B[38;2;249;38;114ma\nreally\u00a0lon\ng string\u001B[0m"),
                Arguments.of("longer", "the quick brown foxxxxxxxxxxxxxxxx jumped over the lazy dog.", 16,
                        "the quick brown\nfoxxxxxxxxxxxxxx\nxx jumped over\nthe lazy dog."),
                Arguments.of("longer asian", "猴 猴 猴猴 猴猴猴猴猴猴猴猴猴 猴猴猴 猴猴 猴’ 猴猴 猴.", 16,
                        "猴 猴 猴猴\n猴猴猴猴猴猴猴猴\n猴 猴猴猴 猴猴\n猴’ 猴猴 猴."),
                Arguments.of("long input", "Rotated keys for a-good-offensive-cheat-code-incorporated/animal-like-law-on-the-rocks.", 76,
                        "Rotated keys for a-good-offensive-cheat-code-incorporated/animal-like-law-\non-the-rocks."),
                Arguments.of("long input2", "Rotated keys for a-good-offensive-cheat-code-incorporated/crypto-line-operating-system.", 76,
                        "Rotated keys for a-good-offensive-cheat-code-incorporated/crypto-line-\noperating-system."),
                Arguments.of("hyphen breakpoint", "a-good-offensive-cheat-code", 10, "a-good-\noffensive-\ncheat-code"),
                Arguments.of("exact", "\u001b[91mfoo\u001b[0", 3, "\u001b[91mfoo\u001b[0"),
                Arguments.of("extra space", "foo ", 3, "foo"),
                Arguments.of(
                        "paragraph with styles",
                        "Lorem ipsum dolor \u001b[1msit\u001b[m amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \u001b[31mUt enim\u001b[m ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea \u001b[38;5;200mcommodo consequat\u001b[m. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \u001b[1;2;33mExcepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\u001b[m",
                        30,
                        """
                                Lorem ipsum dolor \u001b[1msit\u001b[m amet,
                                consectetur adipiscing elit,
                                sed do eiusmod tempor
                                incididunt ut labore et dolore
                                magna aliqua. \u001b[31mUt enim\u001b[m ad minim
                                veniam, quis nostrud
                                exercitation ullamco laboris
                                nisi ut aliquip ex ea \u001b[38;5;200mcommodo
                                consequat\u001b[m. Duis aute irure
                                dolor in reprehenderit in
                                voluptate velit esse cillum
                                dolore eu fugiat nulla
                                pariatur. \u001b[1;2;33mExcepteur sint
                                occaecat cupidatat non
                                proident, sunt in culpa qui
                                officia deserunt mollit anim
                                id est laborum.\u001b[m"""
                ),
                Arguments.of("hyphen break", "foo-bar", 5, "foo-\nbar"),
                Arguments.of("double space", "f  bar foobaz", 6, "f  bar\nfoobaz"),
                Arguments.of("passtrough", "foobar\n ", 0, "foobar\n "),
                Arguments.of("pass", "foo", 3, "foo"),
                Arguments.of("toolong", "foobarfoo", 4, "foob\narfo\no"),
                Arguments.of("white space", "foo bar foo", 4, "foo\nbar\nfoo"),
                Arguments.of("broken_at_spaces", "foo bars foobars", 4, "foo\nbars\nfoob\nars"),
                Arguments.of("hyphen", "foob-foobar", 4, "foob\n-foo\nbar"),
                Arguments.of("wide_emoji_breakpoint", "foo🫧 foobar", 4, "foo\n🫧\nfoob\nar"),
                Arguments.of("space_breakpoint", "foo --bar", 9, "foo --bar"),
                Arguments.of("simple", "foo bars foobars", 4, "foo\nbars\nfoob\nars"),
                Arguments.of("limit", "foo bar", 5, "foo\nbar"),
                Arguments.of("remove white spaces", "foo    \nb   ar   ", 4, "foo\nb\nar"),
                Arguments.of("white space trail width", "foo\nb\t a\n bar", 4, "foo\nb\t a\n bar"),
                Arguments.of("explicit_line_break", "foo bar foo\n", 4, "foo\nbar\nfoo\n"),
                Arguments.of("explicit_breaks", "\nfoo bar\n\n\nfoo\n", 4, "\nfoo\nbar\n\n\nfoo\n"),
                Arguments.of("example", " This is a list: \n\n\t* foo\n\t* bar\n\n\n\t* foo  \nbar    ", 6, " This\nis a\nlist: \n\n\t* foo\n\t* bar\n\n\n\t* foo\nbar"),
                Arguments.of(
                        "style_code_dont_affect_length",
                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m",
                        7,
                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m"
                ),
                Arguments.of(
                        "style_code_dont_get_wrapped",
                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust another test\u001B[38;2;249;38;114m)\u001B[0m",
                        7,
                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust\nanother\ntest\u001B[38;2;249;38;114m)\u001B[0m"
                ),
                Arguments.of(
                        "osc8_wrap",
                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\ สวัสดีสวัสดี\u001B]8;;\u001B\\",
                        8,
                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\\nสวัสดีสวัสดี\u001B]8;;\u001B\\"
                ),
                Arguments.of("tab", "foo\tbar", 3, "foo\nbar"),
                Arguments.of("red background", "\u001B[41mThis is a test of a frame\u001B[0m", 10,
                        "\u001B[41mThis is a\ntest of a\nframe\u001B[0m")
        );
    }
}