package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.list.Rank} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public class Rank extends com.williamcallahan.tui4j.compat.bubbles.list.Rank {

    /**
     * Creates a deprecated compatibility rank wrapper.
     *
     * @param index matched item index
     * @param matchedIndexes matched rune indexes
     * @deprecated Deprecated in tui4j as of 0.3.0; use
     * {@link com.williamcallahan.tui4j.compat.bubbles.list.Rank} instead.
     */
    @Deprecated(since = "0.3.0")
    public Rank(int index, int[] matchedIndexes) {
        super(index, matchedIndexes);
    }

}
