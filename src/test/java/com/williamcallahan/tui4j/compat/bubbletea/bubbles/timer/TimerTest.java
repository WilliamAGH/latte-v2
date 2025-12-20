package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.BatchMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests timer.
 * Bubble Tea: bubbletea/examples/timer/main.go
 */
class TimerTest {

    @Test
    void testRunningAndTimedoutState() {
        Timer timer = new Timer(Duration.ofSeconds(2), Duration.ofSeconds(1));
        assertThat(timer.running()).isTrue();
        assertThat(timer.timedout()).isFalse();
    }

    @Test
    void testStartStopMessageUpdatesRunningState() {
        Timer timer = new Timer(Duration.ofSeconds(2), Duration.ofSeconds(1));
        timer.update(new StartStopMessage(timer.id(), false));
        assertThat(timer.running()).isFalse();
    }

    @Test
    void testTickDecrementsTimeoutAndBatchesCommands() {
        Timer timer = new Timer(Duration.ofSeconds(1), Duration.ofMillis(250));
        TickMessage tickMessage = new TickMessage(timer.id(), false, 0);

        UpdateResult<? extends com.williamcallahan.tui4j.Model> result = timer.update(tickMessage);

        assertThat(timer.timeout()).isEqualTo(Duration.ofMillis(750));
        Message batch = result.command().execute();
        assertThat(batch).isInstanceOf(BatchMessage.class);
    }

    @Test
    void testTimeoutMessageEmittedWhenExpired() {
        Timer timer = new Timer(Duration.ofMillis(10), Duration.ofMillis(10));
        TickMessage tickMessage = new TickMessage(timer.id(), false, 0);

        UpdateResult<? extends com.williamcallahan.tui4j.Model> result = timer.update(tickMessage);
        BatchMessage batchMessage = (BatchMessage) result.command().execute();
        Command timeoutCommand = batchMessage.commands()[1];

        Message timeoutMessage = timeoutCommand.execute();
        assertThat(timeoutMessage).isInstanceOf(TimeoutMessage.class);
    }

    @Test
    void testTickIgnoredWhenStopped() {
        Timer timer = new Timer(Duration.ofSeconds(1), Duration.ofMillis(200));
        timer.update(new StartStopMessage(timer.id(), false));

        Duration before = timer.timeout();
        UpdateResult<? extends com.williamcallahan.tui4j.Model> result = timer.update(new TickMessage(timer.id(), false, 0));

        assertThat(timer.timeout()).isEqualTo(before);
        assertThat(result.command()).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideDurations")
    void testViewFormatting(Duration duration, String expected) {
        Timer timer = new Timer(Duration.ofSeconds(1));
        timer.setTimeout(duration);

        assertThat(timer.view()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDurations() {
        return Stream.of(
                Arguments.of(Duration.ZERO, "0s"),
                Arguments.of(Duration.ofNanos(1), "1ns"),
                Arguments.of(Duration.ofNanos(1_500), "1.5\u00B5s"),
                Arguments.of(Duration.ofMillis(1), "1ms"),
                Arguments.of(Duration.ofMillis(1_500), "1.5s"),
                Arguments.of(Duration.ofSeconds(65), "1m5s"),
                Arguments.of(Duration.ofHours(1).plusMinutes(2).plusSeconds(3), "1h2m3s")
        );
    }
}
