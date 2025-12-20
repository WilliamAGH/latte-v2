package com.williamcallahan.tui4j.compat.bubbletea.input.key;

import java.util.HashMap;
import java.util.Map;

/**
 * Port of Bubble Tea sequences.
 * Bubble Tea: bubbletea/key_sequences.go
 */
public class Sequences {

    public static final Map<String, Key> SEQUENCES = new HashMap<>();

    static {
        SEQUENCES.put("\u001B[A", new Key(KeyType.KeyUp));
        SEQUENCES.put("\u001B[B", new Key(KeyType.KeyDown));
        SEQUENCES.put("\u001B[C", new Key(KeyType.KeyRight));
        SEQUENCES.put("\u001B[D", new Key(KeyType.KeyLeft));
        SEQUENCES.put("\u001B[1;2A", new Key(KeyType.KeyShiftUp));
        SEQUENCES.put("\u001B[1;2B", new Key(KeyType.KeyShiftDown));
        SEQUENCES.put("\u001B[1;2C", new Key(KeyType.KeyShiftRight));
        SEQUENCES.put("\u001B[1;2D", new Key(KeyType.KeyShiftLeft));
        SEQUENCES.put("\u001B[OA", new Key(KeyType.KeyShiftUp)); // DECCKM
        SEQUENCES.put("\u001B[OB", new Key(KeyType.KeyShiftDown)); // DECCKM
        SEQUENCES.put("\u001B[OC", new Key(KeyType.KeyShiftRight)); // DECCKM
        SEQUENCES.put("\u001B[OD", new Key(KeyType.KeyShiftLeft)); // DECCKM
        SEQUENCES.put("\u001B[a", new Key(KeyType.KeyShiftUp)); // urxvt
        SEQUENCES.put("\u001B[b", new Key(KeyType.KeyShiftDown)); // urxvt
        SEQUENCES.put("\u001B[c", new Key(KeyType.KeyShiftRight)); // urxvt
        SEQUENCES.put("\u001B[d", new Key(KeyType.KeyShiftLeft)); // urxvt
        SEQUENCES.put("\u001B[1;3A", new Key(KeyType.KeyUp, true));
        SEQUENCES.put("\u001B[1;3B", new Key(KeyType.KeyDown, true));
        SEQUENCES.put("\u001B[1;3C", new Key(KeyType.KeyRight, true));
        SEQUENCES.put("\u001B[1;3D", new Key(KeyType.KeyLeft, true));
        SEQUENCES.put("\u001B[1;4A", new Key(KeyType.KeyShiftUp, true));
        SEQUENCES.put("\u001B[1;4B", new Key(KeyType.KeyShiftDown, true));
        SEQUENCES.put("\u001B[1;4C", new Key(KeyType.KeyShiftRight, true));
        SEQUENCES.put("\u001B[1;4D", new Key(KeyType.KeyShiftLeft, true));
        SEQUENCES.put("\u001B[1;5A", new Key(KeyType.KeyCtrlUp));
        SEQUENCES.put("\u001B[1;5B", new Key(KeyType.KeyCtrlDown));
        SEQUENCES.put("\u001B[1;5C", new Key(KeyType.KeyCtrlRight));
        SEQUENCES.put("\u001B[1;5D", new Key(KeyType.KeyCtrlLeft));
        SEQUENCES.put("\u001B[Oa", new Key(KeyType.KeyCtrlUp, true)); // urxvt
        SEQUENCES.put("\u001B[Ob", new Key(KeyType.KeyCtrlDown, true)); // urxvt
        SEQUENCES.put("\u001B[Oc", new Key(KeyType.KeyCtrlRight, true)); // urxvt
        SEQUENCES.put("\u001B[Od", new Key(KeyType.KeyCtrlLeft, true)); // urxvt
        SEQUENCES.put("\u001B[1;6A", new Key(KeyType.KeyCtrlShiftUp));
        SEQUENCES.put("\u001B[1;6B", new Key(KeyType.KeyCtrlShiftDown));
        SEQUENCES.put("\u001B[1;6C", new Key(KeyType.KeyCtrlShiftRight));
        SEQUENCES.put("\u001B[1;6D", new Key(KeyType.KeyCtrlShiftLeft));
        SEQUENCES.put("\u001B[1;7A", new Key(KeyType.KeyCtrlUp, true));
        SEQUENCES.put("\u001B[1;7B", new Key(KeyType.KeyCtrlDown, true));
        SEQUENCES.put("\u001B[1;7C", new Key(KeyType.KeyCtrlRight, true));
        SEQUENCES.put("\u001B[1;7D", new Key(KeyType.KeyCtrlLeft, true));
        SEQUENCES.put("\u001B[1;8A", new Key(KeyType.KeyCtrlShiftUp, true));
        SEQUENCES.put("\u001B[1;8B", new Key(KeyType.KeyCtrlShiftDown, true));
        SEQUENCES.put("\u001B[1;8C", new Key(KeyType.KeyCtrlShiftRight, true));
        SEQUENCES.put("\u001B[1;8D", new Key(KeyType.KeyCtrlShiftLeft, true));
        SEQUENCES.put("\u001B[Z", new Key(KeyType.KeyShiftTab));
        SEQUENCES.put("\u001B[2~", new Key(KeyType.KeyInsert));
        SEQUENCES.put("\u001B[3;2~", new Key(KeyType.KeyInsert, true));
        SEQUENCES.put("\u001B[3~", new Key(KeyType.KeyDelete));
        SEQUENCES.put("\u001B[3;3~", new Key(KeyType.KeyDelete, true));
        SEQUENCES.put("\u001B[5~", new Key(KeyType.KeyPgUp));
        SEQUENCES.put("\u001B[5;3~", new Key(KeyType.KeyPgUp, true));
        SEQUENCES.put("\u001B[5;5~", new Key(KeyType.KeyCtrlPgUp));
        SEQUENCES.put("\u001B[5^", new Key(KeyType.KeyCtrlPgUp)); // urxvt
        SEQUENCES.put("\u001B[5;7~", new Key(KeyType.KeyCtrlPgUp, true));
        SEQUENCES.put("\u001B[6~", new Key(KeyType.KeyPgDown));
        SEQUENCES.put("\u001B[6;3~", new Key(KeyType.KeyPgDown, true));
        SEQUENCES.put("\u001B[6;5~", new Key(KeyType.KeyCtrlPgDown));
        SEQUENCES.put("\u001B[6^", new Key(KeyType.KeyCtrlPgDown)); // urxvt
        SEQUENCES.put("\u001B[6;7~", new Key(KeyType.KeyCtrlPgDown, true));
        SEQUENCES.put("\u001B[1~", new Key(KeyType.KeyHome));
        SEQUENCES.put("\u001B[H", new Key(KeyType.KeyHome)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;3H", new Key(KeyType.KeyHome, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;5H", new Key(KeyType.KeyCtrlHome)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;7H", new Key(KeyType.KeyCtrlHome, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;2H", new Key(KeyType.KeyShiftHome)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;4H", new Key(KeyType.KeyShiftHome, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;6H", new Key(KeyType.KeyCtrlShiftHome)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;8H", new Key(KeyType.KeyCtrlShiftHome, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[4~", new Key(KeyType.KeyEnd));
        SEQUENCES.put("\u001B[F", new Key(KeyType.KeyEnd)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;3F", new Key(KeyType.KeyEnd, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;5F", new Key(KeyType.KeyCtrlEnd)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;7F", new Key(KeyType.KeyCtrlEnd, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;2F", new Key(KeyType.KeyShiftEnd)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;4F", new Key(KeyType.KeyShiftEnd, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;6F", new Key(KeyType.KeyCtrlShiftEnd)); // xterm, lxterm
        SEQUENCES.put("\u001B[1;8F", new Key(KeyType.KeyCtrlShiftEnd, true)); // xterm, lxterm
        SEQUENCES.put("\u001B[7~", new Key(KeyType.KeyHome)); // urxvt
        SEQUENCES.put("\u001B[7^", new Key(KeyType.KeyCtrlHome)); // urxvt
        SEQUENCES.put("\u001B[7$", new Key(KeyType.KeyShiftHome)); // urxvt
        SEQUENCES.put("\u001B[7@", new Key(KeyType.KeyCtrlShiftHome)); // urxvt
        SEQUENCES.put("\u001B[8~", new Key(KeyType.KeyEnd)); // urxvt
        SEQUENCES.put("\u001B[8^", new Key(KeyType.KeyCtrlEnd)); // urxvt
        SEQUENCES.put("\u001B[8$", new Key(KeyType.KeyShiftEnd)); // urxvt
        SEQUENCES.put("\u001B[8@", new Key(KeyType.KeyCtrlShiftEnd)); // urxvt
        SEQUENCES.put("\u001B[[A", new Key(KeyType.KeyF1)); // linux console
        SEQUENCES.put("\u001B[[B", new Key(KeyType.KeyF2)); // linux console
        SEQUENCES.put("\u001B[[C", new Key(KeyType.KeyF3)); // linux console
        SEQUENCES.put("\u001B[[D", new Key(KeyType.KeyF4)); // linux console
        SEQUENCES.put("\u001B[[E", new Key(KeyType.KeyF5)); // linux console
        SEQUENCES.put("\u001BOP", new Key(KeyType.KeyF1)); // vt100, xterm
        SEQUENCES.put("\u001BOQ", new Key(KeyType.KeyF2)); // vt100, xterm
        SEQUENCES.put("\u001BOR", new Key(KeyType.KeyF3)); // vt100, xterm
        SEQUENCES.put("\u001BOS", new Key(KeyType.KeyF4)); // vt100, xterm
        SEQUENCES.put("\u001B[1;3P", new Key(KeyType.KeyF1, true)); // vt100, xterm
        SEQUENCES.put("\u001B[1;3Q", new Key(KeyType.KeyF2, true)); // vt100, xterm
        SEQUENCES.put("\u001B[1;3R", new Key(KeyType.KeyF3, true)); // vt100, xterm
        SEQUENCES.put("\u001B[1;3S", new Key(KeyType.KeyF4, true)); // vt100, xterm
        SEQUENCES.put("\u001B[11~", new Key(KeyType.KeyF1)); // urxvt
        SEQUENCES.put("\u001B[12~", new Key(KeyType.KeyF2)); // urxvt
        SEQUENCES.put("\u001B[13~", new Key(KeyType.KeyF3)); // urxvt
        SEQUENCES.put("\u001B[14~", new Key(KeyType.KeyF4)); // urxvt
        SEQUENCES.put("\u001B[15~", new Key(KeyType.KeyF5)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[15;3~", new Key(KeyType.KeyF5, true)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[17~", new Key(KeyType.KeyF6)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[18~", new Key(KeyType.KeyF7)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[19~", new Key(KeyType.KeyF8)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[20~", new Key(KeyType.KeyF9)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[21~", new Key(KeyType.KeyF10)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[17;3~", new Key(KeyType.KeyF6, true)); // vt100, xterm
        SEQUENCES.put("\u001B[18;3~", new Key(KeyType.KeyF7, true)); // vt100, xterm
        SEQUENCES.put("\u001B[19;3~", new Key(KeyType.KeyF8, true)); // vt100, xterm
        SEQUENCES.put("\u001B[20;3~", new Key(KeyType.KeyF9, true)); // vt100, xterm
        SEQUENCES.put("\u001B[21;3~", new Key(KeyType.KeyF10, true)); // vt100, xterm
        SEQUENCES.put("\u001B[23~", new Key(KeyType.KeyF11)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[24~", new Key(KeyType.KeyF12)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[23;3~", new Key(KeyType.KeyF11, true)); // vt100, xterm
        SEQUENCES.put("\u001B[24;3~", new Key(KeyType.KeyF12, true)); // vt100, xterm
        SEQUENCES.put("\u001B[1;2P", new Key(KeyType.KeyF13));
        SEQUENCES.put("\u001B[1;2Q", new Key(KeyType.KeyF14));
        SEQUENCES.put("\u001B[25~", new Key(KeyType.KeyF13)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[26~", new Key(KeyType.KeyF14)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[25;3~", new Key(KeyType.KeyF13, true)); // vt100, xterm
        SEQUENCES.put("\u001B[26;3~", new Key(KeyType.KeyF14, true)); // vt100, xterm
        SEQUENCES.put("\u001B[1;2R", new Key(KeyType.KeyF15));
        SEQUENCES.put("\u001B[1;2S", new Key(KeyType.KeyF16));
        SEQUENCES.put("\u001B[28~", new Key(KeyType.KeyF15)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[29~", new Key(KeyType.KeyF16)); // vt100, xterm, also urxvt
        SEQUENCES.put("\u001B[28;3~", new Key(KeyType.KeyF15, true)); // vt100, xterm
        SEQUENCES.put("\u001B[29;3~", new Key(KeyType.KeyF16, true)); // vt100, xterm
        SEQUENCES.put("\u001B[15;2~", new Key(KeyType.KeyF17));
        SEQUENCES.put("\u001B[17;2~", new Key(KeyType.KeyF18));
        SEQUENCES.put("\u001B[18;2~", new Key(KeyType.KeyF19));
        SEQUENCES.put("\u001B[19;2~", new Key(KeyType.KeyF20));
        SEQUENCES.put("\u001B[31~", new Key(KeyType.KeyF17));
        SEQUENCES.put("\u001B[32~", new Key(KeyType.KeyF18));
        SEQUENCES.put("\u001B[33~", new Key(KeyType.KeyF19));
        SEQUENCES.put("\u001B[34~", new Key(KeyType.KeyF20));
        SEQUENCES.put("\u001BOA", new Key(KeyType.KeyUp)); // Powershell
        SEQUENCES.put("\u001BOB", new Key(KeyType.KeyDown)); // Powershell
        SEQUENCES.put("\u001BOC", new Key(KeyType.KeyRight)); // Powershell
        SEQUENCES.put("\u001BOD", new Key(KeyType.KeyLeft)); // Powershell
    }
}
