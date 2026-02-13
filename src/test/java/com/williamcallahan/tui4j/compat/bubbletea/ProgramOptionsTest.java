package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.render.NilRenderer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of github.com/charmbracelet/bubbletea/options_test.go.
 */
class ProgramOptionsTest {

    @Test
    void testOutputOption() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Program program = new Program(null, ProgramOption.withOutput(output),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])));

        assertThat(getConfigField(program, "output", ByteArrayOutputStream.class)).isSameAs(output);
    }

    @Test
    void testCustomInputOption() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Program program = new Program(null, ProgramOption.withInput(input),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        assertThat(getConfigField(program, "input", ByteArrayInputStream.class)).isSameAs(input);
        assertThat(getConfigBoolean(program, "inputDisabled")).isFalse();
    }

    @Test
    void testRendererOption() {
        Program program = new Program(null,
                ProgramOption.withoutRenderer(),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        Object renderer = getCoreField(program, "renderer", Object.class);
        assertThat(renderer).isInstanceOf(NilRenderer.class);
    }

    @Test
    void testWithoutSignalsOption() {
        Program program = new Program(null,
                ProgramOption.withoutSignals(),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        AtomicBoolean ignoreSignals = getConfigField(program, "ignoreSignals", AtomicBoolean.class);
        assertThat(ignoreSignals.get()).isTrue();
    }

    @Test
    void testFilterOption() {
        BiFunction<Model, Message, Message> filter = (model, msg) -> msg;
        Program program = new Program(null,
                ProgramOption.withFilter(filter),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        assertThat(getConfigField(program, "filter", BiFunction.class)).isSameAs(filter);
    }

    @Test
    void testContextOption() {
        CompletableFuture<?> cancelSignal = new CompletableFuture<>();
        Program program = new Program(null,
                ProgramOption.withContext(cancelSignal),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        CompletableFuture<?> actualCancelSignal = getConfigField(program, "cancelSignal", CompletableFuture.class);
        assertThat(actualCancelSignal).isSameAs(cancelSignal);
    }

    @Test
    void testInputOptions() {
        try {
            Program program = new Program(null,
                    ProgramOption.withInputTTY(),
                    ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                    ProgramOption.withOutput(new ByteArrayOutputStream()));

            assertThat(getConfigBoolean(program, "useInputTTY")).isTrue();
        } catch (RuntimeException e) {
            // In CI environments without TTY, Program.openInputTTY() fails with FileNotFoundException.
            // This exception proves withInputTTY() was applied - if it weren't, openInputTTY() wouldn't be called.
            assertThat(e.getCause())
                    .as("Expected FileNotFoundException when TTY unavailable")
                    .isInstanceOf(java.io.FileNotFoundException.class);
        }
    }

    @Test
    void testStartupOptions() {
        Program program = new Program(null,
                ProgramOption.withAltScreen(),
                ProgramOption.withoutBracketedPaste(),
                ProgramOption.withoutCatchPanics(),
                ProgramOption.withoutSignalHandler(),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        assertThat(getConfigBoolean(program, "enableAltScreen")).isTrue();
        assertThat(getConfigBoolean(program, "withoutBracketedPaste")).isTrue();
        assertThat(getConfigBoolean(program, "withoutCatchPanics")).isTrue();
        assertThat(getConfigBoolean(program, "withoutSignalHandler")).isTrue();
    }

    @Test
    void testMouseOptionsOverride() {
        Program cellMotion = new Program(null,
                ProgramOption.withMouseAllMotion(),
                ProgramOption.withMouseCellMotion(),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        assertThat(getConfigBoolean(cellMotion, "enableMouseCellMotion")).isTrue();
        assertThat(getConfigBoolean(cellMotion, "enableMouseAllMotion")).isFalse();

        Program allMotion = new Program(null,
                ProgramOption.withMouseCellMotion(),
                ProgramOption.withMouseAllMotion(),
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        assertThat(getConfigBoolean(allMotion, "enableMouseAllMotion")).isTrue();
        assertThat(getConfigBoolean(allMotion, "enableMouseCellMotion")).isFalse();
    }

    /**
     * Reads a field directly from ProgramCore (runtime state lives there).
     */
    private static <T> T getCoreField(Program program, String name, Class<T> type) {
        try {
            Field coreField = Program.class.getDeclaredField("core");
            coreField.setAccessible(true);
            Object core = coreField.get(program);

            Field field = ProgramCore.class.getDeclaredField(name);
            field.setAccessible(true);
            return type.cast(field.get(core));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field: " + name, e);
        }
    }

    /**
     * Reads a field from ProgramConfiguration via Program's config field.
     */
    private static <T> T getConfigField(Program program, String name, Class<T> type) {
        try {
            Field configField = Program.class.getDeclaredField("config");
            configField.setAccessible(true);
            Object config = configField.get(program);
            Field field = ProgramConfiguration.class.getDeclaredField(name);
            field.setAccessible(true);
            return type.cast(field.get(config));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read config field: " + name, e);
        }
    }

    /**
     * Reads a boolean config field from ProgramConfiguration.
     */
    private static boolean getConfigBoolean(Program program, String name) {
        return getConfigField(program, name, Boolean.class);
    }
}
