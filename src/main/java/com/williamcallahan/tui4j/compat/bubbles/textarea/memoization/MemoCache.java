package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * LRU memoization cache for textarea wrapping.
 * <p>
 * Port of {@code bubbles/textarea/memoization/memoization.go}.
 *
 * @param <H> hashable key type
 * @param <T> value type
 */
public final class MemoCache<H extends Hasher, T> {

    private final int capacity;
    private final LinkedHashMap<String, T> cache;

    /**
     * Cache lookup result.
     *
     * @param <T> value type
     */
    public static final class Lookup<T> {
        private final T value;
        private final boolean hit;

        /**
         * Creates a lookup result.
         *
         * @param value cached value
         * @param hit whether the key was present
         */
        public Lookup(T value, boolean hit) {
            this.value = value;
            this.hit = hit;
        }

        /**
         * Returns the cached value.
         *
         * @return cached value (may be null)
         */
        public T value() {
            return value;
        }

        /**
         * Reports whether the cache contained the key.
         *
         * @return true when the key was present
         */
        public boolean hit() {
            return hit;
        }
    }

    /**
     * Creates a memoization cache with the provided capacity.
     *
     * @param capacity max number of entries
     */
    public MemoCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            /** {@inheritDoc} */
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
                return size() > MemoCache.this.capacity;
            }
        };
    }

    /**
     * Returns the cache capacity.
     *
     * @return capacity
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Returns the number of entries currently stored.
     *
     * @return current size
     */
    public synchronized int size() {
        return cache.size();
    }

    /**
     * Looks up a value by hashable key.
     *
     * @param key hashable key
     * @return lookup result
     */
    public synchronized Lookup<T> get(H key) {
        Objects.requireNonNull(key, "key");
        String hashedKey = key.hash();
        boolean hit = cache.containsKey(hashedKey);
        return new Lookup<>(cache.get(hashedKey), hit);
    }

    /**
     * Stores a value for the given key.
     *
     * @param key hashable key
     * @param value value to store (may be null)
     */
    public synchronized void set(H key, T value) {
        Objects.requireNonNull(key, "key");
        cache.put(key.hash(), value);
    }
}
