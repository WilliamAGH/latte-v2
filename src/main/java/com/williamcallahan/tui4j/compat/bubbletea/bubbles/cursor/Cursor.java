package com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.message.BlurMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.FocusMessage;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Port of Bubbles initial blink message.
 * Bubble Tea: bubbletea/examples/textinputs/main.go
 */
record InitialBlinkMessage() implements Message {
}

record BlinkMessage(int id, int tag) implements Message {
}

record BlinkCanceled() implements Message {

}

public class Cursor implements Model {

    private static final Duration DEFAULT_BLINK_SPEEd = Duration.ofMillis(530);

    private final int id;

    private Duration blinkSpeed;
    private Style style;
    private Style textStyle;
    private String charUnderCursor;
    private int blinkTag;
    private boolean focus;
    private boolean blink;
    private CursorMode mode;

    public Cursor() {
        this.id = 0;
        this.blinkSpeed = DEFAULT_BLINK_SPEEd;
        this.blink = true;
        this.focus = true;
        this.mode = CursorMode.Blink;
        this.style = Style.newStyle();
        this.textStyle = Style.newStyle();
    }

    @Override
    public Command init() {
        return Cursor::blink;
    }

    @Override
    public UpdateResult<Cursor> update(Message msg) {
        if (msg instanceof InitialBlinkMessage) {
            if (mode != CursorMode.Blink || !focus) {
                return UpdateResult.from(this);
            }
            return UpdateResult.from(this, blinkCommand());
        } else if (msg instanceof FocusMessage) {
            return UpdateResult.from(this, focus());
        } else if (msg instanceof BlurMessage) {
            blur();
            return UpdateResult.from(this);
        } else if (msg instanceof BlinkMessage blinkMessage) {
            if (mode != CursorMode.Blink || !focus) {
                return UpdateResult.from(this);
            }
            if (blinkMessage.id() != id || blinkMessage.tag() != blinkTag) {
                return UpdateResult.from(this);
            }
            if (mode == CursorMode.Blink) {
                this.blink = !blink;
                return UpdateResult.from(this, blinkCommand());
            }
            return UpdateResult.from(this);
        } else if (msg instanceof BlinkCanceled) {
            return UpdateResult.from(this);
        }
        return UpdateResult.from(this);
    }

    public Command blinkCommand() {
        if (mode != CursorMode.Blink) {
            return null;
        }

        blinkTag++;
        final int currentTag = blinkTag;

        return () -> {
            try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
                return executorService
                        .schedule(() -> new BlinkMessage(id, currentTag), blinkSpeed.toMillis(), TimeUnit.MILLISECONDS)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                return new BlinkCanceled();
            }
        };
    }

    public static Message blink() {
        return new InitialBlinkMessage();
    }

    public Command focus() {
        this.focus = true;
        this.blink = this.mode == CursorMode.Hide;
        if (mode == CursorMode.Blink) {
            return blinkCommand();
        }
        return null;
    }

    public void blur() {
        this.focus = false;
        this.blink = true;
    }

    @Override
    public String view() {
        if (blink) {
            return textStyle.inline(true).render(charUnderCursor);
        }
        return style.inline(true).reverse(true).render(charUnderCursor);
    }

    public Command setMode(CursorMode mode) {
        this.mode = mode;
        this.blink = (mode == CursorMode.Hide || !focus);

        if (mode == CursorMode.Blink) {
            return Cursor::blink;
        }
        return null;
    }

    public CursorMode mode() {
        return mode;
    }

    public void setBlinkSpeed(Duration blinkSpeed) {
        this.blinkSpeed = blinkSpeed;
    }

    public void setStyle(Style style) {
        this.style = style.copy();
    }

    public void setTextStyle(Style textStyle) {
        this.textStyle = textStyle.copy();
    }

    public void resetTextStyle() {
        this.textStyle = Style.newStyle();
    }

    public void setChar(String charUnderCursor) {
        this.charUnderCursor = charUnderCursor;
    }

    public void setBlink(boolean blink) {
        this.blink = blink;
    }
}