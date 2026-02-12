package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.BatchMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.SequenceMessage;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.Key;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of github.com/charmbracelet/bubbles/list/list_test.go.
 */
class ListTest {

    @BeforeEach
    void setUp() {
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
        Renderer.defaultRenderer().setColorProfile(ColorProfile.Ascii);
    }

    @Test
    void testStatusBarItemName() {
        List list = createList(new TestItem("foo"), new TestItem("bar"));

        assertThat(statusView(list)).contains("2 items");

        updateItems(list, new TestItem("foo"));

        assertThat(statusView(list)).contains("1 item");
    }

    @Test
    void testStatusBarWithoutItems() {
        List list = createList();

        assertThat(statusView(list)).contains("No items");
    }

    @Test
    void testCustomStatusBarItemName() {
        List list = createList(new TestItem("foo"), new TestItem("bar"));
        list.setStatusBarItemName("connection", "connections");

        assertThat(statusView(list)).contains("2 connections");

        updateItems(list, new TestItem("foo"));
        assertThat(statusView(list)).contains("1 connection");

        updateItems(list);
        assertThat(statusView(list)).contains("No connections");
    }

    @Test
    void testSetFilterText() {
        List list = createList(new TestItem("foo"), new TestItem("bar"), new TestItem("baz"));

        applyCommand(list, list.setFilterText("ba"));

        applyCommand(list, list.setFilterState(FilterState.Unfiltered));
        assertThat(visibleValues(list)).containsExactly("foo", "bar", "baz");

        applyCommand(list, list.setFilterState(FilterState.Filtering));
        assertThat(visibleValues(list)).containsExactly("bar", "baz");

        applyCommand(list, list.setFilterState(FilterState.FilterApplied));
        assertThat(visibleValues(list)).containsExactly("bar", "baz");
    }

    @Test
    void testSetFilterState() {
        List list = createList(new TestItem("foo"), new TestItem("bar"), new TestItem("baz"));

        applyCommand(list, list.setFilterText("ba"));

        applyCommand(list, list.setFilterState(FilterState.Unfiltered));
        String footer = footerLine(list.view());
        assertThat(footer).contains("up").doesNotContain("clear filter");

        applyCommand(list, list.setFilterState(FilterState.Filtering));
        footer = footerLine(list.view());
        assertThat(footer).contains("filter").doesNotContain("more");

        applyCommand(list, list.setFilterState(FilterState.FilterApplied));
        footer = footerLine(list.view());
        assertThat(footer).contains("clear");
    }

    @Test
    void testResizeKeepsAbsoluteSelectionIndex() {
        Item[] items = new Item[100];
        for (int i = 0; i < items.length; i++) {
            items[i] = new TestItem("item-" + i);
        }
        List list = createList(items);

        applyCommand(list, list.setShowTitle(false));
        list.setShowFilter(false);
        list.setShowStatusBar(false);
        list.setShowPagination(false);
        applyCommand(list, list.setShowHelp(false));
        applyCommand(list, list.refresh());

        applyCommand(list, list.select(80));
        assertThat(list.index()).isEqualTo(80);

        applyCommand(list, list.setSize(10, 5));
        assertThat(list.index()).isEqualTo(80);
        assertThat(list.selectedItem().filterValue()).isEqualTo("item-80");
    }

    @Test
    void testPopulatedViewDoesNotShowEmptyStateWhenMatchesExist() {
        ListDataSource dataSource = (page, perPage, filterValue) ->
            new FetchedItems(java.util.List.of(), 5, 5, 5);
        List list = new List(dataSource, new TestDelegate(), 10, 5);
        applyCommand(list, list.init());

        assertThat(populatedView(list)).doesNotContain("No items.");
    }

    @Test
    void testAcceptFilteringUsesMatchedItemsNotCurrentPageSlice() {
        ListDataSource dataSource = (page, perPage, filterValue) ->
            new FetchedItems(java.util.List.of(), 3, 5, 1);
        List list = new List(dataSource, new TestDelegate(), 10, 5);
        applyCommand(list, list.init());
        applyCommand(list, list.setFilterText("x"));
        applyCommand(list, list.setFilterState(FilterState.Filtering));

        applyMessage(list, new KeyPressMessage(new Key(KeyType.keyCR)));

        assertThat(list.filterState()).isEqualTo(FilterState.FilterApplied);
    }

    @Test
    void testEmptyResultsStayOnSinglePageWithPaginationDisabled() {
        ListDataSource dataSource = (page, perPage, filterValue) ->
            new FetchedItems(java.util.List.of(), 0, 0, 0);
        List list = new List(dataSource, new TestDelegate(), 10, 5);
        applyCommand(list, list.init());

        assertThat(list.nextPage()).isNull();
        assertThat(list.prevPage()).isNull();
        assertThat(keyMap(list).nextPage().isEnabled()).isFalse();
        assertThat(keyMap(list).prevPage().isEnabled()).isFalse();
    }

    @Test
    void testCursorUpFromLaterPageKeepsCursorNonNegativeWhenFetchedPageIsEmpty() {
        ListDataSource dataSource = (page, perPage, filterValue) -> {
            if (page == 1) {
                return new FetchedItems(
                    java.util.List.of(new FilteredItem(new TestItem("item-1"))),
                    2,
                    2,
                    2
                );
            }
            return new FetchedItems(java.util.List.of(), 2, 2, 2);
        };
        List list = new List(dataSource, new TestDelegate(), 10, 5);
        applyCommand(list, list.init());
        applyCommand(list, list.setShowTitle(false));
        list.setShowFilter(false);
        list.setShowStatusBar(false);
        list.setShowPagination(false);
        applyCommand(list, list.setShowHelp(false));
        applyCommand(list, list.setSize(10, 1));

        applyCommand(list, list.select(1));
        applyCommand(list, list.cursorUp());

        assertThat(list.cursor()).isZero();
        assertThat(list.index()).isZero();
    }

    private static List createList(Item... items) {
        List list = new List(items, new TestDelegate(), 10, 10);
        applyCommand(list, list.init());
        return list;
    }

    private static void updateItems(List list, Item... items) {
        DefaultDataSource dataSource = (DefaultDataSource) list.dataSource();
        applyCommand(list, dataSource.setItems(items));
    }

    private static void applyCommand(List list, Command command) {
        if (command == null) {
            return;
        }
        applyMessage(list, command.execute());
    }

    private static final int MAX_RECURSION_DEPTH = 100;
    private static int recursionDepth = 0;

    private static void applyMessage(List list, Message msg) {
        if (msg == null) {
            return;
        }

        // Guard against infinite loops in test (real Program handles this async)
        if (recursionDepth++ > MAX_RECURSION_DEPTH) {
            recursionDepth = 0;
            return;
        }

        try {
            applyMessageInner(list, msg);
        } finally {
            recursionDepth--;
        }
    }

    private static void applyMessageInner(List list, Message msg) {
        // Avoid infinite time-based messages in unit tests (they're async in real programs).
        if (msg instanceof com.williamcallahan.tui4j.compat.bubbles.spinner.TickMessage) {
            return;
        }
        // Filter cursor blink messages (package-private, check by class name)
        String className = msg.getClass().getSimpleName();
        if (className.equals("InitialBlinkMessage") || className.equals("BlinkMessage")) {
            return;
        }

        // Bubble Tea: Program handles Batch/Sequence by executing nested commands.
        if (msg instanceof BatchMessage batchMessage) {
            for (Command c : batchMessage.commands()) {
                applyCommand(list, c);
            }
            return;
        }
        if (msg instanceof SequenceMessage sequenceMessage) {
            for (Command c : sequenceMessage.commands()) {
                applyCommand(list, c);
            }
            return;
        }

        com.williamcallahan.tui4j.compat.bubbletea.UpdateResult<List> result = list.update(msg);
        if (result != null && result.command() != null) {
            applyCommand(list, result.command());
        }
    }

    private static String statusView(List list) {
        try {
            Method method = List.class.getDeclaredMethod("statusView");
            method.setAccessible(true);
            return (String) method.invoke(list);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read status view", e);
        }
    }

    private static String populatedView(List list) {
        try {
            Method method = List.class.getDeclaredMethod("populatedView");
            method.setAccessible(true);
            return (String) method.invoke(list);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read populated view", e);
        }
    }

    private static KeyMap keyMap(List list) {
        try {
            var field = List.class.getDeclaredField("keys");
            field.setAccessible(true);
            return (KeyMap) field.get(list);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read key map", e);
        }
    }

    private static java.util.List<String> visibleValues(List list) {
        return list.visibleItems().stream()
                .map(item -> item.item().filterValue())
                .toList();
    }

    private static String footerLine(String view) {
        String[] lines = view.split("\n");
        return lines[lines.length - 1];
    }

    private record TestItem(String value) implements Item {
        @Override
        public String filterValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static final class TestDelegate implements ItemDelegate {
        @Override
        public void render(StringBuilder output, List list, int index, FilteredItem filteredItem) {
            output.append(index + 1).append(". ").append(filteredItem.item().filterValue());
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
        public Command update(Message msg, List listModel) {
            return null;
        }
    }
}
