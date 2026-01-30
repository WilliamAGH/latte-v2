package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.compat.bubbles.textarea.memoization.Hasher;
import com.williamcallahan.tui4j.compat.bubbles.textarea.memoization.MemoCache;
import com.williamcallahan.tui4j.compat.bubbles.textarea.memoization.MemoizationHashing;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Memoized wrapper for textarea line wrapping.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} memoization usage.
 */
final class TextAreaWrapCache {

    private MemoCache<LineKey, List<int[]>> cache;

    /**
     * Creates a cache with the given capacity.
     *
     * @param capacity max number of entries
     */
    TextAreaWrapCache(int capacity) {
        this.cache = new MemoCache<>(capacity);
    }

    /**
     * Returns the cache capacity.
     *
     * @return capacity
     */
    int capacity() {
        return cache.capacity();
    }

    /**
     * Resets the cache with a new capacity.
     *
     * @param capacity new capacity
     */
    void reset(int capacity) {
        this.cache = new MemoCache<>(capacity);
    }

    /**
     * Returns wrapped lines, using memoization when available.
     *
     * @param runes input line runes
     * @param width wrap width
     * @return wrapped lines
     */
    List<int[]> wrap(int[] runes, int width) {
        LineKey key = new LineKey(runes, width);
        MemoCache.Lookup<List<int[]>> lookup = cache.get(key);
        if (lookup.hit()) {
            return lookup.value();
        }
        List<int[]> wrapped = TextAreaWrap.wrap(runes, width);
        cache.set(key, wrapped);
        return wrapped;
    }

    /**
     * Hashable wrap key.
     * <p>
     * Port of {@code bubbles/textarea/textarea.go} line hashing.
     */
    private static final class LineKey implements Hasher {
        private final int[] runes;
        private final int width;

        /**
         * Creates a wrap cache key.
         *
         * @param runes line runes
         * @param width wrap width
         */
        LineKey(int[] runes, int width) {
            this.runes = Objects.requireNonNull(runes, "runes");
            this.width = width;
        }

        /**
         * Returns the hashed key value.
         *
         * @return hash string
         */
        @Override
        public String hash() {
            String input = TextAreaRunes.toString(runes) + ":" + width;
            return MemoizationHashing.sha256Hex(input.getBytes(StandardCharsets.UTF_8));
        }
    }
}
