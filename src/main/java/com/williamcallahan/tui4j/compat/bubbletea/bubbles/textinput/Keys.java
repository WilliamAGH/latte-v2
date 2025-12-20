package com.williamcallahan.tui4j.compat.bubbletea.bubbles.textinput;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;

/**
 * Port of Bubbles keys.
 * Bubble Tea: bubbletea/examples/textinput/main.go
 */
public class Keys {

    private final Binding characterForward;
    private final Binding characterBackward;
    private final Binding wordForward;
    private final Binding wordBackward;
    private final Binding deleteWordBackward;
    private final Binding deleteWordForward;
    private final Binding deleteAfterCursor;
    private final Binding deleteBeforeCursor;
    private final Binding deleteCharacterBackward;
    private final Binding deleteCharacterForward;
    private final Binding lineStart;
    private final Binding lineEnd;
    private final Binding paste;
    private final Binding acceptSuggestion;
    private final Binding nextSuggestion;
    private final Binding prevSuggestion;

    public Keys() {
        this.characterForward = new Binding(Binding.withKeys("right", "ctrl+f"));
        this.characterBackward = new Binding(Binding.withKeys("left", "ctrl+b"));
        this.wordForward = new Binding(Binding.withKeys("alt+right", "ctrl+right", "alt+f"));
        this.wordBackward = new Binding(Binding.withKeys("alt+left", "ctrl+left", "alt+b"));
        this.deleteWordBackward = new Binding(Binding.withKeys("alt+backspace", "ctrl+w"));
        this.deleteWordForward = new Binding(Binding.withKeys("alt+delete", "alt+d"));
        this.deleteAfterCursor = new Binding(Binding.withKeys("ctrl+k"));
        this.deleteBeforeCursor = new Binding(Binding.withKeys("ctrl+u"));
        this.deleteCharacterBackward = new Binding(Binding.withKeys("backspace", "ctrl+h"));
        this.deleteCharacterForward = new Binding(Binding.withKeys("delete", "ctrl+d"));
        this.lineStart = new Binding(Binding.withKeys("home", "ctrl+a"));
        this.lineEnd = new Binding(Binding.withKeys("end", "ctrl+e"));
        this.paste = new Binding(Binding.withKeys("ctrl+v"));
        this.acceptSuggestion = new Binding(Binding.withKeys("tab"));
        this.nextSuggestion = new Binding(Binding.withKeys("down", "ctrl+n"));
        this.prevSuggestion = new Binding(Binding.withKeys("up", "ctrl+p"));
    }

    public Binding characterForward() {
        return characterForward;
    }

    public Binding characterBackward() {
        return characterBackward;
    }

    public Binding wordForward() {
        return wordForward;
    }

    public Binding wordBackward() {
        return wordBackward;
    }

    public Binding deleteWordBackward() {
        return deleteWordBackward;
    }

    public Binding deleteWordForward() {
        return deleteWordForward;
    }

    public Binding deleteAfterCursor() {
        return deleteAfterCursor;
    }

    public Binding deleteBeforeCursor() {
        return deleteBeforeCursor;
    }

    public Binding deleteCharacterBackward() {
        return deleteCharacterBackward;
    }

    public Binding deleteCharacterForward() {
        return deleteCharacterForward;
    }

    public Binding lineStart() {
        return lineStart;
    }

    public Binding lineEnd() {
        return lineEnd;
    }

    public Binding paste() {
        return paste;
    }

    public Binding acceptSuggestion() {
        return acceptSuggestion;
    }

    public Binding nextSuggestion() {
        return nextSuggestion;
    }

    public Binding prevSuggestion() {
        return prevSuggestion;
    }
}
