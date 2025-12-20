package com.williamcallahan.tui4j.compat.bubbletea.input;

import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.ProgramException;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.ExtendedSequences;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.Key;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.message.BlurMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.FocusMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.message.UnknownSequenceMessage;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Port of Bubble Tea old laggy input handler.
 * Bubble Tea: bubbletea/inputreader_other.go
 */
public class OldLaggyInputHandler implements InputHandler {
    private static final Pattern MOUSE_SGR_REGEX = Pattern.compile("(\\d+);(\\d+);(\\d+)([Mm])");
    private final Terminal terminal;
    private final Consumer<Message> messageConsumer;
    private volatile boolean running;
    private final ExecutorService inputExecutor;
    private volatile boolean altPressed = false;

    // Buffer for handling escape sequences
    private static final int READ_TIMEOUT_MS = 50;

    public OldLaggyInputHandler(Terminal terminal, Consumer<Message> messageConsumer) {
        this.terminal = terminal;
        this.messageConsumer = messageConsumer;
        this.inputExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "tui4j-Input-Thread");
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void start() {
        if (!running) {
            running = true;
            inputExecutor.submit(this::handleInput);
        }
    }

    @Override
    public void stop() {
        running = false;
        inputExecutor.shutdownNow();
        try {
            inputExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleInput() {
        try {
            NonBlockingReader reader = terminal.reader();
            while (running) {
                int input = reader.read();
                if (input == -1) continue;

                if (input == '\u001b') { // ESC character
                    // Try to read the next character with a timeout
                    int nextChar = reader.read(READ_TIMEOUT_MS);
                    if (nextChar < 0) {
                        // If no character follows within timeout, it's a standalone ESC
                        messageConsumer.accept(new KeyPressMessage(new Key(KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyEscape))));
                        continue;
                    } else {
                        handleControlSequence(reader, (char) nextChar);
                    }
                    continue;
                }

                // Regular character press
                Key key = ExtendedSequences.getKey(String.valueOf((char) input));
                if (key != null) {
                    messageConsumer.accept(new KeyPressMessage(new Key(key.type(), key.runes())));
                } else {
                    messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{(char) input}, altPressed)));
                }

                if (altPressed) {
                    altPressed = false;
                }
            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted()) {
                throw new ProgramException("Unable to initialize keyboard input", e);
            }
        }
    }

    private void handleControlSequence(NonBlockingReader reader, char firstChar) throws IOException {
        StringBuilder sequence = new StringBuilder("\u001b");
        sequence.append(firstChar);

        int secondChar = reader.read(READ_TIMEOUT_MS);
        if (secondChar < 0) {
            Key key = ExtendedSequences.getKey(sequence.toString());
            if (key != null) {
                messageConsumer.accept(new KeyPressMessage(key));
            } else {
                messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{firstChar}, true)));
            }
            return;
        }

        sequence.append((char) secondChar);

        if (firstChar != '[') {
            Key key = ExtendedSequences.getKey(sequence.toString());
            if (key != null) {
                messageConsumer.accept(new KeyPressMessage(new Key(key.type())));
            } else {
                altPressed = true;
                messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{firstChar}, altPressed)));
            }
            return;
        }

        // Handle special cases based on secondChar
        switch (secondChar) {
            case 'I': // Focus event
                messageConsumer.accept(new FocusMessage());
                return;
            case 'O': // Blur event
                messageConsumer.accept(new BlurMessage());
                return;
            case 'M': // X10 Mouse Event
                handleX10MouseEvent(reader);
                return;
            case '<': // SGR Mouse Event
                handleSGRMouseEvent(reader);
                return;
        }

        // If not matched, continue reading the rest of the sequence
        while (true) {
            int ch = reader.read(READ_TIMEOUT_MS);
            if (ch <= 0) break;

            if (ch == 27) {
                Key key = ExtendedSequences.getKey(sequence.toString());
                if (key != null) {
                    messageConsumer.accept(new KeyPressMessage(key));
                } else {
                    messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, sequence.toString().toCharArray(), altPressed)));
                }

                if (altPressed) {
                    altPressed = false;
                }
                sequence = new StringBuilder();
            }
            sequence.append((char) ch);
        }

        // Attempt to resolve the sequence
        Key key = ExtendedSequences.getKey(sequence.toString());
        if (key != null) {
            messageConsumer.accept(new KeyPressMessage(key));
        } else {
            messageConsumer.accept(new UnknownSequenceMessage(sequence.toString()));
        }
    }


    private void handleX10MouseEvent(NonBlockingReader reader) throws IOException {
        // Read 3 bytes for button and coordinates
        int button = reader.read();
        int col = reader.read();
        int row = reader.read();

        messageConsumer.accept(MouseMessage.parseX10MouseEvent(button, col, row));
    }

    private void handleSGRMouseEvent(NonBlockingReader reader) throws IOException {
        StringBuilder buf = new StringBuilder();

        // Read until we find 'M' or 'm'
        int ch;
        while ((ch = reader.read(READ_TIMEOUT_MS)) != -1) {
            char c = (char) ch;
            buf.append(c);
            if (c == 'M' || c == 'm') {
                break;
            }
        }

        Matcher matcher = MOUSE_SGR_REGEX.matcher(buf.toString());
        if (matcher.matches()) {
            int button = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            int row = Integer.parseInt(matcher.group(3));
            boolean release = matcher.group(4).equals("m");

            messageConsumer.accept(MouseMessage.parseSGRMouseEvent(button, col, row, release));
        }
    }
}