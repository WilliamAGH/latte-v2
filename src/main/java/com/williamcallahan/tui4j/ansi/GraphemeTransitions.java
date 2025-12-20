package com.williamcallahan.tui4j.ansi;

/**
 * Transition rules for grapheme cluster parsing.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/GraphemeTransitions.java
 */
public class GraphemeTransitions {
    // Grapheme cluster parser states
    private static final int GR_ANY = 0;
    private static final int GR_CR = 1;
    private static final int GR_CONTROL_LF = 2;
    private static final int GR_L = 3;
    private static final int GR_LVV = 4;
    private static final int GR_LVTT = 5;
    private static final int GR_PREPEND = 6;
    private static final int GR_EXTENDED_PICTOGRAPHIC = 7;
    private static final int GR_EXTENDED_PICTOGRAPHIC_ZWJ = 8;
    private static final int GR_RI_ODD = 9;
    private static final int GR_RI_EVEN = 10;

    // Breaking instructions
    private static final int GR_NO_BOUNDARY = 0;
    private static final int GR_BOUNDARY = 1;

    // Properties (these should match the values from propertyGraphemes)
    private static final int PR_ANY = 0;
    private static final int PR_CR = 1;
    private static final int PR_LF = 2;
    private static final int PR_CONTROL = 3;
    private static final int PR_EXTEND = 4;
    private static final int PR_ZWJ = 5;
    private static final int PR_PREPEND = 7;
    private static final int PR_SPACING_MARK = 8;
    private static final int PR_L = 9;
    private static final int PR_V = 10;
    private static final int PR_T = 11;
    private static final int PR_LV = 12;
    private static final int PR_LVT = 13;
    private static final int PR_EXTENDED_PICTOGRAPHIC = 14;
    private static final int PR_REGIONAL_INDICATOR = 15;

    static class TransitionResult {
        final int newState;
        final int newProp;
        final int boundary;

        TransitionResult(int newState, int newProp, int boundary) {
            this.newState = newState;
            this.newProp = newProp;
            this.boundary = boundary;
        }
    }

    private static final long KEY_GR_ANY_PR_CR = ((long)GR_ANY | ((long)PR_CR << 32));
    private static final long KEY_GR_ANY_PR_LF = ((long)GR_ANY | ((long)PR_LF << 32));
    private static final long KEY_GR_ANY_PR_CONTROL = ((long)GR_ANY | ((long)PR_CONTROL << 32));
    private static final long KEY_GR_CR_PR_ANY = ((long)GR_CR | ((long)PR_ANY << 32));
    private static final long KEY_GR_CONTROL_LF_PR_ANY = ((long)GR_CONTROL_LF | ((long)PR_ANY << 32));
    private static final long KEY_GR_CR_PR_LF = ((long)GR_CR | ((long)PR_LF << 32));
    // Add more key constants as needed...

    private static TransitionResult grTransitions(int state, int prop) {
        long key = ((long)state) | ((long)prop << 32);

        if (key == KEY_GR_ANY_PR_CR) {
            return new TransitionResult(GR_CR, GR_BOUNDARY, 50);
        }
        if (key == KEY_GR_ANY_PR_LF) {
            return new TransitionResult(GR_CONTROL_LF, GR_BOUNDARY, 50);
        }
        if (key == KEY_GR_ANY_PR_CONTROL) {
            return new TransitionResult(GR_CONTROL_LF, GR_BOUNDARY, 50);
        }
        if (key == KEY_GR_CR_PR_ANY) {
            return new TransitionResult(GR_ANY, GR_BOUNDARY, 40);
        }
        if (key == KEY_GR_CONTROL_LF_PR_ANY) {
            return new TransitionResult(GR_ANY, GR_BOUNDARY, 40);
        }
        if (key == KEY_GR_CR_PR_LF) {
            return new TransitionResult(GR_CONTROL_LF, GR_NO_BOUNDARY, 30);
        }
        if (key == ((long)GR_ANY | ((long)PR_L << 32))) {
            return new TransitionResult(GR_L, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_L | ((long)PR_L << 32))) {
            return new TransitionResult(GR_L, GR_NO_BOUNDARY, 60);
        }
        if (key == ((long)GR_L | ((long)PR_V << 32))) {
            return new TransitionResult(GR_LVV, GR_NO_BOUNDARY, 60);
        }
        if (key == ((long)GR_L | ((long)PR_LV << 32))) {
            return new TransitionResult(GR_LVV, GR_NO_BOUNDARY, 60);
        }
        if (key == ((long)GR_L | ((long)PR_LVT << 32))) {
            return new TransitionResult(GR_LVTT, GR_NO_BOUNDARY, 60);
        }
        if (key == ((long)GR_ANY | ((long)PR_LV << 32))) {
            return new TransitionResult(GR_LVV, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_ANY | ((long)PR_V << 32))) {
            return new TransitionResult(GR_LVV, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_LVV | ((long)PR_V << 32))) {
            return new TransitionResult(GR_LVV, GR_NO_BOUNDARY, 70);
        }
        if (key == ((long)GR_LVV | ((long)PR_T << 32))) {
            return new TransitionResult(GR_LVTT, GR_NO_BOUNDARY, 70);
        }
        if (key == ((long)GR_ANY | ((long)PR_LVT << 32))) {
            return new TransitionResult(GR_LVTT, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_ANY | ((long)PR_T << 32))) {
            return new TransitionResult(GR_LVTT, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_LVTT | ((long)PR_T << 32))) {
            return new TransitionResult(GR_LVTT, GR_NO_BOUNDARY, 80);
        }
        if (key == ((long)GR_ANY | ((long)PR_EXTEND << 32))) {
            return new TransitionResult(GR_ANY, GR_NO_BOUNDARY, 90);
        }
        if (key == ((long)GR_ANY | ((long)PR_ZWJ << 32))) {
            return new TransitionResult(GR_ANY, GR_NO_BOUNDARY, 90);
        }
        if (key == ((long)GR_ANY | ((long)PR_SPACING_MARK << 32))) {
            return new TransitionResult(GR_ANY, GR_NO_BOUNDARY, 91);
        }
        if (key == ((long)GR_ANY | ((long)PR_PREPEND << 32))) {
            return new TransitionResult(GR_PREPEND, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_PREPEND | ((long)PR_ANY << 32))) {
            return new TransitionResult(GR_ANY, GR_NO_BOUNDARY, 92);
        }
        if (key == ((long)GR_ANY | ((long)PR_EXTENDED_PICTOGRAPHIC << 32))) {
            return new TransitionResult(GR_EXTENDED_PICTOGRAPHIC, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_EXTENDED_PICTOGRAPHIC | ((long)PR_EXTEND << 32))) {
            return new TransitionResult(GR_EXTENDED_PICTOGRAPHIC, GR_NO_BOUNDARY, 110);
        }
        if (key == ((long)GR_EXTENDED_PICTOGRAPHIC | ((long)PR_ZWJ << 32))) {
            return new TransitionResult(GR_EXTENDED_PICTOGRAPHIC_ZWJ, GR_NO_BOUNDARY, 110);
        }
        if (key == ((long)GR_EXTENDED_PICTOGRAPHIC_ZWJ | ((long)PR_EXTENDED_PICTOGRAPHIC << 32))) {
            return new TransitionResult(GR_EXTENDED_PICTOGRAPHIC, GR_NO_BOUNDARY, 110);
        }
        if (key == ((long)GR_ANY | ((long)PR_REGIONAL_INDICATOR << 32))) {
            return new TransitionResult(GR_RI_ODD, GR_BOUNDARY, 9990);
        }
        if (key == ((long)GR_RI_ODD | ((long)PR_REGIONAL_INDICATOR << 32))) {
            return new TransitionResult(GR_RI_EVEN, GR_NO_BOUNDARY, 120);
        }
        if (key == ((long)GR_RI_EVEN | ((long)PR_REGIONAL_INDICATOR << 32))) {
            return new TransitionResult(GR_RI_ODD, GR_BOUNDARY, 120);
        }
        return new TransitionResult(-1, -1, -1);
    }

    public static int[] transitionGraphemeState(int state, int r) {
        // Determine the property of the next character
        int prop = propertyGraphemes(r);

        // Find the applicable transition
        TransitionResult specificTrans = grTransitions(state, prop);
        if (specificTrans.newState >= 0) {
            // We have a specific transition. We'll use it.
            return new int[] { specificTrans.newState, prop, specificTrans.boundary == GR_BOUNDARY ? 1 : 0 };
        }

        // No specific transition found. Try the less specific ones.
        TransitionResult anyPropTrans = grTransitions(state, PR_ANY);
        TransitionResult anyStateTrans = grTransitions(GR_ANY, prop);

        if (anyPropTrans.newState >= 0 && anyStateTrans.newState >= 0) {
            // Both apply. We'll use a mix
            boolean boundary = anyStateTrans.boundary == GR_BOUNDARY;
            if (anyPropTrans.boundary < anyStateTrans.boundary) {
                boundary = anyPropTrans.boundary == GR_BOUNDARY;
            }
            return new int[] { anyStateTrans.newState, prop, boundary ? 1 : 0 };
        }

        if (anyPropTrans.newState >= 0) {
            // We only have a specific state
            return new int[] { anyPropTrans.newState, prop, anyPropTrans.boundary == GR_BOUNDARY ? 1 : 0 };
        }

        if (anyStateTrans.newState >= 0) {
            // We only have a specific property
            return new int[] { anyStateTrans.newState, prop, anyStateTrans.boundary == GR_BOUNDARY ? 1 : 0 };
        }

        // No known transition. GB999: Any ÷ Any
        return new int[] { GR_ANY, prop, 1 };
    }

    private static int propertyGraphemes(int codePoint) {
        // This needs to be implemented based on Unicode properties
        // For now returning PR_ANY
        return PR_ANY;
    }
}