package com.williamcallahan.tui4j.message;

import com.williamcallahan.tui4j.Message;

/**
 * Reports an error from command execution.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/message/ErrorMessage.java
 *
 * @param error error payload
 */
public record ErrorMessage(Throwable error) implements Message {
}
