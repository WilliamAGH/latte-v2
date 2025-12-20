package com.williamcallahan.tui4j.compat.bubbletea.input.key;

import java.util.HashMap;
import java.util.Map;

/**
 * Port of Bubble Tea key names.
 * Bubble Tea: bubbletea/key.go
 */
public class KeyNames {
    private static final Map<KeyType, String> KEY_NAMES = new HashMap<>();

    static {
        // Control keys
        KEY_NAMES.put(KeyType.keyNUL, "ctrl+@"); // also ctrl+` (that's ctrl+backtick)
        KEY_NAMES.put(KeyType.keySOH, "ctrl+a");
        KEY_NAMES.put(KeyType.keySTX, "ctrl+b");
        KEY_NAMES.put(KeyType.keyETX, "ctrl+c");
        KEY_NAMES.put(KeyType.keyEOT, "ctrl+d");
        KEY_NAMES.put(KeyType.keyENQ, "ctrl+e");
        KEY_NAMES.put(KeyType.keyACK, "ctrl+f");
        KEY_NAMES.put(KeyType.keyBEL, "ctrl+g");
        KEY_NAMES.put(KeyType.keyBS, "ctrl+h");
        KEY_NAMES.put(KeyType.keyHT, "tab"); // also ctrl+i
        KEY_NAMES.put(KeyType.keyLF, "ctrl+j");
        KEY_NAMES.put(KeyType.keyVT, "ctrl+k");
        KEY_NAMES.put(KeyType.keyFF, "ctrl+l");
        KEY_NAMES.put(KeyType.keyCR, "enter");
        KEY_NAMES.put(KeyType.keySO, "ctrl+n");
        KEY_NAMES.put(KeyType.keySI, "ctrl+o");
        KEY_NAMES.put(KeyType.keyDLE, "ctrl+p");
        KEY_NAMES.put(KeyType.keyDC1, "ctrl+q");
        KEY_NAMES.put(KeyType.keyDC2, "ctrl+r");
        KEY_NAMES.put(KeyType.keyDC3, "ctrl+s");
        KEY_NAMES.put(KeyType.keyDC4, "ctrl+t");
        KEY_NAMES.put(KeyType.keyNAK, "ctrl+u");
        KEY_NAMES.put(KeyType.keySYN, "ctrl+v");
        KEY_NAMES.put(KeyType.keyETB, "ctrl+w");
        KEY_NAMES.put(KeyType.keyCAN, "ctrl+width");
        KEY_NAMES.put(KeyType.keyEM, "ctrl+height");
        KEY_NAMES.put(KeyType.keySUB, "ctrl+z");
        KEY_NAMES.put(KeyType.keyESC, "esc");
        KEY_NAMES.put(KeyType.keyFS, "ctrl+\\");
        KEY_NAMES.put(KeyType.keyGS, "ctrl+]");
        KEY_NAMES.put(KeyType.keyRS, "ctrl+^");
        KEY_NAMES.put(KeyType.keyUS, "ctrl+_");
        KEY_NAMES.put(KeyType.keyDEL, "backspace");

        // Other keys
        KEY_NAMES.put(KeyType.KeyRunes, "runes");
        KEY_NAMES.put(KeyType.KeyUp, "up");
        KEY_NAMES.put(KeyType.KeyDown, "down");
        KEY_NAMES.put(KeyType.KeyRight, "right");
        KEY_NAMES.put(KeyType.KeySpace, " "); // for backwards compatibility
        KEY_NAMES.put(KeyType.KeyLeft, "left");
        KEY_NAMES.put(KeyType.KeyShiftTab, "shift+tab");
        KEY_NAMES.put(KeyType.KeyHome, "home");
        KEY_NAMES.put(KeyType.KeyEnd, "end");
        KEY_NAMES.put(KeyType.KeyCtrlHome, "ctrl+home");
        KEY_NAMES.put(KeyType.KeyCtrlEnd, "ctrl+end");
        KEY_NAMES.put(KeyType.KeyShiftHome, "shift+home");
        KEY_NAMES.put(KeyType.KeyShiftEnd, "shift+end");
        KEY_NAMES.put(KeyType.KeyCtrlShiftHome, "ctrl+shift+home");
        KEY_NAMES.put(KeyType.KeyCtrlShiftEnd, "ctrl+shift+end");
        KEY_NAMES.put(KeyType.KeyPgUp, "pgup");
        KEY_NAMES.put(KeyType.KeyPgDown, "pgdown");
        KEY_NAMES.put(KeyType.KeyCtrlPgUp, "ctrl+pgup");
        KEY_NAMES.put(KeyType.KeyCtrlPgDown, "ctrl+pgdown");
        KEY_NAMES.put(KeyType.KeyDelete, "delete");
        KEY_NAMES.put(KeyType.KeyInsert, "insert");
        KEY_NAMES.put(KeyType.KeyCtrlUp, "ctrl+up");
        KEY_NAMES.put(KeyType.KeyCtrlDown, "ctrl+down");
        KEY_NAMES.put(KeyType.KeyCtrlRight, "ctrl+right");
        KEY_NAMES.put(KeyType.KeyCtrlLeft, "ctrl+left");
        KEY_NAMES.put(KeyType.KeyShiftUp, "shift+up");
        KEY_NAMES.put(KeyType.KeyShiftDown, "shift+down");
        KEY_NAMES.put(KeyType.KeyShiftRight, "shift+right");
        KEY_NAMES.put(KeyType.KeyShiftLeft, "shift+left");
        KEY_NAMES.put(KeyType.KeyCtrlShiftUp, "ctrl+shift+up");
        KEY_NAMES.put(KeyType.KeyCtrlShiftDown, "ctrl+shift+down");
        KEY_NAMES.put(KeyType.KeyCtrlShiftLeft, "ctrl+shift+left");
        KEY_NAMES.put(KeyType.KeyCtrlShiftRight, "ctrl+shift+right");
        KEY_NAMES.put(KeyType.KeyF1, "f1");
        KEY_NAMES.put(KeyType.KeyF2, "f2");
        KEY_NAMES.put(KeyType.KeyF3, "f3");
        KEY_NAMES.put(KeyType.KeyF4, "f4");
        KEY_NAMES.put(KeyType.KeyF5, "f5");
        KEY_NAMES.put(KeyType.KeyF6, "f6");
        KEY_NAMES.put(KeyType.KeyF7, "f7");
        KEY_NAMES.put(KeyType.KeyF8, "f8");
        KEY_NAMES.put(KeyType.KeyF9, "f9");
        KEY_NAMES.put(KeyType.KeyF10, "f10");
        KEY_NAMES.put(KeyType.KeyF11, "f11");
        KEY_NAMES.put(KeyType.KeyF12, "f12");
        KEY_NAMES.put(KeyType.KeyF13, "f13");
        KEY_NAMES.put(KeyType.KeyF14, "f14");
        KEY_NAMES.put(KeyType.KeyF15, "f15");
        KEY_NAMES.put(KeyType.KeyF16, "f16");
        KEY_NAMES.put(KeyType.KeyF17, "f17");
        KEY_NAMES.put(KeyType.KeyF18, "f18");
        KEY_NAMES.put(KeyType.KeyF19, "f19");
        KEY_NAMES.put(KeyType.KeyF20, "f20");
    }

    public static String getKeyName(KeyType keyType) {
        return KEY_NAMES.getOrDefault(keyType, "unknown");
    }
}
