package com.williamcallahan.tui4j.compat.bubbletea.input.key;

/**
 * Port of Bubble Tea key type.
 * Bubble Tea: bubbletea/key.go
 */
public enum KeyType {
    keyNUL(0), keySOH(1), keySTX(2), keyETX(3), keyEOT(4), keyENQ(5), keyACK(6), keyBEL(7), keyBS(8),
    keyHT(9), keyLF(10), keyVT(11), keyFF(12), keyCR(13), keySO(14), keySI(15), keyDLE(16), keyDC1(17),
    keyDC2(18), keyDC3(19), keyDC4(20), keyNAK(21), keySYN(22), keyETB(23), keyCAN(24), keyEM(25),
    keySUB(26), keyESC(27), keyFS(28), keyGS(29), keyRS(30), keyUS(31), keyDEL(127),

    KeyRunes(-1), KeyUp(-2), KeyDown(-3), KeyRight(-4), KeyLeft(-5), KeyShiftTab(-6), KeyHome(-7),
    KeyEnd(-8), KeyPgUp(-9), KeyPgDown(-10), KeyCtrlPgUp(-11), KeyCtrlPgDown(-12), KeyDelete(-13),
    KeyInsert(-14), KeySpace(-15), KeyCtrlUp(-16), KeyCtrlDown(-17), KeyCtrlRight(-18), KeyCtrlLeft(-19),
    KeyCtrlHome(-20), KeyCtrlEnd(-21), KeyShiftUp(-22), KeyShiftDown(-23), KeyShiftRight(-24), KeyShiftLeft(-25),
    KeyShiftHome(-26), KeyShiftEnd(-27), KeyCtrlShiftUp(-28), KeyCtrlShiftDown(-29), KeyCtrlShiftLeft(-30),
    KeyCtrlShiftRight(-31), KeyCtrlShiftHome(-32), KeyCtrlShiftEnd(-33), KeyF1(-34), KeyF2(-35), KeyF3(-36),
    KeyF4(-37), KeyF5(-38), KeyF6(-39), KeyF7(-40), KeyF8(-41), KeyF9(-42), KeyF10(-43), KeyF11(-44),
    KeyF12(-45), KeyF13(-46), KeyF14(-47), KeyF15(-48), KeyF16(-49), KeyF17(-50), KeyF18(-51), KeyF19(-52),
    KeyF20(-53);

    private final int code;

    KeyType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static KeyType fromCode(int code) {
        for (KeyType key : values()) {
            if (key.code == code) {
                return key;
            }
        }
        throw new IllegalArgumentException("No KeyType with code " + code);
    }
}
