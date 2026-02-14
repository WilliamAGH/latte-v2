package com.williamcallahan.tui4j.compat.bubbletea;

import com.williamcallahan.tui4j.message.CopyToClipboardMessage;
import com.williamcallahan.tui4j.message.OpenUrlMessage;
import com.williamcallahan.tui4j.message.ResetMouseCursorMessage;
import com.williamcallahan.tui4j.message.SetMouseCursorPointerMessage;
import com.williamcallahan.tui4j.message.SetMouseCursorTextMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests command.
 * tui4j: src/test/java/com/williamcallahan/tui4j/compat/bubbletea/CommandTest.java
 */
class CommandTest {

    @Test
    @DisplayName("Command.setMouseCursorText should return a SetMouseCursorTextMessage")
    void test_SetMouseCursorText() {
        Command cmd = Command.setMouseCursorText();
        Message msg = cmd.execute();
        assertThat(msg).isInstanceOf(SetMouseCursorTextMessage.class);
    }

    @Test
    @DisplayName("Command.setMouseCursorPointer should return a SetMouseCursorPointerMessage")
    void test_SetMouseCursorPointer() {
        Command cmd = Command.setMouseCursorPointer();
        Message msg = cmd.execute();
        assertThat(msg).isInstanceOf(SetMouseCursorPointerMessage.class);
    }

    @Test
    @DisplayName("Command.resetMouseCursor should return a ResetMouseCursorMessage")
    void test_ResetMouseCursor() {
        Command cmd = Command.resetMouseCursor();
        Message msg = cmd.execute();
        assertThat(msg).isInstanceOf(ResetMouseCursorMessage.class);
    }

    @Test
    @DisplayName("Command.copyToClipboard should return a CopyToClipboardMessage with correct text")
    void test_CopyToClipboard() {
        String text = "hello";
        Command cmd = Command.copyToClipboard(text);
        Message msg = cmd.execute();
        assertThat(msg).isInstanceOf(CopyToClipboardMessage.class);
        assertThat(((CopyToClipboardMessage) msg).text()).isEqualTo(text);
    }

    @Test
    @DisplayName("Command.openUrl should return an OpenUrlMessage with correct url")
    void test_OpenUrl() {
        String url = "https://example.com";
        Command cmd = Command.openUrl(url);
        Message msg = cmd.execute();
        assertThat(msg).isInstanceOf(OpenUrlMessage.class);
        assertThat(((OpenUrlMessage) msg).url()).isEqualTo(url);
    }

    /** Verifies {@link Command#none()} returns the NO_OP sentinel instance. */
    @Test
    @DisplayName("Command.none() returns the NO_OP sentinel")
    void test_NoneReturnsSentinel() {
        assertThat(Command.none()).isSameAs(Command.NO_OP);
    }

    /** Verifies executing the NO_OP sentinel yields null (no message). */
    @Test
    @DisplayName("Command.none() execute returns null")
    void test_NoneExecuteReturnsNull() {
        assertThat(Command.none().execute()).isNull();
    }

    /** Verifies null is recognized as absent by {@link Command#isNone}. */
    @Test
    @DisplayName("Command.isNone() returns true for null")
    void test_IsNoneNull() {
        assertThat(Command.isNone(null)).isTrue();
    }

    /** Verifies the NO_OP sentinel is recognized as absent by {@link Command#isNone}. */
    @Test
    @DisplayName("Command.isNone() returns true for Command.none()")
    void test_IsNoneForSentinel() {
        assertThat(Command.isNone(Command.none())).isTrue();
    }

    /** Verifies a real command is not mistaken for absent by {@link Command#isNone}. */
    @Test
    @DisplayName("Command.isNone() returns false for a real command")
    void test_IsNoneForRealCommand() {
        assertThat(Command.isNone(Command.quit())).isFalse();
    }

    /** Verifies {@link Command#batch} excludes null and NO_OP commands from the batch. */
    @Test
    @DisplayName("batch() filters out null and none commands")
    void test_BatchFiltersNoneCommands() {
        Command real = Command.quit();
        Command batched = Command.batch(real, Command.none(), null);
        Message msg = batched.execute();
        assertThat(msg).isInstanceOf(BatchMessage.class);
        assertThat(((BatchMessage) msg).commands()).containsExactly(real);
    }
}

