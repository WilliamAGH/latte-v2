package com.williamcallahan.tui4j.compat.bubbletea.bubbles.timer;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Countdown timer model (ported from charmbracelet/bubbles).
 * Bubble Tea: bubbletea/examples/timer/main.go
 */
public class Timer implements Model {

    private static final Duration DEFAULT_TIMEOUT = Duration.ZERO;
    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(1);

    private static final long NANOSECOND = 1L;
    private static final long MICROSECOND = 1_000L * NANOSECOND;
    private static final long MILLISECOND = 1_000L * MICROSECOND;
    private static final long SECOND = 1_000L * MILLISECOND;
    private static final long MINUTE = 60L * SECOND;
    private static final long HOUR = 60L * MINUTE;

    private static final AtomicInteger LAST_ID = new AtomicInteger(0);

    private Duration timeout;
    private Duration interval;
    private int id;
    private int tag;
    private boolean running;

    public Timer() {
        this(DEFAULT_TIMEOUT, DEFAULT_INTERVAL);
    }

    public Timer(Duration timeout) {
        this(timeout, DEFAULT_INTERVAL);
    }

    public Timer(Duration timeout, Duration interval) {
        this.timeout = timeout;
        this.interval = interval;
        this.running = true;
        this.id = nextId();
    }

    public int id() {
        return id;
    }

    public Duration timeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Duration interval() {
        return interval;
    }

    public void setInterval(Duration interval) {
        this.interval = interval;
    }

    public boolean running() {
        return !timedout() && running;
    }

    public boolean timedout() {
        return timeout.isZero() || timeout.isNegative();
    }

    @Override
    public Command init() {
        return tick();
    }

    @Override
    public UpdateResult<Timer> update(Message msg) {
        if (msg instanceof StartStopMessage startStopMessage) {
            if (startStopMessage.id() != 0 && startStopMessage.id() != id) {
                return UpdateResult.from(this);
            }
            running = startStopMessage.running();
            return UpdateResult.from(this, tick());
        }
        if (msg instanceof TickMessage tickMessage) {
            if (!running() || (tickMessage.id() != 0 && tickMessage.id() != id)) {
                return UpdateResult.from(this);
            }
            if (tickMessage.tag() > 0 && tickMessage.tag() != tag) {
                return UpdateResult.from(this);
            }

            timeout = timeout.minus(interval);
            return UpdateResult.from(this, Command.batch(tick(), timedoutCommand()));
        }

        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return formatDuration(timeout);
    }

    public Command start() {
        return startStop(true);
    }

    public Command stop() {
        return startStop(false);
    }

    public Command toggle() {
        return startStop(!running());
    }

    private static int nextId() {
        return LAST_ID.incrementAndGet();
    }

    private Command tick() {
        return Command.tick(interval, __ -> new TickMessage(id, timedout(), tag));
    }

    private Command timedoutCommand() {
        if (!timedout()) {
            return null;
        }
        return () -> new TimeoutMessage(id);
    }

    private Command startStop(boolean running) {
        return () -> new StartStopMessage(id, running);
    }

    private static String formatDuration(Duration duration) {
        long nanos = duration.toNanos();
        boolean negative = nanos < 0;
        long value = Math.abs(nanos);

        String formatted;
        if (value < SECOND) {
            formatted = formatSubSecond(value);
        } else {
            formatted = formatLargerThanSecond(value);
        }

        if (negative) {
            return "-" + formatted;
        }
        return formatted;
    }

    private static String formatSubSecond(long nanos) {
        if (nanos == 0) {
            return "0s";
        }
        if (nanos < MICROSECOND) {
            return formatWithUnit(nanos, 0, "ns");
        }
        if (nanos < MILLISECOND) {
            return formatWithUnit(nanos, 3, "\u00B5s");
        }
        return formatWithUnit(nanos, 6, "ms");
    }

    private static String formatLargerThanSecond(long nanos) {
        long secondsPart = (nanos / SECOND) % 60;
        long fraction = nanos % SECOND;

        StringBuilder out = new StringBuilder();
        out.append(formatInt(secondsPart));
        String frac = formatFraction(fraction, 9);
        if (!frac.isEmpty()) {
            out.append('.').append(frac);
        }
        out.append('s');

        long totalMinutes = nanos / MINUTE;
        if (totalMinutes > 0) {
            out.insert(0, 'm').insert(0, formatInt(totalMinutes % 60));

            long hours = nanos / HOUR;
            if (hours > 0) {
                out.insert(0, 'h').insert(0, formatInt(hours));
            }
        }

        return out.toString();
    }

    private static String formatWithUnit(long nanos, int precision, String unit) {
        long scale = pow10(precision);
        long intPart = precision == 0 ? nanos : nanos / scale;
        long fraction = precision == 0 ? 0 : nanos % scale;

        StringBuilder out = new StringBuilder();
        out.append(formatInt(intPart));
        String frac = formatFraction(fraction, precision);
        if (!frac.isEmpty()) {
            out.append('.').append(frac);
        }
        out.append(unit);
        return out.toString();
    }

    private static String formatFraction(long value, int precision) {
        if (precision == 0 || value == 0) {
            return "";
        }
        String raw = Long.toString(value);
        if (raw.length() < precision) {
            raw = "0".repeat(precision - raw.length()) + raw;
        }
        int end = raw.length();
        while (end > 0 && raw.charAt(end - 1) == '0') {
            end--;
        }
        return raw.substring(0, end);
    }

    private static String formatInt(long value) {
        return Long.toString(value);
    }

    private static long pow10(int precision) {
        long value = 1;
        for (int i = 0; i < precision; i++) {
            value *= 10;
        }
        return value;
    }
}
