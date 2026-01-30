package com.williamcallahan.tui4j.compat.x.ansi;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for ANSI-aware word wrapping.
 */
class WordWrapTest {

    /** Validates WordWrap behavior against upstream cases. */
    @Test
    void testWordWrapCases() {
        List<Case> cases = List.of(
                new Case("empty string", "", 0, "", ""),
                new Case("passthrough", "foobar\n ", 0, "", "foobar\n "),
                new Case("pass", "foo", 3, "", "foo"),
                new Case("toolong", "foobarfoo", 4, "", "foobarfoo"),
                new Case("white space", "foo bar foo", 4, "", "foo\nbar\nfoo"),
                new Case("broken_at_spaces", "foo bars foobars", 4, "", "foo\nbars\nfoobars"),
                new Case("hyphen", "foo-foobar", 4, "-", "foo-\nfoobar"),
                new Case("emoji_breakpoint", "foo😃 foobar", 4, "😃", "foo😃\nfoobar"),
                new Case("wide_emoji_breakpoint", "foo\uD83E\uDEAB foobar", 4, "🫧", "foo\uD83E\uDEAB\nfoobar"),
                new Case("space_breakpoint", "foo --bar", 9, "-", "foo --bar"),
                new Case("simple", "foo bars foobars", 4, "", "foo\nbars\nfoobars"),
                new Case("limit", "foo bar", 5, "", "foo\nbar"),
                new Case("remove white spaces", "foo    \nb   ar   ", 4, "", "foo\nb\nar"),
                new Case("white space trail width", "foo\nb\t a\n bar", 4, "", "foo\nb\t a\n bar"),
                new Case("explicit_line_break", "foo bar foo\n", 4, "", "foo\nbar\nfoo\n"),
                new Case("explicit_breaks", "\nfoo bar\n\n\nfoo\n", 4, "", "\nfoo\nbar\n\n\nfoo\n"),
                new Case("example",
                        " This is a list: \n\n\t* foo\n\t* bar\n\n\n\t* foo  \nbar    ",
                        6,
                        "",
                        " This\nis a\nlist: \n\n\t* foo\n\t* bar\n\n\n\t* foo\nbar"),
                new Case("style_code_dont_affect_length",
                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m",
                        7,
                        "",
                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m"),
                new Case("style_code_dont_get_wrapped",
                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust another test\u001B[38;2;249;38;114m)\u001B[0m",
                        3,
                        "",
                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust\nanother\ntest\u001B[38;2;249;38;114m)\u001B[0m"),
                new Case("osc8_wrap",
                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\ สวัสดีสวัสดี\u001B]8;;\u001B\\",
                        8,
                        "",
                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\\nสวัสดีสวัสดี\u001B]8;;\u001B\\")
        );

        for (Case testCase : cases) {
            String actual = WordWrap.wordWrap(testCase.input, testCase.limit, testCase.breakpoints);
            assertEquals(testCase.expected, actual, testCase.name);
        }
    }

    /** Test case for word wrap. */
    private static final class Case {
        private final String name;
        private final String input;
        private final int limit;
        private final String breakpoints;
        private final String expected;

        /**
         * Creates a word wrap test case.
         *
         * @param name case name
         * @param input input string
         * @param limit wrap width
         * @param breakpoints breakpoint string
         * @param expected expected output
         */
        private Case(String name, String input, int limit, String breakpoints, String expected) {
            this.name = name;
            this.input = input;
            this.limit = limit;
            this.breakpoints = breakpoints;
            this.expected = expected;
        }
    }
}
