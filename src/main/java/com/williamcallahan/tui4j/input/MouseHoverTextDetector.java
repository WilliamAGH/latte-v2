package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.ansi.Action;
import com.williamcallahan.tui4j.ansi.GraphemeCluster;
import com.williamcallahan.tui4j.ansi.State;
import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.ansi.TransitionTable;

import java.nio.charset.StandardCharsets;

/**
 * Detects hover text under the mouse.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/input/MouseHoverTextDetector.java
 */
public final class MouseHoverTextDetector {
    private String cachedView = "";
    private String[] cachedLines = new String[0];
    private LineCache[] lineCaches = new LineCache[0];

    /**
     * Returns true if the given (column,row) is over a non-whitespace cell in the provided view.
     */
    public boolean isHoveringText(String view, int column, int row) {
        if (view == null || view.isEmpty()) {
            return false;
        }
        if (!view.equals(cachedView)) {
            cachedView = view;
            cachedLines = view.split("\n", -1);
            lineCaches = new LineCache[cachedLines.length];
        }

        if (row < 0 || row >= cachedLines.length) {
            return false;
        }

        String line = cachedLines[row];
        LineCache cache = lineCaches[row];
        if (cache == null || !cache.line.equals(line)) {
            cache = new LineCache(line, buildTextCells(line));
            lineCaches[row] = cache;
        }

        return cache.isTextAt(column);
    }

    private boolean[] buildTextCells(String line) {
        int width = TextWidth.measureCellWidth(line);
        if (width <= 0) {
            return new boolean[0];
        }

        boolean[] cells = new boolean[width];
        TransitionTable table = TransitionTable.get();
        State previousState = State.GROUND;

        byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
        int cellIndex = 0;

        for (int i = 0; i < bytes.length && cellIndex < width; i++) {
            byte byteValue = bytes[i];
            TransitionTable.Transition transition = table.transition(previousState, byteValue);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeCluster.GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(bytes, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int cellWidth = graphemeResult.width();
                boolean isText = !isWhitespaceCluster(cluster);

                for (int j = 0; j < cellWidth && cellIndex + j < width; j++) {
                    cells[cellIndex + j] = isText;
                }

                cellIndex += cellWidth;
                i += cluster.length - 1;
                previousState = State.GROUND;
                continue;
            }

            if (action == Action.PRINT) {
                char ch = (char) (byteValue & 0xFF);
                cells[cellIndex] = !Character.isWhitespace(ch);
                cellIndex++;
            }

            previousState = state;
        }

        return cells;
    }

    private boolean isWhitespaceCluster(byte[] cluster) {
        if (cluster.length == 0) {
            return false;
        }
        String text = new String(cluster, StandardCharsets.UTF_8);
        int index = 0;
        while (index < text.length()) {
            int codePoint = text.codePointAt(index);
            if (!Character.isWhitespace(codePoint)) {
                return false;
            }
            index += Character.charCount(codePoint);
        }
        return true;
    }

    private static final class LineCache {
        private final String line;
        private final boolean[] textCells;

        private LineCache(String line, boolean[] textCells) {
            this.line = line;
            this.textCells = textCells;
        }

        private boolean isTextAt(int column) {
            return column >= 0 && column < textCells.length && textCells[column];
        }
    }
}
