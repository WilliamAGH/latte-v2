package com.williamcallahan.tui4j.ansi;

/**
 * ANSI parser action token.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/Action.java
 */
public enum Action {
    NONE,
    CLEAR,
    COLLECT,
    MARKER,
    DISPATCH,
    EXECUTE,
    START,
    PUT,
    PARAM,
    PRINT,
    IGNORE;

    public static Action fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}