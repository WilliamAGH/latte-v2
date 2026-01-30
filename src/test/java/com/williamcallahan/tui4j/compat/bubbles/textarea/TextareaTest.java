package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.compat.lipgloss.Renderer;
import com.williamcallahan.tui4j.compat.lipgloss.Style;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import com.williamcallahan.tui4j.compat.lipgloss.color.NoColor;
import com.williamcallahan.tui4j.compat.bubbles.cursor.CursorMode;
import com.williamcallahan.tui4j.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextareaTest {

    @BeforeEach
    void setUp() {
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
        Renderer.defaultRenderer().setColorProfile(ColorProfile.Ascii);
    }

    @Test
    void testInitialState() {
        Textarea textarea = new Textarea();

        assertEquals("", textarea.value(), "Initial value should be empty");
        assertEquals(1, textarea.lineCount(), "Initial line count should be 1");
        assertEquals(0, textarea.line(), "Initial cursor line should be 0");
    }

    @Test
    void testInsertString() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.insertString("Hello");
        assertEquals("Hello", textarea.value(), "Value should be 'Hello' after insertion");
        assertEquals(1, textarea.lineCount(), "Should still be 1 line");
    }

    @Test
    void testInsertMultilineString() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.insertString("Hello\nWorld");
        assertTrue(textarea.value().contains("Hello"), "Value should contain 'Hello'");
        assertTrue(textarea.value().contains("World"), "Value should contain 'World'");
        assertTrue(textarea.lineCount() >= 1, "Should have at least 1 line");
    }

    @Test
    void testFocusAndBlur() {
        Textarea textarea = new Textarea();

        assertEquals(false, textarea.focused(), "Should not be focused initially");

        textarea.focus();
        assertEquals(true, textarea.focused(), "Should be focused after focus()");

        textarea.blur();
        assertEquals(false, textarea.focused(), "Should not be focused after blur()");
    }

    @Test
    void testSetValue() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.setValue("Line 1\nLine 2\nLine 3");

        assertTrue(textarea.value().contains("Line 1"), "Value should contain 'Line 1'");
        assertTrue(textarea.value().contains("Line 2"), "Value should contain 'Line 2'");
        assertTrue(textarea.value().contains("Line 3"), "Value should contain 'Line 3'");
    }

    @Test
    void testCharLimit() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);
        textarea.setCharLimit(5);

        textarea.insertString("Hello World");

        assertEquals("Hello", textarea.value().substring(0, 5), "Value should be limited to 5 chars");
    }

    @Test
    void testPlaceholder() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);
        textarea.setPlaceholder("Enter text here...");

        String view = textarea.view();

        assertTrue(view.contains("Enter text here..."), "View should contain placeholder text");
    }

    @Test
    void testViewDoesNotThrowException() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);
        textarea.setPlaceholder("作業ディレクトリを指定してください");

        textarea.view();
    }

    /**
     * Verifies the cursor renders on the correct wrapped line after scrolling.
     */
    @Test
    void testCursorRendersOnWrappedLine() {
        Textarea textarea = new Textarea();
        textarea.setPrompt("");
        textarea.setShowLineNumbers(false);
        textarea.setWidth(5);
        textarea.setHeight(1);
        textarea.insertString("hello world");
        textarea.focus();
        textarea.update(new com.williamcallahan.tui4j.compat.bubbletea.FocusMessage());
        textarea.cursor().setMode(CursorMode.Static);
        textarea.cursor().setStyle(com.williamcallahan.tui4j.compat.lipgloss.Style.newStyle()
            .transform(value -> "{" + value + "}"));

        Textarea.LineInfo lineInfo = textarea.lineInfo();
        assertTrue(lineInfo.rowOffset() > 0, "Expected cursor to be on a wrapped line");

        String view = textarea.view();
        String[] lines = view.split("\n", -1);
        int cursorLineIndex = -1;
        int cursorLineCount = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("{") && lines[i].contains("}")) {
                cursorLineIndex = i;
                cursorLineCount++;
            }
        }

        assertEquals(1, cursorLineCount, "Cursor should render on only one wrapped line");
        assertEquals(0, cursorLineIndex, "Cursor should render on the visible line after scrolling");
    }

    /**
     * Verifies the cursor renders as a visible space when at end-of-line.
     */
    @Test
    void testCursorVisibleAtEndOfLine() {
        Textarea textarea = new Textarea();
        textarea.setPrompt("");
        textarea.setShowLineNumbers(false);
        textarea.setWidth(10);
        textarea.setHeight(1);
        textarea.insertString("a");
        textarea.cursor().setMode(CursorMode.Static);
        textarea.cursor().setStyle(Style.newStyle().transform(value -> "<" + value + ">"));

        String view = textarea.view();

        assertTrue(view.contains("< >"), "Cursor should render a visible space at end of line");
    }


    @Test
    void testReset() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.insertString("Hello World");
        assertEquals("Hello World", textarea.value());

        textarea.reset();
        assertEquals("", textarea.value(), "Value should be empty after reset");
        assertEquals(1, textarea.lineCount(), "Should have 1 line after reset");
    }

    @Test
    void testMaxHeight() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(5);
        textarea.setMaxHeight(3);
        textarea.focus();

        for (int i = 0; i < 10; i++) {
            textarea.insertString("Line " + i);
            textarea.update(new com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage(
                new com.williamcallahan.tui4j.compat.bubbletea.input.key.Key(
                    com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType.keyCR
                )
            ));
        }

        assertTrue(textarea.lineCount() <= 3, "Should be limited by max height");
    }

    @Test
    void testLengthCalculation() {
        Textarea textarea = new Textarea();

        textarea.setValue("Hello\nWorld");

        int len = textarea.length();

        assertTrue(len > 0, "Length should be positive");
    }

    @Test
    void testWidthAndHeightSetters() {
        Textarea textarea = new Textarea();

        textarea.setPrompt("");
        textarea.setShowLineNumbers(false);
        textarea.setWidth(100);
        textarea.setHeight(20);

        assertEquals(100, textarea.width(), "Width should be 100");
        assertEquals(20, textarea.height(), "Height should be 20");
    }

    @Test
    void testEndOfBufferCharacter() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.setEndOfBufferCharacter('~');
    }

    @Test
    void testLineGetter() {
        Textarea textarea = new Textarea();
        textarea.insertString("Hello");

        assertTrue(textarea.line() >= 0, "Cursor line should be non-negative");
    }

    @Test
    void testInsertRune() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.insertRune('H');
        textarea.insertRune('i');
        textarea.insertRune('!');

        assertEquals("Hi!", textarea.value());
    }

    @Test
    void testStyleClasses() {
        Textarea textarea = new Textarea();
        Textarea.Style style = textarea.style();
        assertTrue(style != null, "Style should not be null");
    }

    @Test
    void testFocusedAndBlurredStyles() {
        Textarea textarea = new Textarea();

        assertTrue(textarea.focusedStyle() != null, "Focused style should not be null");
        assertTrue(textarea.blurredStyle() != null, "Blurred style should not be null");
    }

    @Test
    void testCursorAccessor() {
        Textarea textarea = new Textarea();

        assertTrue(textarea.cursor() != null, "Cursor should not be null");
    }

    @Test
    void testLineInfoClass() {
        Textarea.LineInfo lineInfo = new Textarea.LineInfo();
        // Removed redundant null check
    }

    @Test
    void testMultipleInserts() {
        Textarea textarea = new Textarea();
        textarea.setWidth(80);
        textarea.setHeight(10);

        textarea.insertString("First");
        textarea.insertString(" ");
        textarea.insertString("Second");

        assertEquals("First Second", textarea.value());
    }

    @Test
    void testKeyMapClass() {
        Textarea.KeyMap keyMap = new Textarea.KeyMap();

        assertTrue(keyMap.characterForward() != null, "characterForward binding should not be null");
        assertTrue(keyMap.characterBackward() != null, "characterBackward binding should not be null");
    }
}
