package com.williamcallahan.tui4j.term;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility for copying text to the system clipboard via AWT or CLI tools.
 * <p>
 * This is a tui4j extension with no Bubble Tea equivalent. Provides best-effort
 * clipboard access for local terminal sessions, complementing OSC 52 sequences
 * for remote/SSH terminals. Thread-safe for concurrent use.
 *
 * @see com.williamcallahan.tui4j.compat.bubbletea.render.StandardRenderer#copyToClipboard
 */
public final class Clipboard {

    private static final Logger LOG = Logger.getLogger(
        Clipboard.class.getName()
    );

    /** Prevents instantiation of this utility class. */
    private Clipboard() {}

    /**
     * Attempts to copy text to the clipboard.
     * <p>
     * Tries the local system clipboard first (AWT/CLI).
     *
     * @param content the text to copy
     * @return true if copied via local mechanism, false otherwise
     */
    public static boolean tryCopy(String content) {
        if (tryLocalClipboard(content)) {
            return true;
        }
        return copyViaCommand(content);
    }

    /**
     * Attempts clipboard copy via AWT system clipboard.
     *
     * @param content the text to copy
     * @return true if AWT clipboard succeeded, false if headless or AWT failed
     */
    private static boolean tryLocalClipboard(String content) {
        try {
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                StringSelection selection = new StringSelection(content);
                Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(selection, selection);
                return true;
            }
        } catch (Throwable ignored) {
            // AWT failed (headless or other issue), fall through to CLI
            LOG.log(Level.FINE, "AWT clipboard access failed", ignored);
        }
        return false;
    }

    /**
     * Attempts clipboard copy via platform-specific CLI commands.
     * <p>
     * Uses pbcopy (macOS), clip (Windows), or xclip/xsel (Linux).
     *
     * @param content the text to copy
     * @return true if CLI command succeeded, false otherwise
     */
    private static boolean copyViaCommand(String content) {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb = null;

        if (os.contains("mac")) {
            pb = new ProcessBuilder("pbcopy");
        } else if (os.contains("win")) {
            pb = new ProcessBuilder("clip");
        } else {
            // Linux/Unix: try xclip first
            pb = new ProcessBuilder("xclip", "-selection", "clipboard");
        }

        if (pb != null && tryProcess(pb, content)) {
            return true;
        }

        // Fallback for Linux: xsel
        if ((os.contains("nux") || os.contains("nix")) && !os.contains("mac")) {
            pb = new ProcessBuilder("xsel", "--clipboard", "--input");
            return tryProcess(pb, content);
        }

        return false;
    }

    /**
     * Writes content to a process's stdin and waits for completion.
     *
     * @param pb the process builder configured for the clipboard command
     * @param content the text to write to the process
     * @return true if process exited successfully (exit code 0), false otherwise
     */
    private static boolean tryProcess(ProcessBuilder pb, String content) {
        try {
            Process p = pb.start();
            try (
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                        p.getOutputStream(),
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                writer.write(content);
            }
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            LOG.log(Level.FINE, "CLI clipboard process failed", e);
            return false;
        }
    }
}
