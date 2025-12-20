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
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Port of Bubble Tea new input handler.
 * Bubble Tea: bubbletea/inputreader_other.go
 */
public class NewInputHandler implements InputHandler {
    private static final Pattern MOUSE_SGR_REGEX = Pattern.compile("(\\d+);(\\d+);(\\d+)([Mm])");
    private final Terminal terminal;
    private final Consumer<Message> messageConsumer;
    private volatile boolean running;
    private final ExecutorService inputExecutor;

    private static final int PEEK_TIMEOUT_MS = 10;
    private static final int BUFFER_SIZE = 256;

    public NewInputHandler(Terminal terminal, Consumer<Message> messageConsumer) {
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
            char[] buffer = new char[BUFFER_SIZE];
            char[] leftover = new char[0];

            while (running) {
                int numRead = reader.read(buffer, 0, BUFFER_SIZE);
                if (numRead < 0) {
                    continue;
                }

                char[] inputChunk = Arrays.copyOfRange(buffer, 0, numRead);
                if (leftover.length > 0) {
                    inputChunk = append(leftover, inputChunk);
                    leftover = new char[0];
                }

                int i = 0;
                while (i < inputChunk.length) {
                    int processed = processOneMessage(Arrays.copyOfRange(inputChunk, i, inputChunk.length));
                    if (processed == 0) {
                        leftover = append(leftover, Arrays.copyOfRange(inputChunk, i, inputChunk.length));
                        break;
                    }
                    i += processed;
                }


            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted()) {
                throw new ProgramException("Unable to initialize keyboard input", e);
            }
        }
    }

    private int processOneMessage(char[] input) throws IOException {
        if (input.length == 0) return 0;

        char firstChar = input[0];

        if (firstChar == '\u001b') { // ESC character
            if (input.length == 1) {

                // ✅ Peek to check if more data is available
                NonBlockingReader reader = terminal.reader();
                if (reader.peek(PEEK_TIMEOUT_MS) < 0) {
                    // No data available, treat as standalone ESC
                    messageConsumer.accept(new KeyPressMessage(new Key(KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyEscape))));
                    return 1;
                }

                // Buffer might be incomplete, wait for more data
                return 0;
            }

            // Check if it's a known control sequence
            if (input[1] == '[' || input[1] == 'O') {
                return processControlSequence(input);
            }

            Key key = ExtendedSequences.getKey(new String(input));
            if (key != null) {
                messageConsumer.accept(new KeyPressMessage(key));
                return input.length;
            }

            // If there's no control sequence, treat ESC as Alt modifier
            if (input.length == 2) {
                messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{input[1]}, true)));
                return 2;
            }

            // If it's just ESC with no following characters, treat it as standalone ESC
            messageConsumer.accept(new KeyPressMessage(new Key(KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyEscape))));
            return 1;
        }

        // Regular character press
        Key key = ExtendedSequences.getKey(String.valueOf(firstChar));
        if (key != null) {
            messageConsumer.accept(new KeyPressMessage(key));
        } else {
            messageConsumer.accept(new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{firstChar}, false)));
        }

        return 1;
    }

    private int processControlSequence(char[] input) throws IOException {
        if (input.length < 2) return 0;
        char firstChar = input[1];

        if (firstChar == 'O') {
            if (input.length < 3) return 0; // Incomplete sequence
            char secondChar = input[2];
            Key key = ExtendedSequences.getKey("\u001bO" + secondChar);
            if (key != null) {
                messageConsumer.accept(new KeyPressMessage(key));
                return 3;
            }
        }

        if (firstChar == '[') {
            if (input.length < 3) return 0;
            char secondChar = input[2];

            // Focus & Blur Events
            if (secondChar == 'I') {
                messageConsumer.accept(new FocusMessage());
                return 3;
            } else if (secondChar == 'O') {
                messageConsumer.accept(new BlurMessage());
                return 3;
            }

            // X10 Mouse Event
            if (secondChar == 'M') {
                if (input.length < 6) return 0; // Need button, col, row
                handleX10MouseEvent(Arrays.copyOfRange(input, 3, 6));
                return 6;
            }

            // SGR Mouse Event
            if (secondChar == '<') {
                int endIdx = findEndIndex(input, 3, 'M', 'm');
                if (endIdx == -1) return 0;
                handleSGRMouseEvent(Arrays.copyOfRange(input, 3, endIdx + 1));
                return endIdx + 1;
            }

            // Arrow Keys and F5–F12
            StringBuilder sequence = new StringBuilder("\u001b[");
            for (int i = 2; i < input.length; i++) {
                sequence.append(input[i]);
                Key key = ExtendedSequences.getKey(sequence.toString());
                if (key != null) {
                    messageConsumer.accept(new KeyPressMessage(key));
                    return i + 1;
                }
                if (Character.isLetter(input[i]) || input[i] == '~') {
                    break;
                }
            }

            // If we reach here, it's an unrecognized sequence
            if (input.length > 5) { // Arbitrary limit to prevent infinite waiting
                messageConsumer.accept(new UnknownSequenceMessage(sequence.toString()));
                return sequence.length();
            }
        }

        return 0; // Incomplete sequence
    }


    private int findEndIndex(char[] input, int start, char... terminators) {
        for (int i = start; i < input.length; i++) {
            for (char term : terminators) {
                if (input[i] == term) return i;
            }
        }
        return -1;
    }

    private void handleX10MouseEvent(char[] input) {
        if (input.length < 3) return;
        int button = input[0];
        int col = input[1] - 32;
        int row = input[2] - 32;

        messageConsumer.accept(MouseMessage.parseX10MouseEvent(col, row, button));
    }

    private void handleSGRMouseEvent(char[] input) {
        Matcher matcher = MOUSE_SGR_REGEX.matcher(new String(input));
        if (matcher.matches()) {
            int button = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            int row = Integer.parseInt(matcher.group(3));
            boolean release = matcher.group(4).equals("m");

            messageConsumer.accept(MouseMessage.parseSGRMouseEvent(button, col, row, release));
        }
    }

    private char[] append(char[] firstArray, char[] secondArray) {
        char[] result = Arrays.copyOf(firstArray, firstArray.length + secondArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
        return result;
    }
}
