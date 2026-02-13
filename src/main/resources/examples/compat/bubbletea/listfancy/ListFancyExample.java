package examples.compat.bubbletea.listfancy;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Dimensions;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.list.FilterState;
import com.williamcallahan.tui4j.compat.bubbles.list.Item;
import com.williamcallahan.tui4j.compat.bubbles.list.List;
import com.williamcallahan.tui4j.compat.bubbles.list.DefaultDataSource;

/**
 * Demonstrates a styled list with custom delegate actions.
 * Upstream: bubbletea/examples/list-fancy/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/ListFancyExample.java
 */
public class ListFancyExample implements Model {

    private List list;

    private RandomItemGenerator itemGenerator;
    private Keys keys;
    private Delegate.DelegateKeyMap delegateKeys;

    /**
     * Builds the list, seeds items, and wires help keys.
     */
    public ListFancyExample() {
        this.itemGenerator = new RandomItemGenerator();
        this.keys = new Keys();
        this.delegateKeys = new Delegate.DelegateKeyMap();

        int numItems = 24;
        Item[] items = new Item[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = itemGenerator.next();
        }

        this.list = new List(items, Delegate.newItemDelegate(delegateKeys), 0, 0);
        list.setTitle("Groceries");
        list.styles().setTitle(Styles.titleStyle);
        list.setAdditionalFullHelpKeys(() -> new Binding[] {
                keys.toggleSpinner(),
                keys.insertItem(),
                keys.toggleTitleBar(),
                keys.toggleStatusBar(),
                keys.togglePagination(),
                keys.toggleHelpMenu()
        });
    }

    /**
     * Initializes the list model.
     *
     * @return list init command
     */
    @Override
    public Command init() {
        return list.init();
    }

    /**
     * Handles window sizing, toggles, and list updates.
     *
     * @param msg incoming message
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            Dimensions frameSize = Styles.appStyle.frameSize();
            return UpdateResult.from(
                    this,
                    list.setSize(
                            windowSizeMessage.width() - frameSize.width(),
                            windowSizeMessage.height() - frameSize.height()
                    )
            );
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            if (list.filterState() != FilterState.Filtering) {

                if (Binding.matches(keyPressMessage, keys.toggleTitleBar())) {
                    boolean v = !list.showTitle();
                    list.setShowTitle(v);
                    list.setShowFilter(v);
                    return UpdateResult.from(this, list.setFilteringEnabled(v));
                } else if (Binding.matches(keyPressMessage, keys.toggleStatusBar())) {
                    list.setShowStatusBar(!list.showStatusBar());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.togglePagination())) {
                    list.setShowPagination(!list.showPagination());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.toggleHelpMenu())) {
                    list.setShowHelp(!list.showHelp());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.insertItem())) {
                    if (list.dataSource() instanceof DefaultDataSource defaultDataSource) {
                        delegateKeys.remove().setEnabled(true);
                        FancyItem newItem = itemGenerator.next();
                        Command insertCmd = defaultDataSource.insertItem(0, newItem);
                        Command statusCmd = list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Added", newItem.title()}));
                        return UpdateResult.from(this, Command.batch(insertCmd, statusCmd));
                    }
                    return UpdateResult.from(this);
                }
            }
        }

        UpdateResult<List> listUpdateResult = list.update(msg);
        return UpdateResult.from(this, listUpdateResult.command());
    }

    /**
     * Renders the list inside the app style frame.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return Styles.appStyle.render(list.view());
    }

    /**
     * Runs the fancy list example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new Program(new ListFancyExample())
                .withAltScreen()
                .run();
    }
}
