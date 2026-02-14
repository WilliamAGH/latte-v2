package com.williamcallahan.tui4j.compat.bubbletea.render;

import com.williamcallahan.tui4j.ansi.Truncate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

/**
 * Diff-based rendering pipeline for the standard renderer.
 * <p>
 * Owns the render lock, output buffer, and line-diffing state. All terminal
 * writes that require synchronization go through this class.
 * <p>
 * Upstream: bubbletea/standard_renderer.go (flush/write/repaint logic)
 */
class RendererFlush {

    private final Terminal terminal;
    private final Lock renderLock = new ReentrantLock();
    private final StringBuilder buffer = new StringBuilder();
    private volatile String lastRender = "";
    private String[] lastRenderedLines = new String[0];
    private final List<String> queuedMessageLines = new ArrayList<>();
    private volatile boolean needsRender = true;
    private int linesRendered = 0;
    private boolean isInAltScreen;

    /**
     * Creates a flush pipeline for the given terminal.
     *
     * @param terminal JLine terminal to write output to
     */
    RendererFlush(Terminal terminal) {
        this.terminal = terminal;
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go flush behavior.
    void flush(int width, int height) {
        if (!needsRender) {
            return;
        }

        renderLock.lock();
        try {
            if (buffer.isEmpty() || buffer.toString().equals(lastRender)) {
                return;
            }

            StringBuilder out = new StringBuilder();
            String[] newLines = splitAndTruncateHeight(height);

            if (linesRendered > 1) {
                out.append("\033[").append(linesRendered - 1).append("A");
            }

            boolean didFlushQueued = flushQueuedMessages(out, width);
            renderDiffLines(out, newLines, didFlushQueued, width);

            if (linesRendered > newLines.length) {
                out.append("\033[J");
            }

            out.append("\r");

            terminal.writer().print(out);
            terminal.writer().flush();

            lastRender = buffer.toString();
            lastRenderedLines = newLines;
            linesRendered = newLines.length;
            needsRender = false;
        } finally {
            renderLock.unlock();
        }
    }

    private String[] splitAndTruncateHeight(int height) {
        String[] newLines = buffer.toString().split("\n");
        if (height > 0 && newLines.length > height) {
            newLines = Arrays.copyOfRange(newLines, newLines.length - height, newLines.length);
        }
        return newLines;
    }

    private boolean flushQueuedMessages(StringBuilder out, int width) {
        if (queuedMessageLines.isEmpty() || isInAltScreen) {
            return false;
        }
        for (String line : queuedMessageLines) {
            if (width > 0 && line.length() < width) {
                out.append(line).append("\033[K");
            } else {
                out.append(line);
            }
            out.append("\r\n");
        }
        queuedMessageLines.clear();
        return true;
    }

    private void renderDiffLines(StringBuilder out, String[] newLines, boolean forceRender, int width) {
        for (int i = 0; i < newLines.length; i++) {
            boolean canSkip =
                !forceRender &&
                lastRenderedLines.length > i &&
                newLines[i].equals(lastRenderedLines[i]);

            if (canSkip) {
                if (i < newLines.length - 1) {
                    out.append("\033[B");
                }
                continue;
            }

            String line = newLines[i];

            if (width > 0) {
                line = Truncate.truncate(line, width, "");
            }

            if (width > 0 && line.length() < width) {
                out.append("\r").append(line).append("\033[K");
            } else {
                out.append("\r").append(line);
            }

            if (i < newLines.length - 1) {
                out.append("\n");
            }
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go write behavior.
    void write(String view, boolean isRunning) {
        if (!isRunning) return;

        String string = view.isEmpty() ? " " : view;

        renderLock.lock();
        try {
            buffer.setLength(0);
            buffer.append(string);
            needsRender = true;
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go repaint behavior.
    void repaint() {
        lastRender = "";
        lastRenderedLines = new String[] {};
    }

    // tui4j extension; no Bubble Tea equivalent.
    void notifyModelChanged() {
        this.needsRender = true;
    }

    void queuePrintLine(String messageBody) {
        if (isInAltScreen) {
            return;
        }
        renderLock.lock();
        try {
            String[] lines = messageBody.split("\n");
            queuedMessageLines.addAll(Arrays.asList(lines));
            needsRender = true;
            repaint();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go showCursor behavior.
    void showCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_visible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go hideCursor behavior.
    void hideCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go clearScreen behavior.
    void clearScreen() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
            repaint();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go enterAltScreen behavior.
    void enterAltScreen() {
        if (isInAltScreen) return;

        renderLock.lock();
        try {
            if (terminal.getType().equals("dumb")) return;

            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.puts(InfoCmp.Capability.cursor_home);

            repaint();
            needsRender = true;
            isInAltScreen = true;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go exitAltScreen behavior.
    void exitAltScreen() {
        if (!isInAltScreen) return;

        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.exit_ca_mode);

            repaint();
            needsRender = true;
            isInAltScreen = false;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go altScreen behavior.
    boolean altScreen() {
        return isInAltScreen;
    }

    /**
     * Writes a value to the terminal under the render lock.
     *
     * @param value escape code or content to write
     */
    void writeToTerminal(String value) {
        renderLock.lock();
        try {
            terminal.writer().print(value);
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    // Bubble Tea: seeks to replicate bubbletea/standard_renderer.go setWindowTitle behavior.
    void setWindowTitle(String title) {
        terminal.writer().print("\u001b]2;" + title + "\u0007");
    }
}
