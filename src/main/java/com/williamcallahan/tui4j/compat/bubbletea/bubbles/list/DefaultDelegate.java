package com.williamcallahan.tui4j.compat.bubbletea.bubbles.list;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.ansi.Truncate;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Runes;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

import java.util.LinkedList;

/**
 * Port of Bubbles default delegate.
 * Bubble Tea: bubbletea/examples/list-simple/main.go
 */
public class DefaultDelegate implements ItemDelegate, KeyMap {

    private boolean showDescription;
    private DefaultItemStyles styles;
    private UpdateFunction updateFunction;
    private ShortHelpFunc shortHelpFunc;
    private FullHelpFunc fullHelpFunc;
    private int height;
    private int spacing;

    public DefaultDelegate() {
        this.showDescription = true;
        this.styles = new DefaultItemStyles();
        this.height = 2;
        this.spacing = 1;
    }

    @Override
    public void render(StringBuilder output, List list, int index, FilteredItem filteredItem) {
        String title = "";
        String desc = "";
        int[] matchedRunes = filteredItem.matches();

        Item item = filteredItem.item();

        if (item instanceof DefaultItem defaultItem) {
            title = defaultItem.title();
            desc = defaultItem.description();
        } else {
            return;
        }

        if (list.width() <= 0) {
            return;
        }

        int textWidth = list.width() - styles.normalTitle().getHorizontalPadding();
        title = Truncate.truncate(title, textWidth, DefaultItemStyles.ELLIPSIS);

        if (showDescription) {
            java.util.List<String> lines = new LinkedList<>();
            String[] descLines = desc.split("\n", 0);
            for (int i = 0; i < descLines.length; i++) {
                if (i > height - 1) {
                    break;
                }
                lines.add(Truncate.truncate(descLines[i], textWidth, DefaultItemStyles.ELLIPSIS));
            }
            desc = String.join("\n", lines);
        }

        boolean isSelected = index == list.index();
        boolean emptyFilter = list.filterState() == FilterState.Filtering && list.filterValue().isEmpty();
        boolean isFiltered = list.filterState() == FilterState.Filtering || list.filterState() == FilterState.FilterApplied;

        if (emptyFilter) {
            title = styles.dimmedTitle().render(title);
            desc = styles.dimmedDesc().render(desc);
        } else if (isSelected && list.filterState() != FilterState.Filtering) {
            if (isFiltered) {
                Style unmatched = styles.selectedTitle().copy().inline(true);
                Style matched = unmatched.copy().inherit(styles.filterMatch());
                title = Runes.styleRunes(title, matchedRunes, matched, unmatched);
            }
            title = styles.selectedTitle().render(title);
            desc = styles.selectedDesc().render(desc);
        } else {
            if (isFiltered) {
                Style unmatched = styles.normalTitle().copy().inline(true);
                Style matched = unmatched.copy().inherit(styles.filterMatch());
                title = Runes.styleRunes(title, matchedRunes, matched, unmatched);
            }
            title = styles.normalTitle().render(title);
            desc = styles.normalDesc().render(desc);
        }

        if (showDescription) {
            output.append("%s\n%s".formatted(title, desc));
            return;
        }
        output.append(title);
    }

    @Override
    public Command update(Message msg, List listModel) {
        if (updateFunction == null) {
            return null;
        }
        return updateFunction.update(msg, listModel);
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int spacing() {
        return spacing;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void onUpdate(UpdateFunction updateFunction) {
        this.updateFunction = updateFunction;
    }

    public void setShortHelpFunc(ShortHelpFunc shortHelpFunc) {
        this.shortHelpFunc = shortHelpFunc;
    }

    public void setFullHelpFunc(FullHelpFunc fullHelpFunc) {
        this.fullHelpFunc = fullHelpFunc;
    }

    @Override
    public Binding[] shortHelp() {
        if (shortHelpFunc != null) {
            return shortHelpFunc.get();
        }
        return new Binding[0];
    }

    @Override
    public Binding[][] fullHelp() {
        if (fullHelpFunc != null) {
            return fullHelpFunc.get();
        }
        return new Binding[0][];
    }
}