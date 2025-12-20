package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests hsl.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class HSLTest {

    @ParameterizedTest
    @MethodSource("darkAndLightColors")
    void test_ShouldRecognizeDarkColor(String hexColor, boolean isDark) {
        // given
        RGB rgb = RGB.fromHexString(hexColor);

        // when
        HSL hsl = rgb.toHSL();

        // then
        assertThat(hsl.isDark()).isEqualTo(isDark);
    }

    public static Stream<Arguments> darkAndLightColors() {
        return Stream.of(
                // dark colors
                Arguments.of("#000000", true),
                Arguments.of("#800000", true),
                Arguments.of("#008000", true),
                Arguments.of("#000080", true),
                Arguments.of("#404040", true),
                Arguments.of("#2F4F4F", true),
                Arguments.of("#800080", true),
                Arguments.of("#4B0082", true),

                // light colors
                Arguments.of("#FFFFFF", false),
                Arguments.of("#FFB6C1", false),
                Arguments.of("#90EE90", false),
                Arguments.of("#ADD8E6", false),
                Arguments.of("#E0E0E0", false),
                Arguments.of("#F0E68C", false),
                Arguments.of("#DDA0DD", false),
                Arguments.of("#F5F5DC", false)
        );
    }
}