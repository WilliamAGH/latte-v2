package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.BatchMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.SequenceMessage;
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

        // Also handle the legacy message forms that may be emitted internally.
        if (msg instanceof com.williamcallahan.tui4j.compat.bubbletea.BatchMessage batchMsg) {
            for (Command c : batchMsg.commands()) {
                applyCommand(list, c);
            }
            return;
        }
        if (msg instanceof com.williamcallahan.tui4j.compat.bubbletea.SequenceMessage sequenceMsg) {
            for (Command c : sequenceMsg.commands()) {
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
