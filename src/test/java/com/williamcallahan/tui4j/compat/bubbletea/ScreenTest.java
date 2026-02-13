package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.compat.bubbletea.input.NoopInputHandler;
import com.williamcallahan.tui4j.compat.bubbletea.render.NilRenderer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of github.com/charmbracelet/bubbletea/screen_test.go.
 */
class ScreenTest {

    @Test
    void testProgramHandlesScreenMessages() throws Exception {
        ScreenModel model = new ScreenModel();
        Program program = new Program(model,
                ProgramOption.withInput(new ByteArrayInputStream(new byte[0])),
                ProgramOption.withOutput(new ByteArrayOutputStream()));

        SpyRenderer renderer = new SpyRenderer();
        setCoreField(program, "renderer", renderer);
        setCoreField(program, "inputHandler", new NoopInputHandler());

        program.run();

        assertThat(renderer.clearScreenCalled).isTrue();
        assertThat(renderer.enterAltScreenCalled).isTrue();
        assertThat(renderer.exitAltScreenCalled).isTrue();
        assertThat(renderer.enableMouseCellMotionCalled).isTrue();
        assertThat(renderer.enableMouseAllMotionCalled).isTrue();
        assertThat(renderer.disableMouseCalled).isTrue();
    }

    private static void setCoreField(Program program, String name, Object value) throws Exception {
        Field coreField = Program.class.getDeclaredField("core");
        coreField.setAccessible(true);
        Object core = coreField.get(program);

        Field field = ProgramCore.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(core, value);
    }

    private static final class ScreenModel implements Model {
        @Override
        public Command init() {
            return Command.sequence(
                    ClearScreenMessage::new,
                    EnterAltScreenMessage::new,
                    ExitAltScreenMessage::new,
                    EnableMouseCellMotionMessage::new,
                    EnableMouseAllMotionMessage::new,
                    DisableMouseMessage::new,
                    Command.quit()
            );
        }

        @Override
        public UpdateResult<ScreenModel> update(Message msg) {
            return UpdateResult.from(this);
        }

        @Override
        public String view() {
            return "success";
        }
    }

    private static final class SpyRenderer extends NilRenderer {
        private boolean clearScreenCalled;
        private boolean enterAltScreenCalled;
        private boolean exitAltScreenCalled;
        private boolean enableMouseCellMotionCalled;
        private boolean enableMouseAllMotionCalled;
        private boolean disableMouseCalled;

        @Override
        public void clearScreen() {
            clearScreenCalled = true;
        }

        @Override
        public void enterAltScreen() {
            enterAltScreenCalled = true;
        }

        @Override
        public void exitAltScreen() {
            exitAltScreenCalled = true;
        }

        @Override
        public void handleMessage(Message msg) {
            Message internal = msg instanceof MessageShim shim ? shim.toMessage() : msg;
            if (internal instanceof EnableMouseCellMotionMessage) {
                enableMouseCellMotionCalled = true;
            } else if (internal instanceof EnableMouseAllMotionMessage) {
                enableMouseAllMotionCalled = true;
            } else if (internal instanceof DisableMouseMessage) {
                disableMouseCalled = true;
            }
        }
    }
}
