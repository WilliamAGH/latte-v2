package com.williamcallahan.tui4j.message;

import com.williamcallahan.tui4j.Message;

/**
 * Reports an unrecognized input sequence.
 * tui4j extension; no Bubble Tea equivalent.
 * tui4j: src/main/java/com/williamcallahan/tui4j/message/UnknownSequenceMessage.java
 */
public class UnknownSequenceMessage implements Message {
    private final String sequence;

    public UnknownSequenceMessage(String sequence) {
        this.sequence = sequence;
    }

    public String sequence() { return sequence; }
}
