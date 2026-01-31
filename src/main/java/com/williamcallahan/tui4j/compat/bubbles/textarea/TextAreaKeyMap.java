package com.williamcallahan.tui4j.compat.bubbles.textarea;

import com.williamcallahan.tui4j.compat.bubbles.key.Binding;

/**
 * Key bindings for textarea navigation and editing.
 * <p>
 * Port of {@code bubbles/textarea/textarea.go} KeyMap.
 */
public class TextAreaKeyMap {

    private Binding characterBackward;
    private Binding characterForward;
    private Binding deleteAfterCursor;
    private Binding deleteBeforeCursor;
    private Binding deleteCharacterBackward;
    private Binding deleteCharacterForward;
    private Binding deleteWordBackward;
    private Binding deleteWordForward;
    private Binding insertNewline;
    private Binding lineEnd;
    private Binding lineNext;
    private Binding linePrevious;
    private Binding lineStart;
    private Binding paste;
    private Binding wordBackward;
    private Binding wordForward;
    private Binding inputBegin;
    private Binding inputEnd;
    private Binding uppercaseWordForward;
    private Binding lowercaseWordForward;
    private Binding capitalizeWordForward;
    private Binding transposeCharacterBackward;

    /**
     * Creates default key bindings.
     */
    public TextAreaKeyMap() {
        this.characterBackward = new Binding(Binding.withKeys("left", "ctrl+b"), Binding.withHelp("left", "character backward"));
        this.characterForward = new Binding(Binding.withKeys("right", "ctrl+f"), Binding.withHelp("right", "character forward"));
        this.wordForward = new Binding(Binding.withKeys("alt+right", "alt+f"), Binding.withHelp("alt+right", "word forward"));
        this.wordBackward = new Binding(Binding.withKeys("alt+left", "alt+b"), Binding.withHelp("alt+left", "word backward"));
        this.lineNext = new Binding(Binding.withKeys("down", "ctrl+n"), Binding.withHelp("down", "next line"));
        this.linePrevious = new Binding(Binding.withKeys("up", "ctrl+p"), Binding.withHelp("up", "previous line"));
        this.deleteWordBackward = new Binding(Binding.withKeys("alt+backspace", "ctrl+w"), Binding.withHelp("alt+backspace", "delete word backward"));
        this.deleteWordForward = new Binding(Binding.withKeys("alt+delete", "alt+d"), Binding.withHelp("alt+delete", "delete word forward"));
        this.deleteAfterCursor = new Binding(Binding.withKeys("ctrl+k"), Binding.withHelp("ctrl+k", "delete after cursor"));
        this.deleteBeforeCursor = new Binding(Binding.withKeys("ctrl+u"), Binding.withHelp("ctrl+u", "delete before cursor"));
        this.insertNewline = new Binding(Binding.withKeys("enter", "ctrl+m"), Binding.withHelp("enter", "insert newline"));
        this.deleteCharacterBackward = new Binding(Binding.withKeys("backspace", "ctrl+h"), Binding.withHelp("backspace", "delete character backward"));
        this.deleteCharacterForward = new Binding(Binding.withKeys("delete", "ctrl+d"), Binding.withHelp("delete", "delete character forward"));
        this.lineStart = new Binding(Binding.withKeys("home", "ctrl+a"), Binding.withHelp("home", "line start"));
        this.lineEnd = new Binding(Binding.withKeys("end", "ctrl+e"), Binding.withHelp("end", "line end"));
        this.paste = new Binding(Binding.withKeys("ctrl+v"), Binding.withHelp("ctrl+v", "paste"));
        this.inputBegin = new Binding(Binding.withKeys("alt+<", "ctrl+home"), Binding.withHelp("alt+<", "input begin"));
        this.inputEnd = new Binding(Binding.withKeys("alt+>", "ctrl+end"), Binding.withHelp("alt+>", "input end"));
        this.capitalizeWordForward = new Binding(Binding.withKeys("alt+c"), Binding.withHelp("alt+c", "capitalize word forward"));
        this.lowercaseWordForward = new Binding(Binding.withKeys("alt+l"), Binding.withHelp("alt+l", "lowercase word forward"));
        this.uppercaseWordForward = new Binding(Binding.withKeys("alt+u"), Binding.withHelp("alt+u", "uppercase word forward"));
        this.transposeCharacterBackward = new Binding(Binding.withKeys("ctrl+t"), Binding.withHelp("ctrl+t", "transpose character backward"));
    }

    /**
     * Returns the character backward binding.
     *
     * @return character backward binding
     */
    public Binding characterBackward() {
        return characterBackward;
    }

    /**
     * Returns the character forward binding.
     *
     * @return character forward binding
     */
    public Binding characterForward() {
        return characterForward;
    }

    /**
     * Returns the delete after cursor binding.
     *
     * @return delete after cursor binding
     */
    public Binding deleteAfterCursor() {
        return deleteAfterCursor;
    }

    /**
     * Returns the delete before cursor binding.
     *
     * @return delete before cursor binding
     */
    public Binding deleteBeforeCursor() {
        return deleteBeforeCursor;
    }

    /**
     * Returns the delete character backward binding.
     *
     * @return delete character backward binding
     */
    public Binding deleteCharacterBackward() {
        return deleteCharacterBackward;
    }

    /**
     * Returns the delete character forward binding.
     *
     * @return delete character forward binding
     */
    public Binding deleteCharacterForward() {
        return deleteCharacterForward;
    }

    /**
     * Returns the delete word backward binding.
     *
     * @return delete word backward binding
     */
    public Binding deleteWordBackward() {
        return deleteWordBackward;
    }

    /**
     * Returns the delete word forward binding.
     *
     * @return delete word forward binding
     */
    public Binding deleteWordForward() {
        return deleteWordForward;
    }

    /**
     * Returns the insert newline binding.
     *
     * @return insert newline binding
     */
    public Binding insertNewline() {
        return insertNewline;
    }

    /**
     * Returns the line end binding.
     *
     * @return line end binding
     */
    public Binding lineEnd() {
        return lineEnd;
    }

    /**
     * Returns the line next binding.
     *
     * @return line next binding
     */
    public Binding lineNext() {
        return lineNext;
    }

    /**
     * Returns the line previous binding.
     *
     * @return line previous binding
     */
    public Binding linePrevious() {
        return linePrevious;
    }

    /**
     * Returns the line start binding.
     *
     * @return line start binding
     */
    public Binding lineStart() {
        return lineStart;
    }

    /**
     * Returns the paste binding.
     *
     * @return paste binding
     */
    public Binding paste() {
        return paste;
    }

    /**
     * Returns the word backward binding.
     *
     * @return word backward binding
     */
    public Binding wordBackward() {
        return wordBackward;
    }

    /**
     * Returns the word forward binding.
     *
     * @return word forward binding
     */
    public Binding wordForward() {
        return wordForward;
    }

    /**
     * Returns the input begin binding.
     *
     * @return input begin binding
     */
    public Binding inputBegin() {
        return inputBegin;
    }

    /**
     * Returns the input end binding.
     *
     * @return input end binding
     */
    public Binding inputEnd() {
        return inputEnd;
    }

    /**
     * Returns the uppercase word forward binding.
     *
     * @return uppercase word forward binding
     */
    public Binding uppercaseWordForward() {
        return uppercaseWordForward;
    }

    /**
     * Returns the lowercase word forward binding.
     *
     * @return lowercase word forward binding
     */
    public Binding lowercaseWordForward() {
        return lowercaseWordForward;
    }

    /**
     * Returns the capitalize word forward binding.
     *
     * @return capitalize word forward binding
     */
    public Binding capitalizeWordForward() {
        return capitalizeWordForward;
    }

    /**
     * Returns the transpose character backward binding.
     *
     * @return transpose character backward binding
     */
    public Binding transposeCharacterBackward() {
        return transposeCharacterBackward;
    }
}
