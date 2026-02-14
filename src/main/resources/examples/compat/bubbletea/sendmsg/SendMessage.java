package examples.compat.bubbletea.sendmsg;

import com.williamcallahan.tui4j.compat.bubbletea.Message;
import java.time.Duration;

/**
 * Support type for the Sendmsg example.
 */
public record SendMessage(Duration duration, String food) implements Message {
    /**
     * Handles to string for example.
     *
     * @return result
     */
    @Override
    public String toString() {
        if (duration == null || duration.isZero()) {
            return ".";
        }
        return String.format("Ate %s (%s)", food, formatDuration(duration));
    }

    /**
     * Handles format duration for example.
     *
     * @param d d
     * @return result
     */
    private String formatDuration(Duration d) {
        long seconds = d.getSeconds();
        long millis = d.toMillis() % 1000;
        if (seconds > 0) {
            return String.format("%d.%03ds", seconds, millis);
        }
        return String.format("%dms", millis);
    }
}
