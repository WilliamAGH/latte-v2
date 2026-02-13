package examples.compat.bubbletea.tutorials.commands;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Tutorial example demonstrating commands and async work.
 * Upstream: bubbletea/tutorials/commands/main.go.
 */
public class CommandsTutorialExample implements Model {

    private static final String URL_TO_CHECK = "https://charm.sh/";

    private final int status;
    private final String statusText;
    private final Exception error;

    /**
     * Creates a new tutorial model with no status.
     */
    public CommandsTutorialExample() {
        this(0, "", null);
    }

    /**
     * Creates a tutorial model with the provided state.
     *
     * @param status status code
     * @param statusText status text
     * @param error error encountered
     */
    public CommandsTutorialExample(int status, String statusText, Exception error) {
        this.status = status;
        this.statusText = statusText;
        this.error = error;
    }

    /**
     * Starts the server check command.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return CommandsTutorialExample::checkServer;
    }

    /**
     * Applies the status or error response and quits.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof StatusMessage statusMessage) {
            return new UpdateResult<>(
                new CommandsTutorialExample(statusMessage.statusCode(), statusMessage.statusText(), null),
                QuitMessage::new
            );
        }
        if (msg instanceof ErrorMessage errorMessage) {
            return new UpdateResult<>(new CommandsTutorialExample(0, "", errorMessage.error()), QuitMessage::new);
        }
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if ("ctrl+c".equals(keyPressMessage.key())) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the status or error message.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (error != null) {
            return String.format("\nWe had some trouble: %s\n\n", error.getMessage());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\nChecking ").append(URL_TO_CHECK).append(" ... ");
        if (status > 0) {
            builder.append(status);
            if (statusText != null && !statusText.isBlank()) {
                builder.append(" ").append(statusText);
            }
        }
        builder.append("\n\n");
        return builder.toString();
    }

    /**
     * Performs the HTTP request and returns a status message.
     *
     * @return status or error message
     */
    private static Message checkServer() {
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(URL_TO_CHECK).toURL().openConnection();
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();
            return new StatusMessage(status, message);
        } catch (IOException e) {
            return new ErrorMessage(e);
        }
    }

    /**
     * Message carrying a status code.
     *
     * @param statusCode status code
     * @param statusText status text
     */
    private record StatusMessage(int statusCode, String statusText) implements Message {
    }

    /**
     * Message carrying an error.
     *
     * @param error error instance
     */
    private record ErrorMessage(Exception error) implements Message {
    }

    /**
     * Runs the tutorial example.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new CommandsTutorialExample()).run();
    }
}
