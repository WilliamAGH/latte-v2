package com.williamcallahan.tui4j.compat.bubbletea.bubbles.spinner;

import java.time.Duration;

/**
 * Port of Bubbles spinner type.
 * Bubble Tea: bubbletea/examples/spinner/main.go
 */
public enum SpinnerType {

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

    abstract String[] frames();
    abstract Duration duration();
}
