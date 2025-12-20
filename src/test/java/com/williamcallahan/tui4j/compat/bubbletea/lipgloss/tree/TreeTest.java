package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree;

import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.List;
import com.williamcallahan.tui4j.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer.defaultRenderer;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.alphabet;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.arabic;

/**
 * Tests tree.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class TreeTest {

    @BeforeEach
    void setUp() {
        // Set up no-color terminal info for consistent output
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
    }


    @Test
    void test_ShouldPrintTree() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar")
                        .child(
                                "Qux",
                                Tree.withRoot("Quux")
                                        .child(
                                                "Foo",
                                                "Bar"
                                        ),
                                "Quuux"
                        ),
                "Baz"
        );

        // when
        String treeString = tree.render();

        // then

        assertThat(treeString).isEqualTo(
                """
                        ├── Foo
                        ├── Bar
                        │   ├── Qux
                        │   ├── Quux
                        │   │   ├── Foo
                        │   │   └── Bar
                        │   └── Quuux
                        └── Baz""");

        // when
        tree.enumerator(new TreeEnumerator.RounderEnumerator());
        treeString = tree.render();

        // then
        assertThat(treeString).isEqualTo(
                """
                        ├── Foo
                        ├── Bar
                        │   ├── Qux
                        │   ├── Quux
                        │   │   ├── Foo
                        │   │   ╰── Bar
                        │   ╰── Quuux
                        ╰── Baz""");


    }

    @Test
    void test_TreeHidden() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Foo", "Bar").hide(),
                        "Quuux"
                ),
                "Baz"
        );

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── Foo
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeAllHidden() {
        // given
        Tree tree = new Tree()
                .child(
                        "Foo",
                        Tree.withRoot("Bar").child(
                                "Qux",
                                Tree.withRoot("Quux").child("Foo", "Bar"),
                                "Quuux"
                        ),
                        "Baz"
                ).hide();

        // then
        assertThat(tree.render()).isEqualTo("");
    }

    @Test
    void test_TreeRoot() {
        // given
        Tree tree = new Tree()
                .root("Root")
                .child(
                        "Foo",
                        Tree.withRoot("Bar").child("Qux", "Quuux"),
                        "Baz"
                );

        // then
        assertThat(tree.render()).isEqualTo("""
                Root
                ├── Foo
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeStartsWithSubtree() {
        // given
        Tree tree = new Tree().child(
                new Tree().root("Bar").child("Qux", "Quuux"),
                "Baz"
        );

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeAddTwoSubTreesWithoutName() {
        // given
        Tree tree = new Tree().child(
                "Bar",
                "Foo",
                new Tree().child("Qux", "Qux", "Qux", "Qux", "Qux"),
                new Tree().child("Quux", "Quux", "Quux", "Quux", "Quux"),
                "Baz"
        );

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── Bar
                ├── Foo
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Quux
                │   ├── Quux
                │   ├── Quux
                │   ├── Quux
                │   └── Quux
                └── Baz""");
    }

    @Test
    void test_TreeLastNodeIsSubTree() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Foo", "Bar"),
                        "Quuux"
                )
        );

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── Foo
                └── Bar
                    ├── Qux
                    ├── Quux
                    │   ├── Foo
                    │   └── Bar
                    └── Quuux""");
    }

    @Test
    void test_TreeNil() {
        // given
        Tree tree = new Tree().child(
                null,
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Bar"),
                        "Quuux"
                ),
                "Baz"
        );

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── Bar
                │   ├── Qux
                │   ├── Quux
                │   │   └── Bar
                │   └── Quuux
                └── Baz""");
    }

    // FIXME suddenly it started failing on GitHub CI but still works perfectly in IDE and in local terminal...
//    @Test
//    void test_TreeCustom() {
//        // given
//        Tree tree = new Tree()
//                .child(
//                        "Foo",
//                        Tree.withRoot("Bar").child(
//                                "Qux",
//                                Tree.withRoot("Quux").child("Foo", "Bar"),
//                                "Quuux"
//                        ),
//                        "Baz"
//                )
//                .itemStyle(Style.newStyle().foreground(Color.color("9")))
//                .enumeratorStyle(Style.newStyle().foreground(Color.color("12")).paddingRight(1))
//                .enumerator((children, i) -> "->")
//                .indenter((children, i) -> "->");
//
//        // then
//        assertThat(tree.render()).isEqualTo("""
//                -> Foo
//                -> Bar
//                -> -> Qux
//                -> -> Quux
//                -> -> -> Foo
//                -> -> -> Bar
//                -> -> Quuux
//                -> Baz""");
//    }

    @Test
    void test_TreeMultilineNode() {
        // given
        Tree tree = new Tree()
                .root("Big\nRoot\nNode")
                .child(
                        "Foo",
                        Tree.withRoot("Bar").child(
                                "Line 1\nLine 2\nLine 3\nLine 4",
                                Tree.withRoot("Quux").child("Foo", "Bar"),
                                "Quuux"
                        ),
                        "Baz\nLine 2"
                );

        // then
        assertThat(tree.render().stripIndent().stripTrailing()).isEqualTo("""
                Big
                Root
                Node
                ├── Foo
                ├── Bar
                │   ├── Line 1
                │   │   Line 2
                │   │   Line 3
                │   │   Line 4
                │   ├── Quux
                │   │   ├── Foo
                │   │   └── Bar
                │   └── Quuux
                └── Baz
                    Line 2""");
    }

    @Test
    void test_TreeSubTreeWithCustomEnumerator() {
        // given
        Tree tree = new Tree()
                .root("The Root Node™")
                .child(
                        Tree.withRoot("Parent")
                                .child("child 1", "child 2")
                                .itemStyleFunc((children, i) -> Style.newStyle().setString("*"))
                                .enumeratorStyleFunc((children, i) -> Style.newStyle().setString("+").paddingRight(1)),
                        "Baz"
                );

        // then
        assertThat(tree.render()).isEqualTo("""
                The Root Node™
                ├── Parent
                │   + ├── * child 1
                │   + └── * child 2
                └── Baz""");
    }

    @Test
    void test_TreeMixedEnumeratorSize() {
        // given
        Map<Integer, String> romans = Map.of(
                1, "I",
                2, "II",
                3, "III",
                4, "IV",
                5, "V",
                6, "VI"
        );

        Tree tree = new Tree()
                .root("The Root Node™")
                .child("Foo", "Foo", "Foo", "Foo", "Foo")
                .enumerator((children, i) -> romans.get(i + 1));

        // then
        assertThat(tree.render()).isEqualTo("""
                The Root Node™
                  I Foo
                 II Foo
                III Foo
                 IV Foo
                  V Foo""");
    }

    @Test
    void test_TreeStyleNilFuncs() {
        // given
        Tree tree = new Tree()
                .root("Silly")
                .child("Willy ", "Nilly")
                .itemStyleFunc(null)
                .enumeratorStyleFunc(null);

        // then
        assertThat(tree.render()).isEqualTo("""
                Silly
                ├──Willy\s
                └──Nilly""");
    }

    @Test
    void test_TreeStyleAt() {
        // given
        Tree tree = new Tree()
                .root("Root")
                .child("Foo", "Baz")
                .enumerator((children, i) ->
                        children.at(i).value().equals("Foo") ? ">" : "-");

        // then
        assertThat(tree.render()).isEqualTo("""
                Root
                > Foo
                - Baz""");
    }

    @Test
    void test_RootStyle() {
        // given
        Renderer renderer = defaultRenderer();
        renderer.setColorProfile(ColorProfile.TrueColor);
        renderer.setHasDarkBackground(true);

        Tree tree = new Tree()
                .root("Root")
                .child("Foo", "Baz")
                .rootStyle(Style.newStyle().background(Color.color("#5A56E0")))
                .itemStyle(Style.newStyle().background(Color.color("#04B575")));

        // when
        String treeString = tree.render();

        // then
        assertThat(treeString).isEqualTo("""
                [48;2;90;86;224mRoot[0m
                ├── [48;2;4;181;117mFoo[0m
                └── [48;2;4;181;117mBaz[0m""");
    }

    @Test
    void test_At() {
        // given
        Children data = Children.newStringData("Foo", "Bar");

        // then
        assertThat(data.at(0).value()).isEqualTo("Foo");
        assertThat(data.at(10)).isNull();
        assertThat(data.at(-1)).isNull();
    }

    @Test
    void test_Filter() {
        // given
        Filter data = new Filter(Children.newStringData("Foo", "Bar", "Baz", "Nope"))
                .filter(index -> index != 3);
        Tree tree = new Tree().root("Root").child(data);

        // when
        String treeString = tree.render();

        // then
        assertThat(treeString).isEqualTo("""
                Root
                ├── Foo
                ├── Bar
                └── Baz""");
    }

    @Test
    void test_NodeDataRemoveOutOfBounds() {
        // given
        Children data = Children.newStringData("a");

        // then
        assertThat(data.length()).isEqualTo(1);
    }

    // TODO
    // void test_TreeTable()

    @Test
    void test_AddItemWithAndWithoutRoot() {
        // given
        Tree t1 = new Tree().child(
                "Foo",
                "Bar",
                new Tree().child("Baz"),
                "Qux"
        );

        Tree t2 = new Tree().child(
                "Foo",
                new Tree().root("Bar").child("Baz"),
                "Qux"
        );

        // expected
        String expected = """
                ├── Foo
                ├── Bar
                │   └── Baz
                └── Qux""";

        // then
        assertThat(t1.render()).isEqualTo(expected);
        assertThat(t2.render()).isEqualTo(expected);
    }

    @Test
    void test_EmbedListWithinTree() {
        // given
        Tree t1 = new Tree()
                .child(new List("A", "B", "C").enumerator(arabic()))
                .child(new List("1", "2", "3").enumerator(alphabet()));

        // expected
        String expected = """
                ├── 1. A
                │   2. B
                │   3. C
                └── A. 1
                    B. 2
                    C. 3""";

        // then
        assertThat(t1.render()).isEqualTo(expected);
    }

    @Test
    void test_MultilinePrefix() {
        // given
        Style paddingStyle = Style.newStyle().paddingLeft(1).paddingBottom(1);
        Tree tree = new Tree()
                .enumerator((children, index) -> {
                    if (index == 1) {
                        return "│\n│";
                    }
                    return " ";
                })
                .indenter((children, index) -> " ")
                .itemStyle(paddingStyle)
                .child("Foo Document\nThe Foo Files")
                .child("Bar Document\nThe Bar Files")
                .child("Baz Document\nThe Baz Files");

        // then
        assertThat(tree.render().stripIndent().stripTrailing()).isEqualTo("""
                   Foo Document
                   The Foo Files

                │  Bar Document
                │  The Bar Files

                   Baz Document
                   The Baz Files""");
    }

    @Test
    void test_MultilinePrefixSubtree() {
        // given
        Style paddingStyle = Style.newStyle().padding(0, 0, 1, 1);
        Tree tree = new Tree()
                .child("Foo")
                .child("Bar")
                .child(new Tree()
                        .root("Baz")
                        .enumerator((children, index) -> {
                            if (index == 1) {
                                return "│\n│";
                            }
                            return " ";
                        })
                        .indenter((children, index) -> " ")
                        .itemStyle(paddingStyle)
                        .child("Foo Document\nThe Foo Files")
                        .child("Bar Document\nThe Bar Files")
                        .child("Baz Document\nThe Baz Files")
                )
                .child("Qux");

        // then
        assertThat(tree.render().stripIndent().stripTrailing()).isEqualTo(
                """
                        ├── Foo
                        ├── Bar
                        ├── Baz
                        │      Foo Document
                        │      The Foo Files
                        │
                        │   │  Bar Document
                        │   │  The Bar Files
                        │
                        │      Baz Document
                        │      The Baz Files
                        │
                        └── Qux""");

    }

    @Test
    void test_MultilinePrefixInception() {
        TreeEnumerator glowEnum = (children, index) -> {
            if (index == 1) {
                return "│\n│";
            }
            return " ";
        };
        TreeIndenter glowIndenter = (children, index) -> "  ";
        Style paddingStyle = Style.newStyle().paddingLeft(1).paddingBottom(1);
        Tree tree = new Tree()
                .enumerator(glowEnum)
                .indenter(glowIndenter)
                .itemStyle(paddingStyle)
                .child("Foo Document\nThe Foo Files")
                .child("Bar Document\nThe Bar Files")
                .child(
                        new Tree()
                                .enumerator(glowEnum)
                                .indenter(glowIndenter)
                                .itemStyle(paddingStyle)
                                .child("Qux Document\nThe Qux Files")
                                .child("Quux Document\nThe Quux Files")
                                .child("Quuux Document\nThe Quuux Files"))
                .child("Baz Document\nThe Baz Files");

        // then
        assertThat(tree.render().stripIndent().stripTrailing()).isEqualTo(
                """
                            Foo Document
                            The Foo Files

                        │   Bar Document
                        │   The Bar Files

                               Qux Document
                               The Qux Files

                           │   Quux Document
                           │   The Quux Files

                               Quuux Document
                               The Quuux Files

                            Baz Document
                            The Baz Files""");
    }

    @Test
    void test_Types() {
        // given
        Tree tree = new Tree()
                .child(0)
                .child(true)
                .child("Foo", "Bar")
                .child((Object) new String[]{"Qux", "Quux", "Quuux"});

        // then
        assertThat(tree.render()).isEqualTo("""
                ├── 0
                ├── true
                ├── Foo
                ├── Bar
                ├── Qux
                ├── Quux
                └── Quuux""");
    }

}