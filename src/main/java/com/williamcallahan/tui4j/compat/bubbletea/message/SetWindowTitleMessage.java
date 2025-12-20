package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests setting the terminal window title.
 * Bubble Tea: bubbletea/commands.go
 *
 * @param title window title to set
 */
public record SetWindowTitleMessage(String title) implements Message {
}
