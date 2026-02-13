package examples.compat.lipgloss.table;

import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.lipgloss.table.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Example program demonstrating lipgloss table component.
 * <p>
 * Shows a Pokemon data table with type-based coloring and alternating row styles.
 *
 * @see <a href="https://github.com/charmbracelet/lipgloss/blob/main/examples/table/pokemon/main.go">lipgloss/examples/table</a>
 */
public class TablePokemonExample {

    private static final Map<String, String> TYPE_COLORS = new HashMap<>();
    private static final Map<String, String> DIM_TYPE_COLORS = new HashMap<>();

    static {
        TYPE_COLORS.put("Bug", "#D7FF87");
        TYPE_COLORS.put("Electric", "#FDFF90");
        TYPE_COLORS.put("Fire", "#FF7698");
        TYPE_COLORS.put("Flying", "#FF87D7");
        TYPE_COLORS.put("Grass", "#75FBAB");
        TYPE_COLORS.put("Ground", "#FF875F");
        TYPE_COLORS.put("Normal", "#929292");
        TYPE_COLORS.put("Poison", "#7D5AFC");
        TYPE_COLORS.put("Water", "#00E2C7");

        DIM_TYPE_COLORS.put("Bug", "#97AD64");
        DIM_TYPE_COLORS.put("Electric", "#FCFF5F");
        DIM_TYPE_COLORS.put("Fire", "#BA5F75");
        DIM_TYPE_COLORS.put("Flying", "#C97AB2");
        DIM_TYPE_COLORS.put("Grass", "#59B980");
        DIM_TYPE_COLORS.put("Ground", "#C77252");
        DIM_TYPE_COLORS.put("Normal", "#727272");
        DIM_TYPE_COLORS.put("Poison", "#634BD0");
        DIM_TYPE_COLORS.put("Water", "#439F8E");
    }

    private static final String[][] DATA = {
            {"1", "Bulbasaur", "Grass", "Poison", "フシギダネ", "Fushigidane"},
            {"2", "Ivysaur", "Grass", "Poison", "フシギソウ", "Fushigisou"},
            {"3", "Venusaur", "Grass", "Poison", "フシギバナ", "Fushigibana"},
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

    private static final String[] HEADERS = {"#", "Name", "Type 1", "Type 2", "Japanese", "Official Rom."};

    /**
     * Runs the example to render a table with type-aware styling and highlights.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Style baseStyle = Style.newStyle().padding(0, 1);
        Style headerStyle = baseStyle.foreground(Color.color("252")).bold(true);
        Style selectedStyle = baseStyle.foreground(Color.color("#01BE85")).background(Color.color("#00432F"));

        String[] capitalizeHeaders = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            capitalizeHeaders[i] = HEADERS[i].toUpperCase();
        }

        Style finalBaseStyle = baseStyle.copy();
        Style finalHeaderStyle = headerStyle.copy();
        Style finalSelectedStyle = selectedStyle.copy();

        Table t = Table.create().
                border(StandardBorder.NormalBorder).
                borderStyle(finalBaseStyle.foreground(Color.color("238"))).
                headers(capitalizeHeaders)
                .width(80)
                .styleFunc((row, col) -> {
                    if (row == Table.HEADER_ROW) {
                        return finalHeaderStyle;
                    }

                    if ("Pikachu".equals(DATA[row][1])) {
                        return finalSelectedStyle;
                    }

                    boolean even = row % 2 == 0;

                    if (col == 2 || col == 3) {
                        String type = DATA[row][col];
                        Map<String, String> colors = even ? DIM_TYPE_COLORS : TYPE_COLORS;
                        String color = colors.getOrDefault(type, "");
                        return baseStyle.foreground(Color.color(color));
                    }

                    if (even) {
                        return baseStyle.foreground(Color.color("245"));
                    }

                    return baseStyle.foreground(Color.color("252"));
                });

        for (String[] row : DATA) {
            t.rows(row);
        }

        System.out.println(t);
    }
}
