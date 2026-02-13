package examples.compat.bubbletea.table;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbles.table.Column;
import com.williamcallahan.tui4j.compat.bubbles.table.Row;
import com.williamcallahan.tui4j.compat.bubbles.table.Table;
import com.williamcallahan.tui4j.compat.bubbletea.WindowSizeMessage;

import java.util.List;

/**
 * Example program for table component.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/table/TableExample.java
 */
public class TableExample implements Model {

    private Table table;
    private String selectedInfo = "";

    /**
     * Creates TableExample to keep example ready for use.
     */
    public TableExample() {
        table = Table.create()
                .columns(List.of(
                        new Column("ID", 10),
                        new Column("Name", 25),
                        new Column("Status", 15),
                        new Column("Power", 10)
                ))
                .rows(List.of(
                        new Row("001", "Bulbasaur", "Active", "45"),
                        new Row("002", "Ivysaur", "Active", "60"),
                        new Row("003", "Venusaur", "Active", "80"),
                        new Row("004", "Charmander", "Active", "39"),
                        new Row("005", "Charmeleon", "Active", "58"),
                        new Row("006", "Charizard", "Active", "78"),
                        new Row("007", "Squirtle", "Active", "44"),
                        new Row("008", "Wartortle", "Active", "59"),
                        new Row("009", "Blastoise", "Active", "79"),
                        new Row("010", "Caterpie", "Active", "20"),
                        new Row("011", "Metapod", "Active", "25"),
                        new Row("012", "Butterfree", "Active", "45"),
                        new Row("013", "Weedle", "Active", "20"),
                        new Row("014", "Kakuna", "Active", "25"),
                        new Row("015", "Beedrill", "Active", "45")
                ))
                .focused(true);
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
            if ("q".equals(key) || "esc".equals(key)) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            if ("enter".equals(key)) {
                Row selected = table.selectedRow();
                if (selected != null) {
                    selectedInfo = "Selected: " + String.join(" | ", selected.cells());
                }
            }
        } else if (msg instanceof WindowSizeMessage windowSizeMessage) {
            table.setWidth(windowSizeMessage.width());
            table.setHeight(windowSizeMessage.height() - 5);
        }

        UpdateResult<? extends Model> result = table.update(msg);
        return UpdateResult.from(this, result.command());
    }

    /**
     * Renders the model view for display.
     *
     * @return rendered view
     */
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pokemon Table\n");
        sb.append("=".repeat(70)).append("\n");
        sb.append(table.view()).append("\n");
        sb.append("=".repeat(70)).append("\n");
        sb.append("Controls: ↑/↓ navigate, g/home go to top, G/end go to bottom, q/esc quit, enter select\n");
        if (!selectedInfo.isEmpty()) {
            sb.append("Selection: ").append(selectedInfo).append("\n");
        }
        return sb.toString();
    }

    /**
     * Runs the example program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new Program(new TableExample()).run();
    }
}
