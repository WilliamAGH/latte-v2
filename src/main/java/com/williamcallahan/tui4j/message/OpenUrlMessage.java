package com.williamcallahan.tui4j.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests opening a URL with the system handler.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/message/OpenUrlMessage.java
 *
 * @param url URL to open
 */
public record OpenUrlMessage(String url) implements Message {
}
