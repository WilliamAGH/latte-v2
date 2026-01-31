package com.williamcallahan.tui4j.compat.x.ansi;

import com.williamcallahan.tui4j.compat.x.ansi.parser.Action;
import com.williamcallahan.tui4j.compat.x.ansi.parser.State;
import com.williamcallahan.tui4j.compat.x.ansi.parser.TransitionTable;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * ANSI-aware string width calculation.
 * Port of github.com/charmbracelet/x/ansi (width.go)
 */
public final class StringWidth {
    private StringWidth() {}

    /**
     * Returns the width of a string in cells, using grapheme clustering.
     * ANSI escape codes are ignored.
     *
     * @param s the input string (must not be null)
     * @return the cell width
     * @throws NullPointerException if s is null
     */
    public static int stringWidth(String s) {
        return stringWidth(Method.GRAPHEME_WIDTH, s);
    }

    /**
     * Returns the width of a string in cells, using wcwidth.
     * ANSI escape codes are ignored.
     *
     * @param s the input string (must not be null)
     * @return the cell width
     * @throws NullPointerException if s is null
     */
    public static int stringWidthWc(String s) {
        return stringWidth(Method.WC_WIDTH, s);
    }

    /**
     * Returns the width of a string in cells using the specified method.
     * ANSI escape codes are ignored.
     *
     * @param method the width calculation method (must not be null)
     * @param s the input string (must not be null)
     * @return the cell width
     * @throws NullPointerException if method or s is null
     */
    public static int stringWidth(Method method, String s) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(s, "s");
        if (s.isEmpty()) {
            return 0;
        }

        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;
        int width = 0;

        String normalized = normalizeC1Controls(s);
        byte[] bytes = normalized.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < bytes.length; i++) {
            TransitionTable.Transition transition = table.transition(pstate, bytes[i]);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeCluster.Result result = GraphemeCluster.getFirstGraphemeCluster(bytes, i, method);
                if (result != null) {
                    width += result.width();
                    i += result.clusterBytes().length - 1;
                }
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT) {
                width++;
            }

            pstate = state;
        }

        return width;
    }

    private static String normalizeC1Controls(String s) {
        for (int i = 0; i < s.length(); ) {
            int codePoint = s.codePointAt(i);
            if (codePoint >= 0x80 && codePoint <= 0x9F) {
                StringBuilder normalized = new StringBuilder(s.length() + 4);
                normalized.append(s, 0, i);
                while (i < s.length()) {
                    codePoint = s.codePointAt(i);
                    if (codePoint >= 0x80 && codePoint <= 0x9F) {
                        normalized.append(Ansi.ESC_CHAR);
                        normalized.append((char) (codePoint - 0x40));
                    } else {
                        normalized.appendCodePoint(codePoint);
                    }
                    i += Character.charCount(codePoint);
                }
                return normalized.toString();
            }
            i += Character.charCount(codePoint);
        }
        return s;
    }
}
