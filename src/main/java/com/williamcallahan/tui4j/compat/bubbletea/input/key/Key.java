package com.williamcallahan.tui4j.compat.bubbletea.input.key;

/**
 * Port of Bubble Tea key.
 * Bubble Tea: bubbletea/key.go
 */
public record Key(KeyType type, char[] runes, boolean alt, boolean paste) {

    public Key(KeyType type) {
        this(type, new char[]{}, false, false);
    }

    public Key(KeyType type, char[] runes) {
        this(type, runes, false, false);
    }

    public Key(KeyType type, boolean alt) {
        this(type, new char[]{}, alt, false);
    }

    public Key(KeyType type, char[] runes, boolean alt) {
        this(type, runes, alt, false);
    }

    @Override
    public String toString() {
        String keyName = KeyNames.getKeyName(type);
        StringBuilder builder = new StringBuilder();

        if (alt) {
            builder.append("alt+");
        }

        if (type == KeyType.KeyRunes) {
            if (paste) {
                builder.append('[');
            }
            builder.append(new String(runes));
            if (paste) {
                builder.append(']');
            }
            return builder.toString();
        } else if (keyName != null) {
            builder.append(keyName);
            return builder.toString();
        }

        return builder.toString();
    }
}
