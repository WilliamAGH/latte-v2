package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import java.time.Duration;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.timer.Timer} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Timer extends com.williamcallahan.tui4j.compat.bubbles.timer.Timer {

    /**
     * Creates a timer with default timeout and interval.
     *
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.timer.Timer#Timer()} instead.
     */
    @Deprecated(since = "0.3.0")
    public Timer() {
        super();
    }

    /**
     * Creates a timer with the specified timeout.
     *
     * @param timeout countdown duration
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.timer.Timer#Timer(Duration)} instead.
     */
    @Deprecated(since = "0.3.0")
    public Timer(Duration timeout) {
        super(timeout);
    }

    /**
     * Creates a timer with the specified timeout and interval.
     *
     * @param timeout countdown duration
     * @param interval tick interval
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.bubbles.timer.Timer#Timer(Duration, Duration)} instead.
     */
    @Deprecated(since = "0.3.0")
    public Timer(Duration timeout, Duration interval) {
        super(timeout, interval);
    }

}
