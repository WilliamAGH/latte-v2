package examples.compat.bubbletea.tableresize;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.StyleFunc;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;

/**
 * Example program for Tableresize.
 */
public class TableResizeExample implements Model {

    private Table table;

    /**
     * Applies row and column styling for the table based on header, selection, and type colors.
     */
    private static final class TableRowStyleSelector implements StyleFunc {
        private final Style baseStyle;
        private final Style headerStyle;
        private final Style selectedStyle;
        private final String[][] rows;
        private final String[][] typeColors;
        private final String[][] dimTypeColors;

        /**
         * Creates a style selector for table rows and columns.
         *
         * @param baseStyle base style for table rows
         * @param headerStyle style for the header row
         * @param selectedStyle style for the highlighted row
         * @param rows data rows backing the table
         * @param typeColors colors for type columns
         * @param dimTypeColors dimmed colors for type columns
         */
        private TableRowStyleSelector(
                Style baseStyle,
                Style headerStyle,
                Style selectedStyle,
                String[][] rows,
                String[][] typeColors,
                String[][] dimTypeColors
        ) {
            this.baseStyle = baseStyle;
            this.headerStyle = headerStyle;
            this.selectedStyle = selectedStyle;
            this.rows = rows;
            this.typeColors = typeColors;
            this.dimTypeColors = dimTypeColors;
        }

        /**
         * Chooses a cell style based on header, selection, and type-specific colors.
         *
         * @param row row index
         * @param col column index
         * @return style for the cell
         */
        @Override
        public Style apply(int row, int col) {
            if (row == 0) {
                return headerStyle;
            }

            int rowIndex = row - 1;
            if (rowIndex < 0 || rowIndex >= rows.length) {
                return baseStyle;
            }

            if ("Pikachu".equals(rows[rowIndex][1])) {
                return selectedStyle;
            }

            boolean even = row % 2 == 0;

            if (col == 2 || col == 3) {
                String[][] colors = even ? dimTypeColors : typeColors;
                if (col >= rows[rowIndex].length) {
                    return baseStyle;
                }

                String type = rows[rowIndex][col];
                for (String[] pair : colors) {
                    if (pair[0].equals(type)) {
                        return baseStyle.foreground(Color.color(pair[1]));
                    }
                }
                return baseStyle;
            }

            if (even) {
                return baseStyle.foreground(Color.color("245"));
            }
            return baseStyle.foreground(Color.color("252"));
        }
    }

    /**
     * Creates TableResizeExample to keep example ready for use.
     */
    public TableResizeExample() {
        Renderer re = Renderer.defaultRenderer();
        Style baseStyle = re.newStyle().paddingLeft(1);
        Style headerStyle = baseStyle.foreground(Color.color("252")).bold(true);
        Style selectedStyle = baseStyle.foreground(Color.color("#01BE85")).background(Color.color("#00432F"));

        String[][] typeColors = {
                {"Bug", "#D7FF87"},
                {"Electric", "#FDFF90"},
                {"Fire", "#FF7698"},
                {"Flying", "#FF87D7"},
                {"Grass", "#75FBAB"},
                {"Ground", "#FF875F"},
                {"Normal", "#929292"},
                {"Poison", "#7D5AFC"},
                {"Water", "#00E2C7"}
        };

        String[][] dimTypeColors = {
                {"Bug", "#97AD64"},
                {"Electric", "#FCFF5F"},
                {"Fire", "#BA5F75"},
                {"Flying", "#C97AB2"},
                {"Grass", "#59B980"},
                {"Ground", "#C77252"},
                {"Normal", "#727272"},
                {"Poison", "#634BD0"},
                {"Water", "#439F8E"}
        };

        String[] headers = {"#", "NAME", "TYPE 1", "TYPE 2", "JAPANESE", "OFFICIAL ROM."};

        String[][] rows = {
                {"1", "Bulbasaur", "Grass", "Poison", "フシギダネ", "Bulbasaur"},
                {"2", "Ivysaur", "Grass", "Poison", "フシギソウ", "Ivysaur"},
                {"3", "Venusaur", "Grass", "Poison", "フシギバナ", "Venusaur"},
                {"4", "Charmander", "Fire", "", "ヒトカゲ", "Hitokage"},
                {"5", "Charmeleon", "Fire", "", "リザード", "Lizardo"},
                {"6", "Charizard", "Fire", "Flying", "リザードン", "Lizardon"},
                {"7", "Squirtle", "Water", "", "ゼニガメ", "Zenigame"},
                {"8", "Wartortle", "Water", "", "カメール", "Kameil"},
                {"9", "Blastoise", "Water", "", "カメックス", "Kamex"},
                {"10", "Caterpie", "Bug", "", "キャタピー", "Caterpie"},
                {"11", "Metapod", "Bug", "", "トランセル", "Trancell"},
                {"12", "Butterfree", "Bug", "Flying", "バタフリー", "Butterfree"},
                {"13", "Weedle", "Bug", "Poison", "ビードル", "Beedle"},
                {"14", "Kakuna", "Bug", "Poison", "コクーン", "Cocoon"},
                {"15", "Beedrill", "Bug", "Poison", "スピアー", "Spear"},
                {"16", "Pidgey", "Normal", "Flying", "ポッポ", "Poppo"},
                {"17", "Pidgeotto", "Normal", "Flying", "ピジョン", "Pigeon"},
                {"18", "Pidgeot", "Normal", "Flying", "ピジョット", "Pigeot"},
                {"19", "Rattata", "Normal", "", "コラッタ", "Koratta"},
                {"20", "Raticate", "Normal", "", "ラッタ", "Ratta"},
                {"21", "Spearow", "Normal", "Flying", "オニスズメ", "Onisuzume"},
                {"22", "Fearow", "Normal", "Flying", "オニドリル", "Onidrill"},
                {"23", "Ekans", "Poison", "", "アーボ", "Arbo"},
                {"24", "Arbok", "Poison", "", "アーボック", "Arbok"},
                {"25", "Pikachu", "Electric", "", "ピカチュウ", "Pikachu"},
                {"26", "Raichu", "Electric", "", "ライチュウ", "Raichu"},
                {"27", "Sandshrew", "Ground", "", "サンド", "Sand"},
                {"28", "Sandslash", "Ground", "", "サンドパン", "Sandpan"}
        };

        this.table = Table.create()
                .headers(headers)
                .rows(rows)
                .border(StandardBorder.NormalBorder)
                .borderStyle(re.newStyle().foreground(Color.color("238")))
                .styleFunc(new TableRowStyleSelector(baseStyle, headerStyle, selectedStyle, rows, typeColors, dimTypeColors))
                .border(StandardBorder.ThickBorder);
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
    public UpdateResult<? extends Model> update(com.williamcallahan.tui4j.compat.bubbletea.Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMsg) {
            table = table.width(windowSizeMsg.width());
            table = table.height(windowSizeMsg.height());
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if ("q".equals(key) || "ctrl+c".equals(key)) {
                return UpdateResult.from(this, QuitMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        return "\n" + table.toString() + "\n";
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TableResizeExample()).run();
    }
}
