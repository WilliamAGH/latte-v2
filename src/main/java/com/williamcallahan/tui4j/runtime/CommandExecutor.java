package com.williamcallahan.tui4j.runtime;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Runs commands and routes their messages.
 * tui4j: src/main/java/com/williamcallahan/tui4j/runtime/CommandExecutor.java
 */
public class CommandExecutor {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Creates a command executor.
     */
    public CommandExecutor() {
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * Executes a command if present and routes its message.
     *
     * @param command command to execute
     * @param messageConsumer message consumer
     * @param errorConsumer error consumer
     * @return future for completion
     */
    public CompletableFuture<Void> executeIfPresent(Command command,
                                                    Consumer<Message> messageConsumer,
                                                    Consumer<Throwable> errorConsumer) {
        if (!Command.isNone(command)) {
            return CompletableFuture
                    .supplyAsync(command::execute, executorService)
                    .thenAccept(msg -> {
                        if (msg != null) {
                            messageConsumer.accept(msg);
                        }
                    })
                    .exceptionally(ex -> {
                        errorConsumer.accept(ex);
                        return null;
                    });
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Executes a runnable task on the executor.
     *
     * @param task task to run
     */
    public void execute(Runnable task) {
        executorService.submit(task);
    }
}
