package com.williamcallahan.tui4j.ansi;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests grapheme cluster.
 * tui4j: src/test/java/com/williamcallahan/tui4j/ansi/GraphemeClusterTest.java
 */
class GraphemeClusterTest {

    @Test
    void testSimpleCharacters() {
        String text = "abc";
        List<String> expected = List.of("a", "b", "c");
        assertClustersEqual(text, expected);
    }

    @Test
    void testCombiningCharacters() {
        String text = "áb́"; // 'a' + acute accent, 'b' + acute accent
        List<String> expected = List.of("á", "b́");
        assertClustersEqual(text, expected);
    }

    @Test
    void testEmojis() {
        String text = "👩‍👩‍👧‍👦👨‍👩‍👦"; // Family emojis
        List<String> expected = List.of("👩‍👩‍👧‍👦", "👨‍👩‍👦");
        assertClustersEqual(text, expected);
    }

    @Test
    void testEdgeCases() {
        assertClustersEqual("", new ArrayList<>()); // Empty string
        assertClustersEqual("a", List.of("a")); // Single character
    }

    private void assertClustersEqual(String text, List<String> expected) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        List<String> actual = new ArrayList<>();
        int offset = 0;

        while (offset < bytes.length) {
            GraphemeCluster.GraphemeResult result = GraphemeCluster.getFirstGraphemeCluster(bytes, offset, -1);
            actual.add(new String(result.cluster(), StandardCharsets.UTF_8));
            offset += result.cluster().length;
        }

        assertEquals(expected, actual, "Clusters do not match for input: " + text);
    }
}