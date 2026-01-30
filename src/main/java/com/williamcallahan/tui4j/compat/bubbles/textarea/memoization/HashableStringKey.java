package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        return sha256Hex(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns a SHA-256 hex digest of the input bytes.
     *
     * @param input input bytes
     * @return hex digest
     */
    private static String sha256Hex(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input);
            StringBuilder builder = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
