package com.williamcallahan.tui4j.compat.bubbletea.input.key;

import java.util.EnumMap;

/**
 * Port of Bubble Tea key aliases.
 * Bubble Tea: bubbletea/key.go
 */
public class KeyAliases {
    public static final EnumMap<KeyAlias, KeyType> CONTROL_KEY_ALIASES = new EnumMap<>(KeyAlias.class);

    public enum KeyAlias {
        KeyNull, KeyBreak, KeyEnter, KeyBackspace, KeyTab, KeyEsc, KeyEscape,
        KeyCtrlAt, KeyCtrlA, KeyCtrlB, KeyCtrlC, KeyCtrlD, KeyCtrlE, KeyCtrlF,
        KeyCtrlG, KeyCtrlH, KeyCtrlI, KeyCtrlJ, KeyCtrlK, KeyCtrlL, KeyCtrlM,
        KeyCtrlN, KeyCtrlO, KeyCtrlP, KeyCtrlQ, KeyCtrlR, KeyCtrlS, KeyCtrlT,
        KeyCtrlU, KeyCtrlV, KeyCtrlW, KeyCtrlX, KeyCtrlY, KeyCtrlZ,
        KeyCtrlOpenBracket, KeyCtrlBackslash, KeyCtrlCloseBracket, KeyCtrlCaret,
        KeyCtrlUnderscore, KeyCtrlQuestionMark
    }

    static {
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyNull, KeyType.keyNUL);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyBreak, KeyType.keyETX);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyEnter, KeyType.keyCR);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyBackspace, KeyType.keyDEL);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyTab, KeyType.keyHT);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyEsc, KeyType.keyESC);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyEscape, KeyType.keyESC);

        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlAt, KeyType.keyNUL);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlA, KeyType.keySOH);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlB, KeyType.keySTX);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlC, KeyType.keyETX);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlD, KeyType.keyEOT);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlE, KeyType.keyENQ);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlF, KeyType.keyACK);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlG, KeyType.keyBEL);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlH, KeyType.keyBS);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlI, KeyType.keyHT);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlJ, KeyType.keyLF);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlK, KeyType.keyVT);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlL, KeyType.keyFF);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlM, KeyType.keyCR);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlN, KeyType.keySO);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlO, KeyType.keySI);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlP, KeyType.keyDLE);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlQ, KeyType.keyDC1);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlR, KeyType.keyDC2);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlS, KeyType.keyDC3);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlT, KeyType.keyDC4);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlU, KeyType.keyNAK);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlV, KeyType.keySYN);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlW, KeyType.keyETB);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlX, KeyType.keyCAN);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlY, KeyType.keyEM);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlZ, KeyType.keySUB);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlOpenBracket, KeyType.keyESC);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlBackslash, KeyType.keyFS);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlCloseBracket, KeyType.keyGS);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlCaret, KeyType.keyRS);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlUnderscore, KeyType.keyUS);
        CONTROL_KEY_ALIASES.put(KeyAlias.KeyCtrlQuestionMark, KeyType.keyDEL);
    }

    public static KeyType getKeyType(KeyAlias alias) {
        return CONTROL_KEY_ALIASES.get(alias);
    }
}
