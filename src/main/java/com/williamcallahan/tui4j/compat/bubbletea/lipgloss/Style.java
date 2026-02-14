package com.williamcallahan.tui4j.compat.bubbletea.lipgloss;

import java.util.function.Function;

/**
 * Style wrapper for Bubble Tea-compatible APIs.
 * <p>
 * Lipgloss: style.go.
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this compatibility type moved; use {@link com.williamcallahan.tui4j.compat.lipgloss.Style}.
 * This transitional shim preserves the legacy Bubble Tea fluent API and will be removed
 * in a future release.
 */
@Deprecated(since = "0.3.0")
public class Style extends com.williamcallahan.tui4j.compat.lipgloss.Style {

    /**
     * Creates a new Style with the default renderer.
     *
     * @return new style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#newStyle()} instead.
     */
    @Deprecated(since = "0.3.1")
    public static Style newStyle() {
        return new Style(Renderer.defaultRenderer());
    }

    /**
     * Wraps a canonical style with the legacy Bubble Tea shim.
     *
     * @param canonical canonical style
     * @return legacy style shim
     * @deprecated Use canonical {@link com.williamcallahan.tui4j.compat.lipgloss.Style} directly.
     */
    @Deprecated(since = "0.3.1")
    public static Style fromCanonical(com.williamcallahan.tui4j.compat.lipgloss.Style canonical) {
        if (canonical instanceof Style legacy) {
            return legacy;
        }
        Style legacy = new Style(Renderer.defaultRenderer());
        if (canonical != null) {
            legacy.inherit(canonical);
        }
        return legacy;
    }

    /**
     * Creates a Style with the given renderer.
     *
     * @param renderer renderer
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#Style(com.williamcallahan.tui4j.compat.lipgloss.Renderer)} instead.
     */
    @Deprecated(since = "0.3.1")
    public Style(Renderer renderer) {
        super(renderer.toCanonical());
    }

    /**
     * Copy constructor that preserves the deprecated shim type.
     * <p>
     * Without this, {@link #copy()} would delegate to {@code super.copy()} which
     * returns the canonical type, causing a {@link ClassCastException} on the downcast.
     *
     * @param source style to copy from
     */
    private Style(Style source) {
        super(source);
    }

    /**
     * Sets the string value for this style.
     *
     * @param strings strings to render
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#setString(String...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style setString(String... strings) {
        super.setString(strings);
        return this;
    }

    /**
     * Sets the foreground color.
     *
     * @param color terminal color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#foreground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style foreground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.foreground(color);
        return this;
    }

    /**
     * Sets the background color.
     *
     * @param color terminal color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#background(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style background(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.background(color);
        return this;
    }

    /**
     * Sets bold styling.
     *
     * @param bold bold flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#bold(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style bold(boolean bold) {
        super.bold(bold);
        return this;
    }

    /**
     * Sets italic styling.
     *
     * @param italic italic flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#italic(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style italic(boolean italic) {
        super.italic(italic);
        return this;
    }

    /**
     * Sets underline styling.
     *
     * @param underline underline flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#underline(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style underline(boolean underline) {
        super.underline(underline);
        return this;
    }

    /**
     * Sets reverse styling.
     *
     * @param reverse reverse flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#reverse(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style reverse(boolean reverse) {
        super.reverse(reverse);
        return this;
    }

    /**
     * Sets blink styling.
     *
     * @param blink blink flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#blink(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style blink(boolean blink) {
        super.blink(blink);
        return this;
    }

    /**
     * Sets faint styling.
     *
     * @param faint faint flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#faint(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style faint(boolean faint) {
        super.faint(faint);
        return this;
    }

    /**
     * Sets inline rendering.
     *
     * @param inline inline flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#inline(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style inline(boolean inline) {
        super.inline(inline);
        return this;
    }

    /**
     * Sets the width.
     *
     * @param width width
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#width(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style width(int width) {
        super.width(width);
        return this;
    }

    /**
     * Sets the height.
     *
     * @param height height
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#height(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style height(int height) {
        super.height(height);
        return this;
    }

    /**
     * Sets the maximum width.
     *
     * @param maxWidth max width
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#maxWidth(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style maxWidth(int maxWidth) {
        super.maxWidth(maxWidth);
        return this;
    }

    /**
     * Sets the maximum height.
     *
     * @param maxHeight max height
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#maxHeight(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style maxHeight(int maxHeight) {
        super.maxHeight(maxHeight);
        return this;
    }

    /**
     * Sets the ellipsis string used for truncation.
     *
     * @param ellipsis ellipsis string
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#ellipsis(String)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style ellipsis(String ellipsis) {
        super.ellipsis(ellipsis);
        return this;
    }

    /**
     * Sets alignment.
     *
     * @param positions positions
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#align(com.williamcallahan.tui4j.compat.lipgloss.Position...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style align(com.williamcallahan.tui4j.compat.lipgloss.Position... positions) {
        super.align(positions);
        return this;
    }

    /**
     * Sets horizontal alignment.
     *
     * @param position horizontal position
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#alignHorizontal(com.williamcallahan.tui4j.compat.lipgloss.Position)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style alignHorizontal(com.williamcallahan.tui4j.compat.lipgloss.Position position) {
        super.alignHorizontal(position);
        return this;
    }

    /**
     * Sets vertical alignment.
     *
     * @param position vertical position
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#alignVertical(com.williamcallahan.tui4j.compat.lipgloss.Position)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style alignVertical(com.williamcallahan.tui4j.compat.lipgloss.Position position) {
        super.alignVertical(position);
        return this;
    }

    /**
     * Clears the max width.
     *
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#unsetMaxWidth()} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style unsetMaxWidth() {
        super.unsetMaxWidth();
        return this;
    }

    /**
     * Clears the max height.
     *
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#unsetMaxHeight()} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style unsetMaxHeight() {
        super.unsetMaxHeight();
        return this;
    }

    /**
     * Sets padding.
     *
     * @param values padding values
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#padding(int...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style padding(int... values) {
        super.padding(values);
        return this;
    }

    /**
     * Sets top padding.
     *
     * @param topPadding top padding
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#paddingTop(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style paddingTop(int topPadding) {
        super.paddingTop(topPadding);
        return this;
    }

    /**
     * Sets right padding.
     *
     * @param rightPadding right padding
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#paddingRight(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style paddingRight(int rightPadding) {
        super.paddingRight(rightPadding);
        return this;
    }

    /**
     * Sets bottom padding.
     *
     * @param bottomPadding bottom padding
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#paddingBottom(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style paddingBottom(int bottomPadding) {
        super.paddingBottom(bottomPadding);
        return this;
    }

    /**
     * Sets left padding.
     *
     * @param leftPadding left padding
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#paddingLeft(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style paddingLeft(int leftPadding) {
        super.paddingLeft(leftPadding);
        return this;
    }

    /**
     * Sets margin.
     *
     * @param values margin values
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#margin(int...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style margin(int... values) {
        super.margin(values);
        return this;
    }

    /**
     * Sets top margin.
     *
     * @param topMargin top margin
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#marginTop(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style marginTop(int topMargin) {
        super.marginTop(topMargin);
        return this;
    }

    /**
     * Sets right margin.
     *
     * @param rightMargin right margin
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#marginRight(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style marginRight(int rightMargin) {
        super.marginRight(rightMargin);
        return this;
    }

    /**
     * Sets bottom margin.
     *
     * @param bottomMargin bottom margin
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#marginBottom(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style marginBottom(int bottomMargin) {
        super.marginBottom(bottomMargin);
        return this;
    }

    /**
     * Sets left margin.
     *
     * @param leftMargin left margin
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#marginLeft(int)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style marginLeft(int leftMargin) {
        super.marginLeft(leftMargin);
        return this;
    }

    /**
     * Sets the margin background color.
     *
     * @param marginBackgroundColor terminal color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#marginBackgroundColor(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style marginBackgroundColor(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor marginBackgroundColor) {
        super.marginBackgroundColor(marginBackgroundColor);
        return this;
    }

    /**
     * Sets the border.
     *
     * @param border border
     * @param sides sides to apply
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#border(com.williamcallahan.tui4j.compat.lipgloss.border.Border, boolean...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style border(com.williamcallahan.tui4j.compat.lipgloss.border.Border border, boolean... sides) {
        super.border(border, sides);
        return this;
    }

    /**
     * Sets the border using the Bubble Tea border type.
     *
     * @param border bubbletea border
     * @param sides sides to apply
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#border(com.williamcallahan.tui4j.compat.lipgloss.border.Border, boolean...)} instead.
     */
    @Deprecated(since = "0.3.1")
    public Style border(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.Border border, boolean... sides) {
        return border(border == null ? null : border.toNew(), sides);
    }

    /**
     * Sets the border decoration.
     *
     * @param borderDecoration border decoration
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderDecoration(com.williamcallahan.tui4j.compat.lipgloss.border.Border)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderDecoration(com.williamcallahan.tui4j.compat.lipgloss.border.Border borderDecoration) {
        super.borderDecoration(borderDecoration);
        return this;
    }

    /**
     * Sets the border decoration using the Bubble Tea border type.
     *
     * @param borderDecoration bubbletea border decoration
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderDecoration(com.williamcallahan.tui4j.compat.lipgloss.border.Border)} instead.
     */
    @Deprecated(since = "0.3.1")
    public Style borderDecoration(com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.Border borderDecoration) {
        super.borderDecoration(borderDecoration == null ? null : borderDecoration.toNew());
        return this;
    }

    /**
     * Enables or disables the top border.
     *
     * @param borderTop top border flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderTop(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderTop(boolean borderTop) {
        super.borderTop(borderTop);
        return this;
    }

    /**
     * Enables or disables the right border.
     *
     * @param borderRight right border flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderRight(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderRight(boolean borderRight) {
        super.borderRight(borderRight);
        return this;
    }

    /**
     * Enables or disables the bottom border.
     *
     * @param borderBottom bottom border flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderBottom(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderBottom(boolean borderBottom) {
        super.borderBottom(borderBottom);
        return this;
    }

    /**
     * Enables or disables the left border.
     *
     * @param borderLeft left border flag
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderLeft(boolean)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderLeft(boolean borderLeft) {
        super.borderLeft(borderLeft);
        return this;
    }

    /**
     * Sets border background colors.
     *
     * @param colors colors
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor... colors) {
        super.borderBackground(colors);
        return this;
    }

    /**
     * Sets the top border background color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderTopBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderTopBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderTopBackground(color);
        return this;
    }

    /**
     * Sets the right border background color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderRightBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderRightBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderRightBackground(color);
        return this;
    }

    /**
     * Sets the bottom border background color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderBottomBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderBottomBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderBottomBackground(color);
        return this;
    }

    /**
     * Sets the left border background color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderLeftBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderLeftBackground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderLeftBackground(color);
        return this;
    }

    /**
     * Sets border foreground colors.
     *
     * @param colors colors
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor...)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor... colors) {
        super.borderForeground(colors);
        return this;
    }

    /**
     * Sets the top border foreground color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderTopForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderTopForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderTopForeground(color);
        return this;
    }

    /**
     * Sets the right border foreground color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderRightForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderRightForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderRightForeground(color);
        return this;
    }

    /**
     * Sets the bottom border foreground color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderBottomForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderBottomForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderBottomForeground(color);
        return this;
    }

    /**
     * Sets the left border foreground color.
     *
     * @param color color
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#borderLeftForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style borderLeftForeground(com.williamcallahan.tui4j.compat.lipgloss.color.TerminalColor color) {
        super.borderLeftForeground(color);
        return this;
    }

    /**
     * Sets the transform function.
     *
     * @param transformFunction transform function
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#transform(Function)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style transform(Function<String, String> transformFunction) {
        super.transform(transformFunction);
        return this;
    }

    /**
     * Returns a copy of this style.
     *
     * @return copied style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#copy()} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style copy() {
        return new Style(this);
    }

    /**
     * Inherits unset values from another style.
     *
     * @param style style to inherit from
     * @return this style
     * @deprecated Use {@link com.williamcallahan.tui4j.compat.lipgloss.Style#inherit(com.williamcallahan.tui4j.compat.lipgloss.Style)} instead.
     */
    @Deprecated(since = "0.3.1")
    @Override
    public Style inherit(com.williamcallahan.tui4j.compat.lipgloss.Style style) {
        super.inherit(style);
        return this;
    }
}
