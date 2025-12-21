package com.williamcallahan.tui4j.examples.listsimple;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.FilteredItem;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.Item;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.ItemDelegate;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.List;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.Styles;

/**
 * Example program for list simple.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listsimple/ListSimpleExample.java
 */
public class ListSimpleExample implements Model {

    private final Style titleStyle = Style.newStyle().marginLeft(2);
    private final Style itemStyle = Style.newStyle().paddingLeft(4);
    private final Style selectedItemStyle = Style.newStyle().paddingLeft(2).foreground(Color.color("170"));
    private final Style paginationStyle = Styles.defaultStyles().paginationStyle().paddingLeft(4);
    private final Style helpStyle = Styles.defaultStyles().helpStyle().paddingLeft(4).paddingBottom(1);
    private final Style quitTextStyle = Style.newStyle().margin(1, 0, 2, 4);

    record SimpleItem(String value) implements Item {
        @Override
        public String filterValue() {
            return "";
        }
    }

    class SimpleItemDelegate implements ItemDelegate {

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

        @Override
        public int height() {
            return 1;
        }

        @Override
        public int spacing() {
            return 0;
        }

        @Override
        public Command update(Message msg, List model) {
            return null;
        }
    }

    private List list;
    private String choice;
    private boolean quitting;

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
            return null;
        });
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            return UpdateResult.from(this, list.setWidth(windowSizeMessage.width()));
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

    public static void main(String[] args) {
        new Program(new ListSimpleExample()).run();
    }
}
