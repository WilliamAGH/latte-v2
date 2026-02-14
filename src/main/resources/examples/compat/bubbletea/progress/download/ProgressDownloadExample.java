package examples.compat.bubbletea.progress.download;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.FrameMessage;
import com.williamcallahan.tui4j.compat.bubbles.progress.Progress;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates a multi-download progress manager with pause and reset.
 * Upstream: bubbletea/examples/progress-download/main.go
 */
public class ProgressDownloadExample implements Model {

    private static final Duration TICK_INTERVAL = Duration.ofMillis(50);

    private final Progress progressBar;
    private final List<DownloadFile> downloads;
    private int selectedIndex;
    private boolean allComplete;

    /**
     * Seeds sample downloads and configures the shared progress bar.
     */
    public ProgressDownloadExample() {
        this.progressBar = new Progress()
                .withWidth(50)
                .withShowPercentage(true)
                .withPercentFormat(" %5.1f%%")
                .withFullColor("#28A745");
        this.downloads = new ArrayList<>();
        this.selectedIndex = 0;
        this.allComplete = false;

        downloads.add(new DownloadFile("ubuntu-22.04.iso", 3800, 450));
        downloads.add(new DownloadFile("node-v18.16.0.pkg", 180, 80));
        downloads.add(new DownloadFile("dataset-v2.csv", 950, 120));
    }

    /**
     * Starts the periodic download tick.
     *
     * @return tick command
     */
    @Override
    public Command init() {
        return Command.tick(TICK_INTERVAL, time -> new DownloadTickMessage());
    }

    /**
     * Handles input, updates downloads, and forwards progress frames.
     *
     * @param msg incoming message
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
                selectedIndex = Math.min(selectedIndex + 1, downloads.size() - 1);
            } else if ("k".equals(key) || "up".equals(key)) {
                selectedIndex = Math.max(selectedIndex - 1, 0);
            } else if (" ".equals(key)) {
                downloads.get(selectedIndex).togglePause();
            } else if ("r".equals(key)) {
                boolean wasComplete = allComplete;
                for (DownloadFile download : downloads) {
                    download.reset();
                }
                selectedIndex = 0;
                allComplete = false;
                if (wasComplete) {
                    return UpdateResult.from(
                            this,
                            Command.tick(TICK_INTERVAL, time -> new DownloadTickMessage())
                    );
                }
            }
        }

        if (msg instanceof DownloadTickMessage) {
            for (DownloadFile download : downloads) {
                download.update();
            }

            allComplete = downloads.stream().allMatch(DownloadFile::isComplete);

            if (!allComplete) {
                Command cmd = Command.tick(TICK_INTERVAL, time -> new DownloadTickMessage());
                return UpdateResult.from(this, cmd);
            }
        }

        if (msg instanceof FrameMessage) {
            UpdateResult<? extends Model> updateResult = progressBar.update(msg);
            return UpdateResult.from(this, updateResult.command());
        }

        return UpdateResult.from(this);
    }

    /**
     * Renders the download list with progress details.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n  File Download Manager\n");
        sb.append("  =====================\n\n");

        for (int i = 0; i < downloads.size(); i++) {
            DownloadFile download = downloads.get(i);
            String prefix = i == selectedIndex ? ">" : " ";
            String icon;
            String status;
            String eta;
            if (download.isComplete()) {
                icon = "✓";
                status = "Done";
                eta = "";
            } else if (download.isPaused()) {
                icon = "⏸";
                status = "Paused";
                eta = "Paused";
            } else {
                icon = "↓";
                status = download.getSpeedFormatted();
                eta = "ETA: " + download.getEtaFormatted();
            }

            if (i == selectedIndex) {
                sb.append(prefix).append(" ").append(icon).append(" ").append(download.getName()).append("\n");
                progressBar.setPercent(download.getPercent());
                sb.append("      ").append(progressBar.view()).append("\n");
                sb.append("      ").append(download.getSizeFormatted()).append(" / ").append(download.getTotalSizeFormatted())
                        .append("  ").append(status).append("\n");
                if (!download.isComplete() && !download.isPaused()) {
                    sb.append("      ").append(eta).append("\n");
                }
            } else {
                double displayPercent = download.getPercent();
                Progress tempProgress = new Progress()
                        .withWidth(30)
                        .withShowPercentage(false)
                        .withFullColor("#28A745");
                sb.append(prefix).append(" ").append(icon).append(" ").append(download.getName());
                sb.append("  ").append(tempProgress.viewAs(displayPercent));
                sb.append(String.format(" %3.0f%%", displayPercent * 100));
                sb.append("\n");
            }
        }

        sb.append("\n  Controls: j/k or up/down to select, space to pause/resume, r to reset, q to quit\n");

        if (allComplete) {
            sb.append("\n  All downloads complete!\n");
        }

        return sb.toString();
    }

    /**
     * Runs the progress download example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ProgressDownloadExample()).run();
    }

    /**
     * Tick message to advance simulated downloads.
     */
    private static class DownloadTickMessage implements Message {
    }

    /**
     * Tracks simulated download state and formatting helpers.
     */
    private static class DownloadFile {
        private final String name;
        private final long totalBytes;
        private long downloadedBytes;
        private double lastSpeed;
        private boolean paused;
        private long bytesAtSpeedUpdate;
        private LocalDateTime speedUpdateTime;

        /**
         * Creates a simulated download with a fixed base speed.
         *
         * @param name file name
         * @param totalBytes size in megabytes
         * @param downloadSpeed speed in kilobytes per second
         */
        public DownloadFile(String name, long totalBytes, long downloadSpeed) {
            this.name = name;
            this.totalBytes = totalBytes * 1024 * 1024;
            this.downloadedBytes = 0;
            this.lastSpeed = (double) downloadSpeed * 1024;
            this.paused = false;
            this.bytesAtSpeedUpdate = 0;
            this.speedUpdateTime = LocalDateTime.now();
        }

        /**
         * Advances the simulated download if not paused or complete.
         */
        public void update() {
            if (paused || isComplete()) return;

            long bytesToDownload = (long) (lastSpeed * 0.05);
            downloadedBytes = Math.min(downloadedBytes + bytesToDownload, totalBytes);

            updateSpeedEstimate();
        }

        /**
         * Updates the moving average speed estimate.
         */
        private void updateSpeedEstimate() {
            long now = System.currentTimeMillis();
            long elapsed = now - (speedUpdateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
            if (elapsed > 500) {
                long bytesDownloaded = downloadedBytes - bytesAtSpeedUpdate;
                if (bytesDownloaded > 0) {
                    double newSpeed = bytesDownloaded / (elapsed / 1000.0);
                    lastSpeed = lastSpeed * 0.7 + newSpeed * 0.3;
                }
                bytesAtSpeedUpdate = downloadedBytes;
                speedUpdateTime = LocalDateTime.now();
            }
        }

        /**
         * Toggles the paused state when the download is incomplete.
         */
        public void togglePause() {
            if (!isComplete()) {
                paused = !paused;
            }
        }

        /**
         * Resets the download progress and pause state.
         */
        public void reset() {
            downloadedBytes = 0;
            paused = false;
        }

        /**
         * Returns the completion percentage as a fraction.
         *
         * @return completion ratio
         */
        public double getPercent() {
            return (double) downloadedBytes / totalBytes;
        }

        /**
         * Reports whether the download has completed.
         *
         * @return true when complete
         */
        public boolean isComplete() {
            return downloadedBytes >= totalBytes;
        }

        /**
         * Reports whether the download is paused.
         *
         * @return true when paused
         */
        public boolean isPaused() {
            return paused;
        }

        /**
         * Returns the file name for display.
         *
         * @return file name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the downloaded size formatted for display.
         *
         * @return formatted size
         */
        public String getSizeFormatted() {
            return formatBytes(downloadedBytes);
        }

        /**
         * Returns the total size formatted for display.
         *
         * @return formatted size
         */
        public String getTotalSizeFormatted() {
            return formatBytes(totalBytes);
        }

        /**
         * Returns the current speed formatted for display.
         *
         * @return formatted speed
         */
        public String getSpeedFormatted() {
            return formatBytes((long) lastSpeed) + "/s";
        }

        /**
         * Returns a human-readable ETA estimate.
         *
         * @return formatted ETA string
         */
        public String getEtaFormatted() {
            if (isComplete()) return "";
            long remainingBytes = totalBytes - downloadedBytes;
            if (remainingBytes <= 0) return "Almost done";
            double seconds = remainingBytes / lastSpeed;
            if (seconds < 1) return "< 1s";
            if (seconds < 60) return String.format("%ds", (int) seconds);
            int minutes = (int) (seconds / 60);
            if (minutes < 60) return String.format("%dm %ds", minutes, (int) (seconds % 60));
            int hours = minutes / 60;
            return String.format("%dh %dm", hours, minutes % 60);
        }

        /**
         * Formats bytes into a compact human-readable size.
         *
         * @param bytes byte count
         * @return formatted size string
         */
        private String formatBytes(long bytes) {
            if (bytes >= 1024 * 1024 * 1024) {
                return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
            } else if (bytes >= 1024 * 1024) {
                return String.format("%.1f MB", bytes / (1024.0 * 1024));
            } else if (bytes >= 1024) {
                return String.format("%.1f KB", bytes / 1024.0);
            }
            return bytes + " B";
        }
    }
}
