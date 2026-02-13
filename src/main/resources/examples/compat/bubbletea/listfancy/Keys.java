package examples.compat.bubbletea.listfancy;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;

/**
 * Defines key bindings for toggling list UI features.
 * Upstream: bubbletea/examples/list-fancy/main.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/Keys.java
 */
public class Keys {

    private Binding toggleSpinner;
    private Binding toggleTitleBar;
    private Binding toggleStatusBar;
    private Binding togglePagination;
    private Binding toggleHelpMenu;
    private Binding insertItem;

    /**
     * Initializes the list feature toggle bindings.
     */
    public Keys() {
        this.insertItem = new Binding(
                Binding.withKeys("a"),
                Binding.withHelp("a", "add item")
        );
        this.toggleSpinner = new Binding(
                Binding.withKeys("s"),
                Binding.withHelp("s", "toggle spinner")
        );
        this.toggleTitleBar = new Binding(
                Binding.withKeys("T"),
                Binding.withHelp("T", "toggle title")
        );
        this.toggleStatusBar = new Binding(
                Binding.withKeys("S"),
                Binding.withHelp("S", "toggle status")
        );
        this.togglePagination = new Binding(
                Binding.withKeys("P"),
                Binding.withHelp("P", "toggle pagination")
        );
        this.toggleHelpMenu = new Binding(
                Binding.withKeys("H"),
                Binding.withHelp("H", "toggle help")
        );
    }

    /**
     * Returns the binding for toggling the spinner.
     *
     * @return toggle spinner binding
     */
    public Binding toggleSpinner() {
        return toggleSpinner;
    }

    /**
     * Returns the binding for toggling the title bar.
     *
     * @return toggle title binding
     */
    public Binding toggleTitleBar() {
        return toggleTitleBar;
    }

    /**
     * Returns the binding for toggling the status bar.
     *
     * @return toggle status binding
     */
    public Binding toggleStatusBar() {
        return toggleStatusBar;
    }

    /**
     * Returns the binding for toggling pagination.
     *
     * @return toggle pagination binding
     */
    public Binding togglePagination() {
        return togglePagination;
    }

    /**
     * Returns the binding for toggling the help menu.
     *
     * @return toggle help binding
     */
    public Binding toggleHelpMenu() {
        return toggleHelpMenu;
    }

    /**
     * Returns the binding for adding a new item.
     *
     * @return insert binding
     */
    public Binding insertItem() {
        return insertItem;
    }
}
