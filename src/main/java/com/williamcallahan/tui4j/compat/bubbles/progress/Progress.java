package com.williamcallahan.tui4j.compat.bubbles.progress;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.lipgloss.color.RGB;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Progress bar bubble with gradient support.
 * <p>
 * Port of {@code bubbles/progress}.
 * Upstream: bubbles/progress/progress.go
 * Visualizes a percentage value, optionally using a spring animation for smooth transitions.
 * <p>
 * Rendering is delegated to {@link ProgressRenderer};
 * animation lifecycle is delegated to {@link ProgressAnimator}.
 */
public class Progress implements Model {

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

    private final ProgressRenderer renderer;
    private final ProgressAnimator animator;

    /** Creates a progress model with default settings. */
    public Progress() {
        this.id = nextId();
        this.width = 40;
        this.full = '█';
        this.fullColor = "#7571F9";
        this.empty = '░';
        this.emptyColor = "#606060";
        this.showPercentage = true;
        this.percentFormat = " %3.0f%%";
        this.percentageStyle = Style.newStyle();
        this.renderer = new ProgressRenderer();
        this.animator = new ProgressAnimator();
    }

    private static int nextId() {
        return LAST_ID.incrementAndGet();
    }

    /** Sets the progress bar width. */
    public Progress withWidth(int width) {
        this.width = width;
        return this;
    }

    /** Sets the filled character. */
    public Progress withFull(char full) {
        this.full = full;
        return this;
    }

    /** Sets the empty character. */
    public Progress withEmpty(char empty) {
        this.empty = empty;
        return this;
    }

    /** Sets the filled color (disables gradient). */
    public Progress withFullColor(String fullColor) {
        this.fullColor = fullColor;
        renderer.disableRamp();
        return this;
    }

    /** Sets the empty color. */
    public Progress withEmptyColor(String emptyColor) {
        this.emptyColor = emptyColor;
        return this;
    }

    /** Disables percentage display. */
    public Progress withoutPercentage() {
        this.showPercentage = false;
        return this;
    }

    /** Sets whether to show percentage text. */
    public Progress withShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
        return this;
    }

    /** Sets the percentage format string. */
    public Progress withPercentFormat(String percentFormat) {
        this.percentFormat = percentFormat;
        return this;
    }

    /** Sets the style for percentage text. */
    public Progress withPercentageStyle(Style percentageStyle) {
        this.percentageStyle = percentageStyle;
        return this;
    }

    /** Applies the default gradient. */
    public Progress withDefaultGradient() {
        return withGradient("#5A56E0", "#EE6FF8");
    }

    /** Configures a color gradient for the filled portion of the bar. */
    public Progress withGradient(String colorA, String colorB) {
        return setRamp(colorA, colorB, false);
    }

    /** Applies the default scaled gradient. */
    public Progress withDefaultScaledGradient() {
        return withScaledGradient("#5A56E0", "#EE6FF8");
    }

    /** Configures a scaled gradient for the filled portion of the bar. */
    public Progress withScaledGradient(String colorA, String colorB) {
        return setRamp(colorA, colorB, true);
    }

    private Progress setRamp(String colorA, String colorB, boolean scaled) {
        RGB a = ProgressRenderer.parseColor(colorA);
        RGB b = ProgressRenderer.parseColor(colorB);
        renderer.setRamp(a, b, scaled);
        return this;
    }

    /**
     * Sets spring animation parameters.
     *
     * @param frequency spring frequency
     * @param damping spring damping
     */
    public void setSpringOptions(double frequency, double damping) {
        animator.setSpringOptions(frequency, damping);
    }

    /** Returns whether spring options have been customized. */
    public boolean isSpringCustomized() {
        return animator.isSpringCustomized();
    }

    /** Sets the color profile override. */
    public void setColorProfile(ColorProfile colorProfile) {
        renderer.setColorProfile(colorProfile);
    }

    /** Returns the progress model id. */
    public int id() {
        return id;
    }

    /** Returns the bar width. */
    public int width() {
        return width;
    }

    /** Sets the bar width. */
    public void setWidth(int width) {
        this.width = width;
    }

    /** Returns the filled character. */
    public char full() {
        return full;
    }

    /** Returns the empty character. */
    public char empty() {
        return empty;
    }

    /** Returns the filled color string. */
    public String fullColor() {
        return fullColor;
    }

    /** Returns the empty color string. */
    public String emptyColor() {
        return emptyColor;
    }

    /** Returns whether percentage display is enabled. */
    public boolean showPercentage() {
        return showPercentage;
    }

    /** Returns the percent format string. */
    public String percentFormat() {
        return percentFormat;
    }

    /** Returns the target percent value. */
    public double percent() {
        return animator.targetPercent();
    }

    /** Returns the target percent value. */
    public double targetPercent() {
        return animator.targetPercent();
    }

    /** Returns the current animation tag. */
    public int tag() {
        return tag;
    }

    /** Returns the currently displayed percent. */
    public double percentShown() {
        return animator.percentShown();
    }

    /** Returns whether the spring animation is active. */
    public boolean isAnimating() {
        return animator.isAnimating();
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
            return animator.handleFrame(this, frameMsg.id(), frameMsg.tag());
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return viewAs(animator.percentShown());
    }

    /** Renders the progress bar at the provided percent. */
    public String viewAs(double percent) {
        return renderer.viewAs(percent, width, full, fullColor,
                empty, emptyColor, showPercentage, percentFormat, percentageStyle);
    }

    /**
     * Sets the target percent and schedules animation.
     *
     * @param p target percent (0-1)
     * @return animation command
     */
    public Command setPercent(double p) {
        this.tag++;
        return animator.setPercent(p, id, tag);
    }

    /**
     * Increments the target percent.
     *
     * @param v delta percent
     * @return animation command
     */
    public Command incrPercent(double v) {
        return setPercent(animator.targetPercent() + v);
    }

    /**
     * Decrements the target percent.
     *
     * @param v delta percent
     * @return animation command
     */
    public Command decrPercent(double v) {
        return setPercent(animator.targetPercent() - v);
    }
}
