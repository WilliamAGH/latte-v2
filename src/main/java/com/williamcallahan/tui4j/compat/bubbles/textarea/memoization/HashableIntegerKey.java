package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        return sha256Hex(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
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
