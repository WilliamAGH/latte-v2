package examples.compat.bubbletea.exec;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.ExecCompletedMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.io.IOException;

/**
 * Demonstrates launching an external editor from a TUI.
 * Upstream: bubbletea/examples/exec/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/exec/ExecExample.java
 */
public class ExecExample implements Model {

    private String status;

    /**
     * Initializes the status prompt for the editor demo.
     */
    public ExecExample() {
        this.status = "Press 'e' to open your EDITOR";
    }

    /**
     * Sets the terminal title for the example.
     *
     * @return window title command
     */
    @Override
    public Command init() {
        return Command.setWindowTitle("Exec Example - tui4j");
    }

    /**
     * Handles key input and completion of the exec command.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(com.williamcallahan.tui4j.compat.bubbletea.Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "e", "E" -> handleEditorOpen();
                case "q", "Q" -> UpdateResult.from(this, QuitMessage::new);
                default -> UpdateResult.from(this);
            };
        } else if (msg instanceof ExecCompletedMessage execCompleted) {
            return handleExecCompleted(execCompleted);
        }
        return UpdateResult.from(this);
    }

    /**
     * Launches the configured editor and tracks process completion.
     *
     * @return updated model and command
     */
    private UpdateResult<? extends Model> handleEditorOpen() {
        String editor = System.getenv("EDITOR");
        if (editor == null || editor.isBlank()) {
            editor = detectEditor();
        }

        if (editor.isBlank()) {
            this.status = "No editor found. Set $EDITOR environment variable.";
            return UpdateResult.from(this);
        }

        this.status = "Opening " + editor + "...";

        ProcessBuilder processBuilder = new ProcessBuilder(editor.split("\\s+"));
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            Process process = processBuilder.start();
            return UpdateResult.from(this, Command.execProcess(process, (exitCode, output) -> {
            }, (exitCode, error) -> {
            }));
        } catch (IOException e) {
            this.status = "Failed to open editor: " + e.getMessage();
            return UpdateResult.from(this);
        }
    }

    /**
     * Updates status after the editor process finishes.
     *
     * @param execCompleted completion message
     * @return updated model
     */
    private UpdateResult<? extends Model> handleExecCompleted(ExecCompletedMessage execCompleted) {
        if (execCompleted.error() != null) {
            this.status = "Editor error: " + execCompleted.error().getMessage();
        } else if (execCompleted.exitCode() != 0) {
            this.status = "Editor exited with code: " + execCompleted.exitCode();
        } else {
            this.status = "Editor closed successfully!";
        }

        return UpdateResult.from(this);
    }

    /**
     * Probes common editors to find a usable fallback.
     *
     * @return detected editor or empty string if none found
     */
    private String detectEditor() {
        String[] editors = {"vim", "nvim", "nano", "vi", "emacs", "code", "subl"};
        for (String editor : editors) {
            try {
                Process check = new ProcessBuilder("which", editor).start();
                if (check.waitFor() == 0) {
                    return editor;
                }
            } catch (IOException e) {
                System.err.println("Failed to check editor: " + editor + " (" + e.getMessage() + ")");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "";
            }
        }
        return "";
    }

    /**
     * Renders the framed UI with status and controls.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────────────────────────────┐\n");
        sb.append("│                        Exec Example                             │\n");
        sb.append("├─────────────────────────────────────────────────────────────────┤\n");
        sb.append("│                                                                 │\n");
        sb.append("│  This example demonstrates how to execute external commands    │\n");
        sb.append("│  from within a TUI application, such as launching an editor.   │\n");
        sb.append("│                                                                 │\n");
        sb.append("├─────────────────────────────────────────────────────────────────┤\n");
        sb.append("│                                                                 │\n");
        sb.append("│  Status: ").append(status).append("\n");
        sb.append("│                                                                 │\n");
        sb.append("├─────────────────────────────────────────────────────────────────┤\n");
        sb.append("│                                                                 │\n");
        sb.append("│  Controls:                                                       │\n");
        sb.append("│    e  - Open your $EDITOR                                       │\n");
        sb.append("│    q  - Quit                                                    │\n");
        sb.append("│                                                                 │\n");
        sb.append("└─────────────────────────────────────────────────────────────────┘\n");
        return sb.toString();
    }

    /**
     * Runs the exec example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ExecExample()).run();
    }
}
