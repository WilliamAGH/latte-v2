package com.williamcallahan.tui4j.term.jline;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.RGBColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;
import com.williamcallahan.tui4j.term.TerminalInfo;
import com.williamcallahan.tui4j.term.TerminalInfoProvider;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * TerminalInfoProvider backed by JLine.
 * tui4j: src/main/java/com/williamcallahan/tui4j/term/jline/JLineTerminalInfoProvider.java
 */
public class JLineTerminalInfoProvider implements TerminalInfoProvider {

    private static class Response {

        String response;
        boolean isOSC;

        Response(String response, boolean isOSC) {
            this.response = response;
            this.isOSC = isOSC;
        }
    }

    private static final char ESC = '\u001b';
    private static final char BEL = '\u0007';
    private static final char LEFT_BRACKET = '[';
    private static final char RIGHT_BRACKET = ']';
    private static final char R = 'R';

    private boolean tty;
    private TerminalColor backgroundColor;

    public JLineTerminalInfoProvider(Terminal terminal) {
        readFromTerminal(terminal);
    }

    @Override
    public TerminalInfo provide() {
        return new TerminalInfo(tty, backgroundColor);
    }

    private void readFromTerminal(Terminal terminal) {
        this.tty = !"dumb".equals(terminal.getType());
        if (!tty) {
            return;
        }

        String backgroundColor = "";

        // Check if we're running in screen or tmux
        String term = System.getenv("TERM");
        if (term != null && (term.startsWith("screen") || term.startsWith("tmux"))) {
            return;
        }
        NonBlockingReader reader = terminal.reader();
        PrintWriter writer = terminal.writer();

        // Background color query
        writer.print(ESC + "]11;?" + ESC + "\\");
        // Cursor position query
        writer.print(ESC + "[6n");
        writer.flush();

        try {
            Response response = readNextResponse(reader);
            if (response != null && response.isOSC) {
                backgroundColor = response.response;
                // Read and discard cursor position response
                readNextResponse(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!backgroundColor.isEmpty()) {
            this.backgroundColor = parseColor(backgroundColor);
        }
    }

    private static Response readNextResponse(NonBlockingReader reader) throws IOException {
        StringBuilder response = new StringBuilder();

        // Find ESC
        char start = (char) reader.read(100);
        while (start != ESC) {
            int c = reader.read(100);
            if (c == -1) continue;
            start = (char) c;
        }
        response.append(start);

        // Read type character ([ or ])
        int typeInt = reader.read(100);
        if (typeInt == -1) return null;
        char type = (char) typeInt;
        response.append(type);

        boolean isOSC;
        if (type == LEFT_BRACKET) {
            isOSC = false;
        } else if (type == RIGHT_BRACKET) {
            isOSC = true;
        } else {
            return null;
        }

        // Read the rest of the response
        while (response.length() <= 25) {  // Safety limit
            int b = reader.read(100);
            if (b == -1) continue;

            char ch = (char) b;
            response.append(ch);

            if (isOSC) {
                // OSC can be terminated by BEL or ST (ESC \)
                if (ch == BEL ||
                        (ch == '\\' && response.charAt(response.length() - 2) == ESC)) {
                    return new Response(response.toString(), true);
                }
            } else {
                // Cursor position response is terminated by 'R'
                if (ch == R) {
                    return new Response(response.toString(), false);
                }
            }
        }

        return null;
    }

    private static RGBColor parseColor(String response) {
        // Check length validity
        if (response.length() < 24 || response.length() > 25) {
            throw new IllegalArgumentException("Invalid color format: incorrect length");
        }

        // Strip terminators
        String s = response;
        if (s.endsWith(String.valueOf(BEL))) {
            s = s.substring(0, s.length() - 1);
        } else if (s.endsWith(String.valueOf(ESC))) {
            s = s.substring(0, s.length() - 1);
        } else if (s.endsWith(ESC + "\\")) {  // ST
            s = s.substring(0, s.length() - 2);
        } else {
            throw new IllegalArgumentException("Invalid color format: no valid terminator");
        }

        // Skip first 4 chars (ESC + "]11")
        s = s.substring(4);

        // Check and remove ";rgb:" prefix
        String prefix = ";rgb:";
        if (!s.startsWith(prefix)) {
            throw new IllegalArgumentException("Invalid color format: missing rgb prefix");
        }
        s = s.substring(prefix.length());

        // Split into components and take first 2 chars of each
        String[] parts = s.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid color format: wrong number of components");
        }

        // Convert each component from 2-char hex to int
        try {
            int r = Integer.parseInt(parts[0].substring(0, 2), 16) / 255;
            int g = Integer.parseInt(parts[1].substring(0, 2), 16) / 255;
            int b = Integer.parseInt(parts[2].substring(0, 2), 16) / 255;
            return new RGBColor(r, g, b);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid color format: invalid hex values");
        }
    }
}
