package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import com.williamcallahan.tui4j.ansi.TextWrapper;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.align.AlignmentDecorator;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.TerminalColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.margin.MarginDecorator;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.padding.PaddingDecorator;
import org.jline.utils.AttributedCharSequence.ForceMode;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer.defaultRenderer;

/**
 * Port of Lip Gloss style.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class Style implements Cloneable {

    public static Style newStyle() {
        return defaultRenderer.newStyle();
    }

    private final Renderer renderer;

    private String value;
    private Function<String, String> transformFunction;
    private TerminalColor background = new NoColor();
    private TerminalColor foreground = new NoColor();
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean blink;
    private boolean faint;
    private boolean reverse;
    private boolean inline;
    private int width;
    private int height;
    private Position horizontalAlign = Position.Left;
    private Position verticalAlign = Position.Top;

    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    private int leftPadding;

    private TerminalColor marginBackgroundColor = new NoColor();
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;
    private int leftMargin;

    private Border borderDecoration;
    private boolean borderTop;
    private boolean borderRight;
    private boolean borderBottom;
    private boolean borderLeft;
    private boolean borderTopSet;
    private boolean borderRightSet;
    private boolean borderBottomSet;
    private boolean borderLeftSet;
    private TerminalColor borderTopForeground = new NoColor();
    private TerminalColor borderRightForeground = new NoColor();
    private TerminalColor borderBottomForeground = new NoColor();
    private TerminalColor borderLeftForeground = new NoColor();
    private TerminalColor borderTopBackground = new NoColor();
    private TerminalColor borderRightBackground = new NoColor();
    private TerminalColor borderBottomBackground = new NoColor();
    private TerminalColor borderLeftBackground = new NoColor();

    public Style(Renderer renderer) {
        this.renderer = renderer;
    }

    public Style setString(String... strings) {
        this.value = String.join(" ", strings);
        return this;
    }

    public Style foreground(TerminalColor color) {
        this.foreground = color;
        return this;
    }

    public Style background(TerminalColor color) {
        this.background = color;
        return this;
    }

    public Style bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Style italic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public Style underline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public Style reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public Style blink(boolean blink) {
        this.blink = blink;
        return this;
    }

    public Style faint(boolean faint) {
        this.faint = faint;
        return this;
    }

    public Style inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public Style width(int width) {
        this.width = width;
        return this;
    }

    public Style height(int height) {
        this.height = height;
        return this;
    }

    public Style align(Position... positions) {
        if (positions.length > 0) {
            this.horizontalAlign = positions[0];
        }
        if (positions.length > 1) {
            this.verticalAlign = positions[1];
        }
        return this;
    }

    public Style alignHorizontal(Position position) {
        this.horizontalAlign = position;
        return this;
    }

    public Style alignVertical(Position position) {
        this.verticalAlign = position;
        return this;
    }

    public Style padding(int... values) {
        int[] boxValues = expandBoxValues(IntStream.range(0, values.length).toArray());
        this.topPadding = values[boxValues[0]];
        this.rightPadding = values[boxValues[1]];
        this.bottomPadding = values[boxValues[2]];
        this.leftPadding = values[boxValues[3]];
        return this;
    }

    public Style paddingTop(int topPadding) {
        this.topPadding = topPadding;
        return this;
    }

    public Style paddingRight(int rightPadding) {
        this.rightPadding = rightPadding;
        return this;
    }

    public int rightPadding() {
        return rightPadding;
    }

    public Style paddingBottom(int bottomPadding) {
        this.bottomPadding = bottomPadding;
        return this;
    }

    public Style paddingLeft(int leftPadding) {
        this.leftPadding = leftPadding;
        return this;
    }

    public int leftPadding() {
        return leftPadding;
    }

    public Style margin(int... values) {
        int[] boxValues = expandBoxValues(IntStream.range(0, values.length).toArray());
        this.topMargin = values[boxValues[0]];
        this.rightMargin = values[boxValues[1]];
        this.bottomMargin = values[boxValues[2]];
        this.leftMargin = values[boxValues[3]];
        return this;
    }

    public Style marginTop(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public Style marginRight(int rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }

    public Style marginBottom(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public Style marginLeft(int leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    public Style marginBackgroundColor(TerminalColor marginBackgroundColor) {
        this.marginBackgroundColor = marginBackgroundColor;
        return this;
    }

    public int topMargin() {
        return topMargin;
    }

    public Style border(Border border, boolean... sides) {
        if (sides.length == 0) {
            return border(border, true);
        }

        borderDecoration(border);

        int[] boxValues = expandBoxValues(
                IntStream.range(0, sides.length)
                        .map(i -> Boolean.compare(sides[i], true))
                        .toArray()
        );

        return borderTop(sides[boxValues[0]])
                .borderRight(sides[boxValues[1]])
                .borderBottom(sides[boxValues[2]])
                .borderLeft(sides[boxValues[3]]);
    }

    public Style borderDecoration(Border borderDecoration) {
        this.borderDecoration = borderDecoration;
        return this;
    }

    public Style borderTop(boolean borderTop) {
        this.borderTop = borderTop;
        this.borderTopSet = true;
        return this;
    }

    public Style borderRight(boolean borderRight) {
        this.borderRight = borderRight;
        this.borderRightSet = true;
        return this;
    }

    public Style borderBottom(boolean borderBottom) {
        this.borderBottom = borderBottom;
        this.borderBottomSet = true;
        return this;
    }

    public Style borderLeft(boolean borderLeft) {
        this.borderLeft = borderLeft;
        this.borderLeftSet = true;
        return this;
    }

    public Style borderBackground(TerminalColor... colors) {
        int[] boxValues = expandBoxValues(
                IntStream.range(0, colors.length).toArray()
        );
        this.borderTopBackground = colors[boxValues[0]];
        this.borderRightBackground = colors[boxValues[1]];
        this.borderBottomBackground = colors[boxValues[2]];
        this.borderLeftBackground = colors[boxValues[3]];

        return this;
    }

    public Style borderTopBackground(TerminalColor color) {
        this.borderTopBackground = color;
        return this;
    }

    public Style borderRightBackground(TerminalColor color) {
        this.borderRightBackground = color;
        return this;
    }

    public Style borderBottomBackground(TerminalColor color) {
        this.borderBottomBackground = color;
        return this;
    }

    public Style borderLeftBackground(TerminalColor color) {
        this.borderLeftBackground = color;
        return this;
    }

    public Style borderForeground(TerminalColor... colors) {
        int[] boxValues = expandBoxValues(
                IntStream.range(0, colors.length).toArray()
        );
        this.borderTopForeground = colors[boxValues[0]];
        this.borderRightForeground = colors[boxValues[1]];
        this.borderBottomForeground = colors[boxValues[2]];
        this.borderLeftForeground = colors[boxValues[3]];

        return this;
    }

    public Style borderTopForeground(TerminalColor color) {
        this.borderTopForeground = color;
        return this;
    }

    public Style borderRightForeground(TerminalColor color) {
        this.borderRightForeground = color;
        return this;
    }

    public Style borderBottomForeground(TerminalColor color) {
        this.borderBottomForeground = color;
        return this;
    }

    public Style borderLeftForeground(TerminalColor color) {
        this.borderLeftForeground = color;
        return this;
    }

    public Style transform(Function<String, String> transformFunction) {
        this.transformFunction = transformFunction;
        return this;
    }

    public String render(String... strings) {
        AttributedStyle style = new AttributedStyle();
        if (foreground != null) {
            style = foreground.applyAsForeground(style, renderer);
        }
        if (background != null) {
            style = background.applyAsBackground(style, renderer);
        }
        if (bold) {
            style = style.bold();
        }
        if (italic) {
            style = style.italic();
        }
        if (underline) {
            style = style.underline();
        }
        if (faint) {
            style = style.faint();
        }
        if (blink) {
            style = style.blink();
        }
        if (reverse) {
            style = style.inverse();
        }

        List<String> strs = new ArrayList<>(List.of(strings));
        if (value != null && !value.isEmpty()) {
            strs.addFirst(value);
        }
        String string = String.join(" ", strs);

        if (this.transformFunction != null) {
            string = transformFunction.apply(string);
        }

        string = string.replaceAll("\r\n", "\n");

        if (inline) {
            string = string.replaceAll("\n", "");
        }

        if (!inline && width > 0) {
            int wrapAt = width - leftPadding - rightPadding;
            string = new TextWrapper().wrap(string, wrapAt);
        }

        // core rendering
        ColorProfile colorProfile = renderer.colorProfile();
        renderer.newStyle();
        String[] lines = string.split("\n");

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (colorProfile != ColorProfile.Ascii) {
                buffer.append(new AttributedString(line, style).toAnsi(colorProfile.colorsCount(), ForceMode.None));
            } else {
                buffer.append(line);
            }
            if (i < lines.length - 1) {
                buffer.append('\n');
            }
        }
        string = buffer.toString();

        if (!inline) {
            AttributedStyle st = new AttributedStyle();
            if (background != null) {
                st = background.applyAsBackground(st, renderer);
            }
            string = PaddingDecorator.applyPadding(string, topPadding, rightPadding, bottomPadding, leftPadding, st, renderer);
        }
        if (height > 0) {
            string = AlignmentDecorator.alignTextVertical(string, verticalAlign, height);
        }

        int numLines = string.split("\n", 0).length;
        if (!(numLines == 0 && width == 0)) {
            AttributedStyle st = new AttributedStyle();
            if (background != null) {
                st = background.applyAsBackground(st, renderer);
            }
            string = AlignmentDecorator.alignTextHorizontal(string, horizontalAlign, width, st);
        }
        if (!inline) {
            string = applyBorders(string);

            AttributedStyle st = new AttributedStyle();
            st = marginBackgroundColor.applyAsBackground(st, renderer);

            string = MarginDecorator.applyMargins(string, topMargin, rightMargin, bottomMargin, leftMargin, st, renderer);
        }
        return string;
    }

    private String applyBorders(String string) {
        boolean hasTop = this.borderTop;
        boolean hasRight = this.borderRight;
        boolean hasBottom = this.borderBottom;
        boolean hasLeft = this.borderLeft;

        if (implicitBorders()) {
            hasTop = true;
            hasRight = true;
            hasBottom = true;
            hasLeft = true;
        }

        if (borderDecoration == null || (!hasTop && !hasRight && !hasBottom && !hasLeft)) {
            return string;
        }

        return borderDecoration.applyBorders(string,
                hasTop,
                hasRight,
                hasBottom,
                hasLeft,
                borderTopForeground,
                borderRightForeground,
                borderBottomForeground,
                borderLeftForeground,
                borderTopBackground,
                borderRightBackground,
                borderBottomBackground,
                borderLeftBackground,
                renderer);
    }

    private boolean implicitBorders() {
        return borderDecoration != null && !(borderTopSet || borderRightSet || borderBottomSet || borderLeftSet);
    }

    public static int[] expandBoxValues(int... values) {
        int[] result = new int[4];  // top, right, bottom, left

        switch (values.length) {
            case 1:
                Arrays.fill(result, values[0]);
                break;
            case 2:
                result[0] = 0;  // top
                result[1] = 1;  // right
                result[2] = 0;  // bottom
                result[3] = 1;  // left
                break;
            case 3:
                result[0] = 0;  // top
                result[1] = 1;  // right
                result[2] = 2;  // bottom
                result[3] = 1;  // left
                break;
            case 4:
                result[0] = 0;  // top
                result[1] = 1;  // right
                result[2] = 2;  // bottom
                result[3] = 3;
                break;// left                break;
            default:
                throw new IllegalArgumentException("Expected 1-4 values, got " + values.length);
        }
        return result;
    }

    public Dimensions frameSize() {
        return new Dimensions(getHorizontalFrameSize(), getVerticalFrameSize());
    }

    public int getVerticalFrameSize() {
        return getVerticalMargins() + getVerticalPadding() + getVerticalBorderSize();
    }

    public int getVerticalMargins() {
        return topMargin + bottomMargin;
    }

    public int getVerticalPadding() {
        return topPadding + bottomPadding;
    }

    public int getVerticalBorderSize() {
        return getBorderTopSize() + getBorderBottomSize();
    }

    public int getBorderTopSize() {
        if (!borderTop && !implicitBorders()) {
            return 0;
        }
        return borderDecoration.getTopSize();
    }

    public int getBorderBottomSize() {
        if (!borderBottom && !implicitBorders()) {
            return 0;
        }
        return borderDecoration.getBottomSize();
    }

    public int getHorizontalFrameSize() {
        return getHorizontalMargins() + getHorizontalPadding() + getHorizontalBorderSize();
    }

    public int getHorizontalMargins() {
        return rightMargin + leftMargin;
    }

    public int getHorizontalPadding() {
        return rightPadding + leftPadding;
    }

    public int getHorizontalBorderSize() {
        return getBorderLeftSize() + getBorderRightSize();
    }

    public int getBorderLeftSize() {
        if (!borderLeft && !implicitBorders()) {
            return 0;
        }
        return borderDecoration.getLeftSize();
    }

    public int getBorderRightSize() {
        if (!borderRight && !implicitBorders()) {
            return 0;
        }
        return borderDecoration.getRightSize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Style copy() {
        try {
            return (Style) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Style inherit(Style style) {
        // TODO copy the rest of the inherited properties
        this.underline = style.underline;
        return this;
    }
}
