package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;

/**
 * Executes commands in order.
 * Bubble Tea: bubbletea/commands.go
 *
 * @param commands commands to execute in order
 */
public record SequenceMessage(Command... commands) implements Message {
}
