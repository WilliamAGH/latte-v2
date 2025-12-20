package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border;

import com.williamcallahan.tui4j.ansi.Action;
import com.williamcallahan.tui4j.ansi.GraphemeCluster;
import com.williamcallahan.tui4j.ansi.State;
import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.ansi.TransitionTable;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.TextLines;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.nio.charset.StandardCharsets;

/**
 * Port of Lip Gloss border.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
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

    public int getTopSize() {
        return getBorderEdgeWidth(topLeft, top, topRight);
    }

    public int getRightSize() {
        return getBorderEdgeWidth(topRight, right, bottomRight);
    }

    public int getBottomSize() {
        return getBorderEdgeWidth(bottomLeft, bottom, bottomRight);
    }

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

        String top = this.top;
        String bottom = this.bottom;
        String left = this.left;
        String right = this.right;
        String topLeft = this.topLeft;
        String topRight = this.topRight;
        String bottomLeft = this.bottomLeft;
        String bottomRight = this.bottomRight;

        if (hasLeft) {
            if (left == null || left.isEmpty()) {
                left = " ";
            }
            width += maxRuneWidth(left);
            //width += TextWidth.measureWidth(left);
        }

        if (hasRight) {
            if (right == null || right.isEmpty()) {
                right = " ";
            }
        }

        if (hasTop && hasLeft && (topLeft == null || topLeft.isEmpty())) {
            topLeft = " ";
        }
        if (hasTop && hasRight && (topRight == null || topRight.isEmpty())) {
            topRight = " ";
        }
        if (hasBottom && hasLeft && (bottomLeft == null || bottomLeft.isEmpty())) {
            bottomLeft = " ";
        }
        if (hasBottom && hasRight && (bottomRight == null || bottomRight.isEmpty())) {
            bottomRight = " ";
        }

        if (hasTop) {
            if (!hasLeft && !hasRight) {
                topLeft = "";
                topRight = "";
            } else if (!hasLeft) {
                topLeft = "";
            } else if (!hasRight) {
                topRight = "";
            }
        }

        if (hasBottom) {
            if (!hasLeft && !hasRight) {
                bottomLeft = "";
                bottomRight = "";
            } else if (!hasLeft) {
                bottomLeft = "";
            } else if (!hasRight) {
                bottomRight = "";
            }
        }

        topLeft = getFirstCharOrEmpty(topLeft);
        topRight = getFirstCharOrEmpty(topRight);
        bottomRight = getFirstCharOrEmpty(bottomRight);
        bottomLeft = getFirstCharOrEmpty(bottomLeft);

        StringBuilder out = new StringBuilder();

        if (hasTop) {
            String topEdge = renderHorizontalEdge(topLeft, top, topRight, width);
            topEdge = styleBorder(topEdge, borderTopForeground, borderTopBackground, renderer);
            out.append(topEdge).append('\n');
        }

        char[] leftRunes = left.toCharArray();
        int leftIndex = 0;

        char[] rightRunes = right.toCharArray();
        int rightIndex = 0;

        int index = 0;
        String[] lines = textLines.lines();
        for (String line : lines) {
            if (hasLeft) {
                String rune = String.valueOf(leftRunes[leftIndex]);
                leftIndex++;

                if (leftIndex >= leftRunes.length) {
                    leftIndex = 0;
                }
                out.append(styleBorder(rune, borderLeftForeground, borderLeftBackground, renderer));
            }

            out.append(line);

            if (hasRight) {
                String rune = String.valueOf(rightRunes[rightIndex]);
                rightIndex++;

                if (rightIndex >= rightRunes.length) {
                    rightIndex = 0;
                }
                out.append(styleBorder(rune, borderRightForeground, borderRightBackground, renderer));
            }

            if (index < lines.length - 1) {
                out.append('\n');
            }
            index++;
        }

        if (hasBottom) {
            String bottomEdge = renderHorizontalEdge(bottomLeft, bottom, bottomRight, width);
            bottomEdge = styleBorder(bottomEdge, borderBottomForeground, borderBottomBackground, renderer);
            out.append('\n').append(bottomEdge);
        }

        return out.toString();
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

        for (int i = leftWidth + rightWidth; i < width + rightWidth; ) {
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


    private String getFirstCharOrEmpty(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        return string.substring(0, 1);
    }

    private int maxRuneWidth(String input) {
        int maxWidth = 0;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;

        byte[] b = input.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < b.length; i++) {
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


                i += cluster.length - 1; // Skip processed bytes
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT) {
                if (maxWidth < 1) {
                    maxWidth = 1;
                }
            }

            pstate = state;
        }

        return maxWidth;
    }
}