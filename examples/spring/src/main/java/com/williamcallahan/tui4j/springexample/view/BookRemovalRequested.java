package com.williamcallahan.tui4j.springexample.view;

import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.springexample.model.Book;

/**
 * Example program for book removal requested.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/view/BookRemovalRequested.java
 */
public record BookRemovalRequested(Book book) implements Message {
}
