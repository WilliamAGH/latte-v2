package com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list;

import org.assertj.core.api.Assertions;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.Tree;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.tree.TreeEnumerator;
import com.williamcallahan.tui4j.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.alphabet;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.arabic;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.asterisk;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.bullet;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.dash;
import static com.williamcallahan.tui4j.compat.bubbletea.lipgloss.list.ListEnumerator.roman;

/**
 * Tests list.
 * Bubble Tea: bubbletea/examples/list-fancy/main.go
 */
class ListTest {

    @BeforeEach
    void setUp() {
        // Set up no-color terminal info for consistent output
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
        // Explicitly set ASCII color profile to avoid pollution from other tests
        Renderer.defaultRenderer().setColorProfile(ColorProfile.Ascii);
    }

    @Test
    void test_List() {
        // given
        List list = new List()
                .item("Foo")
                .item("Bar")
                .item("Baz");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • Foo
                • Bar
                • Baz""");
    }

    @Test
    void test_ListItems() {
        // given
        List list = new List()
                .items("Foo", "Bar", "Baz");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • Foo
                • Bar
                • Baz""");
    }

    @Test
    void test_Sublist() {
        // given
        List list = new List()
                .item("Foo")
                .item("Bar")
                .item(new List("Hi", "Hello", "Halo").enumerator(roman()))
                .item("Qux");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • Foo
                • Bar
                    I. Hi
                   II. Hello
                  III. Halo
                • Qux""");
    }

    @Test
    void test_SublistItems() {
        // given
        List list = new List(
                "A",
                "B",
                "C",
                new List("D", "E", "F").enumerator(roman()),
                "G"
        );

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • A
                • B
                • C
                    I. D
                   II. E
                  III. F
                • G""");
    }

    @Test
    void test_ComplexSublist() {
        // given
        Style style1 = Style.newStyle().paddingRight(1);
        Style style2 = Style.newStyle().paddingRight(1);

        List list = new List()
                .item("Foo")
                .item("Bar")
                .item(new List("foo2", "bar2"))
                .item("Qux")
                .item(
                        new List("aaa", "bbb")
                                .enumeratorStyle(style1)
                                .enumerator(roman())
                )
                .item("Deep")
                .item(
                        new List()
                                .enumeratorStyle(style2)
                                .enumerator(alphabet())
                                .item("foo")
                                .item("Deeper")
                                .item(
                                        new List()
                                                .enumeratorStyle(style1)
                                                .enumerator(arabic())
                                                .item("a")
                                                .item("b")
                                                .item("Even Deeper, inherit parent renderer")
                                                .item(
                                                        new List()
                                                                .enumerator(asterisk())
                                                                .enumeratorStyle(style2)
                                                                .item("sus")
                                                                .item("d minor")
                                                                .item("f#")
                                                                .item("One ore level, with another renderer")
                                                                .item(
                                                                        new List()
                                                                                .enumeratorStyle(style1)
                                                                                .enumerator(dash())
                                                                                .item("a\nmultine\nstring")
                                                                                .item("hoccus poccus")
                                                                                .item("abra kadabra")
                                                                                .item("And finally, a tree within all this")
                                                                                .item(
                                                                                        new Tree()
                                                                                                .enumeratorStyle(style2)
                                                                                                .child("another\nmultine\nstring")
                                                                                                .child("something")
                                                                                                .child("a subtree")
                                                                                                .child(
                                                                                                        new Tree()
                                                                                                                .enumeratorStyle(style2)
                                                                                                                .child("yup")
                                                                                                                .child("many itens")
                                                                                                                .child("another")
                                                                                                )
                                                                                                .child("hallo")
                                                                                                .child("wunderbar!")
                                                                                )
                                                                                .item("this is a tree\nand other obvious statements")
                                                                )
                                                )
                                )
                                .item("bar")
                )
                .item("Baz");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered.stripIndent().stripTrailing()).isEqualTo("""
                • Foo
                • Bar
                  • foo2
                  • bar2
                • Qux
                   I. aaa
                  II. bbb
                • Deep
                  A. foo
                  B. Deeper
                    1. a
                    2. b
                    3. Even Deeper, inherit parent renderer
                      * sus
                      * d minor
                      * f#
                      * One ore level, with another renderer
                        - a
                          multine
                          string
                        - hoccus poccus
                        - abra kadabra
                        - And finally, a tree within all this
                          ├── another
                          │   multine
                          │   string
                          ├── something
                          ├── a subtree
                          │   ├── yup
                          │   ├── many itens
                          │   └── another
                          ├── hallo
                          └── wunderbar!
                        - this is a tree
                          and other obvious statements
                  C. bar
                • Baz""");
    }

    @Test
    void test_Multiline() {
        // given
        List list = new List()
                .item("Item1\nline 2\nline 3")
                .item("Item2\nline 2\nline 3")
                .item("3");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered.stripIndent().stripTrailing()).isEqualTo("""
                • Item1
                  line 2
                  line 3
                • Item2
                  line 2
                  line 3
                • 3""");
    }

    @Test
    void test_ListIntegers() {
        // given
        List list = new List()
                .item("1")
                .item("2")
                .item("3");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • 1
                • 2
                • 3""");
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test different enumerator styles")
    @MethodSource("enumeratorTestCases")
    void test_Enumerators(String name, TreeEnumerator enumerator, String expected) {
        // given
        List list = new List()
                .enumerator(enumerator)
                .item("Foo")
                .item("Bar")
                .item("Baz");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo(expected);
    }

    private static Stream<Arguments> enumeratorTestCases() {
        return Stream.of(
                Arguments.of(
                        "alphabet",
                        alphabet(),
                        """
                                A. Foo
                                B. Bar
                                C. Baz"""
                ),
                Arguments.of(
                        "arabic",
                        arabic(),
                        """
                                1. Foo
                                2. Bar
                                3. Baz"""
                ),
                Arguments.of(
                        "roman",
                        roman(),
                        """
                                  I. Foo
                                 II. Bar
                                III. Baz"""
                ),
                Arguments.of(
                        "bullet",
                        bullet(),
                        """
                                • Foo
                                • Bar
                                • Baz"""
                ),
                Arguments.of(
                        "asterisk",
                        asterisk(),
                        """
                                * Foo
                                * Bar
                                * Baz"""
                ),
                Arguments.of(
                        "dash",
                        dash(),
                        """
                                - Foo
                                - Bar
                                - Baz"""
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test enumerator transformations")
    @MethodSource("enumeratorTransformTestCases")
    void test_EnumeratorsTransform(String name, TreeEnumerator enumerator, Style style, String expected) {
        // given
        List list = new List()
                .enumeratorStyle(style)
                .enumerator(enumerator)
                .item("Foo")
                .item("Bar")
                .item("Baz");

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo(expected);
    }

    private static Stream<Arguments> enumeratorTransformTestCases() {
        return Stream.of(
                Arguments.of(
                        "alphabet lower",
                        alphabet(),
                        Style.newStyle()
                                .paddingRight(1)
                                .transform(String::toLowerCase),
                        """
                                a. Foo
                                b. Bar
                                c. Baz"""
                ),
                Arguments.of(
                        "arabic)",
                        arabic(),
                        Style.newStyle()
                                .paddingRight(1)
                                .transform(s -> s.replace(".", ")")),
                        """
                                1) Foo
                                2) Bar
                                3) Baz"""
                ),
                Arguments.of(
                        "roman within ()",
                        roman(),
                        Style.newStyle()
                                .transform(s -> "(" + s.toLowerCase().replace(".", "") + ") "),
                        """
                                  (i) Foo
                                 (ii) Bar
                                (iii) Baz"""
                ),
                Arguments.of(
                        "bullet is dash",
                        bullet(),
                        Style.newStyle()
                                .transform(s -> "- "),
                        """
                                - Foo
                                - Bar
                                - Baz"""
                )
        );
    }

    @ParameterizedTest
    @MethodSource("bulletTestCases")
    void test_Bullet(TreeEnumerator enumerator, int index, String expected) {
        // when
        String prefix = enumerator.enumerate(null, index);
        String bullet = prefix.replaceAll("\\.$", "");

        // then
        Assertions.assertThat(bullet).isEqualTo(expected);
    }

    private static Stream<Arguments> bulletTestCases() {
        return Stream.of(
                // Alphabet cases
                Arguments.of(alphabet(), 0, "A"),
                Arguments.of(alphabet(), 25, "Z"),
                Arguments.of(alphabet(), 26, "AA"),
                Arguments.of(alphabet(), 51, "AZ"),
                Arguments.of(alphabet(), 52, "BA"),
                Arguments.of(alphabet(), 79, "CB"),
                Arguments.of(alphabet(), 701, "ZZ"),
                Arguments.of(alphabet(), 702, "AAA"),
                Arguments.of(alphabet(), 801, "ADV"),
                Arguments.of(alphabet(), 1000, "ALM"),

                // Roman numeral cases
                Arguments.of(roman(), 0, "I"),
                Arguments.of(roman(), 25, "XXVI"),
                Arguments.of(roman(), 26, "XXVII"),
                Arguments.of(roman(), 50, "LI"),
                Arguments.of(roman(), 100, "CI"),
                Arguments.of(roman(), 701, "DCCII"),
                Arguments.of(roman(), 1000, "MI")
        );
    }

    @Test
    void test_EnumeratorsAlign() {
        // given
        List list = new List().enumerator(roman());

        // Create array of 100 "Foo" strings
        String[] fooArray = new String[100];
        Arrays.fill(fooArray, "Foo");

        // Build the list
        for (String foo : fooArray) {
            list.item(foo);
        }

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                       I. Foo
                      II. Foo
                     III. Foo
                      IV. Foo
                       V. Foo
                      VI. Foo
                     VII. Foo
                    VIII. Foo
                      IX. Foo
                       X. Foo
                      XI. Foo
                     XII. Foo
                    XIII. Foo
                     XIV. Foo
                      XV. Foo
                     XVI. Foo
                    XVII. Foo
                   XVIII. Foo
                     XIX. Foo
                      XX. Foo
                     XXI. Foo
                    XXII. Foo
                   XXIII. Foo
                    XXIV. Foo
                     XXV. Foo
                    XXVI. Foo
                   XXVII. Foo
                  XXVIII. Foo
                    XXIX. Foo
                     XXX. Foo
                    XXXI. Foo
                   XXXII. Foo
                  XXXIII. Foo
                   XXXIV. Foo
                    XXXV. Foo
                   XXXVI. Foo
                  XXXVII. Foo
                 XXXVIII. Foo
                   XXXIX. Foo
                      XL. Foo
                     XLI. Foo
                    XLII. Foo
                   XLIII. Foo
                    XLIV. Foo
                     XLV. Foo
                    XLVI. Foo
                   XLVII. Foo
                  XLVIII. Foo
                    XLIX. Foo
                       L. Foo
                      LI. Foo
                     LII. Foo
                    LIII. Foo
                     LIV. Foo
                      LV. Foo
                     LVI. Foo
                    LVII. Foo
                   LVIII. Foo
                     LIX. Foo
                      LX. Foo
                     LXI. Foo
                    LXII. Foo
                   LXIII. Foo
                    LXIV. Foo
                     LXV. Foo
                    LXVI. Foo
                   LXVII. Foo
                  LXVIII. Foo
                    LXIX. Foo
                     LXX. Foo
                    LXXI. Foo
                   LXXII. Foo
                  LXXIII. Foo
                   LXXIV. Foo
                    LXXV. Foo
                   LXXVI. Foo
                  LXXVII. Foo
                 LXXVIII. Foo
                   LXXIX. Foo
                    LXXX. Foo
                   LXXXI. Foo
                  LXXXII. Foo
                 LXXXIII. Foo
                  LXXXIV. Foo
                   LXXXV. Foo
                  LXXXVI. Foo
                 LXXXVII. Foo
                LXXXVIII. Foo
                  LXXXIX. Foo
                      XC. Foo
                     XCI. Foo
                    XCII. Foo
                   XCIII. Foo
                    XCIV. Foo
                     XCV. Foo
                    XCVI. Foo
                   XCVII. Foo
                  XCVIII. Foo
                    XCIX. Foo
                       C. Foo""");
    }

    @Test
    void test_SubListItems() {
        // given
        List list = new List().items(
                "S",
                new List().items("neovim", "vscode"),
                "HI",
                new List().items("vim", "doom emacs"),
                "Parent 2",
                new List().item("I like fuzzy socks")
        );

        // when
        String rendered = list.render();

        // then
        Assertions.assertThat(rendered).isEqualTo("""
                • S
                  • neovim
                  • vscode
                • HI
                  • vim
                  • doom emacs
                • Parent 2
                  • I like fuzzy socks""");
    }
}