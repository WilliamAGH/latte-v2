package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.runtime.CommandExecutor;
import java.util.Objects;
import java.util.function.Consumer;
import org.jline.utils.Signals;

/**
 * OS signal registration for {@link Program}.
 * <p>
 * Extracted to keep {@link Program} focused on orchestration.
 * <p>
 * Upstream: bubbletea/signals_unix.go
 *
 * @see Program
 * @see ProgramConfiguration
 */
final class ProgramSignals {

    private ProgramSignals() {
    }

    /**
     * Registers all signals used by Program.
     *
     * @param config program configuration
     * @param executor command executor
     * @param send message sink
     * @param sendError error sink
     */
    static void registerAll(
        ProgramConfiguration config,
        CommandExecutor executor,
        Consumer<Message> send,
        Consumer<Throwable> sendError
    ) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(executor, "executor");
        Objects.requireNonNull(send, "send");
        Objects.requireNonNull(sendError, "sendError");

        registerTermination(config, executor, send, sendError);
        registerSuspend(config, executor, send, sendError);
        registerResize(executor, send, sendError);
    }

    private static void registerTermination(
        ProgramConfiguration config,
        CommandExecutor executor,
        Consumer<Message> send,
        Consumer<Throwable> sendError
    ) {
        if (config.isWithoutSignalHandler()) {
            return;
        }
        Signals.register("INT", () -> {
            if (config.ignoreSignals().get()) {
                return;
            }
            executor.executeIfPresent(QuitMessage::new, send, sendError);
        });
        Signals.register("TERM", () -> {
            if (config.ignoreSignals().get()) {
                return;
            }
            executor.executeIfPresent(QuitMessage::new, send, sendError);
        });
    }

    private static void registerSuspend(
        ProgramConfiguration config,
        CommandExecutor executor,
        Consumer<Message> send,
        Consumer<Throwable> sendError
    ) {
        if (config.isWithoutSignalHandler()) {
            return;
        }
        Signals.register("TSTP", () -> {
            if (config.ignoreSignals().get()) {
                return;
            }
            executor.executeIfPresent(SuspendMessage::new, send, sendError);
        });
        Signals.register("CONT", () -> {
            if (config.ignoreSignals().get()) {
                return;
            }
            executor.executeIfPresent(ResumeMessage::new, send, sendError);
        });
    }

    private static void registerResize(
        CommandExecutor executor,
        Consumer<Message> send,
        Consumer<Throwable> sendError
    ) {
        Signals.register("WINCH", () ->
            executor.executeIfPresent(
                com.williamcallahan.tui4j.message.CheckWindowSizeMessage::new,
                send,
                sendError
            )
        );
        executor.executeIfPresent(
            com.williamcallahan.tui4j.message.CheckWindowSizeMessage::new,
            send,
            sendError
        );
    }
}

