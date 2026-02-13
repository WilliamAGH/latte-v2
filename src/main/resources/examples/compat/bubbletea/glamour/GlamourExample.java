package examples.compat.bubbletea.glamour;

import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbles.viewport.Viewport;
import com.williamcallahan.tui4j.compat.lipgloss.Borders;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

/**
 * Markdown viewport example rendered as plain text.
 * <p>
 * Glamour has no Java port, so this example renders the raw markdown content
 * inside a styled viewport.
 * Upstream: bubbletea/examples/glamour/main.go.
 */
public class GlamourExample implements Model {

    private static final String CONTENT = """
            # Today’s Menu

            ## Appetizers

            | Name        | Price | Notes                           |
            | ---         | ---   | ---                             |
            | Tsukemono   | $2    | Just an appetizer               |
            | Tomato Soup | $4    | Made with San Marzano tomatoes  |
            | Okonomiyaki | $4    | Takes a few minutes to make     |
            | Curry       | $3    | We can add squash if you’d like |

            ## Seasonal Dishes

            | Name                 | Price | Notes              |
            | ---                  | ---   | ---                |
            | Steamed bitter melon | $2    | Not so bitter      |
            | Takoyaki             | $3    | Fun to eat         |
            | Winter squash        | $3    | Today it's pumpkin |

            ## Desserts

            | Name         | Price | Notes                 |
            | ---          | ---   | ---                   |
            | Dorayaki     | $4    | Looks good on rabbits |
            | Banana Split | $5    | A classic             |
            | Cream Puff   | $3    | Pretty creamy!        |

            All our dishes are made in-house by Karen, our chef. Most of our ingredients
            are from our garden or the fish market down the street.

            Some famous people that have eaten here lately:

            * [x] René Redzepi
            * [x] David Chang
            * [ ] Jiro Ono (maybe some day)

            Bon appétit!
            """;

    private final Viewport viewport;
    private final Style helpStyle;

    /**
     * Creates the glamour viewport example.
     */
    public GlamourExample() {
        int width = 78;
        this.viewport = Viewport.create(width, 20);
        Style border = Style.newStyle()
            .border(Borders.roundedBorder())
            .borderForeground(Color.color("62"))
            .paddingRight(2);
        viewport.setStyle(border);
        viewport.setContent(CONTENT);
        this.helpStyle = Style.newStyle().foreground(Color.color("241"));
    }

    /**
     * No startup command for the example.
     *
     * @return null
     */
    @Override
    public com.williamcallahan.tui4j.compat.bubbletea.Command init() {
        return null;
    }

    /**
     * Routes key events to the viewport or quits.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("q".equals(key) || "ctrl+c".equals(key) || "esc".equals(key)) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            UpdateResult<? extends Model> result = viewport.update(msg);
            return UpdateResult.from(this, result.command());
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the viewport and help text.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return viewport.view() + helpView();
    }

    /**
     * Builds the help footer.
     *
     * @return help view
     */
    private String helpView() {
        return helpStyle.render("\n  ↑/↓: Navigate • q: Quit\n");
    }

    /**
     * Runs the glamour example.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new GlamourExample()).run();
    }
}
