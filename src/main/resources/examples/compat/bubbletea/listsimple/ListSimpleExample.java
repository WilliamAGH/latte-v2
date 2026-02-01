package com.williamcallahan.tui4j.examples.listsimple;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.list.FilteredItem;
import com.williamcallahan.tui4j.compat.bubbles.list.Item;
import com.williamcallahan.tui4j.compat.bubbles.list.ItemDelegate;
import com.williamcallahan.tui4j.compat.bubbles.list.List;
import com.williamcallahan.tui4j.compat.bubbles.list.Styles;

/**
 * Demonstrates a minimal list with a custom delegate.
 * Upstream: bubbletea/examples/list-simple/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listsimple/ListSimpleExample.java
 */
public class ListSimpleExample implements Model {

    private final Style titleStyle = Style.newStyle().marginLeft(2);
    private final Style itemStyle = Style.newStyle().paddingLeft(4);
    private final Style selectedItemStyle = Style.newStyle().paddingLeft(2).foreground(Color.color("170"));
    private final Style paginationStyle = Styles.defaultStyles().paginationStyle().paddingLeft(4);
    private final Style helpStyle = Styles.defaultStyles().helpStyle().paddingLeft(4).paddingBottom(1);
    private final Style quitTextStyle = Style.newStyle().margin(1, 0, 2, 4);

    /**
     * Simple item carrying the display value.
     */
    record SimpleItem(String value) implements Item {
        /**
         * Returns the value used for filtering (unused in this example).
         *
         * @return filter value
         */
        @Override
        public String filterValue() {
            return "";
        }
    }

    /**
     * Renders list items with a custom selection indicator.
     */
    class SimpleItemDelegate implements ItemDelegate {

        /**
         * Renders each item with padding and selection styling.
         *
         * @param output output buffer
         * @param list list model
         * @param index item index
         * @param filteredItem filtered item wrapper
         */
        @Override
        public void render(StringBuilder output, List list, int index, FilteredItem filteredItem) {
            Item item = filteredItem.item();
            if (item instanceof SimpleItem simpleItem) {
                String str = "%d. %s".formatted(index + 1, simpleItem.value());
                if (index == list.index()) {
                    output.append(selectedItemStyle.render(">", str));
                } else {
                    output.append(itemStyle.render(str));
                }
            }
        }

        /**
         * Returns the number of lines per item.
         *
         * @return item height
         */
        @Override
        public int height() {
            return 1;
        }

        /**
         * Returns the spacing between items.
         *
         * @return spacing lines
         */
        @Override
        public int spacing() {
            return 0;
        }

        /**
         * Returns no delegate-specific command for this example.
         *
         * @param msg incoming message
         * @param model list model
         * @return null to indicate no command
         */
        @Override
        public Command update(Message msg, List model) {
            return null;
        }
    }

    private List list;
    private String choice;
    private boolean quitting;

    /**
     * Seeds the list with sample items and a custom delegate.
     */
    public ListSimpleExample() {
        this.list = new List(
                new Item[]{
                        new SimpleItem("Ramen"),
                        new SimpleItem("Tomato Soup"),
                        new SimpleItem("Hamburgers"),
                        new SimpleItem("Cheeseburgers"),
                        new SimpleItem("Currywurst"),
                        new SimpleItem("Okonomiyaki"),
                        new SimpleItem("Pasta"),
                        new SimpleItem("Fillet Mignon"),
                        new SimpleItem("Caviar"),
                        new SimpleItem("Just Wine")
                },
                new SimpleItemDelegate(),
                20,
                14
        );

    }

    /**
     * Initializes the list and applies list styling.
     *
     * @return initialization command
     */
    @Override
    public Command init() {
        Command listInitCmd = list.init();

        list.setFilteringEnabled(false);
        list.setTitle("What do you want for dinner?");
        list.styles().setTitle(titleStyle);
        list.styles().setPaginationStyle(paginationStyle);
        list.styles().setHelpStyle(helpStyle);

        return Command.sequence(listInitCmd, () -> {
            list.setShowStatusBar(false);
            // no-op
            return new Message() {
               
            };
        });
    }

    /**
     * Handles resizing and selection/quit input.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            return UpdateResult.from(this, list.setSize(windowSizeMessage.width(), windowSizeMessage.height()));
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if ("q".equals(keyPressMessage.key()) || "ctrl+c".equals(keyPressMessage.key())) {
                this.quitting = true;
                return UpdateResult.from(this, Command.quit());
            } else if ("enter".equals(keyPressMessage.key())) {
                Item item = list.selectedItem();
                if (item instanceof SimpleItem simpleItem) {
                    this.choice = simpleItem.value();
                }
                return UpdateResult.from(this, Command.quit());
            }
        }

        UpdateResult<List> listUpdateResult = list.update(msg);
        return UpdateResult.from(this, listUpdateResult.command());
    }

    /**
     * Renders the list or the final selection message.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        if (this.choice != null && !this.choice.isEmpty()) {
            return quitTextStyle.render("%s? Sounds good to me.".formatted(choice));
        }
        if (this.quitting) {
            return quitTextStyle.render("Not hungry? That's cool.");
        }
        return "\n" + list.view();
    }

    /**
     * Runs the simple list example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ListSimpleExample()).run();
    }
}
