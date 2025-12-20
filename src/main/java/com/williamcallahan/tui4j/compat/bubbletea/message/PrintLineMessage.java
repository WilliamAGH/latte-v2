package com.williamcallahan.tui4j.compat.bubbletea.message;

import com.williamcallahan.tui4j.Message;

/**
 * Requests printing a line above the program output.
 * Bubble Tea: bubbletea/standard_renderer.go
 *
 * @param messageBody line content
 */
public record PrintLineMessage(String messageBody) implements Message {
}
