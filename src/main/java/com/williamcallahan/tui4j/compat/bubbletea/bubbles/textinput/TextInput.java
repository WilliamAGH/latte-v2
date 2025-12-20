package com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput;

import com.ibm.icu.lang.UCharacter;
import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.ansi.TextWidth;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Size;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor.Cursor;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.cursor.CursorMode;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.runeutil.Sanitizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.clamp;
import static com.williamcallahan.tui4j.Command.batch;

/**
 * Port of Bubbles text input.
 * Bubble Tea: bubbletea/examples/textinput/main.go
 */
public class TextInput implements Model {

    @FunctionalInterface
    interface ValidateFunction {

        boolean isValid(char[] runes);
    }
    private String prompt;

    private String placeholder;
    private EchoMode echoMode;
    private char echoCharacter;
    private Cursor cursor;
    // styles

    private Style promptStyle;
    private Style textStyle;
    private Style placeholderStyle;
    private Style completionStyle;
    private int charLimit;

    private int width;
    private Keys keys;
    private char[] value;
    private boolean focus;
    private int pos;
    private int offset;
    private int offsetRight;
    private ValidateFunction validateFunction;

    private Sanitizer sanitizer;
    private boolean showSuggestions;

    private char[][] suggestions;
    private char[][] matchedSuggestions;
    private int currentSuggestionIndex;
    public TextInput() {
        this.prompt = "> ";
        this.echoCharacter = '*';
        this.echoMode = EchoMode.EchoNormal;
        this.charLimit = 0;
        this.placeholderStyle = Style.newStyle().foreground(Color.color("240"));
        this.showSuggestions = false;
        this.completionStyle = Style.newStyle().foreground(Color.color("240"));
        this.promptStyle = Style.newStyle();
        this.textStyle = Style.newStyle();
        this.cursor = new Cursor();
        this.keys = new Keys();
        this.sanitizer = new Sanitizer(
                Sanitizer.replaceTabs(" "),
                Sanitizer.replaceNewlines(" ")
        );

        this.suggestions = new char[][]{};
        this.matchedSuggestions = new char[][]{};
        this.value = new char[0];
        this.focus = false;
        this.pos = 0;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String prompt() {
        return prompt;
    }

    public void setPromptStyle(Style promptStyle) {
        this.promptStyle = promptStyle;
    }

    public void setTextStyle(Style textStyle) {
        this.textStyle = textStyle;
    }

    public void setValue(String value) {
        char[] sanitized = sanitizer.sanitize(value.toCharArray());
        if (validateFunction != null) {
            validateFunction.isValid(sanitized);
        }
        setValueInternal(sanitized);
    }

    private void setValueInternal(char[] runes) {
        boolean empty = value.length == 0;

        if (charLimit > 0 && runes.length > charLimit) {
            this.value = Arrays.copyOfRange(runes, 0, charLimit);
        } else {
            this.value = runes;
        }

        if ((pos == 0 && empty) | pos > value.length) {
            setCursor(value.length);
        }
        handleOverflow();
    }

    public String value() {
        return String.valueOf(value);
    }

    public boolean isEmpty() {
        return value == null || value.length == 0;
    }

    public int position() {
        return pos;
    }

    public void setCursor(int position) {
        this.pos = clamp(position, 0, value.length);
        handleOverflow();
    }

    public void cursorStart() {
        setCursor(0);
    }

    public void cursorEnd() {
        setCursor(value.length);
    }

    public boolean isFocused() {
        return focus;
    }

    public Command focus() {
        this.focus = true;
        return cursor.focus();
    }

    public void blur() {
        this.focus = false;
        cursor.blur();
    }

    public void reset() {
        this.value = new char[0];
        setCursor(0);
    }

    public void setSuggestions(String[] suggestions) {
        this.suggestions = new char[suggestions.length][];
        for (int i = 0; i < suggestions.length; i++) {
            this.suggestions[i] = suggestions[i].toCharArray();
        }

        updateSuggestions();
    }

    private void insertRunesFromUserInput(char[] runes) {
        // Clean up any special characters in the input
        char[] paste = sanitizer.sanitize(runes);

        int availSpace = 0;
        if (charLimit > 0) {
            availSpace = charLimit - value.length;

            // If the char limit has been reached, cancel
            if (availSpace <= 0) {
                return;
            }

            // If there's not enough space to paste the whole input, truncate it
            if (availSpace < paste.length) {
                paste = Arrays.copyOfRange(paste, 0, availSpace);
            }
        }

        // Split the current value into head and tail parts
        char[] head = Arrays.copyOfRange(value, 0, pos);
        char[] tailSrc = Arrays.copyOfRange(value, pos, value.length);

        // Prepare a new tail to avoid overwriting the original
        char[] tail = Arrays.copyOf(tailSrc, tailSrc.length);

        // Insert pasted runes into the head and update the cursor position
        for (char r : paste) {
            head = appendChar(head, r);
            pos++;
            if (charLimit > 0) {
                availSpace--;
                if (availSpace <= 0) {
                    break;
                }
            }
        }

        // Combine head, paste, and tail to form the new value
        char[] newValue = combineArrays(head, tail);

        // Validate and set the new value
        if (validateFunction != null) {
            validateFunction.isValid(newValue);
        }
        setValueInternal(newValue);
    }

    // Helper method to append a character to a char array
    private char[] appendChar(char[] array, char c) {
        char[] newArray = Arrays.copyOf(array, array.length + 1);
        newArray[array.length] = c;
        return newArray;
    }

    // Helper method to combine two char arrays
    private char[] combineArrays(char[] head, char[] tail) {
        char[] combined = new char[head.length + tail.length];
        System.arraycopy(head, 0, combined, 0, head.length);
        System.arraycopy(tail, 0, combined, head.length, tail.length);
        return combined;
    }

    private void handleOverflow() {
        if (width <= 0 || Size.width(new String(value)) <= width) {
            this.offset = 0;
            this.offsetRight = value.length;
            return;
        }

        this.offsetRight = Math.min(offsetRight, value.length);
        if (pos < offset) {
            this.offset = pos;

            int w = 0;
            int i = 0;
            char[] runes = Arrays.copyOfRange(value, offset, value.length);

            while (i < runes.length && w <= width) {
                w += TextWidth.measureCellWidth(String.valueOf(runes[i]));
                if (w <= width + 1) {
                    i++;
                }
            }

            this.offsetRight = offset + i;
        } else if (pos >= offsetRight) {
            this.offsetRight = pos;

            int w = 0;
            char[] runes = Arrays.copyOfRange(value, 0, offsetRight);
            int i = runes.length - 1;

            while (i > 0 && w < this.width) {
                w += TextWidth.measureCellWidth(String.valueOf(runes[i]));
                if (w <= this.width) {
                    i--;
                }
            }
            this.offset = offsetRight - (runes.length - 1 - i);
        }
    }

    private void deleteBeforeCursor() {
        this.value = Arrays.copyOfRange(value, pos, value.length);
        // validate?
        this.offset = 0;
        setCursor(0);
    }

    private void deleteAfterCursor() {
        this.value = Arrays.copyOfRange(value, 0, pos);
        setCursor(value.length);
    }

    private void deleteCharacterBackward() {
        if (value.length > 0) {
            value = combineArrays(
                    Arrays.copyOfRange(value, 0, Math.max(0, pos - 1)),
                    Arrays.copyOfRange(value, pos, value.length)
            );
            if (pos > 0) {
                setCursor(pos - 1);
            }
        }
    }

    private void deleteWordBackward() {
        if (pos == 0 || value.length == 0) {
            return;
        }

        if (echoMode != EchoMode.EchoNormal) {
            deleteBeforeCursor();
            return;
        }

        int oldPos = pos;
        setCursor(pos - 1);
        while (UCharacter.isSpaceChar(value[pos])) {
            if (pos <= 0) {
                break;
            }
            setCursor(pos - 1);
        }

        while (pos > 0) {
            if (!UCharacter.isSpaceChar(value[pos])) {
                setCursor(pos - 1);
            } else {
                if (pos > 0) {
                    setCursor(pos + 1);
                }
                break;
            }
        }

        if (oldPos > value.length) {
            this.value = Arrays.copyOfRange(value, 0, pos);
        } else {
            this.value = combineArrays(
                    Arrays.copyOfRange(value, 0, pos),
                    Arrays.copyOfRange(value, oldPos, value.length)
            );
        }
        // validate?
    }

    private void deleteCharacterForward() {
        if (value.length > 0 && pos < value.length) {
            value = combineArrays(
                    Arrays.copyOfRange(value, 0, pos),
                    Arrays.copyOfRange(value, pos + 1, value.length)
            );
            // validate?
        }
    }

    private void deleteWordForward() {
        if (pos >= value.length || value.length == 0) {
            return;
        }

        if (echoMode != EchoMode.EchoNormal) {
            deleteAfterCursor();
            return;
        }

        int oldPos = pos;
        setCursor(pos + 1);

        while (pos < value.length && UCharacter.isSpaceChar(value[pos])) {
            setCursor(pos + 1);
            if (pos >= value.length) {
                break;
            }
        }

        while (pos < value.length) {
            if (!UCharacter.isSpaceChar(value[pos])) {
                setCursor(pos + 1);
            } else {
                break;
            }
        }

        if (pos > value.length) {
            this.value = Arrays.copyOfRange(value, 0, oldPos);
        } else {
            this.value = combineArrays(
                    Arrays.copyOfRange(value, 0, oldPos),
                    Arrays.copyOfRange(value, pos, value.length)
            );
        }
        // validate?
        setCursor(oldPos);
    }

    private void wordBackward() {
        if (pos == 0 || value.length == 0) {
            return;
        }

        if (echoMode != EchoMode.EchoNormal) {
            cursorStart();
            return;
        }

        int i = pos - 1;

        // Skip over any whitespace before the cursor
        while (i >= 0) {
            if (UCharacter.isSpaceChar(value[i])) {
                setCursor(pos - 1);
                i--;
            } else {
                break;
            }
        }

        // Move the cursor back to the start of the word
        while (i >= 0) {
            if (!UCharacter.isSpaceChar(value[i])) {
                setCursor(pos - 1);
                i--;
            } else {
                break;
            }
        }
    }

    private void wordForward() {
        if (pos >= value.length || value.length == 0) {
            return;
        }

        if (echoMode != EchoMode.EchoNormal) {
            cursorEnd();
            return;
        }

        int i = pos;

        // Skip over any whitespace after the cursor
        while (i < value.length) {
            if (UCharacter.isSpaceChar(value[i])) {
                setCursor(pos + 1);
                i++;
            } else {
                break;
            }
        }

        // Move the cursor forward through the word
        while (i < value.length) {
            if (!UCharacter.isSpaceChar(value[i])) {
                setCursor(pos + 1);
                i++;
            } else {
                break;
            }
        }
    }

    private String echoTransform(String input) {
        return switch (echoMode) {
            case EchoNormal -> input;
            case EchoPassword -> String.valueOf(echoCharacter).repeat(TextWidth.measureCellWidth(input));
            case EchoNone -> "";
        };
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<TextInput> update(Message msg) {
        if (!focus) {
            return UpdateResult.from(this);
        }
        // TODO suggestions check

        int oldPos = pos;

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keys.deleteWordBackward())) {
                deleteWordBackward();
            } else if (Binding.matches(keyPressMessage, keys.deleteCharacterBackward())) {
                deleteCharacterBackward();
            } else if (Binding.matches(keyPressMessage, keys.wordBackward())) {
                wordBackward();
            } else if (Binding.matches(keyPressMessage, keys.characterBackward())) {
                if (pos > 0) {
                    setCursor(pos - 1);
                }
            } else if (Binding.matches(keyPressMessage, keys.wordForward())) {
                wordForward();
            } else if (Binding.matches(keyPressMessage, keys.characterForward())) {
                if (pos < value.length) {
                    setCursor(pos + 1);
                }
            } else if (Binding.matches(keyPressMessage, keys.lineStart())) {
                cursorStart();
            } else if (Binding.matches(keyPressMessage, keys.deleteCharacterForward())) {
                deleteCharacterForward();
            } else if (Binding.matches(keyPressMessage, keys.lineEnd())) {
                cursorEnd();
            } else if (Binding.matches(keyPressMessage, keys.deleteAfterCursor())) {
                deleteAfterCursor();
            } else if (Binding.matches(keyPressMessage, keys.deleteBeforeCursor())) {
                deleteBeforeCursor();
            } else if (Binding.matches(keyPressMessage, keys.paste())) {
                return UpdateResult.from(this, this::paste);
            } else if (Binding.matches(keyPressMessage, keys.deleteWordForward())) {
                deleteWordForward();
            } else if (Binding.matches(keyPressMessage, keys.nextSuggestion())) {
                nextSuggestion();
            } else if (Binding.matches(keyPressMessage, keys.prevSuggestion())) {
                previousSuggestion();
            } else {
                // also handles paste
                insertRunesFromUserInput(keyPressMessage.runes());
            }

            updateSuggestions();
        }

        // TODO handle paste!
        List<Command> commands = new LinkedList<>();

        UpdateResult<Cursor> cursorUpdateResult = cursor.update(msg);
        this.cursor = cursorUpdateResult.model();
        commands.add(cursorUpdateResult.command());

        if (oldPos != pos && cursor.mode() == CursorMode.Blink) {
            cursor.setBlink(false);
            commands.add(cursor.blinkCommand());
        }

        handleOverflow();
        return UpdateResult.from(this, batch(commands.toArray(new Command[0])));
    }

    public Message paste() {
        // TODO implement clipboard paste!
        return null;
    }

    @Override
    public String view() {
        if (value.length == 0 && placeholder != null && !placeholder.isEmpty()) {
            return placeholderView();
        }
        cursor.resetTextStyle();

        Style styleText = textStyle.copy().inline(true);

        char[] value = Arrays.copyOfRange(this.value, offset, offsetRight);
        int pos = Math.max(0, this.pos - offset);
        String v = styleText.render(
                echoTransform(
                        String.valueOf(Arrays.copyOfRange(value, 0, pos))
                )
        );

        if (pos < value.length) {
            String character = echoTransform(String.valueOf(value[pos]));
            cursor.setChar(character);

            v += cursor.view();
            v += styleText.render(echoTransform(String.valueOf(Arrays.copyOfRange(value, pos + 1, value.length))));
            v += completionView(0);
        } else {
            if (canAcceptSuggestion()) {
                char[] suggestion = matchedSuggestions[currentSuggestionIndex];

                if (value.length < suggestion.length) {
                    cursor.setTextStyle(this.completionStyle.copy());
                    cursor.setChar(echoTransform(String.valueOf(suggestion[pos])));
                    v += cursor.view();
                    v += completionView(1);
                } else {
                    cursor.setChar(" ");
                    v += cursor.view();
                }
            } else {
                cursor.setChar(" ");
                v += cursor.view();
            }
        }

        int valWidth = Size.width(String.valueOf(value));
        if (width > 0 && valWidth < width) {
            int padding = Math.max(0, this.width - valWidth);
            if (valWidth + padding <= this.width && pos < value.length) {
                padding++;
            }
            v += styleText.render(" ".repeat(padding));
        }

        return promptStyle.render(prompt) + v;
    }

    public String placeholderView() {
        String v = "";
        Style style = placeholderStyle.copy().inline(true);

        char[] p = new char[width + 1];
        char[] placeholder = this.placeholder.toCharArray();
        if (placeholder.length > width + 1) {
            p = new char[placeholder.length];
        }
        System.arraycopy(placeholder, 0, p, 0, placeholder.length);

        cursor.setTextStyle(placeholderStyle.copy());
        cursor.setChar(String.valueOf(p[0]));
        v += cursor.view();

        if (width < 1 && p.length <= 1) {
            return promptStyle.render(prompt) + v;
        }

        if (width > 0) {
            int minWidth = Size.width(this.placeholder);
            int availWidth = width - minWidth + 1;

            if (availWidth < 0) {
                minWidth += availWidth;
                availWidth = 0;
            }

            v += style.render(String.valueOf(Arrays.copyOfRange(p, 1, minWidth)));
            v += style.render(" ".repeat(availWidth));
        } else {
            // If no width is set, the placeholder can be any length
            v += style.render(String.valueOf(Arrays.copyOfRange(p, 1, p.length)));
        }

        return promptStyle.render(prompt) + v;
    }

    public static Message blink() {
        return Cursor.blink();
    }

    public String completionView(int offset) {
        char[] value = this.value;
        Style style = placeholderStyle.copy().inline(true);

        if (canAcceptSuggestion()) {
            char[] suggestion = matchedSuggestions[currentSuggestionIndex];
            if (value.length < suggestion.length) {
                return style.render(
                        String.valueOf(
                                Arrays.copyOfRange(suggestion, value.length + offset, suggestion.length)
                        )
                );
            }
        }
        return "";
    }

    private boolean canAcceptSuggestion() {
        return matchedSuggestions.length > 0;
    }

    public void nextSuggestion() {
        this.currentSuggestionIndex = currentSuggestionIndex + 1;
        if (currentSuggestionIndex > matchedSuggestions.length) {
            this.currentSuggestionIndex = 0;
        }
    }

    public void previousSuggestion() {
        this.currentSuggestionIndex = currentSuggestionIndex - 1;
        if (currentSuggestionIndex < 0) {
            this.currentSuggestionIndex = matchedSuggestions.length;
        }
    }

    public void updateSuggestions() {
        if (!showSuggestions) {
            return;
        }

        if (value.length == 0 || suggestions.length == 0) {
            matchedSuggestions = new char[0][];
            return;
        }

        List<char[]> matches = new ArrayList<>();
        String currentValue = new String(value).toLowerCase();

        for (char[] suggestion : suggestions) {
            String suggestionStr = new String(suggestion);
            if (suggestionStr.toLowerCase().startsWith(currentValue)) {
                matches.add(suggestion);
            }
        }

        char[][] matchesArray = matches.toArray(new char[0][]);
        if (!Arrays.deepEquals(matchesArray, matchedSuggestions)) {
            currentSuggestionIndex = 0;
        }

        matchedSuggestions = matchesArray;
    }

    private String[] getSuggestions(char[][] sugs) {
        String[] suggestions = new String[sugs.length];
        for (int i = 0; i < sugs.length; i++) {
            suggestions[i] = new String(sugs[i]);
        }
        return suggestions;
    }

    public String[] availableSuggestions() {
        return getSuggestions(suggestions);
    }

    public String[] matchedSuggestions() {
        return getSuggestions(matchedSuggestions);
    }

    public int currentSuggestionIndex() {
        return currentSuggestionIndex;
    }

    public String currentSuggestion() {
        if (currentSuggestionIndex >= matchedSuggestions.length) {
            return "";
        }
        return new String(matchedSuggestions[currentSuggestionIndex]);
    }

    public void setShowSuggestions(boolean showSuggestions) {
        this.showSuggestions = showSuggestions;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setCharLimit(int charLimit) {
        this.charLimit = charLimit;
    }

    public void setEchoMode(EchoMode echoMode) {
        this.echoMode = echoMode;
    }

    public void setEchoCharacter(char echoCharacter) {
        this.echoCharacter = echoCharacter;
    }

    public Cursor cursor() {
        return cursor;
    }
}
