package examples.compat.bubbletea.progress.staticbar;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.Progress;

import java.util.ArrayList;
import java.util.List;

/**
 * Example program for Progress.
 */
public class ProgressStaticExample implements Model {

    private final List<Progress> progressBars;
    private final List<String> labels;
    private int selectedIndex;

    /**
     * Creates ProgressStaticExample to keep example ready for use.
     */
    public ProgressStaticExample() {
        this.progressBars = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.selectedIndex = 0;

        progressBars.add(new Progress()
                .withWidth(40)
                .withShowPercentage(true));

        labels.add("Default");

        progressBars.add(new Progress()
                .withWidth(40)
                .withFull('█')
                .withEmpty('░')
                .withShowPercentage(true));

        labels.add("Block characters");

        progressBars.add(new Progress()
                .withWidth(40)
                .withFull('#')
                .withEmpty('.')
                .withShowPercentage(true));

        labels.add("ASCII");

        progressBars.add(new Progress()
                .withWidth(20)
                .withFull('=')
                .withEmpty('-')
                .withShowPercentage(true)
                .withPercentFormat(" %2.0f%%"));

        labels.add("Narrow (20 chars)");

        progressBars.add(new Progress()
                .withWidth(60)
                .withFull('▓')
                .withEmpty('░')
                .withShowPercentage(true)
                .withPercentFormat(" %5.1f%%"));

        labels.add("Wide (60 chars)");
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("q".equals(key) || "Q".equals(key) || "ctrl+c".equals(key)) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
            if ("j".equals(key) || "down".equals(key) || " ".equals(key)) {
                selectedIndex = Math.min(selectedIndex + 1, progressBars.size() - 1);
            } else if ("k".equals(key) || "up".equals(key)) {
                selectedIndex = Math.max(selectedIndex - 1, 0);
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  Static Progress Bar Examples\n");
        sb.append("  =============================\n\n");

        for (int i = 0; i < progressBars.size(); i++) {
            Progress progress = progressBars.get(i);
            String label = labels.get(i);
            String prefix = i == selectedIndex ? ">" : " ";
            String marker = i == selectedIndex ? " ◉ " : "   ";
            sb.append(prefix).append(marker).append(label).append("\n");
            sb.append("      ").append(progress.view()).append("\n\n");
        }

        sb.append("\n  Controls: j/k or up/down to navigate, q to quit\n");

        return sb.toString();
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new ProgressStaticExample()).run();
    }
}
