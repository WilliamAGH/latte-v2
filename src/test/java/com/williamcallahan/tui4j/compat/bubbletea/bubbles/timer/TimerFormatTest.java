package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests timer format.
 * Bubble Tea: bubbletea/examples/timer/main.go
 */
class TimerFormatTest {

    @Test
    void view_shouldFormatDurationsExactly() {
        assertFormatted(Duration.ZERO, "0s");
        assertFormatted(Duration.ofNanos(1), "1ns");
        assertFormatted(Duration.ofNanos(1_000), "1\u00B5s");
        assertFormatted(Duration.ofMillis(1), "1ms");

        assertFormatted(Duration.ofSeconds(1), "1s");
        assertFormatted(Duration.ofSeconds(1).plusNanos(230_000_000), "1.23s");

        assertFormatted(Duration.ofSeconds(59), "59s");
        assertFormatted(Duration.ofSeconds(60), "1m0s");
        assertFormatted(Duration.ofSeconds(61), "1m1s");

        assertFormatted(Duration.ofSeconds(3_599), "59m59s");
        assertFormatted(Duration.ofSeconds(3_600), "1h0m0s");
        assertFormatted(Duration.ofSeconds(3_661), "1h1m1s");
        assertFormatted(Duration.ofSeconds(3_661).plusNanos(1_000), "1h1m1.000001s");

        assertFormatted(Duration.ofSeconds(-1), "-1s");
        assertFormatted(Duration.ofMillis(-1), "-1ms");
    }

    private static void assertFormatted(Duration duration, String expected) {
        Timer timer = new Timer(duration, Duration.ofSeconds(1));
        assertThat(timer.view()).isEqualTo(expected);
    }
}

