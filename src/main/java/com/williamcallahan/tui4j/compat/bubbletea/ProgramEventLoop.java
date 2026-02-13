package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import com.williamcallahan.tui4j.compat.bubbletea.render.Renderer;
import com.williamcallahan.tui4j.message.SequencedMessage;
import com.williamcallahan.tui4j.runtime.CommandExecutor;
import com.williamcallahan.tui4j.runtime.UrlOpener;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

/**
 * Core message polling loop and system message dispatch for {@link Program}.
 * <p>
 * Extracted to keep {@link Program} focused on public API and orchestration.
 * <p>
 * Upstream: bubbletea/tea.go
 *
 * @see Program
 */
final class ProgramEventLoop {

    private static final Logger logger = Logger.getLogger(
        ProgramEventLoop.class.getName()
    );

    private final AtomicBoolean isRunning;
    private final BlockingQueue<Message> messageQueue;
    private final ProgramConfiguration config;
    private final Renderer renderer;
    private final Terminal terminal;
    private final AtomicReference<Model> currentModel;
    private final CommandExecutor commandExecutor;
    private final ProgramMouseHandler mouseHandler;
    private final ProgramProcessExecutor processExecutor;
    private final Consumer<Message> send;
    private final Consumer<Throwable> sendError;

    private long currentSequenceId = 0;
    private long lastHandledSequenceId = 0;

    ProgramEventLoop(
        AtomicBoolean isRunning,
        BlockingQueue<Message> messageQueue,
        ProgramConfiguration config,
        Renderer renderer,
        Terminal terminal,
        AtomicReference<Model> currentModel,
        CommandExecutor commandExecutor,
        ProgramMouseHandler mouseHandler,
        ProgramProcessExecutor processExecutor,
        Consumer<Message> send,
        Consumer<Throwable> sendError
    ) {
        this.isRunning = Objects.requireNonNull(isRunning, "isRunning");
        this.messageQueue = Objects.requireNonNull(messageQueue, "messageQueue");
        this.config = Objects.requireNonNull(config, "config");
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.terminal = Objects.requireNonNull(terminal, "terminal");
        this.currentModel = Objects.requireNonNull(currentModel, "currentModel");
        this.commandExecutor = Objects.requireNonNull(
            commandExecutor,
            "commandExecutor"
        );
        this.mouseHandler = Objects.requireNonNull(mouseHandler, "mouseHandler");
        this.processExecutor = Objects.requireNonNull(
            processExecutor,
            "processExecutor"
        );
        this.send = Objects.requireNonNull(send, "send");
        this.sendError = Objects.requireNonNull(sendError, "sendError");
    }

    /**
     * Runs the event loop until quit, error, or program termination.
     *
     * @return final model and optional error
     */
    Result run() {
        while (isRunning.get()) {
            Message msg;
            try {
                msg = messageQueue.poll(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (msg == null) {
                continue;
            }

            if (config.filter() != null) {
                msg = config.filter().apply(currentModel.get(), msg);
            }

            if (msg == null) {
                continue;
            }

            Message internalMsg = normalizeMessage(msg);
            Message updateMsg = internalMsg;

            if (internalMsg instanceof SequencedMessage(var seqMessage, var sequenceId)) {
                if (sequenceId < lastHandledSequenceId) {
                    continue;
                }
                lastHandledSequenceId = sequenceId;
                if (seqMessage == null) {
                    continue;
                }
                updateMsg = normalizeMessage(seqMessage);
                internalMsg = updateMsg;
            }

            if (handleSystemMessage(internalMsg)) {
                continue;
            }

            if (internalMsg instanceof QuitMessage) {
                return new Result(currentModel.get(), null);
            }

            if (internalMsg instanceof ErrorMessage errorMessage) {
                return new Result(currentModel.get(), errorMessage.error());
            }

            if (internalMsg instanceof MouseMessage mouseMessage) {
                mouseHandler.handleMouse(
                    mouseMessage,
                    currentModel.get(),
                    renderer
                );
            }

            // Process internal messages for the renderer.
            renderer.handleMessage(internalMsg);

            UpdateResult<? extends Model> updateResult =
                currentModel.get().update(updateMsg);

            currentModel.set(updateResult.model());
            renderer.notifyModelChanged();
            commandExecutor.executeIfPresent(
                updateResult.command(),
                send,
                sendError
            );

            renderer.write(currentModel.get().view());
        }

        return new Result(currentModel.get(), null);
    }

    private boolean handleSystemMessage(Message msg) {
        return switch (msg) {
            case ClearScreenMessage ignored -> {
                renderer.clearScreen();
                yield true;
            }
            case EnterAltScreenMessage ignored -> {
                renderer.enterAltScreen();
                yield true;
            }
            case ExitAltScreenMessage ignored -> {
                renderer.exitAltScreen();
                yield true;
            }
            case BatchMessage batchMessage -> {
                handleBatch(batchMessage.commands());
                yield true;
            }
            case SequenceMessage sequenceMessage -> {
                handleSequence(sequenceMessage.commands());
                yield true;
            }
            case com.williamcallahan.tui4j.message.CheckWindowSizeMessage ignored -> {
                commandExecutor.executeIfPresent(
                    this::checkSize,
                    send,
                    sendError
                );
                yield true;
            }
            case OpenUrlMessage(var url) -> {
                handleOpenUrl(url);
                yield true;
            }
            case ExecProcessMessage execProcessMessage -> {
                processExecutor.executeProcess(execProcessMessage);
                yield true;
            }
            case SuspendMessage ignored -> {
                processExecutor.suspend();
                yield true;
            }
            case ResumeMessage ignored -> {
                processExecutor.resume();
                yield true;
            }
            default -> false;
        };
    }

    private static Message normalizeMessage(Message msg) {
        if (msg instanceof MessageShim shim) {
            return shim.toMessage();
        }
        return msg;
    }

    private void handleBatch(Command... commands) {
        Arrays.stream(commands).forEach(command ->
            commandExecutor.executeIfPresent(command, send, sendError)
        );
    }

    private void handleSequence(Command... commands) {
        long sequenceId = ++currentSequenceId;
        CompletableFuture<Void> chain = Arrays.stream(commands).reduce(
            CompletableFuture.completedFuture(null),
            (CompletableFuture<Void> future, Command command) ->
                future.thenCompose(ignored ->
                    commandExecutor.executeIfPresent(
                        command,
                        msg -> send.accept(new SequencedMessage(msg, sequenceId)),
                        sendError
                    )
                ),
            (f1, f2) -> f2
        );
        chain.exceptionally(e -> {
            sendError.accept(e);
            return null;
        });
    }

    private void handleOpenUrl(String url) {
        boolean success = UrlOpener.open(url);
        if (!success) {
            logger.log(Level.WARNING, "Failed to open URL: {0}", url);
        }
    }

    private Message checkSize() {
        Size size = terminal.getSize();
        return new WindowSizeMessage(size.getColumns(), size.getRows());
    }

    record Result(Model finalModel, Throwable error) {
        Result {
            Objects.requireNonNull(finalModel, "finalModel");
        }
    }
}

