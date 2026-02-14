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
    private volatile boolean isInAltScreen;

    /**
     * Creates a flush pipeline for the given terminal.
     *
     * @param terminal JLine terminal to write output to
     */
    RendererFlush(Terminal terminal) {
        this.terminal = terminal;
    }

    /**
     * Diffs the current buffer against the last render and writes only changed lines.
     * <p>
     * Upstream: bubbletea/standard_renderer.go flush
     *
     * @param width  terminal width for truncation (0 = unlimited)
     * @param height terminal height for overflow trimming (0 = unlimited)
     */
    void flush(int width, int height) {
        if (!needsRender) {
            return;
        }

        renderLock.lock();
        try {
            if (queuedMessageLines.isEmpty()
                && (buffer.isEmpty() || buffer.toString().equals(lastRender))) {
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

    /** Splits the buffer into lines and trims overflow beyond the given height. */
    private String[] splitAndTruncateHeight(int height) {
        String[] newLines = buffer.toString().split("\n");
        if (height > 0 && newLines.length > height) {
            newLines = Arrays.copyOfRange(newLines, newLines.length - height, newLines.length);
        }
        return newLines;
    }

    /** Appends queued print-line messages to the output, skipped in alt-screen mode. */
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

    /** Emits only the lines that differ from the previous render, using cursor-down to skip unchanged ones. */
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

    /**
     * Replaces the render buffer with the latest view string.
     * <p>
     * Upstream: bubbletea/standard_renderer.go write
     *
     * @param view      rendered model output
     * @param isRunning ignored when false (program shutting down)
     */
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

    /**
     * Resets cached render state so the next flush redraws everything.
     * <p>
     * Acquires the render lock because external callers (e.g. StandardRenderer)
     * may invoke this without already holding it. Internal callers that already
     * hold the lock must use {@link #resetRenderState()} instead.
     * <p>
     * Upstream: bubbletea/standard_renderer.go repaint
     */
    void repaint() {
        renderLock.lock();
        try {
            resetRenderState();
        } finally {
            renderLock.unlock();
        }
    }

    /** Clears cached render state. Caller must already hold {@code renderLock}. */
    private void resetRenderState() {
        lastRender = "";
        lastRenderedLines = new String[0];
    }

    /** Marks the renderer as needing a redraw on the next tick (tui4j extension). */
    void notifyModelChanged() {
        this.needsRender = true;
    }

    /**
     * Queues a message for printing above the rendered view on the next flush.
     * Ignored in alt-screen mode because queued messages use inline scrollback.
     *
     * @param messageBody text to print (may contain newlines)
     */
    void queuePrintLine(String messageBody) {
        renderLock.lock();
        try {
            if (isInAltScreen) {
                return;
            }
            String[] lines = messageBody.split("\n");
            queuedMessageLines.addAll(Arrays.asList(lines));
            needsRender = true;
            resetRenderState();
        } finally {
            renderLock.unlock();
        }
    }

    /** Shows the terminal cursor. Upstream: bubbletea/standard_renderer.go showCursor. */
    void showCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_visible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    /** Hides the terminal cursor. Upstream: bubbletea/standard_renderer.go hideCursor. */
    void hideCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    /** Clears the screen and resets render state. Upstream: bubbletea/standard_renderer.go clearScreen. */
    void clearScreen() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
            resetRenderState();
        } finally {
            renderLock.unlock();
        }
    }

    /** Switches to the alternate screen buffer. Upstream: bubbletea/standard_renderer.go enterAltScreen. */
    void enterAltScreen() {
        if (isInAltScreen) return;

        renderLock.lock();
        try {
            if (terminal.getType().equals("dumb")) return;

            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.puts(InfoCmp.Capability.cursor_home);

            resetRenderState();
            needsRender = true;
            isInAltScreen = true;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    /** Returns from the alternate screen buffer. Upstream: bubbletea/standard_renderer.go exitAltScreen. */
    void exitAltScreen() {
        if (!isInAltScreen) return;

        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.exit_ca_mode);

            resetRenderState();
            needsRender = true;
            isInAltScreen = false;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    /** Returns whether the terminal is currently in alternate-screen mode. */
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

    /**
     * Sets the terminal window title via OSC 2 escape sequence.
     * <p>
     * Delegates to {@link #writeToTerminal(String)} to serialize the write
     * with other flush output.
     * <p>
     * Upstream: bubbletea/standard_renderer.go setWindowTitle
     *
     * @param title window title text
     */
    void setWindowTitle(String title) {
        writeToTerminal("\u001b]2;" + title + "\u0007");
    }
}
