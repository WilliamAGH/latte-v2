package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color;

/**
 * Port of Lip Gloss hsl.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public record HSL(float h, float s, float l) {

    public boolean isDark() {
        return l < 0.5;
    }

    public float distance(HSL hsluv2) {
        double dH = (h - hsluv2.h) / 100.0;
        double dS = s - hsluv2.s;
        double dL = l - hsluv2.l;
        return (float)Math.sqrt(dH * dH + dS * dS + dL * dL);
    }
}
