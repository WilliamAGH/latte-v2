package com.williamcallahan.tui4j.springexample.repository;

import com.williamcallahan.tui4j.springexample.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Example program for book repository.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/repository/BookRepository.java
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(name = "Book.findByTitleIsContaining")
    Page<Book> findByTitleIsContaining(@Param("title") String title, Pageable pageable);
}
