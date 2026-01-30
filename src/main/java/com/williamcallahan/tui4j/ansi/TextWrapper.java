package com.williamcallahan.tui4j.ansi;

import com.ibm.icu.lang.UCharacter;
import com.williamcallahan.tui4j.ansi.GraphemeCluster.GraphemeResult;
import com.williamcallahan.tui4j.compat.x.ansi.parser.Action;
import com.williamcallahan.tui4j.compat.x.ansi.parser.State;
import com.williamcallahan.tui4j.compat.x.ansi.parser.TransitionTable;
import com.williamcallahan.tui4j.compat.x.ansi.parser.TransitionTable.Transition;
import java.nio.charset.StandardCharsets;

/**
 * Wraps text to fit within a specified display width while preserving ANSI escape sequences.
 * <p>
 * Uses an ANSI state machine to parse input, handling:
 * <ul>
 *   <li>UTF-8 multibyte characters with correct display width calculation</li>
 *   <li>ANSI escape sequences (passed through without counting toward width)</li>
 *   <li>Word boundaries at whitespace and hyphens</li>
 *   <li>Non-breaking spaces (U+00A0) treated as non-whitespace</li>
 * </ul>
 * <p>
 * The algorithm accumulates words and spaces, inserting line breaks when the
 * current line width would exceed the limit, and hard-wrapping words that are
 * longer than the available line width.
 */
public class TextWrapper {

    private static final String NBSP = "\u00A0"; // Non-breaking space

    /**
     * Creates a text wrapper instance.
     */
    public TextWrapper() {}

    private static final String BREAKPOINTS = "";

    private int limit;
    private int curWidth;
    private int wordLen;
    private int spaceWidth;

    private StringBuilder buf = new StringBuilder(); // Final wrapped text
    private StringBuilder word = new StringBuilder(); // Current word
    private StringBuilder space = new StringBuilder(); // Current spaces
    private StringBuilder ansiSeq = new StringBuilder(); // Current ANSI escape sequence being parsed
    private StringBuilder activeStyle = new StringBuilder(); // Active SGR style to re-apply after wrap

    /**
     * Wraps text to the specified display width limit.
     *
     * @param text  the text to wrap
     * @param limit maximum display width per line
     * @return wrapped text with line breaks inserted
     */
    public String wrap(String text, int limit) {
        return wrap(text, limit, false);
    }

    /**
     * Wraps text to the specified display width limit, optionally preserving styles across lines.
     *
     * @param text          the text to wrap
     * @param limit         maximum display width per line
     * @param preserveStyle whether to reset and re-apply styles across line breaks
     * @return wrapped text with line breaks inserted
     */
    public String wrap(String text, int limit, boolean preserveStyle) {
        if (limit < 1 || text == null) {
            return text;
        }

        this.limit = limit;
        this.curWidth = 0;
        this.wordLen = 0;
        this.spaceWidth = 0;
        this.buf.setLength(0);
        this.word.setLength(0);
        this.space.setLength(0);
        this.ansiSeq.setLength(0);
        this.activeStyle.setLength(0);

        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        int i = 0;
        while (i < bytes.length) {
            Transition transition = table.transition(pstate, bytes[i]);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                i = handleUtf8(bytes, i, preserveStyle);
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT || action == Action.EXECUTE) {
                handlePrint(bytes[i], preserveStyle);
            } else {
                // We're inside an ANSI escape sequence
                char ch = (char) bytes[i];
                word.append(ch);
                
                if (preserveStyle) {
                    trackStyle(ch);
                }
            }

            if (pstate != State.UTF8) {
                pstate = state;
            }
            i++;
        }

        if (wordLen == 0) {
            if (curWidth + spaceWidth > limit) {
                curWidth = 0;
            } else {
                buf.append(space);
            }
            space.setLength(0);
            spaceWidth = 0;
        }

        if (curWidth + wordLen + spaceWidth > limit) {
            addNewLine(false);
        }
        addWord();
        return buf.toString();
    }

    private void trackStyle(char ch) {
        ansiSeq.append(ch);
        // Check if this completes an SGR sequence (ends with 'm')
        if (ch == 'm' && ansiSeq.length() >= 2) {
            String seq = ansiSeq.toString();
            // Check if it's a reset sequence (ESC[m, ESC[0m, or any sequence starting with 0;)
            if (isResetSequence(seq)) {
                activeStyle.setLength(0);
            } else if (seq.endsWith("m")) {
                // It's an SGR sequence - update active style
                // The seq already contains ESC and everything else
                // Note: This replaces rather than accumulates SGR parameters.
                // Cumulative styles (e.g., color then bold) are not preserved.
                activeStyle.setLength(0);
                activeStyle.append(seq);
            }
            ansiSeq.setLength(0);
        } else if (ch == 0x1b) {
            // Start of new escape sequence
            ansiSeq.setLength(0);
            ansiSeq.append(ch);
        }
    }

    private int handleUtf8(byte[] bytes, int i, boolean preserveStyle) {
        GraphemeResult graphemeResult =
            GraphemeCluster.getFirstGraphemeCluster(bytes, i, -1);
        if (graphemeResult == null) {
            return i + 1;
        }

        byte[] cluster = graphemeResult.cluster();
        int width = graphemeResult.width();
        String grapheme = new String(cluster, StandardCharsets.UTF_8);
        int codePoint = grapheme.codePointAt(0);

        if (
            UCharacter.isWhitespace(codePoint) &&
            codePoint != NBSP.codePointAt(0)
        ) {
            addWord();
            space.append(grapheme);
            spaceWidth += width;
        } else if (containsAny(grapheme, BREAKPOINTS)) {
            addSpace();
            if (curWidth + wordLen + width > limit) {
                word.append(grapheme);
                wordLen += width;
            } else {
                addWord();
                buf.append(grapheme);
                curWidth += width;
            }
        } else {
            if (wordLen + width > limit) {
                addWord();
            }
            word.append(grapheme);
            wordLen += width;
            if (curWidth + wordLen + spaceWidth > limit) {
                addNewLine(preserveStyle);
            }
            if (wordLen == limit) {
                addWord();
            }
        }

        return i + cluster.length;
    }

    private void handlePrint(byte b, boolean preserveStyle) {
        char ch = (char) b;
        if (ch == '\n') {
            if (wordLen == 0) {
                if (curWidth + spaceWidth > limit) {
                    curWidth = 0;
                } else {
                    buf.append(space);
                }
                space.setLength(0);
                spaceWidth = 0;
            }
            addWord();
            addNewLine(preserveStyle);
        } else if (UCharacter.isWhitespace(ch)) {
            addWord();
            space.append(ch);
            spaceWidth++;
        } else if (ch == '-' || containsAny(ch, BREAKPOINTS)) {
            addSpace();
            if (curWidth + wordLen >= limit) {
                word.append(ch);
                wordLen++;
            } else {
                addWord();
                buf.append(ch);
                curWidth++;
            }
        } else {
            if (curWidth == limit) {
                addNewLine(preserveStyle);
            }
            word.append(ch);
            wordLen++;
            if (wordLen == limit) {
                addWord();
            }
            if (curWidth + wordLen + spaceWidth > limit) {
                addNewLine(preserveStyle);
            }
        }
    }

    /**
     * Handles add space for this component.
     */
    private void addSpace() {
        if (spaceWidth == 0 && space.length() == 0) {
            return;
        }
        curWidth += spaceWidth;
        buf.append(space);
        space.setLength(0);
        spaceWidth = 0;
    }

    /**
     * Handles add word for this component.
     */
    private void addWord() {
        if (word.isEmpty()) {
            return;
        }
        addSpace();
        curWidth += wordLen;
        buf.append(word);
        word.setLength(0);
        wordLen = 0;
    }

    /**
     * Handles add new line for this component.
     */
    private void addNewLine() {
        addNewLine(false);
    }

    private void addNewLine(boolean preserveStyle) {
        if (preserveStyle && activeStyle.length() > 0) {
            buf.append("\033[m");
        }
        buf.append('\n');
        if (preserveStyle && activeStyle.length() > 0) {
            buf.append(activeStyle);
        }
        curWidth = 0;
        space.setLength(0);
        spaceWidth = 0;
    }

    /**
     * Checks if an SGR sequence is a reset sequence.
     * The sequence includes ESC[...m format.
     *
     * @param seq the full sequence including ESC, [, parameters, and 'm'
     * @return true if this is a reset sequence
     */
    private static boolean isResetSequence(String seq) {
        if (seq == null || seq.length() < 3) {
            return false;
        }
        // Find the parameters between '[' and 'm'
        int bracketIdx = seq.indexOf('[');
        if (bracketIdx < 0 || !seq.endsWith("m")) {
            return false;
        }
        String params = seq.substring(bracketIdx + 1, seq.length() - 1);
        // Reset sequences: empty params (ESC[m), "0" (ESC[0m), or starting with "0;"
        return params.isEmpty() || params.equals("0") || params.startsWith("0;");
    }

    /**
     * Returns whether a value contains any configured breakpoints.
     *
     * @param value string value to scan
     * @param breakpoints breakpoint characters
     * @return true when any breakpoint is present
     */
    private static boolean containsAny(String value, String breakpoints) {
        if (breakpoints.isEmpty() || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            int codePoint = value.codePointAt(i);
            if (breakpoints.indexOf(codePoint) >= 0) {
                return true;
            }
            i += Character.charCount(codePoint) - 1;
        }
        return false;
    }

    /**
     * Returns whether a value contains any configured breakpoints.
     *
     * @param value character to check
     * @param breakpoints breakpoint characters
     * @return true when any breakpoint matches
     */
    private static boolean containsAny(char value, String breakpoints) {
        return !breakpoints.isEmpty() && breakpoints.indexOf(value) >= 0;
    }
}
