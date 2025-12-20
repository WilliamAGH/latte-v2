package com.williamcallahan.tui4j.springexample.view;

import lombok.RequiredArgsConstructor;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.FetchedItems;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.FilteredItem;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.ListDataSource;
import com.williamcallahan.tui4j.springexample.model.Book;
import com.williamcallahan.tui4j.springexample.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Example program for book data source.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/view/BookDataSource.java
 */
@Component
@RequiredArgsConstructor
public class BookDataSource implements ListDataSource {

    private final BookRepository bookRepository;

    @Override
    public FetchedItems fetchItems(int page, int perPage, String filterValue) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long allItems = bookRepository.count();
        PageRequest pageRequest = PageRequest.of(page, perPage);

        return fetchedItems(
                bookRepository.findByTitleIsContaining(filterValue, pageRequest),
                allItems,
                filterValue);
    }

    private static FetchedItems fetchedItems(Page<Book> filteredBooks, long allItems, String filterValue) {
        return new FetchedItems(
                filteredBooks
                        .stream()
                        .map(BookItem::new)
                        .map(item -> new FilteredItem(0, item, findMatchedIndexes(item.title(), filterValue)))
                        .toList(),
                filteredBooks.getTotalElements(),
                allItems,
                filteredBooks.getTotalPages()
        );
    }

    private static int[] findMatchedIndexes(String title, String filterValue) {
        if (filterValue == null || filterValue.isEmpty()) {
            return new int[0];
        }

        List<Integer> indexes = new ArrayList<>();
        int index = title.indexOf(filterValue);

        while (index != -1) {
            // Add all characters of the match to the list
            for (int i = 0; i < filterValue.length(); i++) {
                indexes.add(index + i);
            }
            index = title.indexOf(filterValue, index + 1); // Find next occurrence
        }

        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }
}
