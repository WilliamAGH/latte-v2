package examples.compat.bubbletea.chat;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.textarea.Textarea;
import com.williamcallahan.tui4j.compat.bubbles.viewport.Viewport;
import com.williamcallahan.tui4j.compat.bubbles.cursor.Cursor;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates a chat-style UI using a textarea and viewport.
 * Upstream: bubbletea/examples/chat/main.go
 */
public class ChatExample implements Model {

    private static final String GAP = "\n\n";

    private Viewport viewport;
    private Textarea textarea;
    private List<String> messages;
    private Style senderStyle;

    /**
     * Configures the textarea, viewport, and initial message state.
     */
    public ChatExample() {
        this.textarea = new Textarea();
        textarea.setPlaceholder("Send a message...");
        textarea.focus();
        textarea.setPrompt("┃ ");
        textarea.setCharLimit(280);
        textarea.setWidth(30);
        textarea.setHeight(3);
        textarea.setShowLineNumbers(false);

        this.viewport = Viewport.create(30, 5);
        viewport.setContent("Welcome to the chat room!\nType a message and press Enter to send.");

        this.messages = new ArrayList<>();
        this.senderStyle = Style.newStyle().foreground(Color.color("5"));
    }

    /**
     * Starts the cursor blink animation for the input field.
     *
     * @return cursor blink command
     */
    @Override
    public Command init() {
        return Cursor::blink;
    }

    /**
     * Updates the input, viewport, and message list in response to events.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        UpdateResult<? extends Model> taResult = textarea.update(msg);
        UpdateResult<? extends Model> vpResult = viewport.update(msg);

        Command combinedCommand = null;
        if (taResult.command() != null && vpResult.command() != null) {
            combinedCommand = Command.batch(taResult.command(), vpResult.command());
        } else if (taResult.command() != null) {
            combinedCommand = taResult.command();
        } else if (vpResult.command() != null) {
            combinedCommand = vpResult.command();
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key().equals("ctrl+c") || keyPressMessage.key().equals("esc")) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            if (keyPressMessage.key().equals("enter")) {
                String message = senderStyle.render("You: ") + textarea.value();
                messages.add(message);
                textarea.reset();

                String content = String.join("\n", messages);
                int vpWidth = viewport.getWidth();
                if (vpWidth > 0) {
                    content = Style.newStyle().width(vpWidth).render(content);
                }
                viewport.setContent(content);
                viewport.gotoBottom();
            }
        }

        return UpdateResult.from(this, combinedCommand);
    }

    /**
     * Renders the viewport above the input area.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return viewport.view() + GAP + textarea.view();
    }

    /**
     * Runs the chat example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ChatExample()).run();
    }
}
