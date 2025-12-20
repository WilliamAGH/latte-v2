package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.StandardBorder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests borders.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class BordersTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("testValues")
    void test_HasCorrectBordersValues(String caseName, Style style, int expectedX, int expectedY) {
        // when
        int gotX = style.getHorizontalBorderSize();
        int gotY = style.getVerticalBorderSize();

        // then
        assertThat(gotX).isEqualTo(expectedX);
        assertThat(gotY).isEqualTo(expectedY);

        // when
        gotX = style.getHorizontalFrameSize();
        gotY = style.getVerticalFrameSize();

        // then
        assertThat(gotX).isEqualTo(expectedX);
        assertThat(gotY).isEqualTo(expectedY);
    }

    private static Stream<Arguments> testValues() {
        return Stream.of(
                Arguments.of("Default style", Style.newStyle(), 0, 0),
                Arguments.of("Border(NormalBorder())", Style.newStyle().border(StandardBorder.NormalBorder), 2, 2),
                Arguments.of("Border(NormalBorder(true))", Style.newStyle().border(StandardBorder.NormalBorder, true), 2, 2),
                Arguments.of("Border(NormalBorder(true, false))", Style.newStyle().border(StandardBorder.NormalBorder, true, false), 0, 2),
                Arguments.of("Border(NormalBorder(true, true, false))", Style.newStyle().border(StandardBorder.NormalBorder, true, true, false), 2, 1),
                Arguments.of("Border(NormalBorder(true, true, false, false))", Style.newStyle().border(StandardBorder.NormalBorder, true, true, false, false), 1, 1),
                Arguments.of("BorderTop(true).BorderStyle(NormalBorder())", Style.newStyle().borderTop(true).borderDecoration(StandardBorder.NormalBorder), 0, 1),
                Arguments.of("BorderStyle(NormalBorder())", Style.newStyle().borderDecoration(StandardBorder.NormalBorder), 2, 2),
                Arguments.of("Custom BorderStyle", Style.newStyle().borderDecoration(new Border(
                        "", "", "123456789", "", "", "", "", "", "", "", "", "", ""
                )), 1, 0)
        );
    }
}