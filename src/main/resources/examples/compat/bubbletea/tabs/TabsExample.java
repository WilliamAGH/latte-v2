package examples.compat.bubbletea.tabs;

import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.AdaptiveColor;
import com.williamcallahan.tui4j.compat.lipgloss.join.HorizontalJoinDecorator;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;

import java.util.List;

/**
 * Example program for Tabs.
 */
public class TabsExample implements Model {

    private final List<String> tabs;
    private final List<String> tabContent;
    private int activeTab;

    /**
     * Creates TabsExample to keep example ready for use.
     */
    public TabsExample() {
        this.tabs = List.of("Lip Gloss", "Blush", "Eye Shadow", "Mascara", "Foundation");
        this.tabContent = List.of(
                "Lip Gloss Tab",
                "Blush Tab",
                "Eye Shadow Tab",
                "Mascara Tab",
                "Foundation Tab"
        );
        this.activeTab = 0;
    }

    /**
     * Creates TabsExample to keep example ready for use.
     *
     * @param tabs tabs
     * @param tabContent tab content
     * @param activeTab active tab
     */
    private TabsExample(List<String> tabs, List<String> tabContent, int activeTab) {
        this.tabs = tabs;
        this.tabContent = tabContent;
        this.activeTab = activeTab;
    }

    /**
     * Supplies the initial command for the model.
     *
     * @return initial command
     */
    @Override
    public Command init() {
        return null;
    }

    /**
     * Applies an incoming message and returns the next model state.
     *
     * @param msg msg
     * @return next model state and command
     */
    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            switch (key) {
                case "ctrl+c", "q":
                    return new UpdateResult<>(this, QuitMessage::new);
                case "right", "l", "n", "tab":
                    int newActive = Math.min(activeTab + 1, tabs.size() - 1);
                    return new UpdateResult<>(new TabsExample(tabs, tabContent, newActive), null);
                case "left", "h", "p", "shift+tab":
                    int prevActive = Math.max(activeTab - 1, 0);
                    return new UpdateResult<>(new TabsExample(tabs, tabContent, prevActive), null);
            }
        }
        return new UpdateResult<>(this, null);
    }

    /**
     * Creates tab border for example.
     *
     * @param bottomLeft bottom left
     * @param bottom bottom
     * @param bottomRight bottom right
     * @return result
     */
    private Border createTabBorder(String bottomLeft, String bottom, String bottomRight) {
        Border border = StandardBorder.RoundedBorder;
        return new Border(
                border.top(),
                border.bottom(),
                border.left(),
                border.right(),
                border.topLeft(),
                border.topRight(),
                bottomLeft,
                bottomRight,
                border.middleLeft(),
                border.middleRight(),
                border.middle(),
                border.middleTop(),
                bottom
        );
    }

    /**
     * Creates tab style for example.
     *
     * @param border border
     * @return result
     */
    private Style createTabStyle(Border border) {
        AdaptiveColor highlightColor = new AdaptiveColor("#874BFD", "#7D56F4");
        return Style.newStyle()
                .border(border, true)
                .borderForeground(highlightColor)
                .padding(0, 1);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        Style docStyle = Style.newStyle()
                .padding(1, 2, 1, 2);

        AdaptiveColor highlightColor = new AdaptiveColor("#874BFD", "#7D56F4");

        Style windowStyle = Style.newStyle()
                .borderForeground(highlightColor)
                .padding(2, 0)
                .align(Position.Center)
                .borderTop(false)
                .border(StandardBorder.NormalBorder);

        StringBuilder doc = new StringBuilder();

        Border firstActiveBorder = createTabBorder("│", " ", "│");
        Border firstInactiveBorder = createTabBorder("├", "─", "┤");
        Border lastActiveBorder = createTabBorder("│", " ", "│");
        Border lastInactiveBorder = createTabBorder("├", "─", "┤");
        Border middleActiveBorder = createTabBorder("│", " ", "│");
        Border middleInactiveBorder = createTabBorder("│", "─", "│");

        String[] renderedTabs = new String[tabs.size()];
        for (int i = 0; i < tabs.size(); i++) {
            boolean isFirst = i == 0;
            boolean isLast = i == tabs.size() - 1;
            boolean isActive = i == activeTab;

            Border border;
            if (isFirst && isActive) {
                border = firstActiveBorder;
            } else if (isFirst && !isActive) {
                border = firstInactiveBorder;
            } else if (isLast && isActive) {
                border = lastActiveBorder;
            } else if (isLast && !isActive) {
                border = lastInactiveBorder;
            } else if (isActive) {
                border = middleActiveBorder;
            } else {
                border = middleInactiveBorder;
            }

            Style style = createTabStyle(border);
            renderedTabs[i] = style.render(tabs.get(i));
        }

        String row = HorizontalJoinDecorator.joinHorizontal(Position.Top, renderedTabs);
        doc.append(row).append("\n");

        int windowWidth = Math.max(
                0,
                measureWidth(row) - windowStyle.getHorizontalFrameSize()
        );
        String content = windowStyle.width(windowWidth).render(tabContent.get(activeTab));
        doc.append(content);

        return docStyle.render(doc.toString());
    }

    /**
     * Handles measure width for example.
     *
     * @param str str
     * @return result
     */
    private int measureWidth(String str) {
        String firstLine = str.split("\n")[0];
        return TextWidth.measureCellWidth(firstLine);
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TabsExample()).run();
    }
}
