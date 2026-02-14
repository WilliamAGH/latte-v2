package com.williamcallahan.tui4j.compat.bubbletea.input;

import java.util.Arrays;

/**
 * Character array operations for input sequence matching.
 * <p>
 * Provides prefix/suffix/search operations used by the input parser
 * to match escape sequences against raw character buffers.
 * <p>
 * Bubble Tea: bubbletea/inputreader_other.go (inline helpers)
 */
final class InputChars {

    private InputChars() {}

    /**
     * Tests whether the character array starts with the given prefix string.
     *
     * @param input character array to test
     * @param prefix prefix to look for
     * @return true if input starts with prefix
     */
    static boolean startsWith(char[] input, String prefix) {
        if (input.length < prefix.length()) {
            return false;
        }
        for (int i = 0; i < prefix.length(); i++) {
            if (input[i] != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests whether the character array ends with the given suffix string.
     *
     * @param input character array to test
     * @param suffix suffix to look for
     * @return true if input ends with suffix
     */
    static boolean endsWith(char[] input, String suffix) {
        if (input.length < suffix.length()) {
            return false;
        }
        for (int i = 0; i < suffix.length(); i++) {
            if (input[input.length - suffix.length() + i] != suffix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first occurrence of the search string in the character array.
     *
     * @param input character array to search
     * @param search string to find
     * @return index of first occurrence, or -1 if not found
     */
    static int indexOf(char[] input, String search) {
        return new String(input).indexOf(search);
    }

    /**
     * Concatenates two character arrays.
     *
     * @param first first array
     * @param second second array
     * @return new array containing first followed by second
     */
    static char[] append(char[] first, char[] second) {
        char[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
