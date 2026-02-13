package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.render.Renderer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jline.terminal.Terminal;

/**
 * Subprocess execution and terminal suspend/resume for {@link Program}.
 * <p>
 * This preserves Bubble Tea's behavior of suspending the renderer/terminal while
 * an external process runs, then resuming and re-rendering the model view.
 * <p>
 * Upstream: bubbletea/exec.go
 *
 * @see Program
 */
final class ProgramProcessExecutor {

    private static final Logger logger = Logger.getLogger(
        ProgramProcessExecutor.class.getName()
    );

    private final Terminal terminal;
    private final boolean terminalIsTty;
    private final Renderer renderer;
    private final Supplier<Model> currentModelSupplier;
    private final Consumer<Message> send;

    private volatile boolean isSuspended;

    ProgramProcessExecutor(
        Terminal terminal,
        boolean terminalIsTty,
        Renderer renderer,
        Supplier<Model> currentModelSupplier,
        Consumer<Message> send
    ) {
        this.terminal = Objects.requireNonNull(terminal, "terminal");
        this.terminalIsTty = terminalIsTty;
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.currentModelSupplier = Objects.requireNonNull(
            currentModelSupplier,
            "currentModelSupplier"
        );
        this.send = Objects.requireNonNull(send, "send");
    }

    /**
     * Executes an external process, blocking until completion.
     *
     * @param execProcessMessage process execution request
     */
    void executeProcess(ExecProcessMessage execProcessMessage) {
        Objects.requireNonNull(execProcessMessage, "execProcessMessage");

        // Run synchronously to block the event loop, matching Bubble Tea's behavior.
        Process process = execProcessMessage.process();
        BiConsumer<Integer, byte[]> outputHandler =
            execProcessMessage.outputHandler();
        BiConsumer<Integer, byte[]> errorHandler =
            execProcessMessage.errorHandler();

        suspend();

        try {
            // Drain stdout/stderr concurrently to prevent deadlock from filled buffers.
            CompletableFuture<byte[]> stdoutFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return process.getInputStream().readAllBytes();
                    } catch (IOException e) {
                        throw new UncheckedIOException(
                            "Failed to read stdout",
                            e
                        );
                    }
                });
            CompletableFuture<byte[]> stderrFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return process.getErrorStream().readAllBytes();
                    } catch (IOException e) {
                        throw new UncheckedIOException(
                            "Failed to read stderr",
                            e
                        );
                    }
                });

            int exitCode = process.waitFor();

            if (outputHandler != null) {
                byte[] stdoutBytes = stdoutFuture.get();
                outputHandler.accept(exitCode, stdoutBytes);
            }
            if (errorHandler != null) {
                byte[] error = stderrFuture.get();
                errorHandler.accept(exitCode, error);
            }

            send.accept(new ExecCompletedMessage(exitCode, null));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            send.accept(new ExecCompletedMessage(-1, e));
        } catch (java.util.concurrent.ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            logger.log(Level.WARNING, "Error reading process streams", cause);
            send.accept(new ExecCompletedMessage(-1, cause));
        } finally {
            resume(); // Restore terminal and renderer.
        }
    }

    /**
     * Suspends the renderer and terminal (if TTY) and shows the cursor.
     */
    void suspend() {
        if (isSuspended) {
            return;
        }
        isSuspended = true;
        renderer.showCursor();
        renderer.pause();
        if (terminalIsTty) {
            terminal.pause();
        }
    }

    /**
     * Resumes the terminal (if TTY) and renderer, then re-renders the model.
     */
    void resume() {
        if (!isSuspended) {
            return;
        }
        if (terminalIsTty) {
            terminal.resume();
        }
        renderer.resume();
        renderer.hideCursor();
        renderer.write(currentModelSupplier.get().view());
        isSuspended = false;
    }
}

