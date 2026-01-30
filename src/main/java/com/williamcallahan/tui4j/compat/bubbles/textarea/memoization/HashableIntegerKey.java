package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.nio.charset.StandardCharsets;

/**
 * Hashable integer key for memoization.
 * <p>
 * Port of {@code bubbles/textarea/memoization/memoization.go} HInt.
 */
public final class HashableIntegerKey implements Hasher {

    private final int value;

    /**
     * Creates a hashable integer.
     *
     * @param value integer value
     */
    public HashableIntegerKey(int value) {
        this.value = value;
    }

    /**
     * Returns the underlying integer value.
     *
     * @return integer value
     */
    public int value() {
        return value;
    }

    /**
     * Returns the SHA-256 hash of the integer value.
     *
     * @return hash string
     */
    @Override
    public String hash() {
        return MemoizationHashing.sha256Hex(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
    }
}
