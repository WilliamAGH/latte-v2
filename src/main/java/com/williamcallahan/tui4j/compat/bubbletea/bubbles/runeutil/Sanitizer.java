package com.williamcallahan.tui4j.compat.bubbletea.bubbles.runeutil;

import com.ibm.icu.lang.UCharacter;

import java.util.function.Consumer;

/**
 * Port of Bubbles sanitizer.
 * Bubble Tea: bubbletea/examples/textinput/main.go
 */
public class Sanitizer {

    private String replaceNewLine;
    private String replaceTab;

    // Constructor to initialize the sanitizer with options
    @SafeVarargs
    public Sanitizer(Consumer<Sanitizer>... options) {
        this.replaceNewLine = "\n";
        this.replaceTab = "    ";
        for (Consumer<Sanitizer> option : options) {
            option.accept(this);
        }
    }

    // Method to sanitize the input char array
    public char[] sanitize(char[] runes) {
        StringBuilder result = new StringBuilder(runes.length);
        boolean copied = false;

        for (int src = 0; src < runes.length; src++) {
            char r = runes[src];
            switch (r) {
                case '\r':
                case '\n':
                    if (result.length() + replaceNewLine.length() > src && !copied) {
                        result.ensureCapacity(runes.length + replaceNewLine.length());
                        copied = true;
                    }
                    result.append(replaceNewLine);
                    break;
                case '\t':
                    if (result.length() + replaceTab.length() > src && !copied) {
                        result.ensureCapacity(runes.length + replaceTab.length());
                        copied = true;
                    }
                    result.append(replaceTab);
                    break;
                default:
                    if (!isLatinLetter(r)) {
                        continue;
                    } else {
                        result.append(r);
                    }
                    break;
            }
        }
        return result.toString().toCharArray();
    }

    // Function to replace tabs
    public static Consumer<Sanitizer> replaceTabs(String tabReplacement) {
        return sanitizer -> sanitizer.replaceTab = tabReplacement;
    }

    // Function to replace newlines
    public static Consumer<Sanitizer> replaceNewlines(String newlineReplacement) {
        return sanitizer -> sanitizer.replaceNewLine = newlineReplacement;
    }

    private static boolean isLatinLetter(char c) {
        int type = UCharacter.getType(c);
        return UCharacter.isLetterOrDigit(c) ||
                UCharacter.isWhitespace(c) ||
                type == UCharacter.OTHER_PUNCTUATION ||
                type == UCharacter.MATH_SYMBOL ||
                type == UCharacter.DASH_PUNCTUATION ||
                type == UCharacter.CURRENCY_SYMBOL ||
                type == UCharacter.START_PUNCTUATION ||
                type == UCharacter.END_PUNCTUATION ||
                type == UCharacter.MODIFIER_SYMBOL ||
                type == UCharacter.CONNECTOR_PUNCTUATION;
    }

}
