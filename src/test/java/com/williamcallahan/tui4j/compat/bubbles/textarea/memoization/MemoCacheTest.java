package com.williamcallahan.tui4j.compat.bubbles.textarea.memoization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the textarea memoization cache.
 */
class MemoCacheTest {

    /**
     * Verifies a cache miss returns no hit.
     */
    @Test
    void testCacheMiss() {
        MemoCache<HashableStringKey, String> cache = new MemoCache<>(5);
        MemoCache.Lookup<String> lookup = cache.get(new HashableStringKey("missing"));
        assertFalse(lookup.hit(), "Expected miss for missing key");
        assertEquals(null, lookup.value(), "Expected null value on miss");
    }

    /**
     * Verifies set and get behavior.
     */
    @Test
    void testSetAndGet() {
        MemoCache<HashableStringKey, String> cache = new MemoCache<>(10);
        cache.set(new HashableStringKey("key1"), "value1");
        MemoCache.Lookup<String> lookup = cache.get(new HashableStringKey("key1"));
        assertTrue(lookup.hit(), "Expected hit for key1");
        assertEquals("value1", lookup.value(), "Expected stored value");

        cache.set(new HashableStringKey("key1"), "value2");
        lookup = cache.get(new HashableStringKey("key1"));
        assertTrue(lookup.hit(), "Expected hit after update");
        assertEquals("value2", lookup.value(), "Expected updated value");
    }

    /**
     * Verifies LRU eviction behavior.
     */
    @Test
    void testEviction() {
        MemoCache<HashableStringKey, Integer> cache = new MemoCache<>(2);
        cache.set(new HashableStringKey("1"), 1);
        cache.set(new HashableStringKey("2"), 2);
        cache.get(new HashableStringKey("1")); // mark "1" as recently used
        cache.set(new HashableStringKey("3"), 3);

        MemoCache.Lookup<Integer> lookup = cache.get(new HashableStringKey("2"));
        assertFalse(lookup.hit(), "Expected key 2 to be evicted");
        assertEquals(null, lookup.value(), "Expected null for evicted key");
    }

    /**
     * Verifies null values are stored as hits.
     */
    @Test
    void testNullValue() {
        MemoCache<HashableStringKey, String> cache = new MemoCache<>(5);
        cache.set(new HashableStringKey("nilKey"), null);
        MemoCache.Lookup<String> lookup = cache.get(new HashableStringKey("nilKey"));
        assertTrue(lookup.hit(), "Expected hit for null value");
        assertEquals(null, lookup.value(), "Expected null value to be stored");
    }
}
