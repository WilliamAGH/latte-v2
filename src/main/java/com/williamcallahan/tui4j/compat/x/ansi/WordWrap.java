package com.williamcallahan.tui4j.compat.x.ansi;

import com.williamcallahan.tui4j.compat.x.ansi.parser.Action;
import com.williamcallahan.tui4j.compat.x.ansi.parser.State;
import com.williamcallahan.tui4j.compat.x.ansi.parser.TransitionTable;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * ANSI-aware word wrap implementation.
 * <p>
 * Port of {@code x/ansi/wrap.go}.
 */
public final class WordWrap {

    /**
     * Prevents instantiation.
     */
    private WordWrap() {
    }

    /**
     * Word-wraps a string using grapheme width.
     *
     * @param input input string
     * @param limit wrap width
     * @param breakpoints breakpoint characters
     * @return wrapped string
     */
    public static String wordWrap(String input, int limit, String breakpoints) {
        return wordWrapInternal(Method.GRAPHEME_WIDTH, input, limit, breakpoints);
    }

    /**
     * Word-wraps a string using wcwidth.
     *
     * @param input input string
     * @param limit wrap width
     * @param breakpoints breakpoint characters
     * @return wrapped string
     */
    public static String wordWrapWc(String input, int limit, String breakpoints) {
        return wordWrapInternal(Method.WC_WIDTH, input, limit, breakpoints);
    }

    /**
     * Word-wraps a string using the provided width calculation method.
     *
     * @param method width calculation method
     * @param input input string
     * @param limit wrap width
     * @param breakpoints breakpoint characters
     * @return wrapped string
     */
    private static String wordWrapInternal(Method method, String input, int limit, String breakpoints) {
        if (limit < 1) {
            return input;
        }

        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);
        ByteArrayOutputStream word = new ByteArrayOutputStream();
        ByteArrayOutputStream space = new ByteArrayOutputStream();
        int[] spaceWidth = {0};
        int curWidth = 0;
        int wordLen = 0;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;

        int i = 0;
        while (i < bytes.length) {
            TransitionTable.Transition transition = table.transition(pstate, bytes[i]);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeCluster.Result result = GraphemeCluster.getFirstGraphemeCluster(bytes, i, method);
                if (result == null) {
                    break;
                }
                byte[] clusterBytes = result.clusterBytes();
                int width = result.width();
                i += clusterBytes.length;

                int codePoint = WrapSupport.firstCodePoint(clusterBytes);
                if (codePoint >= 0 && WrapSupport.isWhitespace(codePoint) && codePoint != WrapSupport.NON_BREAKING_SPACE) {
                    if (wordLen > 0) {
                        curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);
                        wordLen = 0;
                    }
                    space.writeBytes(clusterBytes);
                    spaceWidth[0] += width;
                } else if (WrapSupport.containsBreakpointCodePoint(codePoint, breakpoints)) {
                    curWidth = flushSpace(buffer, space, curWidth, spaceWidth);
                    if (wordLen > 0) {
                        curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);
                        wordLen = 0;
                    }
                    buffer.writeBytes(clusterBytes);
                    curWidth += width;
                } else {
                    word.writeBytes(clusterBytes);
                    wordLen += width;
                    if (curWidth + spaceWidth[0] + wordLen > limit && wordLen < limit) {
                        buffer.write('\n');
                        curWidth = 0;
                        space.reset();
                        spaceWidth[0] = 0;
                    }
                }

                pstate = State.GROUND;
                continue;
            }

            switch (action) {
                case PRINT, EXECUTE -> {
                    char ch = (char) (bytes[i] & 0xFF);
                    switch (ch) {
                        case '\n' -> {
                            if (wordLen == 0) {
                                if (curWidth + spaceWidth[0] > limit) {
                                    curWidth = 0;
                                } else {
                                    buffer.writeBytes(space.toByteArray());
                                }
                                space.reset();
                                spaceWidth[0] = 0;
                            }
                            curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);
                            wordLen = 0;
                            buffer.write('\n');
                            curWidth = 0;
                            space.reset();
                            spaceWidth[0] = 0;
                        }
                        default -> {
                            if (Character.isWhitespace(ch)) {
                                curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);
                                wordLen = 0;
                                space.write(bytes[i]);
                                spaceWidth[0] += 1;
                            } else if (ch == '-' || containsBreakpointChar(ch, breakpoints)) {
                                curWidth = flushSpace(buffer, space, curWidth, spaceWidth);
                                curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);
                                wordLen = 0;
                                buffer.write(bytes[i]);
                                curWidth++;
                            } else {
                                word.write(bytes[i]);
                                wordLen++;
                                if (curWidth + spaceWidth[0] + wordLen > limit && wordLen < limit) {
                                    buffer.write('\n');
                                    curWidth = 0;
                                    space.reset();
                                    spaceWidth[0] = 0;
                                }
                            }
                        }
                    }
                }
                default -> word.write(bytes[i]);
            }

            if (pstate != State.UTF8) {
                pstate = state;
            }
            i++;
        }

        curWidth = flushWord(buffer, word, space, curWidth, wordLen, spaceWidth);

        return buffer.toString(StandardCharsets.UTF_8);
    }

    /**
     * Flushes the pending space buffer to the output.
     *
     * @param buffer output buffer
     * @param space space buffer
     * @param curWidth current width
     * @param spaceWidth display width accumulator for spaces
     * @return updated width
     */
    private static int flushSpace(ByteArrayOutputStream buffer, ByteArrayOutputStream space, int curWidth, int[] spaceWidth) {
        if (space.size() == 0) {
            return curWidth;
        }
        buffer.writeBytes(space.toByteArray());
        curWidth += spaceWidth[0];
        space.reset();
        spaceWidth[0] = 0;
        return curWidth;
    }

    /**
     * Flushes the pending word buffer to the output.
     *
     * @param buffer output buffer
     * @param word word buffer
     * @param space space buffer
     * @param curWidth current width
     * @param wordLen word width
     * @param spaceWidth display width accumulator for spaces
     * @return updated width
     */
    private static int flushWord(ByteArrayOutputStream buffer,
                                 ByteArrayOutputStream word,
                                 ByteArrayOutputStream space,
                                 int curWidth,
                                 int wordLen,
                                 int[] spaceWidth) {
        if (word.size() == 0) {
            return curWidth;
        }
        curWidth = flushSpace(buffer, space, curWidth, spaceWidth);
        buffer.writeBytes(word.toByteArray());
        word.reset();
        return curWidth + wordLen;
    }

    /**
     * Reports whether the character is a breakpoint character.
     *
     * @param ch character to test
     * @param breakpoints breakpoint string
     * @return true if the character is a breakpoint
     */
    private static boolean containsBreakpointChar(char ch, String breakpoints) {
        if (breakpoints == null || breakpoints.isEmpty()) {
            return false;
        }
        return breakpoints.indexOf(ch) >= 0;
    }
}
