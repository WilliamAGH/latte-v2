package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.Key;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;

/**
 * Key press input message.
 * Bubble Tea: bubbletea/key.go
 */
public class KeyPressMessage implements Message {

    private final Key key;

    public KeyPressMessage(Key key) {
        this.key = key;
    }

    public String key() {
        return key.toString();
    }

    public char[] runes() {
        return key.runes();
    }

    public KeyType type() {
        return key.type();
    }

    public boolean alt() {
        return key.alt();
    }
}
