package com.williamcallahan.tui4j.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests resetting the mouse cursor.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/message/ResetMouseCursorMessage.java
 */
public record ResetMouseCursorMessage() implements Message {
}
