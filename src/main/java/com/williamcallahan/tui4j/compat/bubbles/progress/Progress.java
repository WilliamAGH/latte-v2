package com.williamcallahan.tui4j.compat.bubbles.progress;

import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.lipgloss.color.RGB;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Progress bar bubble with gradient support.
 * <p>
 * Port of `bubbles/progress`.
 * Upstream: bubbles/progress/progress.go
 * Visualizes a percentage value, optionally using a spring animation for smooth transitions.
 */
public class Progress implements Model {

    private static final double FPS = 60.0;
    private static final int DEFAULT_WIDTH = 40;
    private static final double DEFAULT_FREQUENCY = 18.0;
    private static final double DEFAULT_DAMPING = 1.0;
    private static final AtomicInteger LAST_ID = new AtomicInteger(0);

    private final int id;
    private int tag;

    private int width;
    private char full;
    private char empty;
    private String fullColor;
    private String emptyColor;
    private boolean showPercentage;
    private String percentFormat;
    private Style percentageStyle;

    private Spring spring;
    private boolean springCustomized;
    private double percentShown;
    private double targetPercent;
    private double velocity;

    private boolean useRamp;
    private RGB rampColorA;
    private RGB rampColorB;
    private boolean scaleRamp;

    private ColorProfile colorProfile;
    private ColorProfile cachedColorProfile;

    /**
     * Creates a progress model with default settings.
     */
    public Progress() {
        this.id = nextId();
        this.width = DEFAULT_WIDTH;
        this.full = '█';
        this.fullColor = "#7571F9";
        this.empty = '░';
        this.emptyColor = "#606060";
        this.showPercentage = true;
        this.percentFormat = " %3.0f%%";
        this.percentageStyle = Style.newStyle();
        this.spring = null;
        this.springCustomized = false;
    }

    private static int nextId() {
        return LAST_ID.incrementAndGet();
    }

    /**
     * Sets the progress bar width.
     *
     * @param width bar width
     * @return this progress model
     */
    public Progress withWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the filled character.
     *
     * @param full filled character
     * @return this progress model
     */
    public Progress withFull(char full) {
        this.full = full;
        return this;
    }

    /**
     * Sets the empty character.
     *
     * @param empty empty character
     * @return this progress model
     */
    public Progress withEmpty(char empty) {
        this.empty = empty;
        return this;
    }

    /**
     * Sets the filled color (disables gradient).
     *
     * @param fullColor filled color
     * @return this progress model
     */
    public Progress withFullColor(String fullColor) {
        this.fullColor = fullColor;
        this.useRamp = false;
        return this;
    }

    /**
     * Sets the empty color.
     *
     * @param emptyColor empty color
     * @return this progress model
     */
    public Progress withEmptyColor(String emptyColor) {
        this.emptyColor = emptyColor;
        return this;
    }

    /**
     * Disables percentage display.
     *
     * @return this progress model
     */
    public Progress withoutPercentage() {
        this.showPercentage = false;
        return this;
    }

    /**
     * Sets whether to show percentage text.
     *
     * @param showPercentage show percentage flag
     * @return this progress model
     */
    public Progress withShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
        return this;
    }

    /**
     * Sets the percentage format string.
     *
     * @param percentFormat percent format
     * @return this progress model
     */
    public Progress withPercentFormat(String percentFormat) {
        this.percentFormat = percentFormat;
        return this;
    }

    /**
     * Sets the style for percentage text.
     *
     * @param percentageStyle percentage style
     * @return this progress model
     */
    public Progress withPercentageStyle(Style percentageStyle) {
        this.percentageStyle = percentageStyle;
        return this;
    }

    /**
     * Applies the default gradient.
     *
     * @return this progress model
     */
    public Progress withDefaultGradient() {
        return withGradient("#5A56E0", "#EE6FF8");
    }

    /**
     * Configures a color gradient for the filled portion of the bar.
     *
     * @param colorA start color
     * @param colorB end color
     * @return this progress model
     */
    public Progress withGradient(String colorA, String colorB) {
        return setRamp(colorA, colorB, false);
    }

    /**
     * Applies the default scaled gradient.
     *
     * @return this progress model
     */
    public Progress withDefaultScaledGradient() {
        return withScaledGradient("#5A56E0", "#EE6FF8");
    }

    /**
     * Configures a scaled gradient for the filled portion of the bar.
     *
     * @param colorA start color
     * @param colorB end color
     * @return this progress model
     */
    public Progress withScaledGradient(String colorA, String colorB) {
        return setRamp(colorA, colorB, true);
    }

    private Progress setRamp(String colorA, String colorB, boolean scaled) {
        RGB a = parseColor(colorA);
        RGB b = parseColor(colorB);

        this.useRamp = true;
        this.scaleRamp = scaled;
        this.rampColorA = a;
        this.rampColorB = b;
        return this;
    }

    private static RGB parseColor(String color) {
        return RGB.fromHexString(color);
    }

    /**
     * Sets spring animation parameters.
     *
     * @param frequency spring frequency
     * @param damping spring damping
     */
    public void setSpringOptions(double frequency, double damping) {
        this.spring = new Spring(frequency, damping);
        this.springCustomized = true;
    }

    /**
     * Returns whether spring options have been customized.
     *
     * @return {@code true} when custom spring options have been set
     */
    public boolean isSpringCustomized() {
        return springCustomized;
    }

    /**
     * Sets the color profile override.
     *
     * @param colorProfile color profile
     */
    public void setColorProfile(ColorProfile colorProfile) {
        this.colorProfile = colorProfile;
    }

    private ColorProfile getColorProfile() {
        if (colorProfile != null) {
            return colorProfile;
        }
        if (cachedColorProfile == null) {
            cachedColorProfile = Renderer.defaultRenderer().colorProfile();
        }
        return cachedColorProfile;
    }

    /**
     * Returns the progress model id.
     *
     * @return model id
     */
    public int id() {
        return id;
    }

    /**
     * Returns the bar width.
     *
     * @return bar width
     */
    public int width() {
        return width;
    }

    /**
     * Sets the bar width.
     *
     * @param width bar width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the filled character.
     *
     * @return filled character
     */
    public char full() {
        return full;
    }

    /**
     * Returns the empty character.
     *
     * @return empty character
     */
    public char empty() {
        return empty;
    }

    /**
     * Returns the filled color string.
     *
     * @return filled color
     */
    public String fullColor() {
        return fullColor;
    }

    /**
     * Returns the empty color string.
     *
     * @return empty color
     */
    public String emptyColor() {
        return emptyColor;
    }

    /**
     * Returns whether percentage display is enabled.
     *
     * @return {@code true} when percentage is shown
     */
    public boolean showPercentage() {
        return showPercentage;
    }

    /**
     * Returns the percent format string.
     *
     * @return percent format
     */
    public String percentFormat() {
        return percentFormat;
    }

    /**
     * Returns the target percent value.
     *
     * @return target percent
     */
    public double percent() {
        return targetPercent;
    }

    /**
     * Returns the target percent value.
     *
     * @return target percent
     */
    public double targetPercent() {
        return targetPercent;
    }

    /**
     * Returns the current animation tag.
     *
     * @return animation tag
     */
    public int tag() {
        return tag;
    }

    /**
     * Returns the currently displayed percent.
     *
     * @return displayed percent
     */
    public double percentShown() {
        return percentShown;
    }

    /**
     * Returns whether the spring animation is active.
     *
     * @return {@code true} when animating
     */
    public boolean isAnimating() {
        double dist = Math.abs(percentShown - targetPercent);
        return !(dist < 0.001 && Math.abs(velocity) < 0.01);
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<Progress> update(Message msg) {
        if (msg instanceof SetPercentMessage setMsg) {
            return UpdateResult.from(this, setPercent(setMsg.percent()));
        }
        if (msg instanceof FrameMessage frameMsg) {
            return handleFrame(frameMsg.id(), frameMsg.tag());
        }

        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return viewAs(percentShown);
    }

    /**
     * Renders the progress bar at the provided percent.
     *
     * @param percent percent to render
     * @return rendered progress bar
     */
    public String viewAs(double percent) {
        StringBuilder b = new StringBuilder();
        String percentView = percentageView(percent);
        int textWidth = textWidth(percentView);
        barView(b, percent, textWidth);
        b.append(percentView);
        return b.toString();
    }

    /**
     * Returns the display width of a string in terminal cells.
     * <p>
     * Delegates to {@link TextWidth#measureCellWidth(String)} which uses
     * grapheme clustering for accurate width calculation of:
     * <ul>
     *   <li>ANSI escape codes (ignored, not counted)</li>
     *   <li>CJK characters (counted as 2 cells based on Unicode blocks)</li>
     *   <li>Emojis and ZWJ sequences (counted as 2 cells)</li>
     *   <li>Combining marks (counted as 0 cells)</li>
     * </ul>
     * This replaces the previous simple heuristic (chars &gt;= 0x1100 = 2 cells)
     * with proper Unicode-aware width calculation.
     */
    private static int textWidth(String s) {
        return TextWidth.measureCellWidth(s);
    }

    private void barView(StringBuilder b, double percent, int textWidth) {
        int tw = Math.max(0, width - textWidth);
        int fw = (int) Math.round(tw * percent);

        fw = Math.clamp(fw, 0, tw);

        if (useRamp) {
            for (int i = 0; i < fw; i++) {
                double p;
                if (fw == 1) {
                    p = 0.5;
                } else if (scaleRamp) {
                    p = (double) i / (fw - 1);
                } else {
                    p = (double) i / (tw - 1);
                }
                RGB blended = blend(rampColorA, rampColorB, p);
                String color = rgbToHex(blended);
                b.append(colorize(String.valueOf(full), color));
            }
        } else {
            String colored = colorize(String.valueOf(full), fullColor);
            b.append(colored.repeat(fw));
        }

        String emptyColored = colorize(String.valueOf(empty), emptyColor);
        int n = Math.max(0, tw - fw);
        b.append(emptyColored.repeat(n));
    }

    private static RGB blend(RGB a, RGB b, double t) {
        return new RGB(
                (float) (a.r() + (b.r() - a.r()) * t),
                (float) (a.g() + (b.g() - a.g()) * t),
                (float) (a.b() + (b.b() - a.b()) * t)
        );
    }

    private static String rgbToHex(RGB rgb) {
        int r = Math.round(rgb.r() * 255.0f);
        int g = Math.round(rgb.g() * 255.0f);
        int b = Math.round(rgb.b() * 255.0f);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    private String colorize(String text, String color) {
        ColorProfile profile = getColorProfile();
        if (profile == null || profile == ColorProfile.Ascii) {
            return text;
        }
        return "\033[" + getANSIColorCode(color, profile) + "m" + text + "\033[0m";
    }

    private String getANSIColorCode(String color, ColorProfile profile) {
        RGB rgb = parseColor(color);
        int r = Math.round(rgb.r() * 255.0f);
        int g = Math.round(rgb.g() * 255.0f);
        int b = Math.round(rgb.b() * 255.0f);

        if (profile == ColorProfile.TrueColor) {
            return "38;2;" + r + ";" + g + ";" + b;
        } else {
            int ansi256 = rgbToANSI256(r, g, b);
            return "38;5;" + ansi256;
        }
    }

    private static int rgbToANSI256(int r, int g, int b) {
        if (r == g && g == b) {
            if (r < 8) {
                return 16;
            }
            if (r > 248) {
                return 231;
            }
            return (int) Math.round(((double) r - 8) / 247 * 24) + 232;
        }

        int rIdx = Math.round((float) r / 255 * 5);
        int gIdx = Math.round((float) g / 255 * 5);
        int bIdx = Math.round((float) b / 255 * 5);

        rIdx = Math.clamp(rIdx, 0, 5);
        gIdx = Math.clamp(gIdx, 0, 5);
        bIdx = Math.clamp(bIdx, 0, 5);

        return 16 + 36 * rIdx + 6 * gIdx + bIdx;
    }

    private String percentageView(double percent) {
        if (!showPercentage) {
            return "";
        }
        percent = Math.clamp(percent, 0.0, 1.0);
        String percentage = String.format(percentFormat, percent * 100);
        if (percentageStyle != null) {
            percentage = percentageStyle.copy().inline(true).render(percentage);
        }
        return percentage;
    }

    private Command nextFrame() {
        return Command.tick(Duration.ofNanos((long) (1_000_000_000.0 / FPS)), time -> new FrameMessage(id, tag));
    }

    private UpdateResult<Progress> handleFrame(int frameId, int frameTag) {
        if (frameId != id || frameTag != tag) {
            return UpdateResult.from(this);
        }

        if (!isAnimating()) {
            return UpdateResult.from(this);
        }

        // Lazy initialization: apply default spring options if not customized
        if (spring == null) {
            this.spring = new Spring(DEFAULT_FREQUENCY, DEFAULT_DAMPING);
        }

        Spring.SpringUpdateResult result = spring.update(percentShown, velocity, targetPercent);
        this.percentShown = result.position();
        this.velocity = result.velocity();
        return UpdateResult.from(this, nextFrame());
    }

    /**
     * Sets the target percent and schedules animation.
     *
     * @param p target percent (0-1)
     * @return animation command
     */
    public Command setPercent(double p) {
        this.targetPercent = Math.clamp(p, 0.0, 1.0);
        this.tag++;
        return nextFrame();
    }

    /**
     * Increments the target percent.
     *
     * @param v delta percent
     * @return animation command
     */
    public Command incrPercent(double v) {
        return setPercent(targetPercent + v);
    }

    /**
     * Decrements the target percent.
     *
     * @param v delta percent
     * @return animation command
     */
    public Command decrPercent(double v) {
        return setPercent(targetPercent - v);
    }
}
