package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.ansi.TextWidth;

import java.util.Arrays;
import java.util.Objects;

/**
 * Code point utilities for textarea input.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} rune helpers.
 */
final class TextAreaRunes {

    /**
     * Prevents instantiation.
     */
    private TextAreaRunes() {
    }

    /**
     * Converts a string to code points.
     *
     * @param input input string
     * @return code point array
     */
    static int[] toCodePoints(String input) {
        Objects.requireNonNull(input, "input");
        return input.codePoints().toArray();
    }

    /**
     * Converts UTF-16 chars to code points.
     *
     * @param chars input chars
     * @return code point array
     */
    static int[] toCodePoints(char[] chars) {
        Objects.requireNonNull(chars, "chars");
        return new String(chars).codePoints().toArray();
    }

    /**
     * Converts code points to a string.
     *
     * @param codePoints code points
     * @return string value
     */
    static String toString(int[] codePoints) {
        Objects.requireNonNull(codePoints, "codePoints");
        return new String(codePoints, 0, codePoints.length);
    }

    /**
     * Returns a slice of the code point array.
     *
     * @param source source array
     * @param start start index (inclusive)
     * @param end end index (exclusive)
     * @return sliced array
     */
    static int[] slice(int[] source, int start, int end) {
        Objects.requireNonNull(source, "source");
        int safeStart = Math.max(0, Math.min(start, source.length));
        int safeEnd = Math.max(safeStart, Math.min(end, source.length));
        return Arrays.copyOfRange(source, safeStart, safeEnd);
    }

    /**
     * Concatenates two code point arrays.
     *
     * @param left left array
     * @param right right array
     * @return combined array
     */
    static int[] concat(int[] left, int[] right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        int[] result = Arrays.copyOf(left, left.length + right.length);
        System.arraycopy(right, 0, result, left.length, right.length);
        return result;
    }

    /**
     * Inserts code points into a base array.
     *
     * @param base base array
     * @param index insertion index
     * @param insert insertion array
     * @return new array with insert applied
     */
    static int[] insert(int[] base, int index, int[] insert) {
        Objects.requireNonNull(base, "base");
        Objects.requireNonNull(insert, "insert");
        int safeIndex = Math.max(0, Math.min(index, base.length));
        int[] result = new int[base.length + insert.length];
        System.arraycopy(base, 0, result, 0, safeIndex);
        System.arraycopy(insert, 0, result, safeIndex, insert.length);
        System.arraycopy(base, safeIndex, result, safeIndex + insert.length, base.length - safeIndex);
        return result;
    }

    /**
     * Removes a range from the code point array.
     *
     * @param base base array
     * @param start start index (inclusive)
     * @param end end index (exclusive)
     * @return new array with range removed
     */
    static int[] removeRange(int[] base, int start, int end) {
        Objects.requireNonNull(base, "base");
        int safeStart = Math.max(0, Math.min(start, base.length));
        int safeEnd = Math.max(safeStart, Math.min(end, base.length));
        int[] result = new int[base.length - (safeEnd - safeStart)];
        System.arraycopy(base, 0, result, 0, safeStart);
        System.arraycopy(base, safeEnd, result, safeStart, base.length - safeEnd);
        return result;
    }

    /**
     * Returns the cell width of the code point array.
     *
     * @param codePoints code points
     * @return cell width
     */
    static int cellWidth(int[] codePoints) {
        Objects.requireNonNull(codePoints, "codePoints");
        return TextWidth.measureCellWidth(toString(codePoints));
    }
}
