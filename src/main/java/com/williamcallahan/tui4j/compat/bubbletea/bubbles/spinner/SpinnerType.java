package com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner;

import java.time.Duration;

/**
 * Port of Bubbles spinner type.
 * Bubble Tea: bubbletea/examples/spinner/main.go
 *
 * @deprecated Deprecated in tui4j as of 0.3.0 because this type moved; use {@link com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 */
@Deprecated(since = "0.3.0")
public enum SpinnerType {

    /** Rotating line spinner (|, /, -, \). */
    @Deprecated(since = "0.3.0")
    LINE {
        @Override
        String[] frames() {
            return new String[]{"|", "/", "-", "\\"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(10);
        }
    },

    /** Braille dot spinner. */
    @Deprecated(since = "0.3.0")
    DOT {
        @Override
        String[] frames() {
            return new String[]{"⣾ ", "⣽ ", "⣻ ", "⢿ ", "⡿ ", "⣟ ", "⣯ ", "⣷ "};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(10);
        }
    },

    /** Minimal braille dot spinner. */
    @Deprecated(since = "0.3.0")
    MINI_DOT {
        @Override
        String[] frames() {
            return new String[]{"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(12);
        }
    },

    /** Jumping braille dot spinner. */
    @Deprecated(since = "0.3.0")
    JUMP {
        @Override
        String[] frames() {
            return new String[]{"⢄", "⢂", "⢁", "⡁", "⡈", "⡐", "⡠"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(10);
        }
    },

    /** Pulsing block shade spinner. */
    @Deprecated(since = "0.3.0")
    PULSE {
        @Override
        String[] frames() {
            return new String[]{"█", "▓", "▒", "░"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(8);
        }
    },

    /** Moving dot points spinner. */
    @Deprecated(since = "0.3.0")
    POINTS {
        @Override
        String[] frames() {
            return new String[]{"∙∙∙", "●∙∙", "∙●∙", "∙∙●"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(7);
        }
    },

    /** Globe emoji spinner. */
    @Deprecated(since = "0.3.0")
    GLOBE {
        @Override
        String[] frames() {
            return new String[]{"🌍", "🌎", "🌏"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(4);
        }
    },

    /** Moon phase emoji spinner. */
    @Deprecated(since = "0.3.0")
    MOON {
        @Override
        String[] frames() {
            return new String[]{"🌑", "🌒", "🌓", "🌔", "🌕", "🌖", "🌗", "🌘"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(8);
        }
    },

    /** Monkey emoji spinner (see-hear-speak no evil). */
    @Deprecated(since = "0.3.0")
    MONKEY {
        @Override
        String[] frames() {
            return new String[]{"🙈", "🙉", "🙊"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(3);
        }
    },

    /** Meter bar spinner. */
    @Deprecated(since = "0.3.0")
    METER {
        @Override
        String[] frames() {
            return new String[]{
                    "▱▱▱",
                    "▰▱▱",
                    "▰▰▱",
                    "▰▰▰",
                    "▰▰▱",
                    "▰▱▱",
                    "▱▱▱"
            };
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(7);
        }
    },

    /** Hamburger trigram spinner. */
    @Deprecated(since = "0.3.0")
    HAMBURGER {
        @Override
        String[] frames() {
            return new String[]{"☱", "☲", "☴", "☲"};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(3);
        }
    },

    /** Ellipsis dots spinner. */
    @Deprecated(since = "0.3.0")
    ELLIPSIS {
        @Override
        String[] frames() {
            return new String[]{"", ".", "..", "..."};
        }

        @Override
        Duration duration() {
            return Duration.ofSeconds(1).dividedBy(3);
        }
    };

    @Deprecated(since = "0.3.0")
    abstract String[] frames();
    @Deprecated(since = "0.3.0")
    abstract Duration duration();
}
