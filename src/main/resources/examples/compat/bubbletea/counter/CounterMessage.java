package examples.compat.bubbletea.counter;

import com.williamcallahan.tui4j.compat.bubbletea.Message;

/**
 * Defines the counter-specific messages used by the example.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/counter/CounterMessage.java
 */
public enum CounterMessage implements Message {
    INCREMENT,
    DECREMENT,
    INCREMENT_LATER,
}
