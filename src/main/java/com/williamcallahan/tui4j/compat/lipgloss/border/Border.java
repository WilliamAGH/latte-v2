package com.williamcallahan.tui4j.compat.lipgloss.border;

import com.williamcallahan.tui4j.ansi.Action;
import com.williamcallahan.tui4j.ansi.GraphemeCluster;
import com.williamcallahan.tui4j.ansi.State;
import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.ansi.TransitionTable;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.TextLines;
import com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.nio.charset.StandardCharsets;

/**
 * Border character definitions.
 * <p>
 * Port of `lipgloss/border.go`.
 * Defines the character sets used for drawing box borders.
 *
 * @param top top edge characters
 * @param bottom bottom edge characters
 * @param left left edge characters
 * @param right right edge characters
 * @param topLeft top-left corner character
 * @param topRight top-right corner character
 * @param bottomLeft bottom-left corner character
 * @param bottomRight bottom-right corner character
 * @param middleLeft middle-left junction character
 * @param middleRight middle-right junction character
 * @param middle middle junction character
 * @param middleTop middle-top junction character
 * @param middleBottom middle-bottom junction character
 */
public record Border(
        String top,
        String bottom,
        String left,
        String right,
        String topLeft,
        String topRight,
        String bottomLeft,
        String bottomRight,

        // TODO those are used only by Table component in lipgloss...
        String middleLeft,
        String middleRight,
        String middle,
        String middleTop,
        String middleBottom
) {

    /**
     * Returns the width of the top border edge.
     *
     * @return top edge width
     */
    public int getTopSize() {
        return getBorderEdgeWidth(topLeft, top, topRight);
    }

    /**
     * Returns the width of the right border edge.
     *
     * @return right edge width
     */
    public int getRightSize() {
        return getBorderEdgeWidth(topRight, right, bottomRight);
    }

    /**
     * Returns the width of the bottom border edge.
     *
     * @return bottom edge width
     */
    public int getBottomSize() {
        return getBorderEdgeWidth(bottomLeft, bottom, bottomRight);
    }

    /**
     * Returns the width of the left border edge.
     *
     * @return left edge width
     */
    public int getLeftSize() {
        return getBorderEdgeWidth(topLeft, left, bottomLeft);
    }

    private int getBorderEdgeWidth(String... borderParts) {
        int maxWidth = 0;
        for (String piece : borderParts) {
            int width = maxRuneWidth(piece);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    /**
     * Applies border characters around the provided string.
     *
     * @param string content to wrap
     * @param hasTop whether to render top edge
     * @param hasRight whether to render right edge
     * @param hasBottom whether to render bottom edge
     * @param hasLeft whether to render left edge
     * @param borderTopForeground top foreground color
     * @param borderRightForeground right foreground color
     * @param borderBottomForeground bottom foreground color
     * @param borderLeftForeground left foreground color
     * @param borderTopBackground top background color
     * @param borderRightBackground right background color
     * @param borderBottomBackground bottom background color
     * @param borderLeftBackground left background color
     * @param renderer renderer to apply styling
     * @return rendered border string
     */
    public String applyBorders(String string,
                               boolean hasTop,
                               boolean hasRight,
                               boolean hasBottom,
                               boolean hasLeft,
                               TerminalColor borderTopForeground,
                               TerminalColor borderRightForeground,
                               TerminalColor borderBottomForeground,
                               TerminalColor borderLeftForeground,
                               TerminalColor borderTopBackground,
                               TerminalColor borderRightBackground,
                               TerminalColor borderBottomBackground,
                               TerminalColor borderLeftBackground,
                               Renderer renderer) {
        TextLines textLines = TextLines.fromText(string);
        int width = textLines.widestLineLength();

        BorderCharacters chars = BorderCharacters.resolve(this, hasTop, hasRight, hasBottom, hasLeft);
        if (hasLeft) {
            width += maxRuneWidth(chars.left());
        }

        StringBuilder out = new StringBuilder();

        if (hasTop) {
            String topEdge = renderHorizontalEdge(chars.topLeft(), top, chars.topRight(), width);
            topEdge = styleBorder(topEdge, borderTopForeground, borderTopBackground, renderer);
            out.append(topEdge).append('\n');
        }

        appendContentLines(out, textLines.lines(), chars, hasLeft, hasRight,
                borderLeftForeground, borderLeftBackground, borderRightForeground, borderRightBackground, renderer);

        if (hasBottom) {
            String bottomEdge = renderHorizontalEdge(chars.bottomLeft(), bottom, chars.bottomRight(), width);
            bottomEdge = styleBorder(bottomEdge, borderBottomForeground, borderBottomBackground, renderer);
            out.append('\n').append(bottomEdge);
        }

        return out.toString();
    }

    /**
     * Appends content lines with optional left and right border characters.
     */
    private void appendContentLines(StringBuilder out, String[] lines, BorderCharacters chars,
                                    boolean hasLeft, boolean hasRight,
                                    TerminalColor leftFg, TerminalColor leftBg,
                                    TerminalColor rightFg, TerminalColor rightBg,
                                    Renderer renderer) {
        char[] leftRunes = chars.left().toCharArray();
        int leftIndex = 0;
        char[] rightRunes = chars.right().toCharArray();
        int rightIndex = 0;

        for (int i = 0; i < lines.length; i++) {
            if (hasLeft) {
                out.append(styleBorder(String.valueOf(leftRunes[leftIndex]), leftFg, leftBg, renderer));
                leftIndex = (leftIndex + 1) % leftRunes.length;
            }
            out.append(lines[i]);
            if (hasRight) {
                out.append(styleBorder(String.valueOf(rightRunes[rightIndex]), rightFg, rightBg, renderer));
                rightIndex = (rightIndex + 1) % rightRunes.length;
            }
            if (i < lines.length - 1) {
                out.append('\n');
            }
        }
    }

    private String styleBorder(String border, TerminalColor foreground, TerminalColor background, Renderer renderer) {
        AttributedStyle attributedStyle = new AttributedStyle();
        attributedStyle = foreground.applyAsForeground(attributedStyle, renderer);
        attributedStyle = background.applyAsBackground(attributedStyle, renderer);

        return new AttributedString(border, attributedStyle).toAnsi();
    }

    private String renderHorizontalEdge(String left, String middle, String right, int width) {
        if (middle.isEmpty()) {
            middle = " ";
        }

        int leftWidth = TextWidth.measureCellWidth(left);
        int rightWidth = TextWidth.measureCellWidth(right);

        char[] runes = middle.toCharArray();
        int j = 0;

        StringBuilder out = new StringBuilder();
        out.append(left);

        int i = leftWidth + rightWidth;
        while (i < width + rightWidth) {
            out.append(runes[j]);
            j++;
            if (j >= runes.length) {
                j = 0;
            }
            i += TextWidth.measureCellWidth(String.valueOf(runes[j]));
        }
        out.append(right);

        return out.toString();
    }


    private int maxRuneWidth(String input) {
        int maxWidth = 0;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;

        byte[] b = input.getBytes(StandardCharsets.UTF_8);

        int i = 0;
        while (i < b.length) {
            byte byteValue = b[i];

            TransitionTable.Transition transition = table.transition(pstate, byteValue);
            State state = transition.state();
            Action action = transition.action();

            // Handle UTF-8 grapheme clusters
            if (state == State.UTF8) {
                GraphemeCluster.GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(b, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int w = graphemeResult.width();

                if (w > maxWidth) {
                    maxWidth = w;
                }

                i += cluster.length;
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT && maxWidth < 1) {
                maxWidth = 1;
            }

            pstate = state;
            i++;
        }

        return maxWidth;
    }
}