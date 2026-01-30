package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Hashable string key for memoization.
 * <p>
 * Port of {@code bubbles/textarea/memoization/memoization.go} HString.
 */
public final class HashableStringKey implements Hasher {

    private final String value;

    /**
     * Creates a hashable string.
     *
     * @param value string value
     */
    public HashableStringKey(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    /**
     * Returns the underlying string.
     *
     * @return string value
     */
    public String value() {
        return value;
    }

    /**
     * Returns the SHA-256 hash of the string.
     *
     * @return hash string
     */
    @Override
    public String hash() {
        return MemoizationHashing.sha256Hex(value.getBytes(StandardCharsets.UTF_8));
    }
}
