package com.williamcallahan.tui4j.examples.paginator;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator.Bounds;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator.Paginator;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator.Type;

import java.util.Arrays;

/**
 * Example program for paginator.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/paginator/PaginatorExample.java
 */
public class PaginatorExample implements Model {

    private Paginator paginator;
    private String[] items;

    public PaginatorExample() {
        this.items = new String[100];
        for (int i = 1; i < 101; i++) {
            items[i - 1] = "Item %d".formatted(i);
        }

        this.paginator = new Paginator();
        paginator.setType(Type.Dots);
        paginator.setPerPage(10);
        paginator.setTotalPages(items.length);
    }

    @Override
    public Command init() {
        // cannot render until terminal info is provided
        paginator.setActiveDot(Style.newStyle().foreground(new AdaptiveColor("235", "252")).render("•"));
        paginator.setInactiveDot(Style.newStyle().foreground(new AdaptiveColor("250", "238")).render("•"));

        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if (key.equals("q") || key.equals("Q")) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
        }
        this.paginator = paginator.update(msg);
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        StringBuilder view = new StringBuilder();
        view.append("\n  Paginator Example\n\n");
        Bounds sliceBounds = paginator.getSliceBounds(items.length);

        String[] range = Arrays.copyOfRange(items, sliceBounds.start(), sliceBounds.end());
        for (String item : range) {
            view.append("  • ").append(item).append("\n\n");
        }
        view.append("  ").append(paginator.view());
        view.append("\n\n  h/l ←/→ page • q: quit\n");

        return view.toString();
    }

    public static void main(String[] args) {
        new Program(new PaginatorExample()).run();
    }
}
