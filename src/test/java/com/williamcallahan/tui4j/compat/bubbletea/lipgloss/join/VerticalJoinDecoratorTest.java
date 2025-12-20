package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.join;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests vertical join decorator.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class VerticalJoinDecoratorTest {

    @ParameterizedTest
    @MethodSource("joinVerticalArguments")
    void test_ShouldJoinVertical(String caseName, Position position, String first, String second, String expected) {
        // when
        String joined = VerticalJoinDecorator.joinVertical(position, first, second);

        // then
        assertThat(joined).isEqualTo(expected);
    }

    private static Stream<Arguments> joinVerticalArguments() {
        return Stream.of(
                Arguments.of("pos0", Position.Left, "A", "BBBB", "A   \nBBBB"),
                Arguments.of("pos1", Position.Right, "A", "BBBB", "   A\nBBBB"),
                Arguments.of("pos0.25", new Position(0.25d), "A", "BBBB", " A  \nBBBB")
        );
    }
}