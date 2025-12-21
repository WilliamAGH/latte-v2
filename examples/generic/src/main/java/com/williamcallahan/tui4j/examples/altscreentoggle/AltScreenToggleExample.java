package com.williamcallahan.tui4j.examples.altscreentoggle;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.EnterAltScreen;
import com.williamcallahan.tui4j.compat.bubbletea.message.ExitAltScreen;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;

/**
 * Example program for mode.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/altscreentoggle/AltScreenToggleExample.java
 */
enum Mode {
    ALT_SCREEN(" altscreen mode "),
    INLINE(" inline mode ");

    private final String description;

    Mode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

public class AltScreenToggleExample implements Model {

    private static final Style KEYWORD_STYLE = Style.newStyle()
            .foreground(Color.color("204"))
            .background(Color.color("235"));

    private static final Style HELP_STYLE = Style.newStyle()
            .foreground(Color.color("241"));

    private boolean altScreen;
    private boolean quitting;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "q", "Q" -> {
                    quitting = true;
                    yield new UpdateResult<>(this, QuitMessage::new);
                }
                case " " -> {
                    Command cmd;
                    if (altScreen) {
                        cmd = ExitAltScreen::new;
                    } else {
                        cmd = EnterAltScreen::new;
                    }
                    altScreen = !altScreen;
                    yield new UpdateResult<>(this, cmd);
                }
                default -> new UpdateResult<>(this, null);
            };
        }

        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        if (quitting) {
            return "Bye!\n";
        }

        Mode mode;
        if (altScreen) {
            mode = Mode.ALT_SCREEN;
        } else {
            mode = Mode.INLINE;
        }

        return "\n\n  You're in %s\n\n\n".formatted(KEYWORD_STYLE.render(mode.getDescription())) +
                HELP_STYLE.render("  space: switch modes • q: exit\n");
    }

    public static void main(String[] args) {
        Program program = new Program(new AltScreenToggleExample());
        program.run();
    }
}
