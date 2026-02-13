package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

/**
 * Port of Bubbles list.
 * <p>
 * Upstream: bubbles/list/list.go
 */
public class List
    extends ListPublicApi
    implements Model, com.williamcallahan.tui4j.compat.bubbles.help.KeyMap {

    /**
     * Creates a list backed by a static array of items.
     *
     * @param items list items
     * @param width list width
     * @param height list height
     */
    public List(Item[] items, int width, int height) {
        this(items, new DefaultDelegate(), width, height);
    }

    /**
     * Creates a list backed by a static array of items with a custom delegate.
     *
     * @param items list items
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     */
    public List(Item[] items, ItemDelegate delegate, int width, int height) {
        setup(new DefaultDataSource(this, items), delegate, width, height);
    }

    /**
     * Creates a list backed by a data source and delegate.
     *
     * @param dataSource list data source
     * @param delegate item delegate
     * @param width list width
     * @param height list height
     */
    public List(
        ListDataSource dataSource,
        ItemDelegate delegate,
        int width,
        int height
    ) {
        setup(dataSource, delegate, width, height);
    }

    /**
     * Creates a list backed by a data source.
     *
     * @param dataSource list data source
     * @param width list width
     * @param height list height
     */
    public List(ListDataSource dataSource, int width, int height) {
        this(dataSource, new DefaultDelegate(), width, height);
    }

    @Override
    public Command init() {
        paginator.setActiveDot(styles.activePaginationDot().render());
        paginator.setInactiveDot(styles.inactivePaginationDot().render());

        updatePagination();
        return fetchCurrentPageItems();
    }

    @Override
    public UpdateResult<List> update(Message msg) {
        return ListUpdateHandler.update(this, msg);
    }

    /**
     * Moves the cursor up by one row, paging as needed.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command cursorUp() {
        return ListUpdateHandler.cursorUp(this);
    }

    /**
     * Moves the cursor down by one row, paging as needed.
     *
     * @return command to refresh items, or {@code null} if no change
     */
    public Command cursorDown() {
        return ListUpdateHandler.cursorDown(this);
    }

    @Override
    public String view() {
        return ListViewRenderer.view(this);
    }

    @Override
    public Binding[] shortHelp() {
        return ListKeybindingsUpdater.shortHelp(this);
    }

    @Override
    public Binding[][] fullHelp() {
        return ListKeybindingsUpdater.fullHelp(this);
    }
}
