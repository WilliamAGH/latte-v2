package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.InputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.NewInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.NoopInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.input.WindowsInputHandler;
import com.williamcallahan.tui4j.term.TerminalInfo;
import com.williamcallahan.tui4j.term.jline.JLineTerminalInfoProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.DumbTerminal;

/**
 * Terminal and IO initialization for {@link Program}.
 * <p>
 * Extracted to keep {@link Program} focused on lifecycle orchestration.
 * <p>
 * Upstream: bubbletea/tty.go
 *
 * @see Program
 * @see ProgramConfiguration
 */
final class ProgramTerminal {

    private final Terminal terminal;
    private final boolean terminalIsTty;
    private final InputHandler inputHandler;
    private final InputStream openedInput;

    private ProgramTerminal(
        Terminal terminal,
        boolean terminalIsTty,
        InputHandler inputHandler,
        InputStream openedInput
    ) {
        this.terminal = terminal;
        this.terminalIsTty = terminalIsTty;
        this.inputHandler = inputHandler;
        this.openedInput = openedInput;
    }

    /**
     * Initializes the JLine terminal and input handler for a program.
     *
     * @param config program configuration
     * @param send message sink used by the input handler
     * @return initialized terminal state
     * @throws IOException when terminal initialization fails
     */
    static ProgramTerminal initialize(
        ProgramConfiguration config,
        Consumer<Message> send
    ) throws IOException {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(send, "send");

        ResolvedStreams streams = resolveStreams(config);
        Terminal terminal = buildTerminal(config, streams);

        boolean terminalIsTty = !isDumbTerminal(terminal);
        if (terminalIsTty) {
            terminal.enterRawMode();
        }

        TerminalInfo.provide(new JLineTerminalInfoProvider(terminal));

        // Wire environment to lipgloss for SSH/remote session support.
        if (config.environment() != null && !config.environment().isEmpty()) {
            com.williamcallahan.tui4j.compat.lipgloss.Renderer.defaultRenderer().setEnvironment(
                config.environment()
            );
        }

        InputHandler inputHandler = config.isInputDisabled()
            ? new NoopInputHandler()
            : createInputHandler(terminal, send);

        return new ProgramTerminal(
            terminal,
            terminalIsTty,
            inputHandler,
            streams.openedInput
        );
    }

    Terminal terminal() {
        return terminal;
    }

    boolean terminalIsTty() {
        return terminalIsTty;
    }

    InputHandler inputHandler() {
        return inputHandler;
    }

    /**
     * Closes any input stream opened via {@code withInputTTY()}, if present.
     * <p>
     * This is best-effort and intentionally silent.
     */
    void closeOpenedInput() {
        if (openedInput == null || openedInput == System.in) {
            return;
        }
        try {
            openedInput.close();
        } catch (IOException ignored) {
            // Best-effort close; keep silent.
        }
    }

    private static Terminal buildTerminal(
        ProgramConfiguration config,
        ResolvedStreams streams
    ) throws IOException {
        OutputStream resolvedOutput = streams.output;
        boolean systemTerminal = isSystemTerminal(
            config,
            streams.input,
            resolvedOutput
        );
        boolean useDumbTerminal = shouldUseDumbTerminal(
            config,
            streams.input,
            resolvedOutput
        );

        if (useDumbTerminal) {
            return new DumbTerminal(
                "tui4j-dumb",
                "dumb",
                streams.input,
                resolvedOutput,
                Charset.defaultCharset()
            );
        }

        TerminalBuilder builder = TerminalBuilder.builder()
            .jni(true)
            .system(systemTerminal);

        if (!systemTerminal) {
            builder.streams(streams.input, resolvedOutput);
        }

        return builder.build();
    }

    private static ResolvedStreams resolveStreams(ProgramConfiguration config)
        throws IOException {
        InputStream openedInput = null;
        InputStream resolvedInput = config.input();
        if (config.isUseInputTTY()) {
            openedInput = openInputTTY();
            resolvedInput = openedInput;
        }

        if (resolvedInput == null) {
            config.setInputDisabled(true);
            resolvedInput = System.in;
        }

        OutputStream resolvedOutput =
            config.output() == null ? System.out : config.output();

        return new ResolvedStreams(resolvedInput, resolvedOutput, openedInput);
    }

    private static boolean isSystemTerminal(
        ProgramConfiguration config,
        InputStream resolvedInput,
        OutputStream resolvedOutput
    ) {
        if (config.isUseInputTTY()) {
            return false;
        }
        boolean inputIsSystem = resolvedInput == System.in;
        boolean outputIsSystem =
            resolvedOutput == System.out || resolvedOutput == System.err;
        return inputIsSystem && outputIsSystem;
    }

    private static boolean shouldUseDumbTerminal(
        ProgramConfiguration config,
        InputStream resolvedInput,
        OutputStream resolvedOutput
    ) {
        if (config.isUseInputTTY()) {
            return false;
        }
        return !isSystemTerminal(config, resolvedInput, resolvedOutput);
    }

    private static boolean isDumbTerminal(Terminal terminal) {
        String type = terminal.getType();
        return type != null && type.startsWith("dumb");
    }

    private static InputStream openInputTTY() throws IOException {
        String osName = System.getProperty("os.name", "").toLowerCase(
            Locale.ROOT
        );
        if (osName.contains("win")) {
            return new FileInputStream("CONIN$");
        }
        return new FileInputStream("/dev/tty");
    }

    private static InputHandler createInputHandler(
        Terminal terminal,
        Consumer<Message> send
    ) {
        if (isWindows()) {
            return new WindowsInputHandler(terminal, send);
        }
        return new NewInputHandler(terminal, send);
    }

    private static boolean isWindows() {
        String osName = System.getProperty("os.name");
        return (
            osName != null && osName.toLowerCase(Locale.ROOT).contains("win")
        );
    }

    private record ResolvedStreams(
        InputStream input,
        OutputStream output,
        InputStream openedInput
    ) {
    }
}
