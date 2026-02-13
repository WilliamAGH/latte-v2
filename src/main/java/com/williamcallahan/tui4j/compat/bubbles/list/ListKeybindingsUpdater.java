package com.williamcallahan.tui4j.compat.bubbles.list;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Keybinding enablement and help rendering for {@link List}.
 * <p>
 * Upstream: bubbles/list/list.go (extracted from the original port).
 */
final class ListKeybindingsUpdater {

    private ListKeybindingsUpdater() {
    }

    static void updateKeybindings(List list) {
        if (list.filterState == FilterState.Filtering || list.fetchingItems) {
            list.keys.cursorUp().setEnabled(false);
            list.keys.cursorDown().setEnabled(false);
            list.keys.nextPage().setEnabled(false);
            list.keys.prevPage().setEnabled(false);
            list.keys.goToStart().setEnabled(false);
            list.keys.goToEnd().setEnabled(false);
            list.keys.filter().setEnabled(false);
            list.keys.clearFilter().setEnabled(false);
            if (!list.fetchingItems) {
                list.keys.cancelWhileFiltering().setEnabled(true);
            }
            list.keys
                .acceptWhileFiltering()
                .setEnabled(!list.filterInput.value().isEmpty());
            list.keys.quit().setEnabled(false);
            list.keys.showFullHelp().setEnabled(false);
            list.keys.closeFullHelp().setEnabled(false);
            return;
        }

        boolean hasItems = list.totalItems != 0;
        list.keys.cursorUp().setEnabled(hasItems);
        list.keys.cursorDown().setEnabled(hasItems);
        list.keys.goToStart().setEnabled(hasItems);
        list.keys.goToEnd().setEnabled(hasItems);
        list.keys.filter().setEnabled(list.filteringEnabled && hasItems);

        boolean hasPages = list.paginator.totalPages() > 1;
        list.keys.nextPage().setEnabled(hasPages);
        list.keys.prevPage().setEnabled(hasPages);
        list.keys
            .clearFilter()
            .setEnabled(list.filterState == FilterState.FilterApplied);
        list.keys.cancelWhileFiltering().setEnabled(false);
        list.keys.acceptWhileFiltering().setEnabled(false);
        list.keys.quit().setEnabled(!list.disableQuitKeybindings);

        if (list.help.showAll()) {
            list.keys.showFullHelp().setEnabled(true);
            list.keys.closeFullHelp().setEnabled(true);
            return;
        }

        boolean minHelp = countEnabledBindings(fullHelp(list)) > 1;
        list.keys.showFullHelp().setEnabled(minHelp);
        list.keys.closeFullHelp().setEnabled(minHelp);
    }

    private static int countEnabledBindings(Binding[][] groups) {
        int count = 0;
        for (Binding[] group : groups) {
            for (Binding binding : group) {
                if (binding.isEnabled()) {
                    count++;
                }
            }
        }
        return count;
    }

    static Binding[] shortHelp(List list) {
        java.util.List<Binding> kb = new LinkedList<>(
            Arrays.asList(list.keys.cursorUp(), list.keys.cursorDown())
        );

        boolean filtering = list.filterState == FilterState.Filtering;
        if (!filtering && list.itemDelegate instanceof KeyMap delegateKeyMap) {
            kb.addAll(Arrays.asList(delegateKeyMap.shortHelp()));
        }

        kb.addAll(
            Arrays.asList(
                list.keys.filter(),
                list.keys.clearFilter(),
                list.keys.acceptWhileFiltering(),
                list.keys.cancelWhileFiltering()
            )
        );

        if (!filtering) {
            kb.addAll(Arrays.asList(list.additionalShortHelpKeyMap.get()));
        }

        kb.addAll(Arrays.asList(list.keys.quit(), list.keys.showFullHelp()));
        return kb.toArray(new Binding[0]);
    }

    static Binding[][] fullHelp(List list) {
        java.util.List<Binding[]> kb = new LinkedList<>();
        kb.add(
            new Binding[] {
                list.keys.cursorUp(),
                list.keys.cursorDown(),
                list.keys.nextPage(),
                list.keys.prevPage(),
                list.keys.goToStart(),
                list.keys.goToEnd(),
            }
        );

        boolean filtering = list.filterState == FilterState.Filtering;
        if (!filtering && list.itemDelegate instanceof KeyMap delegateKeyMap) {
            kb.addAll(Arrays.asList(delegateKeyMap.fullHelp()));
        }

        java.util.List<Binding> listLevelBindings = new LinkedList<>(
            Arrays.asList(
                list.keys.filter(),
                list.keys.clearFilter(),
                list.keys.acceptWhileFiltering(),
                list.keys.cancelWhileFiltering()
            )
        );

        if (!filtering) {
            listLevelBindings.addAll(
                Arrays.asList(list.additionalFullHelpKeyMap.get())
            );
        }

        kb.add(listLevelBindings.toArray(new Binding[0]));
        kb.add(new Binding[] { list.keys.quit(), list.keys.closeFullHelp() });
        return kb.toArray(new Binding[0][]);
    }
}

