package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer.defaultRenderer;

/**
 * Tests style.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class StyleTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("styleData")
    void test_ShouldRenderStyledText(String caseName, String input, Style style, String expectedOutput) {
        // when
        String rendered = style.render(input);

        // then
        assertThat(rendered).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> styleData() {
        Renderer renderer = defaultRenderer();
        renderer.setColorProfile(ColorProfile.TrueColor);
        renderer.setHasDarkBackground(true);

        return Stream.of(
                Arguments.of("empty", "hello", renderer.newStyle(), "hello"),
                Arguments.of("margin right", "foo", renderer.newStyle().marginRight(1), "foo "),
                Arguments.of("margin left", "foo", renderer.newStyle().marginLeft(1), " foo"),
                Arguments.of("empty text margin left", "", renderer.newStyle().marginLeft(1), " "),
                Arguments.of("empty text margin right", "", renderer.newStyle().marginRight(1), " "),
                Arguments.of("color", "hello", renderer.newStyle().foreground(Color.color("#5A56E0")), "\u001B[38;2;90;86;224mhello\u001B[0m"),
                Arguments.of("adaptive color", "hello", renderer.newStyle().foreground(new AdaptiveColor("#fffe12", "#5A56E0")), "\u001B[38;2;90;86;224mhello\u001B[0m"),
                Arguments.of("bold", "hello", renderer.newStyle().bold(true), "\u001B[1mhello\u001B[0m"),
                Arguments.of("italic", "hello", renderer.newStyle().italic(true), "\u001B[3mhello\u001B[0m"),
                Arguments.of("underline", "hello", renderer.newStyle().underline(true), "\u001B[4mhello\u001B[0m"),
                Arguments.of("blink", "hello", renderer.newStyle().blink(true), "\u001B[5mhello\u001B[0m"),
                Arguments.of("faint", "hello", renderer.newStyle().faint(true), "\u001B[2mhello\u001B[0m")
        );
    }
}