package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

/**
 * Status message lifecycle management for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
final class ListStatusMessageManager {

    private ListStatusMessageManager() {
    }

    static Command newStatusMessage(List list, String status) {
        list.statusMessage = status;
        cancelPendingTimeout(list);

        CompletableFuture<Message> future = new CompletableFuture<>();
        Timer timer = new Timer();
        list.statusMessageFuture = future;
        list.statusMessageTimer = timer;

        return () -> {
            if (future.isCancelled()) {
                timer.cancel();
                return null;
            }
            timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        future.complete(new StatusMessageTimeoutMessage());
                    }
                },
                list.statusMessageLifetime.toMillis()
            );

            try {
                return future.join();
            } catch (CancellationException e) {
                // Future was cancelled by a new status message or hideStatusMessage.
                return null;
            } finally {
                timer.cancel();
                if (list.statusMessageTimer == timer) {
                    list.statusMessageTimer = null;
                }
            }
        };
    }

    static void hideStatusMessage(List list) {
        list.statusMessage = "";
        cancelPendingTimeout(list);
    }

    /** Cancels any pending status message timeout and its associated timer. */
    private static void cancelPendingTimeout(List list) {
        if (list.statusMessageFuture != null && !list.statusMessageFuture.isDone()) {
            list.statusMessageFuture.cancel(true);
        }
        if (list.statusMessageTimer != null) {
            list.statusMessageTimer.cancel();
        }
    }
}

