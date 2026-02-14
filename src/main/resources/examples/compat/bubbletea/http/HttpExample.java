package examples.compat.bubbletea.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.ErrorMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.io.IOException;

/**
 * Carries the HTTP response status for the example.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/http/HttpExample.java
 */
record StatusMessage(int statusCode, String message) implements Message {

}


/**
 * Demonstrates running a simple HTTP check and reporting the status.
 * Upstream: bubbletea/examples/http/main.go
 */
public class HttpExample implements Model {

    private final static String URL = "https://williamcallahan.com";

    private StatusMessage statusMessage;
    private Throwable error;

    /**
     * Starts the HTTP check on program init.
     *
     * @return command to check the server
     */
    @Override
    public Command init() {
        return checkServer();
    }

    /**
     * Issues the HTTP request and returns a status or error message.
     *
     * @return command that performs the request
     */
    private Command checkServer() {
        return () -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return new StatusMessage(response.code(), response.message());
            } catch (IOException e) {
                return new ErrorMessage(e);
            }
        };
    }

    /**
     * Handles keypresses, HTTP responses, and errors.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key().equals("q")) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this);
        } else if (msg instanceof StatusMessage statusMsg) {
            this.statusMessage = statusMsg;
            return UpdateResult.from(this, QuitMessage::new);
        } else if (msg instanceof ErrorMessage errorMessage) {
            this.error = errorMessage.error();
            return UpdateResult.from(this);
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the request state or the final status message.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder("Checking %s...".formatted(URL));
        if (error != null) {
            buffer.append("something went wrong: %s".formatted(error.getMessage()));
        } else if (statusMessage != null) {
            buffer.append("%d %s".formatted(statusMessage.statusCode(), statusMessage.message()));
        }
        return buffer.toString();
    }

    /**
     * Runs the HTTP example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new HttpExample()).run();
    }
}
