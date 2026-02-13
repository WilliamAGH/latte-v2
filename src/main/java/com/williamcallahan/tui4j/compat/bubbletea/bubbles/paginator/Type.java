package com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator;

/**
 * Port of Bubbles type.
 * Bubble Tea: bubbletea/examples/paginator/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.paginator.Type} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum Type {
    /** Arabic numeral pagination (1, 2, 3...). */
    @Deprecated(since = "0.3.0")
    Arabic,
    /** Dot-based pagination. */
    @Deprecated(since = "0.3.0")
    Dots
}