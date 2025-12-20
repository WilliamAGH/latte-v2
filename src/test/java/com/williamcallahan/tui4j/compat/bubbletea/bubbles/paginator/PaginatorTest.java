package com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator.Paginator.Option.withPerPage;
import static com.williamcallahan.tui4j.compat.bubbletea.bubbles.paginator.Paginator.Option.withTotalPages;

/**
 * Tests paginator.
 * Bubble Tea: bubbletea/examples/paginator/main.go
 */
class PaginatorTest {

    @Test
    void test_New() {
        // given
        int perPage = 42;
        int totalPages = 42;

        // when
        Paginator paginator = new Paginator();

        // then
        assertThat(paginator.perPage()).isEqualTo(1);
        assertThat(paginator.totalPages()).isEqualTo(1);

        // when
        paginator = new Paginator(
                withPerPage(perPage),
                withTotalPages(42)
        );

        // then
        assertThat(paginator.perPage()).isEqualTo(perPage);
        assertThat(paginator.totalPages()).isEqualTo(totalPages);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("prevPageCases")
    void test_PrevPage(String name, int totalPages, int page, int expected) {
        // given
        Paginator paginator = new Paginator();
        paginator.setTotalPages(totalPages);
        paginator.setPage(page);

        // when
        paginator.prevPage();

        // then
        assertThat(paginator.page()).isEqualTo(expected);
    }

    private static Stream<Arguments> prevPageCases() {
        return Stream.of(
                Arguments.of("Go to previous page", 10, 1, 0),
                Arguments.of("Stay on first page", 5, 0, 0)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nextPageCases")
    void test_NextPage(String name, int totalPages, int page, int expected) {
        // given
        Paginator paginator = new Paginator();
        paginator.setTotalPages(totalPages);
        paginator.setPage(page);

        // when
        paginator.nextPage();

        // then
        assertThat(paginator.page()).isEqualTo(expected);
    }

    private static Stream<Arguments> nextPageCases() {
        return Stream.of(
                Arguments.of("Go to next page", 2, 0, 1),
                Arguments.of("Stay on last page", 2, 1, 1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("onLastPageCases")
    void test_OnLastPage(String name, int page, int totalPages, boolean expected) {
        // given
        Paginator paginator = new Paginator();
        paginator.setTotalPages(totalPages);
        paginator.setPage(page);

        // when
        boolean result = paginator.onLastPage();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> onLastPageCases() {
        return Stream.of(
                Arguments.of("On last page", 1, 2, true),
                Arguments.of("Not on last page", 0, 2, false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("onFirstPageCases")
    void test_OnFirstPage(String name, int page, int totalPages, boolean expected) {
        // given
        Paginator paginator = new Paginator();
        paginator.setTotalPages(totalPages);
        paginator.setPage(page);

        // when
        boolean result = paginator.onFirstPage();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> onFirstPageCases() {
        return Stream.of(
                Arguments.of("On first page", 0, 2, true),
                Arguments.of("Not on first page", 1, 2, false)
        );
    }

    @ParameterizedTest
    @MethodSource("itemsOnPageCases")
    void test_ItemsOnPage(int currentPage, int totalPages, int totalItems, int expectedItems) {
        // given
        Paginator paginator = new Paginator();
        paginator.setPage(currentPage);
        paginator.setTotalPages(totalPages);

        // when
        int actualItems = paginator.itemsOnPage(totalItems);

        // then
        assertThat(actualItems).isEqualTo(expectedItems);
    }

    private static Stream<Arguments> itemsOnPageCases() {
        return Stream.of(
                Arguments.of(1, 10, 10, 1),
                Arguments.of(3, 10, 10, 1),
                Arguments.of(7, 10, 10, 1)
        );
    }
}