package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.fuzzy;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.Rank;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Tests fuzzy filter.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
class FuzzyFilterTest {

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testDefaultFilter(String term, String[] targets, Rank[] expected) {
        Rank[] result = FuzzyFilter.defaultFilter(term, targets);
        assertArrayEquals(expected, result, "Failed for term: " + term + ", targets: " + Arrays.toString(targets));
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                arguments("mnr", new String[]{"moduleNameResolver.ts"},
                        new Rank[]{new Rank(0, new int[]{0, 6, 10})}),
                arguments("mmt", new String[]{"mémeTemps"},
                        new Rank[]{new Rank(0, new int[]{0, 2, 4})}),
                arguments("mnr", new String[]{"moduleNameResolver.ts", "my name is_Ramsey"},
                        new Rank[]{
                                new Rank(0, new int[]{0, 6, 10}),
                                new Rank(1, new int[]{0, 3, 11})
                        }),
                arguments("aaa", new String[]{"aaa", "bbb"},
                        new Rank[]{new Rank(0, new int[]{0, 1, 2})}),
                arguments("tkk", new String[]{"The Black Knight"},
                        new Rank[]{new Rank(0, new int[]{0, 8, 10})}),
                arguments("cats", new String[]{"cat"}, new Rank[]{}),
                arguments("", new String[]{"cat"}, new Rank[]{}),
                arguments("abcx", new String[]{"abc\\x"},
                        new Rank[]{new Rank(0, new int[]{0, 1, 2, 4})})
        );
    }
}