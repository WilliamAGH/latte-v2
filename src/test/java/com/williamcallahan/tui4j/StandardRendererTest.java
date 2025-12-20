package com.williamcallahan.tui4j;

import com.williamcallahan.tui4j.ansi.Code;
import com.williamcallahan.tui4j.render.StandardRenderer;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests standard renderer.
 * tui4j: src/test/java/com/williamcallahan/tui4j/StandardRendererTest.java
 */
class StandardRendererTest {

    private static Terminal newFakeTerminal(StringWriter out) {
        PrintWriter writer = new PrintWriter(out);

        return (Terminal) Proxy.newProxyInstance(
                Terminal.class.getClassLoader(),
                new Class<?>[]{Terminal.class},
                (proxy, method, args) -> {
                    return switch (method.getName()) {
                        case "writer" -> writer;
                        case "flush" -> null;
                        case "getType" -> "dumb";
                        case "getWidth" -> 80;
                        case "getHeight" -> 24;
                        case "puts" -> false;
                        default -> defaultValue(method.getReturnType());
                    };
                }
        );
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) return false;
        if (returnType == byte.class) return (byte) 0;
        if (returnType == short.class) return (short) 0;
        if (returnType == int.class) return 0;
        if (returnType == long.class) return 0L;
        if (returnType == float.class) return 0f;
        if (returnType == double.class) return 0d;
        if (returnType == char.class) return (char) 0;
        return null;
    }

    @Test
    @DisplayName("Should write SetMouseTextCursor sequence when setMouseCursorText is called")
    void test_ShouldWriteSetMouseTextCursorSequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        renderer.setMouseCursorText();

        assertThat(out.toString()).isEqualTo(Code.SetMouseTextCursor.value());
    }

    @Test
    @DisplayName("Should write SetMousePointerCursor sequence when setMouseCursorPointer is called")
    void test_ShouldWriteSetMousePointerCursorSequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        renderer.setMouseCursorPointer();

        assertThat(out.toString()).isEqualTo(Code.SetMousePointerCursor.value());
    }

    @Test
    @DisplayName("Should write ResetMouseCursor sequence when resetMouseCursor is called")
    void test_ShouldWriteResetMouseCursorSequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        renderer.resetMouseCursor();

        assertThat(out.toString()).isEqualTo(Code.ResetMouseCursor.value());
    }

    @Test
    @DisplayName("Should write EnableMouseCellMotion sequence when enableMouseCellMotion is called")
    void test_ShouldWriteEnableMouseCellMotionSequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        renderer.enableMouseCellMotion();

        assertThat(out.toString()).isEqualTo(Code.EnableMouseCellMotion.value());
    }

    @Test
    @DisplayName("Should write DisableMouseCellMotion sequence when disableMouseCellMotion is called")
    void test_ShouldWriteDisableMouseCellMotionSequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        renderer.disableMouseCellMotion();

        assertThat(out.toString()).isEqualTo(Code.DisableMouseCellMotion.value());
    }

    @Test
    @DisplayName("Should write OSC 52 sequence when copyToClipboard is called")
    void test_ShouldWriteOsc52Sequence() {
        StringWriter out = new StringWriter();
        Terminal terminal = newFakeTerminal(out);
        StandardRenderer renderer = new StandardRenderer(terminal);

        String text = "hello world";
        renderer.copyToClipboard(text);

        assertThat(out.toString()).isEqualTo(Code.copyToClipboard(text));
    }
}
