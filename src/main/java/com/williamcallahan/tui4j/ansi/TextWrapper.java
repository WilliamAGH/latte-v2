package com.williamcallahan.tui4j.ansi;

import com.ibm.icu.lang.UCharacter;
import com.williamcallahan.tui4j.ansi.GraphemeCluster.GraphemeResult;
import com.williamcallahan.tui4j.ansi.TransitionTable.Transition;

import java.nio.charset.StandardCharsets;

/**
 * Wraps text by display width.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/TextWrapper.java
 */
public class TextWrapper {

    private static final String NBSP = "\u00A0";  // Non-breaking space

    private int curWidth;
    private int wordLen;

    private StringBuilder buf = new StringBuilder(); // Final wrapped text
    private StringBuilder word = new StringBuilder(); // Current word
    private StringBuilder space = new StringBuilder(); // Current spaces

    public String wrap(String text, int limit) {
        if (limit < 1 || text == null) {
            return text;
        }

        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;

        byte[] b = text.getBytes(StandardCharsets.UTF_8);

        int i = 0;
        while (i < b.length) {
            Transition transition = table.transition(pstate, b[i]);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(b, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int width = graphemeResult.width();
                i += cluster.length;
                String r = new String(cluster, StandardCharsets.UTF_8);

                if (UCharacter.isWhitespace(r.codePointAt(0)) && !NBSP.equals(r)) {
                    addWord();
                    space.append(r);
                } else {
                    if (wordLen + width > limit) {
                        addWord();
                    }

                    word.append(r);
                    wordLen += width;

                    if (curWidth + wordLen + space.length() > limit) {
                        addNewLine();
                    }
                }

                pstate = State.GROUND;
                continue;
            }

            char r = (char) (b[i]);
            switch (action) {
                case PRINT, EXECUTE -> {
                    if (r == '\n') {
                        if (wordLen == 0) {
                            if (curWidth + space.length() > limit) {
                                curWidth = 0;
                            } else {
                                buf.append(space);
                            }
                            space.setLength(0);
                        }

                        addWord();
                        addNewLine();
                    } else if (UCharacter.isWhitespace(r)) {
                        addWord();
                        space.append(r);
                    } else if (r == '-') {
                        addSpace();
                        if (curWidth + wordLen >= limit) {
                            word.append(r);
                            wordLen++;
                        } else {
                            addWord();
                            buf.append(r);
                            curWidth++;
                        }
                    } else {
                        if (curWidth == limit) {
                            addNewLine();
                        }
                        word.append(r);
                        wordLen++;

                        if (wordLen == limit) {
                            addWord();
                        }

                        if (curWidth + wordLen + space.length() > limit) {
                            addNewLine();
                        }
                    }
                }

                default -> word.append(r);
            }

            if (pstate != State.UTF8) {
                pstate = state;
            }
            i++;
        }

        if (wordLen == 0) {
            if (curWidth + space.length() > limit) {
                curWidth = 0;
            } else {
                buf.append(space);
            }
            space.setLength(0);
        }

        addWord();
        return buf.toString();
    }

    private void addSpace() {
        curWidth += space.length();
        buf.append(space);
        space.setLength(0);
    }

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

    private void addNewLine() {
        buf.append('\n');
        curWidth = 0;
        space.setLength(0);
    }

}
