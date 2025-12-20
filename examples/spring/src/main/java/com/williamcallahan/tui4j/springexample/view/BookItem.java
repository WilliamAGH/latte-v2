package com.williamcallahan.tui4j.springexample.view;

import lombok.RequiredArgsConstructor;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultItem;
import com.williamcallahan.tui4j.springexample.model.Author;
import com.williamcallahan.tui4j.springexample.model.Book;

import java.util.List;

/**
 * Example program for book item.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/view/BookItem.java
 */
@RequiredArgsConstructor
public class BookItem implements DefaultItem {

    private final Book book;

    @Override
    public String title() {
        return book.getTitle();
    }

    public String authors() {
        StringBuilder authorsString = new StringBuilder();

        List<Author> authors = book.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            authorsString.append("- ").append(authors.get(i).getName());
            if (i + 1 < authors.size()) {
                authorsString.append("\n");
            }
        }

        return authorsString.toString();
    }

    public Book book() {
        return book;
    }

    public String getDescription() {
        return book.getDescription();
    }

    @Override
    public String description() {
        return book.getSynopsis();
    }

    @Override
    public String filterValue() {
        return book.getTitle();
    }
}
