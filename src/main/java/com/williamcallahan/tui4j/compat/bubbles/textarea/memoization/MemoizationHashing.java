package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-256 hashing support for memoization keys.
 * <p>
 * Port of {@code bubbles/textarea/memoization/memoization.go} hashing helpers.
 */
public final class MemoizationHashing {

    /** Hex characters for SHA-256 digest formatting. */
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /** Prevents instantiation of this utility class. */
    private MemoizationHashing() {}

    /**
     * Returns a SHA-256 hex digest of the input bytes.
     *
     * @param input input bytes
     * @return hex digest
     */
    public static String sha256Hex(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input);
            StringBuilder builder = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                int v = b & 0xFF;
                builder.append(HEX[v >>> 4]).append(HEX[v & 0x0F]);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
