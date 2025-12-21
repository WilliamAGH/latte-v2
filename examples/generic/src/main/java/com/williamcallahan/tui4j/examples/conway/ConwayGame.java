package com.williamcallahan.tui4j.examples.conway;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.message.WindowSizeMessage;

import static com.williamcallahan.tui4j.compat.bubbletea.Command.checkWindowSize;

/**
 * Example program for conway game.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/conway/ConwayGame.java
 */
public class ConwayGame implements Model {
    private Conway conway;

    @Override
    public Command init() {
        return checkWindowSize();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.conway = Conway.createRandom(
                    windowSizeMessage.width(),
                    windowSizeMessage.height(),
                    0.5
            );
            return UpdateResult.from(this, conway.init());
        }
        if (conway != null) {
            UpdateResult<? extends Model> updateResult = conway.update(msg);
            return UpdateResult.from(this, updateResult.command());
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        if (conway != null) {
            return conway.view();
        }
        return "";
    }

    public static void main(String[] args) {
        new Program(new ConwayGame())
                .withAltScreen()
                .run();
    }
}
