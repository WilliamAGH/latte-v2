package com.williamcallahan.tui4j.term;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility for accessing system clipboard via AWT or CLI tools.
 * Non-compat feature for tui4j.
 */
public final class Clipboard {

    private static final Logger LOG = Logger.getLogger(Clipboard.class.getName());

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

    private static boolean tryLocalClipboard(String content) {
        // 1. Try AWT System Clipboard
        try {
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                StringSelection selection = new StringSelection(content);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                return true;
            }
            } catch (Throwable ignored) {
                // AWT failed (headless or other issue), fall through to CLI
                LOG.log(Level.FINE, "AWT clipboard access failed", ignored);
            }
        return false;
    }

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

    private static boolean tryProcess(ProcessBuilder pb, String content) {
        try {
            Process p = pb.start();
            p.getOutputStream().write(content.getBytes());
            p.getOutputStream().close();
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            LOG.log(Level.FINE, "CLI clipboard process failed", e);
            return false;
        }
    }
}
