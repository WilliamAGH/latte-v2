package com.williamcallahan.tui4j.compat.bubbles.progress;

import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.lipgloss.color.RGB;

/**
 * Rendering and color logic for {@link Progress}.
 * <p>
 * Upstream: bubbles/progress/progress.go (view, color helpers).
 */
final class ProgressRenderer {

    private boolean useRamp;
    private RGB rampColorA;
    private RGB rampColorB;
    private boolean scaleRamp;

    private ColorProfile colorProfile;
    private ColorProfile cachedColorProfile;

    ProgressRenderer() {
    }

    /** Enables a color gradient ramp on the filled bar portion. */
    void setRamp(RGB colorA, RGB colorB, boolean scaled) {
        this.useRamp = true;
        this.scaleRamp = scaled;
        this.rampColorA = colorA;
        this.rampColorB = colorB;
    }

    /** Disables the ramp when a flat fill color is set. */
    void disableRamp() {
        this.useRamp = false;
    }

    boolean useRamp() {
        return useRamp;
    }

    void setColorProfile(ColorProfile colorProfile) {
        this.colorProfile = colorProfile;
    }

    /**
     * Renders the progress bar at the provided percent.
     *
     * @param percent progress fraction (0..1)
     * @param width bar width in cells
     * @param full filled character
     * @param fullColor flat fill color hex
     * @param empty empty character
     * @param emptyColor empty color hex
     * @param showPercentage whether to append percentage text
     * @param percentFormat format string for percentage
     * @param percentageStyle style for percentage text
     * @return rendered progress bar string
     */
    String viewAs(double percent, int width, char full, String fullColor,
                  char empty, String emptyColor, boolean showPercentage,
                  String percentFormat, Style percentageStyle) {
        StringBuilder b = new StringBuilder();
        String percentView = percentageView(percent, showPercentage, percentFormat, percentageStyle);
        int textWidth = textWidth(percentView);
        barView(b, percent, textWidth, width, full, fullColor, empty, emptyColor);
        b.append(percentView);
        return b.toString();
    }

    private void barView(StringBuilder b, double percent, int textWidth,
                         int width, char full, String fullColor,
                         char empty, String emptyColor) {
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

    private static String percentageView(double percent, boolean showPercentage,
                                          String percentFormat, Style percentageStyle) {
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

    private static int textWidth(String s) {
        return TextWidth.measureCellWidth(s);
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

    private ColorProfile getColorProfile() {
        if (colorProfile != null) {
            return colorProfile;
        }
        if (cachedColorProfile == null) {
            cachedColorProfile = Renderer.defaultRenderer().colorProfile();
        }
        return cachedColorProfile;
    }

    static RGB parseColor(String color) {
        return RGB.fromHexString(color);
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
}
