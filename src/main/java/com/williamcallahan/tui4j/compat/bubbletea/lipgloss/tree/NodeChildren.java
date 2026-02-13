package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Deprecated in tui4j as of 0.3.0 because this is a compatibility shim for a relocated type; use {@link com.williamcallahan.tui4j.compat.lipgloss.tree.NodeChildren} instead.
 * This transitional shim is temporary and will be removed in an upcoming release.
 * <p>
 * Lip Gloss: tree/children.go.
 */
@Deprecated(since = "0.3.0")
public class NodeChildren extends com.williamcallahan.tui4j.compat.lipgloss.tree.NodeChildren implements Children {
    /**
     * Creates NodeChildren to keep this component ready for use.
     *
     * @param children children
     */
    @Deprecated(since = "0.3.0")
    public NodeChildren(List<Node> children) {
        super(toNewList(children));
    }

    /**
     * Creates NodeChildren to keep this component ready for use.
     */
    @Deprecated(since = "0.3.0")
    public NodeChildren() {
        super();
    }

    /**
     * Handles at for this component.
     *
     * @param index index
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public Node at(int index) {
        return (Node) super.at(index);
    }

    /**
     * Handles remove for this component.
     *
     * @param index index
     * @return result
     */
    @Deprecated(since = "0.3.0")
    @Override
    public Children remove(int index) {
        return (Children) super.remove(index);
    }

    /**
     * Handles append for this component.
     *
     * @param child child
     * @return result
     */
    @Deprecated(since = "0.3.0")
    public Children append(Node child) {
        return (Children) super.append(child);
    }

    /**
     * Handles to new list for this component.
     *
     * @param children children
     * @return result
     */
    private static List<com.williamcallahan.tui4j.compat.lipgloss.tree.Node> toNewList(List<Node> children) {
        List<com.williamcallahan.tui4j.compat.lipgloss.tree.Node> list = new ArrayList<>(children.size());
        for (Node child : children) {
            list.add(child);
        }
        return list;
    }
}
