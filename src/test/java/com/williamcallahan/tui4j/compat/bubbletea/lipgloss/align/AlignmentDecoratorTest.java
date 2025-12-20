package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.align;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests alignment decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class AlignmentDecoratorTest {

    @ParameterizedTest
    @MethodSource("verticalAlignmentData")
    void test_ShouldAlignVertically(String input, Position position, int height, String expected) {
        // when
        String aligned = AlignmentDecorator.alignTextVertical(input, position, height);

        // then
        assertThat(aligned).isEqualTo(expected);
    }

    private static Stream<Arguments> verticalAlignmentData() {
        return Stream.of(
                Arguments.of("Foo", Position.Top, 2, "Foo\n"),
                Arguments.of("Foo", Position.Center, 5, "\n\nFoo\n\n"),
                Arguments.of("Foo", Position.Bottom, 5, "\n\n\n\nFoo"),

                Arguments.of("Foo\nBar", Position.Bottom, 5, "\n\n\nFoo\nBar"),
                Arguments.of("Foo\nBar", Position.Center, 5, "\nFoo\nBar\n\n"),
                Arguments.of("Foo\nBar", Position.Top, 5, "Foo\nBar\n\n\n"),

                Arguments.of("Foo\nBar\nBaz", Position.Bottom, 5, "\n\nFoo\nBar\nBaz"),
                Arguments.of("Foo\nBar\nBaz", Position.Center, 5, "\nFoo\nBar\nBaz\n"),

                Arguments.of("Foo\nBar\nBaz", Position.Bottom, 3, "Foo\nBar\nBaz"),
                Arguments.of("Foo\nBar\nBaz", Position.Center, 3, "Foo\nBar\nBaz"),
                Arguments.of("Foo\nBar\nBaz", Position.Top, 3, "Foo\nBar\nBaz"),

                Arguments.of("Foo\n\n\n\nBar", Position.Bottom, 5, "Foo\n\n\n\nBar"),
                Arguments.of("Foo\n\n\n\nBar", Position.Center, 5, "Foo\n\n\n\nBar"),
                Arguments.of("Foo\n\n\n\nBar", Position.Top, 5, "Foo\n\n\n\nBar"),

                Arguments.of("Foo\nBar\nBaz", Position.Center, 9, "\n\n\nFoo\nBar\nBaz\n\n\n"),
                Arguments.of("Foo\nBar\nBaz", Position.Center, 10, "\n\n\nFoo\nBar\nBaz\n\n\n\n")
        );
    }
}