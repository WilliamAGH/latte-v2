package com.williamcallahan.tui4j.runtime;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;

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

    public void shutdown() {
        executorService.shutdown();
    }

    public CompletableFuture<Void> executeIfPresent(Command command,
                                                    Consumer<Message> messageConsumer,
                                                    Consumer<Throwable> errorConsumer) {
        if (command != null) {
            return CompletableFuture
                    .supplyAsync(command::execute, executorService)
                    .thenAccept(messageConsumer)
                    .exceptionally(ex -> {
                        errorConsumer.accept(ex);
                        return null;
                    });
        }
        return CompletableFuture.completedFuture(null);
    }
}
