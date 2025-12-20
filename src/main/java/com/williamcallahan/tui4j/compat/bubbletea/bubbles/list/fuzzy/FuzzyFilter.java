package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.fuzzy;

import org.apache.commons.text.similarity.FuzzyScore;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Port of Bubbles fuzzy filter.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class FuzzyFilter {

    public static Rank[] defaultFilter(String term, String[] targets) {
        return filter(term, targets, true);

    }

    public static Rank[] unsortedFilter(String term, String[] targets) {
        return filter(term, targets, false);
    }

    private static Rank[] filter(String term, String[] targets, boolean sort) {
        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);

        List<Rank> ranks = new ArrayList<>();
        for (int i = 0; i < targets.length; i++) {
            String target = targets[i];
            int score = fuzzyScore.fuzzyScore(target, term);

            // Only consider matches if all characters in the term are found in order
            if (score > 0 && isValidMatch(term, target)) {
                int[] matchedIndexes = findMatchedIndexes(term, target);
                ranks.add(new Rank(i, matchedIndexes));
            }
        }

        if (sort) {
            ranks.sort((r1, r2) -> {
                int score1 = fuzzyScore.fuzzyScore(targets[r1.getIndex()], term);
                int score2 = fuzzyScore.fuzzyScore(targets[r2.getIndex()], term);
                if (score1 != score2) return Integer.compare(score2, score1);
                return Integer.compare(r1.getIndex(), r2.getIndex());
            });
        }

        return ranks.toArray(new Rank[0]);
    }

    private static boolean isValidMatch(String term, String target) {
        int termIndex = 0;
        for (int i = 0; i < target.length() && termIndex < term.length(); i++) {
            if (Character.toLowerCase(target.charAt(i)) == Character.toLowerCase(term.charAt(termIndex))) {
                termIndex++;
            }
        }
        // A match is valid only if the entire term is matched
        return termIndex == term.length();
    }

    private static int[] findMatchedIndexes(String term, String target) {
        List<Integer> matchedIndexes = new ArrayList<>();
        int termIndex = 0;

        for (int i = 0; i < target.length() && termIndex < term.length(); i++) {
            if (Character.toLowerCase(target.charAt(i)) == Character.toLowerCase(term.charAt(termIndex))) {
                matchedIndexes.add(i);
                termIndex++;
            }
        }

        return matchedIndexes.stream().mapToInt(Integer::intValue).toArray();
    }
}