package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbles.list.FetchedCurrentPageItems;
import com.williamcallahan.tui4j.compat.bubbles.list.FetchedItems;
import com.williamcallahan.tui4j.compat.bubbles.list.FilterState;
import com.williamcallahan.tui4j.compat.bubbles.list.FilteredItem;
import com.williamcallahan.tui4j.compat.bubbles.list.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that canonical types in {@code com.williamcallahan.tui4j.compat.bubbles.list}
 * implement correct behavior as Messages and value types.
 */
class MessageShimCompatTest {

    @Test
    void fetchedCurrentPageItemsIsMessage() {
        var item = new FilteredItem(
                (Item) () -> "test"
        );

        var fetched = new FetchedItems(
                List.of(item), 1, 10, 1
        );

        var msg = new FetchedCurrentPageItems(fetched);

        assertThat(msg).isInstanceOf(Message.class);
        assertThat(msg.fetchedItems().items()).hasSize(1);
        assertThat(msg.fetchedItems().matchedItems()).isEqualTo(1);
        assertThat(msg.fetchedItems().totalItems()).isEqualTo(10);
        assertThat(msg.fetchedItems().totalPages()).isEqualTo(1);
    }

    @Test
    void fetchedItemsRecordFieldsAreCorrect() {
        var item = new FilteredItem(
                (Item) () -> "v"
        );

        var fetched = new FetchedItems(List.of(item), 5, 20, 3);

        assertThat(fetched.items()).hasSize(1);
        assertThat(fetched.matchedItems()).isEqualTo(5);
        assertThat(fetched.totalItems()).isEqualTo(20);
        assertThat(fetched.totalPages()).isEqualTo(3);
    }

    @Test
    void filterStateEnumValuesExist() {
        assertThat(FilterState.Unfiltered).isNotNull();
        assertThat(FilterState.Filtering).isNotNull();
        assertThat(FilterState.FilterApplied).isNotNull();
        assertThat(FilterState.values()).hasSize(3);
    }
}
