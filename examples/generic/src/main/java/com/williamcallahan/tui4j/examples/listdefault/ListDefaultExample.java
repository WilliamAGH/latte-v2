package com.williamcallahan.tui4j.examples.listdefault;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Dimensions;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultItem;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.Item;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.List;

import java.time.Duration;

/**
 * Example program for list default.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listdefault/ListDefaultExample.java
 */
public class ListDefaultExample implements Model {

    record ExampleItem(String title, String desc) implements DefaultItem {
        @Override
        public String filterValue() {
            return title();
        }

        @Override
        public String description() {
            return desc;
        }

        @Override
        public String title() {
            return title;
        }
    }

    private List list;

    private final Style docStyle = Style.newStyle().margin(1, 2);

    public ListDefaultExample() {
        this.list = new List(
                new Item[]{
                        new ExampleItem("Raspberry Pi’s", "I have ’em all over my house"),
                        new ExampleItem("Nutella", "It's good on toast"),
                        new ExampleItem("Bitter melon", "It cools you down"),
                        new ExampleItem("Nice socks", "And by that I mean socks without holes"),
                        new ExampleItem("Eight hours of sleep", "I had this once"),
                        new ExampleItem("Cats", "Usually"),
                        new ExampleItem("Plantasia, the album", "My plants love it too"),
                        new ExampleItem("Pour over coffee", "It takes forever to make though"),
                        new ExampleItem("VR", "Virtual reality...what is there to say?"),
                        new ExampleItem("Noguchi Lamps", "Such pleasing organic forms"),
                        new ExampleItem("Linux", "Pretty much the best OS"),
                        new ExampleItem("Business school", "Just kidding"),
                        new ExampleItem("Pottery", "Wet clay is a great feeling"),
                        new ExampleItem("Shampoo", "Nothing like clean hair"),
                        new ExampleItem("Table tennis", "It’s surprisingly exhausting"),
                        new ExampleItem("Milk crates", "Great for packing in your extra stuff"),
                        new ExampleItem("Afternoon tea", "Especially the tea sandwich part"),
                        new ExampleItem("Stickers", "The thicker the vinyl the better"),
                        new ExampleItem("20° Weather", "Celsius, not Fahrenheit"),
                        new ExampleItem("Warm light", "Like around 2700 Kelvin"),
                        new ExampleItem("The vernal equinox", "The autumnal equinox is pretty good too"),
                        new ExampleItem("Gaffer’s tape", "Basically sticky fabric"),
                        new ExampleItem("Terrycloth", "In other words, towel fabric")
                },
                0,
                0
        );
        list.setStatusMessageLifetime(Duration.ofSeconds(5));
        list.setTitle("My Fave Things");
    }

    @Override
    public Command init() {
        return Command.sequence(Command.checkWindowSize(), list.init());
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            Dimensions frameSize = docStyle.frameSize();
            this.list.setSize(
                    windowSizeMessage.width() - frameSize.width(),
                    windowSizeMessage.height() - frameSize.height()
            );
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key().equals("o")) {
                return UpdateResult.from(this, list.newStatusMessage("Some status"));
            }
        }
        UpdateResult<List> update = list.update(msg);
        this.list = update.model();
        return UpdateResult.from(this, update.command());
    }

    @Override
    public String view() {
        return docStyle.render(list.view());
    }

    public static void main(String[] args) {
        new Program(new ListDefaultExample())
                .withAltScreen()
                .run();
    }
}
