package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Converts scroll events into selection updates.
 * is held near the top/bottom edge.
 * <p>
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseSelectionAutoScroller.java
 */
public final class MouseSelectionAutoScroller {

    private final IntSupplier terminalHeight;
    private final MouseSelectionTracker selectionTracker;
    private final Consumer<Message> messageConsumer;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler;

    private volatile boolean enabled;
    private volatile int edgeRows = 1;
    private volatile int intervalMs = 50;

    private volatile boolean shift;
    private volatile boolean alt;
    private volatile boolean ctrl;

    public MouseSelectionAutoScroller(
            IntSupplier terminalHeight,
            MouseSelectionTracker selectionTracker,
            Consumer<Message> messageConsumer
    ) {
        this.terminalHeight = Objects.requireNonNull(terminalHeight, "terminalHeight");
        this.selectionTracker = Objects.requireNonNull(selectionTracker, "selectionTracker");
        this.messageConsumer = Objects.requireNonNull(messageConsumer, "messageConsumer");
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "tui4j-MouseSelection-Autoscroll-Thread");
            t.setDaemon(true);
            return t;
        });
    }

    public void enable() {
        enabled = true;
    }

    public void configure(int edgeRows, int intervalMs) {
        enabled = true;
        this.edgeRows = Math.max(edgeRows, 1);
        this.intervalMs = Math.max(intervalMs, 10);
    }

    public void start() {
        if (!enabled) {
            return;
        }
        if (!running.compareAndSet(false, true)) {
            return;
        }
        scheduler.scheduleAtFixedRate(this::tick, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) {
            return;
        }
        scheduler.shutdownNow();
        try {
            scheduler.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void onMouse(MouseMessage message) {
        if (message == null) {
            return;
        }
        // Keep modifier state consistent with the latest user interaction.
        shift = message.isShift();
        alt = message.isAlt();
        ctrl = message.isCtrl();
    }

    private void tick() {
        if (!enabled || !running.get()) {
            return;
        }
        if (!selectionTracker.isSelecting()) {
            return;
        }

        int height = terminalHeight.getAsInt();
        if (height <= 0) {
            return;
        }

        int row = selectionTracker.lastRow();
        int topEdge = edgeRows - 1;
        int bottomEdge = height - edgeRows;

        MouseButton wheel = null;
        if (row <= topEdge) {
            wheel = MouseButton.MouseButtonWheelUp;
        } else if (row >= bottomEdge) {
            wheel = MouseButton.MouseButtonWheelDown;
        }
        if (wheel == null) {
            return;
        }

        messageConsumer.accept(new MouseMessage(
                selectionTracker.lastColumn(),
                row,
                shift,
                alt,
                ctrl,
                MouseAction.MouseActionPress,
                wheel
        ));
    }
}

