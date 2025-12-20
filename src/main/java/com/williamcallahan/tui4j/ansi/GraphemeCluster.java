package com.williamcallahan.tui4j.ansi;

import com.ibm.icu.text.BreakIterator;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Grapheme cluster parser.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/GraphemeCluster.java
 */
public class GraphemeCluster {

    public static GraphemeResult getFirstGraphemeCluster(byte[] bytes, int startIndex, int state) {
        // Convert bytes to string
        String text = new String(bytes, startIndex, bytes.length - startIndex, StandardCharsets.UTF_8);

        // Use BreakIterator to find grapheme boundaries
        BreakIterator iterator = BreakIterator.getCharacterInstance();
        iterator.setText(text);

        int clusterStart = iterator.first();
        int clusterEnd = iterator.next();

        if (clusterEnd == BreakIterator.DONE) {
            return null; // No grapheme cluster found
        }

        // Extract grapheme cluster
        String cluster = text.substring(clusterStart, clusterEnd);

        // Calculate width (e.g., handle wide characters)
        int width = calculateWidth(cluster);

        // Determine the rest of the bytes
        byte[] clusterBytes = cluster.getBytes(StandardCharsets.UTF_8);
        byte[] restBytes = Arrays.copyOfRange(bytes, startIndex + clusterBytes.length, bytes.length);

        // New state logic (customize as needed)
        int newState = (state == State.UTF8.ordinal()) ? State.GROUND.ordinal() : State.UTF8.ordinal();

        return new GraphemeResult(clusterBytes, restBytes, width, newState);
    }

    private static int calculateWidth(String cluster) {
        int codePoint = cluster.codePointAt(0);

        // Check for combining characters and other zero-width characters
        if (Character.getType(codePoint) == Character.NON_SPACING_MARK ||  // Combining marks
                Character.getType(codePoint) == Character.ENCLOSING_MARK ||
                Character.getType(codePoint) == Character.COMBINING_SPACING_MARK ||
                codePoint == 0x200B || // ZERO WIDTH SPACE
                codePoint == 0x200C || // ZERO WIDTH NON-JOINER
                codePoint == 0x200D || // ZERO WIDTH JOINER
                codePoint == 0xFEFF) { // ZERO WIDTH NO-BREAK SPACE
            return 0;
        }

        // Handle control characters
        if (Character.getType(codePoint) == Character.CONTROL) {
            return 0;
        }

        // Handle emojis
        if (Character.isSupplementaryCodePoint(codePoint) ||
                Character.UnicodeBlock.of(codePoint) == Character.UnicodeBlock.EMOTICONS ||
                Character.UnicodeBlock.of(codePoint) == Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS ||
                Character.UnicodeBlock.of(codePoint) == Character.UnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS ||
                Character.UnicodeBlock.of(codePoint) == Character.UnicodeBlock.SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS) {
            return 2; // Emojis and pictographs are wide
        }

        // Check for wide characters based on Unicode scripts and blocks
        Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);

        // Check if the character belongs to CJK or other wide character blocks
        if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                block == Character.UnicodeBlock.HIRAGANA ||
                block == Character.UnicodeBlock.KATAKANA ||
                block == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT ||
                block == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT ||
                block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                block == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS ||
                block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return 2; // Wide character
        }

        // Default width for narrow characters
        return 1;
    }

    public record GraphemeResult(byte[] cluster, byte[] rest, int width, int newState) {
    }
}
