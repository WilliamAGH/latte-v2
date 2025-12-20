package com.williamcallahan.tui4j;

/**
 * Runtime exception for program startup or execution failures.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ProgramException.java
 */
public class ProgramException extends RuntimeException {
    public ProgramException(Throwable error) {
        super(error);
    }

    public ProgramException(String message, Throwable error) {
        super(message, error);
    }
}
