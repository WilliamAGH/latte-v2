package com.williamcallahan.tui4j.compat.lipgloss.table;

import static org.assertj.core.api.Assertions.assertThat;

import com.williamcallahan.tui4j.compat.lipgloss.Borders;
import com.williamcallahan.tui4j.compat.lipgloss.Join;
import com.williamcallahan.tui4j.compat.lipgloss.Position;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Size;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.Border;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Port of github.com/charmbracelet/lipgloss/table/table_test.go.
 */
class TableTest {

    private static final StyleFunc TABLE_STYLE = (row, col) -> {
        if (row == Table.HEADER_ROW) {
            return Style.newStyle().padding(0, 1).align(Position.Center);
        }
        return Style.newStyle().padding(0, 1);
    };

    private static final String LONG_ROW_DESCRIPTION =
        "A command to be executed inside the container to assess its health. Each space delimited token of " +
        "the command is a separate array element. Commands exiting 0 are considered to be successful " +
        "probes, whilst all other exit codes are considered failures.";

    private static final String LONG_HEADER_QUESTION =
        "Why are you going on this trip? Is it a hot or cold climate?";

    private static final String LONG_TEXT_EN =
        "Lorem ipsum dolor sit amet, regione detracto eos an. Has ei quidam hendrerit intellegebat, id tamquam " +
        "iudicabit necessitatibus ius, at errem officiis hendrerit mei. Exerci noster at has, sit id tota " +
        "convenire, vel ex rebum inciderint liberavisse. Quaeque delectus corrumpit cu cum.";

    private static final String LONG_TEXT_JP = """
        耐許ヱヨカハ調出あゆ監件び理別よン國給災レホチ権輝モエフ会割もフ響3現エツ文時しだびほ経機ムイメフ敗文ヨク現義なさド請情ゆじょて憶主管州けでふく。排ゃわつげ美刊ヱミ出見ツ南者オ抜豆ハトロネ論索モネニイ任償スヲ話破リヤヨ秒止口イセソス止央のさ食周健でてつだ官送ト読聴遊容ひるべ。際ぐドらづ市居ネムヤ研校35岩6繹ごわク報拐イ革深52球ゃレスご究東スラ衝3間ラ録占たス。
        禁にンご忘康ざほぎル騰般ねど事超スんいう真表何カモ自浩ヲシミ図客線るふ静王ぱーま写村月掛焼詐面ぞゃ。昇強ごントほ価保キ族85岡モテ恋困ひりこな刊並せご出来ぼぎむう点目ヲウ止環公ニレ事応タス必書タメムノ当84無信升ちひょ。価ーぐ中客テサ告覧ヨトハ極整ラ得95稿はかラせ江利ス宏丸霊ミ考整ス静将ず業巨職ノラホ収嗅ざな。""";

    private static final String LONG_TEXT_AR =
        "شيء قد للحكومة والكوري الأوروبيّون, بوابة تعديل واعتلاء ضرب بـ. إذ أسر اتّجة اعلان, ٣٠ اكتوبر " +
        "العصبة استمرار ومن. أفاق للسيطرة التاريخ، مع بحث, كلّ اتّجة القوى مع. فبعد ايطاليا، تم " +
        "حتى, لكل تم جسيمة الإحتفاظ وباستثناء, عل فرنسا وانتهاءً الإقتصادية عرض. ونتج دأبوا " +
        "إحكام بال إذ. لغات عملية وتم مع, وصل بداية وبغطاء البرية بل, أي قررت بلاده فكانت حدى";

    private static final String LONG_TEXT_KO =
        "각급 선거관리위원회의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 임시회의 회기는 30일을 " +
        "초과할 수 없다. 국가는 여자의 복지와 권익의 향상을 위하여 노력하여야 한다. " +
        "국군의 조직과 편성은 법률로 정한다.";

    private static final String LONG_TEXT_KANJI =
        "版応道潟部中幕爆営報門案名見壌府。博健必権次覧編仕断青場内凄新東深簿代供供。守聞書神秀同浜東波恋闘秀。未格打好作器来利阪持西焦朝三女。権幽問季負娘購合旧資健載員式活陸。未倍校朝遺続術吉迎暮広知角亡志不説空住。法省当死年勝絡聞方北投健。室分性山天態意画詳知浅方裁。変激伝阜中野品省載嗅闘額端反。中必台際造事寄民経能前作臓";

    @BeforeEach
    void setUp() {
        Renderer.defaultRenderer().setColorProfile(ColorProfile.Ascii);
    }

    @Test
    void testTable() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .row("French", "Bonjour", "Salut")
            .row("Japanese", "こんにちは", "やあ")
            .row("Russian", "Zdravstvuyte", "Privet")
            .row("Spanish", "Hola", "¿Qué tal?");

        assertGolden("TestTable", table.render());
    }

    @Test
    void testTableExample() {
        Style headerStyle = Style.newStyle()
            .padding(0, 1)
            .align(Position.Center);
        Style evenRowStyle = Style.newStyle().padding(0, 1);
        Style oddRowStyle = Style.newStyle().padding(0, 1);

        String[][] rows = new String[][] {
            { "Chinese", "您好", "你好" },
            { "Japanese", "こんにちは", "やあ" },
            { "Russian", "Здравствуйте", "Привет" },
            { "Spanish", "Hola", "¿Qué tal?" },
        };

        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderStyle(Style.newStyle().foreground(Color.color("99")))
            .styleFunc((row, col) -> {
                if (row == Table.HEADER_ROW) {
                    return headerStyle;
                }
                if (row % 2 == 0) {
                    return evenRowStyle;
                }
                return oddRowStyle;
            })
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(rows);

        table.row(
            "English",
            "You look absolutely fabulous.",
            "How's it going?"
        );

        assertGolden("TestTableExample", table.render());
    }

    @Test
    void testTableEmpty() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL");

        assertGolden("TestTableEmpty", table.render());
    }

    @Test
    void testTableNoStyleFunc() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(null)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .row("French", "Bonjour", "Salut")
            .row("Japanese", "こんにちは", "やあ")
            .row("Russian", "Zdravstvuyte", "Privet")
            .row("Spanish", "Hola", "¿Qué tal?");

        assertGolden("TestTableNoStyleFunc", table.render());
    }

    @Test
    void testTableMarginAndRightAlignment() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc((row, col) ->
                Style.newStyle().margin(0, 1).align(Position.Right)
            )
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Arabic", "أهلين", "أهلا")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .row("French", "Bonjour", "Salut")
            .row("Japanese", "こんにちは", "やあ")
            .row("Russian", "Zdravstvuyte", "Privet")
            .row("Spanish", "Hola", "¿Qué tal?");

        assertGolden("TestTableMarginAndRightAlignment", table.render());
    }

    @Test
    void testTableOffset() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .row("French", "Bonjour", "Salut")
            .row("Japanese", "こんにちは", "やあ")
            .row("Russian", "Zdravstvuyte", "Privet")
            .row("Spanish", "Hola", "¿Qué tal?")
            .offset(1);

        assertGolden("TestTableOffset", table.render());
    }

    @Test
    void testTableBorder() {
        Table table = Table.create()
            .border(Borders.doubleBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableBorder", table.render());
    }

    @Test
    void testTableSetRows() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableSetRows", table.render());
    }

    @Test
    void testMoreCellsThanHeaders() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL")
            .rows(languageRows());

        assertGolden("TestMoreCellsThanHeaders", table.render());
    }

    @Test
    void testMoreCellsThanHeadersExtra() {
        String[][] rows = new String[][] {
            { "Chinese", "Nǐn hǎo", "Nǐ hǎo" },
            { "French", "Bonjour", "Salut", "Salut" },
            { "Japanese", "こんにちは", "やあ" },
            { "Russian", "Zdravstvuyte", "Privet", "Privet", "Privet" },
            { "Spanish", "Hola", "¿Qué tal?" },
        };
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL")
            .rows(rows);

        assertGolden("TestMoreCellsThanHeadersExtra", table.render());
    }

    @Test
    void testTableNoHeaders() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .rows(languageRows());

        assertGolden("TestTableNoHeaders", table.render());
    }

    @Test
    void testTableNoColumnSeparators() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderColumn(false)
            .styleFunc(TABLE_STYLE)
            .rows(languageRows());

        assertGolden("TestTableNoColumnSeparators", table.render());
    }

    @Test
    void testTableNoColumnSeparatorsWithHeaders() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderColumn(false)
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableNoColumnSeparatorsWithHeaders", table.render());
    }

    @Test
    void testBorderColumnsWithExtraRows() {
        String[][] rows = new String[][] {
            { "Chinese", "Nǐn hǎo", "Nǐ hǎo" },
            { "French", "Bonjour", "Salut", "Salut" },
            { "Japanese", "こんにちは", "やあ" },
            { "Russian", "Zdravstvuyte", "Privet", "Privet", "Privet" },
            { "Spanish", "Hola", "¿Qué tal?" },
        };

        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderColumn(false)
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL")
            .rows(rows);

        assertGolden("TestBorderColumnsWithExtraRows", table.render());
    }

    @Test
    void testNew() {
        Table table = Table.create();
        assertThat(table).isNotNull();
    }

    @Test
    void testTableUnsetBorders() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderTop(false)
            .borderBottom(false)
            .borderLeft(false)
            .borderRight(false)
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableUnsetBorders", table.render());
    }

    @Test
    void testTableUnsetHeaderSeparator() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows())
            .borderHeader(false)
            .borderTop(false)
            .borderBottom(false)
            .borderLeft(false)
            .borderRight(false);

        assertGolden("TestTableUnsetHeaderSeparator", table.render());
    }

    @Test
    void testTableUnsetHeaderSeparatorWithBorder() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .borderHeader(false)
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableUnsetHeaderSeparatorWithBorder", table.render());
    }

    @Test
    void testTableRowSeparators() {
        String[][] rows = languageRows();

        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .borderRow(true)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(rows);

        assertGolden("TestTableRowSeparators/no_overflow", table.render());

        Table withOverflow = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .borderRow(true)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(rows)
            .height(8);

        assertGolden(
            "TestTableRowSeparators/with_overflow",
            withOverflow.render()
        );
    }

    @Test
    void testTableHeights() {
        StyleFunc styleFunc = (row, col) -> {
            if (row == Table.HEADER_ROW) {
                return Style.newStyle().padding(0, 1);
            }
            if (col == 0) {
                return Style.newStyle().width(18).padding(1);
            }
            return Style.newStyle().width(25).padding(1, 2);
        };

        String[][] rows = new String[][] {
            {
                "Chutar o balde",
                "Literally translates to \"kick the bucket.\" It's used when someone gives up or loses patience.",
            },
            {
                "Engolir sapos",
                "Literally means \"to swallow frogs.\" It's used to describe someone who has to tolerate or endure unpleasant situations.",
            },
            {
                "Arroz de festa",
                "Literally means \"party rice.\" It´s used to refer to someone who shows up everywhere.",
            },
        };

        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(styleFunc)
            .headers("EXPRESSION", "MEANING")
            .rows(rows);

        assertGolden("TestTableHeights", table.render());
    }

    @Test
    void testTableMultiLineRowSeparator() {
        StyleFunc styleFunc = (row, col) -> {
            if (row == Table.HEADER_ROW) {
                return Style.newStyle().padding(0, 1);
            }
            if (col == 0) {
                return Style.newStyle().width(18).padding(1);
            }
            return Style.newStyle().width(25).padding(1, 2);
        };

        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(styleFunc)
            .headers("EXPRESSION", "MEANING")
            .borderRow(true)
            .row(
                "Chutar o balde",
                "Literally translates to \"kick the bucket.\" It's used when someone gives up or loses patience."
            )
            .row(
                "Engolir sapos",
                "Literally means \"to swallow frogs.\" It's used to describe someone who has to tolerate or endure unpleasant situations."
            )
            .row(
                "Arroz de festa",
                "Literally means \"party rice.\" It´s used to refer to someone who shows up everywhere."
            );

        assertGolden("TestTableMultiLineRowSeparator", table.render());
    }

    @Test
    void testTableWidthExpand() {
        Table table = Table.create()
            .width(80)
            .styleFunc(TABLE_STYLE)
            .border(Borders.normalBorder())
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertThat(Size.width(table.render())).isEqualTo(80);
        assertGolden("TestTableWidthExpand", table.render());
    }

    @Test
    void testTableWidthShrink() {
        Table table = Table.create()
            .width(30)
            .styleFunc(TABLE_STYLE)
            .border(Borders.normalBorder())
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableWidthShrink", table.render());
    }

    @Test
    void testTableWidthSmartCrop() {
        String[][] rows = new String[][] {
            { "Kini", "40", "New York" },
            { "Eli", "30", "London" },
            { "Iris", "20", "Paris" },
        };
        Table table = Table.create()
            .width(25)
            .styleFunc(TABLE_STYLE)
            .border(Borders.normalBorder())
            .headers("Name", "Age of Person", "Location")
            .rows(rows);

        assertGolden("TestTableWidthSmartCrop", table.render());
    }

    @Test
    void testTableWidthSmartCropExtensive() {
        String[][] rows = new String[][] {
            { "Chinese", "您好", "你好" },
            { "Japanese", "こんにちは", "やあ" },
            { "Arabic", "أهلين", "أهلا" },
            { "Russian", "Здравствуйте", "Привет" },
            { "Spanish", "Hola", "¿Qué tal?" },
            { "English", "You look absolutely fabulous.", "How's it going?" },
        };
        Table table = Table.create()
            .width(18)
            .styleFunc(TABLE_STYLE)
            .border(Borders.thickBorder())
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .wrap(false)
            .rows(rows);

        assertGolden("TestTableWidthSmartCropExtensive", table.render());
    }

    @Test
    void testTableWidthSmartCropTiny() {
        String[][] rows = new String[][] {
            { "Chinese", "您好", "你好" },
            { "Japanese", "こんにちは", "やあ" },
            { "Russian", "Здравствуйте", "Привет" },
            { "Spanish", "Hola", "¿Qué tal?" },
            { "English", "You look absolutely fabulous.", "How's it going?" },
        };
        Table table = Table.create()
            .width(1)
            .styleFunc(TABLE_STYLE)
            .border(Borders.normalBorder())
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(rows);

        assertGolden("TestTableWidthSmartCropTiny", table.render());
    }

    @Test
    void testTableWidths() {
        Table table = Table.create()
            .width(30)
            .styleFunc(TABLE_STYLE)
            .borderLeft(false)
            .borderRight(false)
            .border(Borders.normalBorder())
            .borderColumn(false)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableWidths", table.render());
    }

    @Test
    void testTableWidthShrinkNoBorders() {
        Table table = Table.create()
            .width(30)
            .styleFunc(TABLE_STYLE)
            .borderLeft(false)
            .borderRight(false)
            .border(Borders.normalBorder())
            .borderColumn(false)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows());

        assertGolden("TestTableWidthShrinkNoBorders", table.render());
    }

    @Test
    void testFilter() {
        StringData data = new StringData()
            .item("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .item("French", "Bonjour", "Salut")
            .item("Japanese", "こんにちは", "やあ")
            .item("Russian", "Zdravstvuyte", "Privet")
            .item("Spanish", "Hola", "¿Qué tal?");

        Filter filter = new Filter(data).filter(row ->
            !data.at(row, 0).equals("French")
        );

        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .data(filter);

        assertGolden("TestFilter", table.render());
    }

    @Test
    void testFilterInverse() {
        StringData data = new StringData()
            .item("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .item("French", "Bonjour", "Salut")
            .item("Japanese", "こんにちは", "やあ")
            .item("Russian", "Zdravstvuyte", "Privet")
            .item("Spanish", "Hola", "¿Qué tal?");

        Filter filter = new Filter(data).filter(row ->
            data.at(row, 0).equals("French")
        );

        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .data(filter);

        assertGolden("TestFilterInverse", table.render());
    }

    @Test
    void testTableANSI() {
        String code =
            "\u001B[31mC\u001B[0m\u001B[32mo\u001B[0m\u001B[34md\u001B[0m\u001B[33me\u001B[0m";
        String[][] rows = new String[][] {
            { "Apple", "Red", "\u001B[31m31\u001B[0m" },
            { "Lime", "Green", "\u001B[32m32\u001B[0m" },
            { "Banana", "Yellow", "\u001B[33m33\u001B[0m" },
            { "Blueberry", "Blue", "\u001B[34m34\u001B[0m" },
        };
        Table table = Table.create()
            .width(29)
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("Fruit", "Color", code)
            .rows(rows);

        assertGolden("TestTableANSI", table.render());
    }

    @Test
    void testTableHeightExact() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows())
            .height(9);

        assertGolden("TestTableHeightExact", table.render());
    }

    @Test
    void testTableHeightExtra() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows())
            .height(100);

        assertGolden("TestTableHeightExtra", table.render());
    }

    @Test
    void testTableHeightShrink() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows())
            .height(8);

        assertGolden("TestTableHeightShrink", table.render());
    }

    @Test
    void testTableHeightMinimum() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("ID", "LANGUAGE", "FORMAL", "INFORMAL")
            .row("1", "Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .row("2", "French", "Bonjour", "Salut")
            .row("3", "Japanese", "こんにちは", "やあ")
            .row("4", "Russian", "Zdravstvuyte", "Privet")
            .row("5", "Spanish", "Hola", "¿Qué tal?")
            .height(0);

        assertGolden("TestTableHeightMinimum", table.render());
    }

    @Test
    void testTableHeightMinimumShowData() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo")
            .height(0);

        assertGolden("TestTableHeightMinimumShowData", table.render());
    }

    @Test
    void testTableHeightWithOffset() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .styleFunc(TABLE_STYLE)
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .rows(languageRows())
            .height(8)
            .offset(1);

        assertGolden("TestTableHeightWithOffset", table.render());
    }

    @Test
    void testStyleFunc() {
        Renderer.defaultRenderer().setColorProfile(ColorProfile.TrueColor);
        Object[][] tests = new Object[][] {
            {
                "RightAlignedTextWithMargins",
                (StyleFunc) (row, col) -> {
                    if (row == Table.HEADER_ROW) {
                        return Style.newStyle().align(Position.Center);
                    }
                    return Style.newStyle().margin(0, 1).align(Position.Right);
                },
            },
            {
                "MarginAndPaddingSet",
                (StyleFunc) (row, col) -> {
                    if (row == Table.HEADER_ROW) {
                        return Style.newStyle().align(Position.Center);
                    }
                    return Style.newStyle()
                        .padding(1)
                        .margin(1)
                        .align(Position.Right)
                        .background(Color.color("#874bfc"));
                },
            },
        };

        for (Object[] test : tests) {
            String name = (String) test[0];
            StyleFunc styleFunc = (StyleFunc) test[1];

            Table table = Table.create()
                .border(Borders.normalBorder())
                .headers("LANGUAGE", "FORMAL", "INFORMAL")
                .styleFunc(styleFunc)
                .rows(languageRows());

            assertGolden("TestStyleFunc/" + name, table.render());
        }
    }

    @Test
    void testClearRows() {
        Table table = Table.create()
            .border(Borders.normalBorder())
            .headers("LANGUAGE", "FORMAL", "INFORMAL")
            .row("Chinese", "Nǐn hǎo", "Nǐ hǎo");

        table.clearRows();
        table.row("French", "Bonjour", "Salut");
        String rendered = table.render();
        assertThat(rendered).contains("French");
    }

    @Test
    void testContentWrapping() {
        Object[][] tests = new Object[][] {
            {
                "LongRowContent",
                new String[] {
                    "Name",
                    "Description",
                    "Type",
                    "Required",
                    "Default",
                },
                new String[][] {
                    { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" },
                },
                true,
                TABLE_STYLE,
            },
            {
                "LongRowContentNoWrap",
                new String[] {
                    "Name",
                    "Description",
                    "Type",
                    "Required",
                    "Default",
                },
                new String[][] {
                    { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" },
                },
                false,
                TABLE_STYLE,
            },
            {
                "LongRowContentNoWrapNoMargins",
                new String[] {
                    "Name",
                    "Description",
                    "Type",
                    "Required",
                    "Default",
                },
                new String[][] {
                    { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" },
                },
                false,
                (StyleFunc) (row, col) -> {
                    if (row == Table.HEADER_ROW) {
                        return Style.newStyle()
                            .padding(0)
                            .align(Position.Center);
                    }
                    return Style.newStyle().padding(0);
                },
            },
            {
                "LongRowContentNoWrapCustomMargins",
                new String[] {
                    "Name",
                    "Description",
                    "Type",
                    "Required",
                    "Default",
                },
                new String[][] {
                    { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" },
                },
                false,
                (StyleFunc) (row, col) -> {
                    if (row == Table.HEADER_ROW) {
                        return Style.newStyle()
                            .padding(0, 2)
                            .align(Position.Center);
                    }
                    return Style.newStyle().padding(0, 2);
                },
            },
            {
                "MissingRowContent",
                new String[] {
                    "Name",
                    "Description",
                    "Type",
                    "Required",
                    "Default",
                },
                new String[][] {
                    { "command", LONG_ROW_DESCRIPTION, "yes", "", "" },
                },
                true,
                TABLE_STYLE,
            },
            {
                "LongHeaderContentLongAndShortRows",
                new String[] {
                    "Destination",
                    LONG_HEADER_QUESTION,
                    "Affordability",
                },
                new String[][] {
                    {
                        "Mexico",
                        "I want to go somewhere hot, dry, and affordable. Mexico has really good food, just don't drink tap water!",
                        "$",
                    },
                    {
                        "New York",
                        "I'm thinking about going during the Christmas season to check out Rockefeller center. Might be cold though...",
                        "$$$",
                    },
                    { "California", "", "$$$" },
                },
                true,
                TABLE_STYLE,
            },
            {
                "LongTextDifferentLanguages",
                new String[] { "Hello", "你好", "مرحبًا", "안녕하세요" },
                new String[][] {
                    {
                        LONG_TEXT_EN,
                        LONG_TEXT_JP,
                        LONG_TEXT_AR,
                        LONG_TEXT_KANJI,
                        LONG_TEXT_KO,
                    },
                },
                true,
                TABLE_STYLE,
            },
        };

        for (Object[] test : tests) {
            String name = (String) test[0];
            String[] headers = (String[]) test[1];
            String[][] data = (String[][]) test[2];
            boolean wrap = (boolean) test[3];
            StyleFunc styleFunc = (StyleFunc) test[4];

            Table table = Table.create()
                .headers(headers)
                .rows(data)
                .width(80)
                .styleFunc(styleFunc)
                .wrap(wrap);

            assertGolden("TestContentWrapping/" + name, table.render());
        }
    }

    @Test
    void testContentWrappingWithPadding() {
        String[][] headers = new String[][] {
            { "Name", "Description", "Type", "Required", "Default" },
            { "Name", "Description", "Type", "Required", "Default" },
            { "Destination", LONG_HEADER_QUESTION, "Affordability" },
            { "Hello", "你好", "مرحبًا", "안녕하세요" },
        };
        String[][][] data = new String[][][] {
            { { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" } },
            { { "command", LONG_ROW_DESCRIPTION, "yes", "", "" } },
            {
                {
                    "Mexico",
                    "I want to go somewhere hot, dry, and affordable. Mexico has really good food, just don't drink tap water!",
                    "$",
                },
                {
                    "New York",
                    "I'm thinking about going during the Christmas season to check out Rockefeller center. Might be cold though...",
                    "$$$",
                },
                { "California", "", "$$$" },
            },
            {
                {
                    "",
                    LONG_TEXT_JP,
                    LONG_TEXT_AR,
                    LONG_TEXT_KANJI,
                    LONG_TEXT_KO,
                },
            },
        };
        String[] names = new String[] {
            "LongRowContent",
            "MissingRowContent",
            "LongHeaderContentLongAndShortRows",
            "LongTextDifferentLanguages",
        };

        int defaultWidth = 80;
        for (int i = 0; i < names.length; i++) {
            Table table = Table.create()
                .headers(headers[i])
                .rows(data[i])
                .styleFunc((row, col) -> Style.newStyle().padding(0, 1))
                .width(defaultWidth);

            assertThat(Size.width(table.render())).isEqualTo(defaultWidth);
            assertGolden(
                "TestContentWrapping_WithPadding/" + names[i],
                table.render()
            );
        }
    }

    @Test
    void testContentWrappingWithMargins() {
        String[] names = new String[] {
            "LongRowContent",
            "MissingRowContent",
            "LongHeaderContentLongAndShortRows",
            "LongTextDifferentLanguages",
        };

        String[][] headers = new String[][] {
            { "Name", "Description", "Type", "Required", "Default" },
            { "Name", "Description", "Type", "Required", "Default" },
            { "Destination", LONG_HEADER_QUESTION, "Affordability" },
            { "Hello", "你好", "مرحبًا", "안녕하세요" },
        };

        String[][][] data = new String[][][] {
            { { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" } },
            { { "command", LONG_ROW_DESCRIPTION, "yes", "", "" } },
            {
                {
                    "Mexico",
                    "I want to go somewhere hot, dry, and affordable. Mexico has really good food, just don't drink tap water!",
                    "$",
                },
                {
                    "New York",
                    "I'm thinking about going during the Christmas season to check out Rockefeller center. Might be cold though...",
                    "$$$",
                },
                { "California", "", "$$$" },
            },
            {
                {
                    LONG_TEXT_EN,
                    LONG_TEXT_JP,
                    LONG_TEXT_AR,
                    LONG_TEXT_KANJI,
                    LONG_TEXT_KO,
                },
            },
        };

        for (int i = 0; i < names.length; i++) {
            Table table = Table.create()
                .headers(headers[i])
                .rows(data[i])
                .styleFunc((row, col) -> Style.newStyle().margin(0, 4))
                .width(80);

            assertGolden(
                "TestContentWrapping_WithMargins/" + names[i],
                table.render()
            );
        }
    }

    @Test
    void testContentWrappingColumnWidth() {
        String[] names = new String[] {
            "LongRowContent",
            "MissingRowContent",
            "LongHeaderContentLongAndShortRows",
            "LongTextDifferentLanguages",
        };

        String[][] headers = new String[][] {
            { "Name", "Description", "Type", "Required", "Default" },
            { "Name", "Description", "Type", "Required", "Default" },
            { "Destination", LONG_HEADER_QUESTION, "Affordability" },
            { "Hello", "你好", "مرحبًا", "안녕하세요" },
        };

        String[][][] data = new String[][][] {
            { { "command", LONG_ROW_DESCRIPTION, "yes", "hello", "yep" } },
            { { "command", LONG_ROW_DESCRIPTION, "yes", "", "" } },
            {
                {
                    "Mexico",
                    "I want to go somewhere hot, dry, and affordable. Mexico has really good food, just don't drink tap water!",
                    "$",
                },
                {
                    "New York",
                    "I'm thinking about going during the Christmas season to check out Rockefeller center. Might be cold though...",
                    "$$$",
                },
                { "California", "", "$$$" },
            },
            {
                {
                    LONG_TEXT_EN,
                    LONG_TEXT_JP,
                    LONG_TEXT_AR,
                    LONG_TEXT_KANJI,
                    LONG_TEXT_KO,
                },
            },
        };

        int defaultWidth = 80;
        for (int i = 0; i < names.length; i++) {
            Table table = Table.create()
                .headers(headers[i])
                .rows(data[i])
                .styleFunc((row, col) -> {
                    if (row == 0 && col == 1) {
                        return Style.newStyle().width(30);
                    }
                    if (col == 2) {
                        return Style.newStyle().width(5);
                    }
                    return Style.newStyle();
                })
                .width(defaultWidth);

            assertThat(Size.width(table.render())).isEqualTo(defaultWidth);

            int[] widths = getIntArray(table, "widths");
            assertThat(widths[2]).isEqualTo(5);
            assertThat(widths[1]).isEqualTo(30);

            assertGolden(
                "TestContentWrapping_ColumnWidth/" + names[i],
                table.render()
            );
        }
    }

    @Test
    void testTableOverFlowNoWrap() {
        String[] headers = new String[] {
            "Hello",
            "你好",
            "مرحبًا",
            "안녕하세요",
        };
        String[][] data = new String[][] {
            {
                LONG_TEXT_EN,
                LONG_TEXT_JP,
                LONG_TEXT_AR,
                LONG_TEXT_KO,
                LONG_TEXT_KANJI,
            },
            { "Welcome", "いらっしゃいませ", "مرحباً", "환영", "欢迎" },
            { "Goodbye", "さようなら", "مع السلامة", "안녕히 가세요", "再见" },
        };

        int tableHeight = 6;
        Table table = Table.create()
            .headers(headers)
            .rows(data)
            .styleFunc(TABLE_STYLE)
            .height(tableHeight)
            .width(80)
            .wrap(false);

        assertThat(Size.height(table.render())).isEqualTo(tableHeight);
        assertGolden("TestTableOverFlowNoWrap", table.render());
    }

    @Test
    void testCarriageReturn() {
        String[][] data = new String[][] {
            { "a0", "b0", "c0", "d0" },
            {
                "a1",
                "b1.0\r\nb1.1\r\nb1.2\r\nb1.3\r\nb1.4\r\nb1.5\r\nb1.6",
                "c1",
                "d1",
            },
            { "a2", "b2", "c2", "d2" },
            { "a3", "b3", "c3", "d3" },
        };

        Table table = Table.create().rows(data).border(Borders.normalBorder());

        assertGolden("TestCarriageReturn", table.render());
    }

    @Test
    void testTableShrinkWithOffset() {
        Table table = Table.create().rows(bigCityRows()).offset(80).height(45);

        int height = getIntField(table, "height");
        int offset = getIntField(table, "offset");
        int renderedHeight = Size.height(table.render());
        assertThat(renderedHeight)
            .as("height with offset %d", offset)
            .isEqualTo(height);
    }

    @Test
    void testBorderStyles() {
        String[][] rows = languageRows();

        Object[][] tests = new Object[][] {
            { "NormalBorder", Borders.normalBorder(), true },
            { "RoundedBorder", Borders.roundedBorder(), true },
            { "BlockBorder", Borders.blockBorder(), true },
            { "ThickBorder", Borders.thickBorder(), true },
            { "HiddenBorder", Borders.hiddenBorder(), true },
            { "MarkdownBorder", Borders.markdownBorder(), false },
            { "ASCIIBorder", Borders.asciiBorder(), true },
        };

        for (Object[] test : tests) {
            String name = (String) test[0];
            Border border = (Border) test[1];
            boolean topBottomBorders = (boolean) test[2];

            Table table = Table.create()
                .border(border)
                .styleFunc(TABLE_STYLE)
                .headers("LANGUAGE", "FORMAL", "INFORMAL")
                .rows(rows)
                .borderTop(topBottomBorders)
                .borderBottom(topBottomBorders);

            assertGolden("TestBorderStyles/" + name, table.render());
        }
    }

    @Test
    void testWrapPreStyledContent() {
        Renderer.defaultRenderer().setColorProfile(ColorProfile.TrueColor);
        String[] headers = new String[] { "Package", "Version", "Link" };
        String[][] data = new String[][] {
            {
                "sourcegit",
                "0.19",
                Join.joinHorizontal(
                    Position.Left,
                    Style.newStyle()
                        .foreground(Color.color("#31BB71"))
                        .render(
                            "https://aur.archlinux.org/packages/sourcegit-bin"
                        )
                ),
            },
            {},
            { "Welcome", "いらっしゃいませ", "مرحباً", "환영", "欢迎" },
            { "Goodbye", "さようなら", "مع السلامة", "안녕히 가세요", "再见" },
        };
        Table table = Table.create()
            .headers(headers)
            .rows(data)
            .width(80)
            .wrap(true);

        assertGolden("TestWrapPreStyledContent", table.render());
    }

    @Test
    void testWrapStyleFuncContent() {
        Renderer.defaultRenderer().setColorProfile(ColorProfile.TrueColor);
        String[] headers = new String[] { "Package", "Version", "Link" };
        String[][] data = new String[][] {
            {
                "sourcegit",
                "0.19",
                "https://aur.archlinux.org/packages/sourcegit-bin",
            },
            { "Welcome", "いらっしゃいませ", "مرحباً" },
            { "Goodbye", "さようなら", "مع السلامة" },
        };
        Table table = Table.create()
            .headers(headers)
            .rows(data)
            .styleFunc((row, col) -> {
                if (row == Table.HEADER_ROW) {
                    return Style.newStyle();
                }
                if (data[row][col].contains("https://")) {
                    return Style.newStyle().foreground(Color.color("#31BB71"));
                }
                return Style.newStyle();
            })
            .width(60)
            .wrap(true);

        assertGolden("TestWrapStyleFuncContent", table.render());
    }

    private static String[][] languageRows() {
        return new String[][] {
            { "Chinese", "Nǐn hǎo", "Nǐ hǎo" },
            { "French", "Bonjour", "Salut" },
            { "Japanese", "こんにちは", "やあ" },
            { "Russian", "Zdravstvuyte", "Privet" },
            { "Spanish", "Hola", "¿Qué tal?" },
        };
    }

    private static String[][] bigCityRows() {
        return new String[][] {
            { "1", "Tokyo", "Japan", "37,274,000" },
            { "2", "Delhi", "India", "32,065,760" },
            { "3", "Shanghai", "China", "28,516,904" },
            { "4", "Dhaka", "Bangladesh", "22,478,116" },
            { "5", "São Paulo", "Brazil", "22,429,800" },
            { "6", "Mexico City", "Mexico", "22,085,140" },
            { "7", "Cairo", "Egypt", "21,750,020" },
            { "8", "Beijing", "China", "21,333,332" },
            { "9", "Mumbai", "India", "20,961,472" },
            { "10", "Osaka", "Japan", "19,059,856" },
            { "11", "Chongqing", "China", "16,874,740" },
            { "12", "Karachi", "Pakistan", "16,839,950" },
            { "13", "Istanbul", "Turkey", "15,636,243" },
            { "14", "Kinshasa", "DR Congo", "15,628,085" },
            { "15", "Lagos", "Nigeria", "15,387,639" },
            { "16", "Buenos Aires", "Argentina", "15,369,919" },
            { "17", "Kolkata", "India", "15,133,888" },
            { "18", "Manila", "Philippines", "14,406,059" },
            { "19", "Tianjin", "China", "14,011,828" },
            { "20", "Guangzhou", "China", "13,964,637" },
            { "21", "Rio De Janeiro", "Brazil", "13,634,274" },
            { "22", "Lahore", "Pakistan", "13,541,764" },
            { "23", "Bangalore", "India", "13,193,035" },
            { "24", "Shenzhen", "China", "12,831,330" },
            { "25", "Moscow", "Russia", "12,640,818" },
            { "26", "Chennai", "India", "11,503,293" },
            { "27", "Bogota", "Colombia", "11,344,312" },
            { "28", "Paris", "France", "11,142,303" },
            { "29", "Jakarta", "Indonesia", "11,074,811" },
            { "30", "Lima", "Peru", "11,044,607" },
            { "31", "Bangkok", "Thailand", "10,899,698" },
            { "32", "Hyderabad", "India", "10,534,418" },
            { "33", "Seoul", "South Korea", "9,975,709" },
            { "34", "Nagoya", "Japan", "9,571,596" },
            { "35", "London", "United Kingdom", "9,540,576" },
            { "36", "Chengdu", "China", "9,478,521" },
            { "37", "Nanjing", "China", "9,429,381" },
            { "38", "Tehran", "Iran", "9,381,546" },
            { "39", "Ho Chi Minh City", "Vietnam", "9,077,158" },
            { "40", "Luanda", "Angola", "8,952,496" },
            { "41", "Wuhan", "China", "8,591,611" },
            { "42", "Xi An Shaanxi", "China", "8,537,646" },
            { "43", "Ahmedabad", "India", "8,450,228" },
            { "44", "Kuala Lumpur", "Malaysia", "8,419,566" },
            { "45", "New York City", "United States", "8,177,020" },
            { "46", "Hangzhou", "China", "8,044,878" },
            { "47", "Surat", "India", "7,784,276" },
            { "48", "Suzhou", "China", "7,764,499" },
            { "49", "Hong Kong", "Hong Kong", "7,643,256" },
            { "50", "Riyadh", "Saudi Arabia", "7,538,200" },
            { "51", "Shenyang", "China", "7,527,975" },
            { "52", "Baghdad", "Iraq", "7,511,920" },
            { "53", "Dongguan", "China", "7,511,851" },
            { "54", "Foshan", "China", "7,497,263" },
            { "55", "Dar Es Salaam", "Tanzania", "7,404,689" },
            { "56", "Pune", "India", "6,987,077" },
            { "57", "Santiago", "Chile", "6,856,939" },
            { "58", "Madrid", "Spain", "6,713,557" },
            { "59", "Haerbin", "China", "6,665,951" },
            { "60", "Toronto", "Canada", "6,312,974" },
            { "61", "Belo Horizonte", "Brazil", "6,194,292" },
            { "62", "Khartoum", "Sudan", "6,160,327" },
            { "63", "Johannesburg", "South Africa", "6,065,354" },
            { "64", "Singapore", "Singapore", "6,039,577" },
            { "65", "Dalian", "China", "5,930,140" },
            { "66", "Qingdao", "China", "5,865,232" },
            { "67", "Zhengzhou", "China", "5,690,312" },
            { "68", "Ji Nan Shandong", "China", "5,663,015" },
            { "69", "Barcelona", "Spain", "5,658,472" },
            { "70", "Saint Petersburg", "Russia", "5,535,556" },
            { "71", "Abidjan", "Ivory Coast", "5,515,790" },
            { "72", "Yangon", "Myanmar", "5,514,454" },
            { "73", "Fukuoka", "Japan", "5,502,591" },
            { "74", "Alexandria", "Egypt", "5,483,605" },
            { "75", "Guadalajara", "Mexico", "5,339,583" },
            { "76", "Ankara", "Turkey", "5,309,690" },
            { "77", "Chittagong", "Bangladesh", "5,252,842" },
            { "78", "Addis Ababa", "Ethiopia", "5,227,794" },
            { "79", "Melbourne", "Australia", "5,150,766" },
            { "80", "Nairobi", "Kenya", "5,118,844" },
            { "81", "Hanoi", "Vietnam", "5,067,352" },
            { "82", "Sydney", "Australia", "5,056,571" },
            { "83", "Monterrey", "Mexico", "5,036,535" },
            { "84", "Changsha", "China", "4,809,887" },
            { "85", "Brasilia", "Brazil", "4,803,877" },
            { "86", "Cape Town", "South Africa", "4,800,954" },
            { "87", "Jiddah", "Saudi Arabia", "4,780,740" },
            { "88", "Urumqi", "China", "4,710,203" },
            { "89", "Kunming", "China", "4,657,381" },
            { "90", "Changchun", "China", "4,616,002" },
            { "91", "Hefei", "China", "4,496,456" },
            { "92", "Shantou", "China", "4,490,411" },
            { "93", "Xinbei", "Taiwan", "4,470,672" },
            { "94", "Kabul", "Afghanistan", "4,457,882" },
            { "95", "Ningbo", "China", "4,405,292" },
            { "96", "Tel Aviv", "Israel", "4,343,584" },
            { "97", "Yaounde", "Cameroon", "4,336,670" },
            { "98", "Rome", "Italy", "4,297,877" },
            { "99", "Shijiazhuang", "China", "4,285,135" },
            { "100", "Montreal", "Canada", "4,276,526" },
        };
    }

    private static void assertGolden(String name, String actual) {
        assertGoldenPath("lipgloss/table/testdata/" + name + ".golden", actual);
    }

    private static void assertGoldenPath(String path, String actual) {
        String expected = readResource(path);
        assertThat(actual).isEqualTo(expected);
    }

    private static String readResource(String path) {
        try (
            InputStream input =
                TableTest.class.getClassLoader().getResourceAsStream(path)
        ) {
            if (input == null) {
                throw new IllegalStateException("Missing resource: " + path);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to read resource: " + path,
                e
            );
        }
    }

    private static int[] getIntArray(Table table, String fieldName) {
        try {
            java.lang.reflect.Field field = Table.class.getDeclaredField(
                fieldName
            );
            field.setAccessible(true);
            return (int[]) field.get(table);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "Failed to read field: " + fieldName,
                e
            );
        }
    }

    private static int getIntField(Table table, String fieldName) {
        try {
            java.lang.reflect.Field field = Table.class.getDeclaredField(
                fieldName
            );
            field.setAccessible(true);
            return (int) field.get(table);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "Failed to read field: " + fieldName,
                e
            );
        }
    }
}
