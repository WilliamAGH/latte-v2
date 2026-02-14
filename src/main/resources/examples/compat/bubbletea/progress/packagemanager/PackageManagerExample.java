package examples.compat.bubbletea.progress.packagemanager;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.FrameMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.Progress;
import com.williamcallahan.tui4j.compat.bubbles.spinner.Spinner;
import com.williamcallahan.tui4j.compat.bubbles.spinner.SpinnerType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Example program for Progress.
 */
public class PackageManagerExample implements Model {

    private static final Duration TICK_INTERVAL = Duration.ofMillis(100);

    private final Progress overallProgress;
    private final List<PackageTask> tasks;
    private final Spinner spinner;
    private int selectedTaskIndex;
    private boolean complete;
    private String statusMessage;

    /**
     * Creates PackageManagerExample to keep example ready for use.
     */
    public PackageManagerExample() {
        this.overallProgress = new Progress()
                .withWidth(50)
                .withShowPercentage(true)
                .withDefaultGradient();
        this.tasks = new ArrayList<>();
        this.selectedTaskIndex = 0;
        this.complete = false;
        this.statusMessage = "Initializing...";
        this.spinner = new Spinner(SpinnerType.DOT);

        tasks.add(new PackageTask("Resolving dependencies...", 100, 0.3));
        tasks.add(new PackageTask("Downloading packages...", 200, 0.4));
        tasks.add(new PackageTask("Unpacking packages...", 150, 0.2));
        tasks.add(new PackageTask("Configuring packages...", 80, 0.1));
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return Command.tick(TICK_INTERVAL, time -> new TickMessage());
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
            if ("j".equals(key) || "down".equals(key)) {
                selectedTaskIndex = Math.min(selectedTaskIndex + 1, tasks.size() - 1);
            } else if ("k".equals(key) || "up".equals(key)) {
                selectedTaskIndex = Math.max(selectedTaskIndex - 1, 0);
            } else if (" ".equals(key)) {
                tasks.get(selectedTaskIndex).toggleExpanded();
            }
        }

        if (msg instanceof TickMessage) {
            boolean anyActive = false;
            double totalWeight = 0;
            double completedWeight = 0;

            for (PackageTask task : tasks) {
                task.update();
                totalWeight += task.weight;
                if (task.isComplete()) {
                    completedWeight += task.weight;
                } else if (task.isActive()) {
                    anyActive = true;
                }
            }

            if (totalWeight > 0) {
                overallProgress.setPercent(completedWeight / totalWeight);
            }

            if (anyActive) {
                PackageTask activeTask = tasks.stream()
                        .filter(PackageTask::isActive)
                        .findFirst()
                        .orElse(null);
                if (activeTask != null) {
                    statusMessage = activeTask.getName();
                }
            } else if (tasks.stream().allMatch(PackageTask::isComplete)) {
                statusMessage = "Complete!";
                complete = true;
            }

            if (!complete) {
                Command cmd = Command.tick(TICK_INTERVAL, time -> new TickMessage());
                return UpdateResult.from(this, cmd);
            }
        }

        if (msg instanceof FrameMessage) {
            UpdateResult<? extends Model> updateResult = overallProgress.update(msg);
            UpdateResult<? extends Model> spinnerResult = spinner.update(msg);
            Command combinedCmd = Command.batch(updateResult.command(), spinnerResult.command());
            return UpdateResult.from(this, combinedCmd);
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

        sb.append("\n  Package Manager Installation\n");
        sb.append("  ============================\n\n");

        sb.append("  [");
        for (int i = 0; i < tasks.size(); i++) {
            PackageTask task = tasks.get(i);
            char activeOrPending = task.isActive() ? '●' : '○';
            char statusChar = task.isComplete() ? '✓' : activeOrPending;
            sb.append(statusChar);
            if (i < tasks.size() - 1) sb.append(" ");
        }
        sb.append("]\n\n");

        if (!complete) {
            sb.append("  ").append(spinner.view()).append(" ").append(statusMessage).append("\n\n");
        } else {
            sb.append("  ✓ All operations completed successfully!\n\n");
        }

        sb.append("  Overall Progress:\n");
        sb.append("  ").append(overallProgress.view()).append("\n\n");

        sb.append("  Operations:\n");
        for (int i = 0; i < tasks.size(); i++) {
            PackageTask task = tasks.get(i);
            String prefix = i == selectedTaskIndex ? ">" : " ";
            String activeOrBlank = task.isActive() ? "…" : " ";
            String indicator = task.isComplete() ? "✓" : activeOrBlank;
            String expand = task.isExpanded() ? "▼" : "▶";

            sb.append(prefix).append(" ").append(indicator).append(" ").append(expand).append(" ")
                    .append(task.getName()).append("\n");

            if (task.isExpanded()) {
                Progress taskProgress = new Progress()
                        .withWidth(40)
                        .withShowPercentage(true)
                        .withFullColor(task.isComplete() ? "#28A745" : "#5A56E0");
                sb.append("      ").append(taskProgress.viewAs(task.getPercent())).append("\n");
            }
        }

        sb.append("\n  Controls: j/k or up/down to select, space to expand/collapse, q to quit\n");

        return sb.toString();
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new PackageManagerExample()).run();
    }

    /**
     * Support type for the Progress example.
     */
    private static class TickMessage implements Message {
    }

    /**
     * Support type for the Progress example.
     */
    private static class PackageTask {
        private final String name;
        private final double weight;
        private final double baseSpeed;
        private double progress;
        private boolean complete;
        private boolean expanded;
        private double speedMultiplier;

        /**
         * Creates PackageTask to keep example ready for use.
         *
         * @param name name
         * @param weight weight
         * @param baseSpeed base speed
         */
        public PackageTask(String name, double weight, double baseSpeed) {
            this.name = name;
            this.weight = weight;
            this.baseSpeed = baseSpeed;
            this.progress = 0;
            this.complete = false;
            this.expanded = false;
            this.speedMultiplier = 0.8 + Math.random() * 0.4;
        }

        /**
         * Applies an incoming message and returns the next model state.
         */
        public void update() {
            if (complete) return;

            double increment = baseSpeed * speedMultiplier * 0.01;
            progress = Math.min(progress + increment, 1.0);

            if (progress >= 1.0) {
                complete = true;
                progress = 1.0;
            }
        }

        /**
         * Handles toggle expanded for example.
         */
        public void toggleExpanded() {
            expanded = !expanded;
        }

        /**
         * Reports whether active.
         *
         * @return whether active
         */
        public boolean isActive() {
            return !complete && progress > 0;
        }

        /**
         * Reports whether complete.
         *
         * @return whether complete
         */
        public boolean isComplete() {
            return complete;
        }

        /**
         * Reports whether expanded.
         *
         * @return whether expanded
         */
        public boolean isExpanded() {
            return expanded;
        }

        /**
         * Returns the name.
         *
         * @return result
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the percent.
         *
         * @return result
         */
        public double getPercent() {
            return progress;
        }
    }
}
