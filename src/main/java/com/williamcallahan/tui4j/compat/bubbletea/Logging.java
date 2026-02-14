package com.williamcallahan.tui4j.compat.bubbletea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * Logging helpers for Bubble Tea compatibility.
 * Bubble Tea: bubbletea/logging.go
 */
public final class Logging {

    private Logging() {
    }

    /**
     * Port of Bubble Tea LogOptionsSetter.
     */
    public interface LogOptionsSetter {

        /**
         * Sets the output stream for logging.
         *
         * @param output output stream
         */
        void setOutput(OutputStream output);

        /**
         * Sets the prefix used for log messages.
         *
         * @param prefix prefix text
         */
        void setPrefix(String prefix);
    }

    /**
     * Sets up default logging to log to a file, using a standard logger adapter.
     *
     * @param path   path to the log file
     * @param prefix prefix to apply
     * @return file output stream for closing by caller
     * @throws IOException when the file cannot be opened
     */
    public static FileOutputStream logToFile(String path, String prefix) throws IOException {
        return logToFileWith(path, prefix, new LoggerAdapter(Logger.getLogger(Logging.class.getName())));
    }

    /**
     * Sets up logging to log to a file using a custom logger adapter.
     *
     * @param path   path to the log file
     * @param prefix prefix to apply
     * @param log    logger adapter
     * @return file output stream for closing by caller
     * @throws IOException when the file cannot be opened
     */
    public static FileOutputStream logToFileWith(String path, String prefix, LogOptionsSetter log) throws IOException {
        FileOutputStream file = new FileOutputStream(Path.of(path).toFile(), true);
        log.setOutput(file);

        if (prefix != null && !prefix.isEmpty()) {
            char last = prefix.charAt(prefix.length() - 1);
            if (!Character.isWhitespace(last)) {
                prefix = prefix + " ";
            }
        }
        log.setPrefix(prefix == null ? "" : prefix);
        return file;
    }

    private static final class LoggerAdapter implements LogOptionsSetter {
        private final Logger logger;
        private Handler handler;
        private String prefix = "";

        private LoggerAdapter(Logger logger) {
            this.logger = Objects.requireNonNull(logger, "logger");
        }

        @Override
        public synchronized void setOutput(OutputStream output) {
            if (handler != null) {
                logger.removeHandler(handler);
            }
            StreamHandler streamHandler = new StreamHandler(output, new PrefixedFormatter(prefix));
            streamHandler.setLevel(Level.ALL);
            logger.addHandler(streamHandler);
            logger.setUseParentHandlers(false);
            handler = streamHandler;
        }

        @Override
        public synchronized void setPrefix(String prefix) {
            this.prefix = prefix == null ? "" : prefix;
            if (handler != null) {
                handler.setFormatter(new PrefixedFormatter(this.prefix));
            }
        }
    }

    private static final class PrefixedFormatter extends Formatter {
        private final String prefix;

        private PrefixedFormatter(String prefix) {
            this.prefix = prefix == null ? "" : prefix;
        }

        @Override
        public String format(LogRecord logRecord) {
            return prefix + logRecord.getMessage() + System.lineSeparator();
        }
    }
}
