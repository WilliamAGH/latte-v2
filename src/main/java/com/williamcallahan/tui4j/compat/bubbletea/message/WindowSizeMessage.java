package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Reports the terminal size.
 * Bubble Tea: bubbletea/screen.go
 *
 * @param width terminal width in columns
 * @param height terminal height in rows
 */
public record WindowSizeMessage(int width, int height) implements Message {
}
