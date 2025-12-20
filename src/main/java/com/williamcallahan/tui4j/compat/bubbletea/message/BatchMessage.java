package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;

/**
 * Executes commands concurrently with no ordering guarantees.
 * Bubble Tea: bubbletea/commands.go
 *
 * @param commands commands to execute
 */
public record BatchMessage(Command... commands) implements Message {
}
