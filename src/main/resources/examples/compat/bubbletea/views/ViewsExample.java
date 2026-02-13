package examples.compat.bubbletea.views;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;

import java.util.List;

/**
 * Example program for Views.
 */
public class ViewsExample implements Model {

    private static final int PROGRESS_BAR_WIDTH = 71;
    private static final String PROGRESS_FULL_CHAR = "█";
    private static final String PROGRESS_EMPTY_CHAR = "░";
    private static final String DOT_CHAR = " • ";

    private static final Style KEYWORD_STYLE = Style.newStyle()
            .foreground(Color.color("#D73A49"));
    private static final Style SUBTLE_STYLE = Style.newStyle()
            .foreground(Color.color("#6A737D"));
    private static final Style TICKS_STYLE = Style.newStyle()
            .foreground(Color.color("#4CAF50"));
    private static final Style CHECKBOX_STYLE = Style.newStyle()
            .foreground(Color.color("#E91E63"));
    private static final String PROGRESS_EMPTY = SUBTLE_STYLE.render(PROGRESS_EMPTY_CHAR);
    private static final Style DOT_STYLE = Style.newStyle()
            .foreground(Color.color("#24292E"));
    private static final Style MAIN_STYLE = Style.newStyle()
            .marginLeft(2);

    private static final Style[] RAMP;

    static {
        RAMP = makeRampStyles("#B14FFF", "#00FFA3", PROGRESS_BAR_WIDTH);
    }

    /**
     * Support type for the Views example.
     */
    private enum ViewState {
        CHOOSING,
        CHOSEN
    }

    /**
     * Support type for the Views example.
     */
    private enum Choice {
        PLANT_CARROTS,
        GO_TO_MARKET,
        READ_SOMETHING,
        SEE_FRIENDS
    }

    private final ViewState viewState;
    private final int choice;
    private final int ticks;
    private final int frames;
    private final double progress;
    private final boolean loaded;
    private final boolean quitting;

    private static final List<String> GRADIENT_COLORS = List.of(
            "#B14FFF", "#A94FEF", "#9E4FDF", "#964FCF", "#8E4FBF", "#864FAF", "#7E4F9F", "#764F8F",
            "#6E4F7F", "#664F6F", "#5E4F5F", "#564F4F", "#4E4F3F", "#464F2F", "#3E4F1F", "#364F0F",
            "#2E5F0F", "#266F1F", "#1E7F2F", "#168F3F", "#0E9F4F", "#06AF5F", "#00BF6F", "#00CF7F",
            "#00DF8F", "#00EF9F", "#00FFA3"
    );

    /**
     * Creates ViewsExample to keep example ready for use.
     */
    public ViewsExample() {
        this(ViewState.CHOOSING, 0, 10, 0, 0.0, false, false);
    }

    /**
     * Creates ViewsExample to keep example ready for use.
     *
     * @param viewState view state
     * @param choice choice
     * @param ticks ticks
     * @param frames frames
     * @param progress progress
     * @param loaded loaded
     * @param quitting quitting
     */
    private ViewsExample(ViewState viewState, int choice, int ticks, int frames, double progress, boolean loaded, boolean quitting) {
        this.viewState = viewState;
        this.choice = choice;
        this.ticks = ticks;
        this.frames = frames;
        this.progress = progress;
        this.loaded = loaded;
        this.quitting = quitting;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return ViewsExample::tick;
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
            if (key.equals("q") || key.equals("esc") || key.equals("ctrl+c")) {
                return new UpdateResult<>(new ViewsExample(viewState, choice, ticks, frames, progress, loaded, true), QuitMessage::new);
            }
        }

        if (viewState == ViewState.CHOOSING) {
            return updateChoices(msg);
        } else {
            return updateChosen(msg);
        }
    }

    /**
     * Handles update choices for example.
     *
     * @param msg msg
     * @return result
     */
    private UpdateResult<? extends Model> updateChoices(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "j", "down":
                    int newChoice = Math.min(choice + 1, 3);
                    return new UpdateResult<>(new ViewsExample(viewState, newChoice, ticks, frames, progress, loaded, quitting), null);
                case "k", "up":
                    int prevChoice = Math.max(choice - 1, 0);
                    return new UpdateResult<>(new ViewsExample(viewState, prevChoice, ticks, frames, progress, loaded, quitting), null);
                case "enter":
                    return new UpdateResult<>(new ViewsExample(ViewState.CHOSEN, choice, ticks, frames, progress, loaded, quitting), ViewsExample::frame);
            }
        }

        if (msg instanceof TickMessage) {
            if (ticks <= 1) {
                return new UpdateResult<>(new ViewsExample(viewState, choice, ticks, frames, progress, loaded, true), QuitMessage::new);
            }
            return new UpdateResult<>(new ViewsExample(viewState, choice, ticks - 1, frames, progress, loaded, quitting), ViewsExample::tick);
        }

        return new UpdateResult<>(this, null);
    }

    /**
     * Handles update chosen for example.
     *
     * @param msg msg
     * @return result
     */
    private UpdateResult<? extends Model> updateChosen(Message msg) {
        if (msg instanceof FrameMessage) {
            if (!loaded) {
                int newFrames = frames + 1;
                double newProgress = easeOutBounce((double) newFrames / 100.0);
                if (newProgress >= 1.0) {
                    newProgress = 1.0;
                    return new UpdateResult<>(
                            new ViewsExample(viewState, choice, ticks, newFrames, newProgress, true, quitting),
                            ViewsExample::tick
                    );
                }
                return new UpdateResult<>(
                        new ViewsExample(viewState, choice, ticks, newFrames, newProgress, loaded, quitting),
                        ViewsExample::frame
                );
            }
        }

        if (msg instanceof TickMessage) {
            if (loaded) {
                if (ticks <= 1) {
                    return new UpdateResult<>(new ViewsExample(viewState, choice, ticks, frames, progress, loaded, true), QuitMessage::new);
                }
                return new UpdateResult<>(
                        new ViewsExample(viewState, choice, ticks - 1, frames, progress, loaded, quitting),
                        ViewsExample::tick
                );
            }
        }

        return new UpdateResult<>(this, null);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (quitting) {
            return "\n  See you later!\n\n";
        }

        String content;
        if (viewState == ViewState.CHOOSING) {
            content = choicesView();
        } else {
            content = chosenView();
        }

        return MAIN_STYLE.render("\n" + content + "\n\n");
    }

    /**
     * Handles choices view for example.
     *
     * @return result
     */
    private String choicesView() {
        StringBuilder sb = new StringBuilder();
        sb.append("What to do today?\n\n");

        for (int i = 0; i < Choice.values().length; i++) {
            sb.append(checkbox(choiceLabel(Choice.values()[i]), i == choice)).append("\n");
        }

        sb.append("\nProgram quits in ").append(TICKS_STYLE.render(String.valueOf(ticks))).append(" seconds\n\n");
        sb.append(SUBTLE_STYLE.render("j/k, up/down: select")).append(DOT_STYLE.render(DOT_CHAR))
                .append(SUBTLE_STYLE.render("enter: choose")).append(DOT_STYLE.render(DOT_CHAR))
                .append(SUBTLE_STYLE.render("q, esc: quit"));

        return sb.toString();
    }

    /**
     * Handles chosen view for example.
     *
     * @return result
     */
    private String chosenView() {
        StringBuilder sb = new StringBuilder();

        switch (Choice.values()[choice]) {
            case PLANT_CARROTS:
                sb.append("Carrot planting?\n\nCool, we'll need ")
                        .append(KEYWORD_STYLE.render("libgarden"))
                        .append(" and ")
                        .append(KEYWORD_STYLE.render("vegeutils"))
                        .append("...");
                break;
            case GO_TO_MARKET:
                sb.append("A trip to the market?\n\nOkay, then we should install ")
                        .append(KEYWORD_STYLE.render("marketkit"))
                        .append(" and ")
                        .append(KEYWORD_STYLE.render("libshopping"))
                        .append("...");
                break;
            case READ_SOMETHING:
                sb.append("Reading time?\n\nOkay, cool, then we'll need a library. Yes, an ")
                        .append(KEYWORD_STYLE.render("actual library"))
                        .append(".");
                break;
            case SEE_FRIENDS:
                sb.append("It's always good to see friends.\n\nFetching ")
                        .append(KEYWORD_STYLE.render("social-skills"))
                        .append(" and ")
                        .append(KEYWORD_STYLE.render("conversationutils"))
                        .append("...");
                break;
        }

        sb.append("\n\n");

        String label;
        if (loaded) {
            label = "Downloaded. Exiting in " + TICKS_STYLE.render(String.valueOf(ticks)) + " seconds...";
        } else {
            label = "Downloading...";
        }
        sb.append(label).append("\n");
        sb.append(progressBar(progress)).append(String.format("%3.0f%%", progress * 100));

        return sb.toString();
    }

    /**
     * Handles choice label for example.
     *
     * @param choice choice
     * @return result
     */
    private String choiceLabel(Choice choice) {
        return switch (choice) {
            case PLANT_CARROTS -> "Plant carrots";
            case GO_TO_MARKET -> "Go to the market";
            case READ_SOMETHING -> "Read something";
            case SEE_FRIENDS -> "See friends";
        };
    }

    /**
     * Handles checkbox for example.
     *
     * @param label label
     * @param checked checked
     * @return result
     */
    private String checkbox(String label, boolean checked) {
        if (checked) {
            return CHECKBOX_STYLE.render("[x] " + label);
        }
        return "[ ] " + label;
    }

    /**
     * Handles progress bar for example.
     *
     * @param percent percent
     * @return result
     */
    private String progressBar(double percent) {
        int fullSize = (int) Math.round(PROGRESS_BAR_WIDTH * percent);
        StringBuilder fullCells = new StringBuilder();
        for (int i = 0; i < fullSize; i++) {
            fullCells.append(RAMP[i].render(PROGRESS_FULL_CHAR));
        }

        int emptySize = PROGRESS_BAR_WIDTH - fullSize;
        StringBuilder emptyCells = new StringBuilder();
        for (int i = 0; i < emptySize; i++) {
            emptyCells.append(PROGRESS_EMPTY);
        }

        return fullCells.toString() + emptyCells.toString() + " ";
    }

    /**
     * Handles ease out bounce for example.
     *
     * @param x x
     * @return result
     */
    private static double easeOutBounce(double x) {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (x < 1.0 / d1) {
            return n1 * x * x;
        } else if (x < 2.0 / d1) {
            x -= 1.5 / d1;
            return n1 * x * x + 0.75;
        } else if (x < 2.5 / d1) {
            x -= 2.25 / d1;
            return n1 * x * x + 0.9375;
        } else {
            x -= 2.625 / d1;
            return n1 * x * x + 0.984375;
        }
    }

    /**
     * Handles make ramp styles for example.
     *
     * @param colorA color a
     * @param colorB color b
     * @param steps steps
     * @return result
     */
    private static Style[] makeRampStyles(String colorA, String colorB, int steps) {
        Style[] styles = new Style[steps];
        for (int i = 0; i < steps; i++) {
            int colorIndex = (int) ((double) i / steps * (GRADIENT_COLORS.size() - 1));
            colorIndex = Math.min(colorIndex, GRADIENT_COLORS.size() - 1);
            styles[i] = Style.newStyle().foreground(Color.color(GRADIENT_COLORS.get(colorIndex)));
        }
        return styles;
    }

    /**
     * Handles tick for example.
     *
     * @return result
     */
    private static Message tick() {
        try {
            Thread.sleep(1000);
            return new TickMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Handles frame for example.
     *
     * @return result
     */
    private static Message frame() {
        try {
            Thread.sleep(1000 / 60);
            return new FrameMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new ViewsExample()).run();
    }

    /**
     * Support type for the Views example.
     */
    private static class TickMessage implements Message {
    }

    /**
     * Support type for the Views example.
     */
    private static class FrameMessage implements Message {
    }
}
