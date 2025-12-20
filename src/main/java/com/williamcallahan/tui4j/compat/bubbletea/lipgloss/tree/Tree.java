package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Port of Lip Gloss tree.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
public class Tree implements Node {

    private String value;
    private boolean hidden;
    private int[] offset = new int[2];
    private Children children = new NodeChildren();
    private Renderer renderer;
    private boolean rendererSet;
    private ReentrantLock rendererLock = new ReentrantLock();

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    public Tree hide() {
        this.hidden = true;
        return this;
    }

    public Tree offset(int start, int end) {
        int newStart = start;
        int newEnd = end;

        if (newStart > newEnd) {
            int temp = newStart;
            newStart = newEnd;
            newEnd = temp;
        }

        if (newStart < 0) {
            newStart = 0;
        }
        if (newEnd < 0 || newEnd > children.length()) {
            newEnd = children.length();
        }

        offset[0] = newStart;
        offset[1] = newEnd;
        return this;
    }

    @Override
    public String value() {
        return this.value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public Tree child(Object... children) {
        for (Object child : children) {
            switch (child) {
                case null -> {
                }
                case Tree tree -> {
                    EnsureParentResult ensureParentResult = ensureParent(this.children, tree);
                    if (ensureParentResult.whatever() >= 0) {
                        this.children.remove(ensureParentResult.whatever());
                    }
                    this.children.append(ensureParentResult.tree());
                }
                case Children nestedChildren -> {
                    for (int i = 0; i < nestedChildren.length(); i++) {
                        this.children.append(nestedChildren.at(i));
                    }
                }
                case Node node -> this.children.append(node);
                case String string -> this.children.append(new Leaf(string));
                case Object[] objects -> {
                    return child(objects);
                }
                default -> this.children.append(new Leaf(child.toString()));
            }
        }
        return this;
    }

    private EnsureParentResult ensureParent(Children nodes, Tree item) {
        int nodesLength = nodes.length();
        if ((item.value() != null && !item.value().isEmpty()) || nodesLength == 0) {
            return new EnsureParentResult(item, -1);
        }

        int j = nodesLength - 1;
        Node parent = nodes.at(j);
        if (parent instanceof Tree tree) {
            for (int i = 0; i < item.children().length(); i++) {
                tree.child(item.children().at(i));
            }
            return new EnsureParentResult(tree, j);  // Return parent tree instead of null
        } else if (parent instanceof Leaf leaf) {
            item.setValue(leaf.value());
            return new EnsureParentResult(item, j);
        }
        return new EnsureParentResult(item, -1);
    }

    private Renderer ensureRenderer() {
        rendererLock.lock();
        try {
            if (this.rendererSet) {
                return this.renderer;
            }
            this.rendererSet = true;
            return this.renderer = new Renderer();
        } finally {
            rendererLock.unlock();
        }
    }

    public Tree enumeratorStyle(Style style) {
        ensureRenderer().style().setEnumeratorFunction((children, index) -> style);
        return this;
    }

    public Tree enumeratorStyleFunc(StyleFunction function) {
        StyleFunction fn = function;
        if (fn == null) {
            fn = (children, index) -> Style.newStyle();
        }
        ensureRenderer().style().setEnumeratorFunction(fn);
        return this;
    }

    public Tree rootStyle(Style style) {
        ensureRenderer().style().setRootStyle(style);
        return this;
    }

    public Tree itemStyle(Style style) {
        ensureRenderer().style().setItemFunction((children, index) -> style);
        return this;
    }

    public Tree itemStyleFunc(StyleFunction function) {
        StyleFunction fn = function;
        if (fn == null) {
            fn = (children, index) -> Style.newStyle();
        }
        ensureRenderer().style().setItemFunction(fn);
        return this;
    }

    public Tree enumerator(TreeEnumerator enumerator) {
        ensureRenderer().setEnumerator(enumerator);
        return this;
    }

    public Tree indenter(TreeIndenter indenter) {
        ensureRenderer().setIndenter(indenter);
        return this;
    }

    public static Tree withRoot(Object root) {
        Tree tree = new Tree();
        return tree.root(root);
    }

    public Tree root(Object root) {
        switch (root) {
            case Tree tree -> {
                this.value = tree.value();
                child(tree.children());
            }
            case Node node -> this.value = node.value();
            case String string -> this.value = string;
            case null, default -> this.value = "" + root;
        }
        return this;
    }

    @Override
    public Children children() {
        List<Node> children = new ArrayList<>();
        for (int i = offset[0]; i < this.children.length() - offset[1]; i++) {
            children.add(this.children.at(i));
        }
        return new NodeChildren(children);
    }


    Renderer renderer() {
        return renderer;
    }

    public String render() {
        return ensureRenderer().render(this, true, "");
    }

    private record EnsureParentResult(Tree tree, int whatever) {
    }
}
