package com.williamcallahan.tui4j.ansi;

import java.nio.charset.StandardCharsets;

/**
 * Truncates text by display width.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/Truncate.java
 */
public class Truncate {

    public static String truncate(String input, int length, String tail) {
        int len = length;
        if (TextWidth.measureCellWidth(input) <= len) {
            return input;
        }

        int tw = TextWidth.measureCellWidth(tail);
        len -= tw;
        if (len < 0) {
            return "";
        }

        int curWidth = 0;
        boolean ignoring = false;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;
        byte[] b = input.getBytes(StandardCharsets.UTF_8);

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < b.length; ) {
            byte byteValue = b[i];

            TransitionTable.Transition transition = table.transition(pstate, byteValue);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeCluster.GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(b, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int width = graphemeResult.width();

                if (curWidth + width > len && !ignoring) {
                    ignoring = true;
                    buf.append(tail);
                    i += cluster.length;
                    continue;
                }

                if (ignoring) {
                    i += cluster.length;
                    continue;
                }

                curWidth += width;
                buf.append(new String(cluster, StandardCharsets.UTF_8));
                i += cluster.length;
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT) {
                if (curWidth + 1 > len && !ignoring) {  // Check if next char would exceed limit
                    ignoring = true;
                    buf.append(tail);
                    i++;
                    continue;
                }

                if (ignoring) {
                    i++;
                    continue;
                }

                curWidth++;
                buf.append((char) b[i]);
            } else {
                buf.append((char) b[i]);
            }

            i++;
            pstate = state;
        }
        return buf.toString();
    }
}
