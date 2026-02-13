package com.williamcallahan.tui4j.compat.bubbletea;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests test message.
 * tui4j: src/test/java/com/williamcallahan/tui4j/compat/bubbletea/ProgramTest.java
 */
class TestMessage implements Message {
}

class TestModel implements Model {

    private final List<String> executionOrder;

    TestModel(List<String> executionOrder) {
        this.executionOrder = executionOrder;
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TestMessage) {
            executionOrder.add("processed");
        }
        return new UpdateResult<>(this, null);
    }

    @Override
    public String view() {
        return "";
    }
}

class ProgramTest {

    @Test
    void test_ShouldExecuteCommandsInParallel_ForBatchMessage() {
        // given
        TestModel testModel = new TestModel(new ArrayList<>());
        Program program = new Program(testModel,
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        List<String> executionOrder = new ArrayList<>();

        Command slow = () -> {
            sleep(300);
            executionOrder.add("slow");
            return new TestMessage();
        };

        Command fast = () -> {
            sleep(100);  // Faster command
            executionOrder.add("fast");
            return new TestMessage();
        };

        Thread programThread = new Thread(program::run);
        programThread.start();
        program.waitForInit();

        // when
        program.send(new BatchMessage(
                slow,
                fast,
                () -> {
                    sleep(500);
                    return new QuitMessage();
                }
        ));

        try {
            programThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        assertEquals(Arrays.asList("fast", "slow"), executionOrder,
                "Fast command should complete before slow command when running in parallel");
    }

    @Test
    void test_ShouldExecuteCommandsInOrder_ForSequenceMessage() {
        // given
        TestModel testModel = new TestModel(new ArrayList<>());
        Program program = new Program(testModel,
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        List<String> executionOrder = new ArrayList<>();

        Command first = () -> {
            sleep(300);  // Longer delay
            executionOrder.add("first");
            return new TestMessage();
        };

        Command second = () -> {
            sleep(100);  // Shorter delay
            executionOrder.add("second");
            return new TestMessage();
        };

        Thread programThread = new Thread(program::run);
        programThread.start();
        program.waitForInit();


        // when
        program.send(new SequenceMessage(
                first,
                second,
                new Command() {
                    @Override
                    public Message execute() {
                        return new QuitMessage();
                    }
                }
        ));

        try {
            programThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        assertEquals(Arrays.asList("first", "second"), executionOrder,
                "Commands should execute in sequence despite different delays");
    }

    @Test
    void test_MessageHandling_InDifferentProgramStates() {
        // given
        List<String> executionOrder = new ArrayList<>();
        TestModel testModel = new TestModel(executionOrder);
        Program program = new Program(testModel,
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        // when
        assertEquals(0, executionOrder.size(), "Message before start should be dropped");

        Thread programThread = new Thread(program::run);
        programThread.start();
        program.waitForInit();

        program.send(new TestMessage());

        long startTime = System.currentTimeMillis();
        while (executionOrder.isEmpty() && System.currentTimeMillis() - startTime < 5000) {
            sleep(50);
        }

        // then
        assertEquals(1, executionOrder.size(), "Message during runtime should be processed");

        program.send(new QuitMessage());
        try {
            programThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExecProcessWaitsForCompletion() throws Exception {
        TestModel testModel = new TestModel(new ArrayList<>());
        Program program = new Program(testModel,
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        Thread programThread = new Thread(program::run);
        programThread.start();
        program.waitForInit();

        AtomicLong start = new AtomicLong();
        AtomicLong end = new AtomicLong();
        CountDownLatch completionLatch = new CountDownLatch(1);

        Command execCommand = () -> {
            start.set(System.nanoTime());
            try {
                Process process = new ProcessBuilder("/bin/sh", "-c", "sleep 0.2")
                        .redirectInput(ProcessBuilder.Redirect.PIPE)
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start();
                return new ExecProcessMessage(process, null, null);
            } catch (IOException e) {
                return new QuitMessage();
            }
        };

        Command markEnd = () -> {
            end.set(System.nanoTime());
            completionLatch.countDown();
            return new QuitMessage();
        };

        program.send(new SequenceMessage(
                execCommand,
                markEnd
        ));

        assertTrue(completionLatch.await(2, TimeUnit.SECONDS));
        programThread.join();
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
