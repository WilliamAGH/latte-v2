package examples.compat.bubbletea.simple;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

import java.time.Instant;

/**
 * Support type for the Simple example.
 */
public class TickMessage implements Message {
    private final Instant time;

    /**
     * Creates TickMessage to keep example ready for use.
     */
    public TickMessage() {
        this.time = Instant.now();
    }

    /**
     * Handles time for example.
     *
     * @return result
     */
    public Instant time() {
        return time;
    }
}
