package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.join;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests horizontal join decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class HorizontalJoinDecoratorTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("joinHorizontalArguments")
    void test_ShouldJoinHorizontal(String caseName, Position position, String first, String second, String expected) {
        // when
        String joined = HorizontalJoinDecorator.joinHorizontal(position, first, second);

        // then
        assertThat(joined).isEqualTo(expected);
    }

    private static Stream<Arguments> joinHorizontalArguments() {
        return Stream.of(
                Arguments.of("pos0", Position.Top, "A", "B\nB\nB\nB", "AB\n B\n B\n B"),
                Arguments.of("pos1", Position.Bottom, "A", "B\nB\nB\nB", " B\n B\n B\nAB"),
                Arguments.of("pos0.25", new Position(0.25d), "A", "B\nB\nB\nB", " B\nAB\n B\n B")
        );
    }
}