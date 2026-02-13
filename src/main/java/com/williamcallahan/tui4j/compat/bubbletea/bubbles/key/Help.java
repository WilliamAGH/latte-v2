package com.williamcallahan.tui4j.compat.bubbletea.bubbles.key;

/**
 * Port of Bubbles help.
 * Bubble Tea: bubbletea/examples/help/main.go
 *
 * @param key key binding label
 * @param desc key binding description
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.key.Help} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public record Help(String key, String desc) {

    /**
     * Creates an empty help entry.
     */
    @Deprecated(since = "0.3.0")
    public Help() {
        this("", "");
    }
}
