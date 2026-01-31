package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

/**
 * Hashable key for memoization.
 * <p>
 * Port of {@code bubbles/textarea/memoization/memoization.go} Hasher interface.
 */
public interface Hasher {

    /**
     * Returns a stable hash string for this key.
     *
     * @return hash string
     */
    String hash();
}
