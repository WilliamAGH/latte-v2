package com.williamcallahan.tui4j.ansi;

import java.util.Arrays;

/**
 * Transition table for ANSI parsing.
 * tui4j: src/main/java/com/williamcallahan/tui4j/ansi/TransitionTable.java
 */
public class TransitionTable {

    private static final TransitionTable instance = new TransitionTable();
    public static TransitionTable get() {
        return instance;
    }

    private static final int INDEX_STATE_SHIFT = 8;
    private static final int TRANSITION_STATE_MASK = 15;
    private static final int TRANSITION_ACTION_SHIFT = 4;
    private static final int DEFAULT_TABLE_SIZE = 4096;

    private final int[] table;

    private TransitionTable() {
        this.table = new int[DEFAULT_TABLE_SIZE];
        generateTable();
    }

    public void generateTable() {
        // Set default transitions
        setDefault(Action.NONE, State.GROUND);

        // Anywhere
        for (State state : new State[]{State.GROUND, State.UTF8}) {
            // Anywhere -> Ground
            addMany(new byte[]{0x18, 0x1a, (byte)0x99, (byte)0x9a}, state, Action.EXECUTE, State.GROUND);
            addRange(0x80, 0x8F, state, Action.EXECUTE, State.GROUND);
            addRange(0x90, 0x97, state, Action.EXECUTE, State.GROUND);
            addOne((byte)0x9C, state, Action.EXECUTE, State.GROUND);
            // Anywhere -> Escape
            addOne(0x1B, state, Action.CLEAR, State.ESCAPE);
            // Anywhere -> SosStringState
            addOne((byte)0x98, state, Action.START, State.SOS_STRING);
            // Anywhere -> PmStringState
            addOne((byte)0x9E, state, Action.START, State.PM_STRING);
            // Anywhere -> ApcStringState
            addOne((byte)0x9F, state, Action.START, State.APC_STRING);
            // Anywhere -> CsiEntry
            addOne((byte)0x9B, state, Action.CLEAR, State.CSI_ENTRY);
            // Anywhere -> DcsEntry
            addOne((byte)0x90, state, Action.CLEAR, State.DCS_ENTRY);
            // Anywhere -> OscString
            addOne((byte)0x9D, state, Action.START, State.OSC_STRING);
            // Anywhere -> Utf8
            addRange(0xC2, 0xDF, state, Action.COLLECT, State.UTF8);
            addRange(0xE0, 0xEF, state, Action.COLLECT, State.UTF8);
            addRange(0xF0, 0xF4, state, Action.COLLECT, State.UTF8);
        }

        // Ground
        addRange(0x00, 0x17, State.GROUND, Action.EXECUTE, State.GROUND);
        addOne(0x19, State.GROUND, Action.EXECUTE, State.GROUND);
        addRange(0x1C, 0x1F, State.GROUND, Action.EXECUTE, State.GROUND);
        addRange(0x20, 0x7E, State.GROUND, Action.PRINT, State.GROUND);
        addOne(0x7F, State.GROUND, Action.EXECUTE, State.GROUND);

        // EscapeIntermediate
        addRange(0x00, 0x17, State.ESCAPE_INTERMEDIATE, Action.EXECUTE, State.ESCAPE_INTERMEDIATE);
        addOne(0x19, State.ESCAPE_INTERMEDIATE, Action.EXECUTE, State.ESCAPE_INTERMEDIATE);
        addRange(0x1C, 0x1F, State.ESCAPE_INTERMEDIATE, Action.EXECUTE, State.ESCAPE_INTERMEDIATE);
        addRange(0x20, 0x2F, State.ESCAPE_INTERMEDIATE, Action.COLLECT, State.ESCAPE_INTERMEDIATE);
        addOne(0x7F, State.ESCAPE_INTERMEDIATE, Action.IGNORE, State.ESCAPE_INTERMEDIATE);
        // EscapeIntermediate -> Ground
        addRange(0x30, 0x7E, State.ESCAPE_INTERMEDIATE, Action.DISPATCH, State.GROUND);

        // Escape
        addRange(0x00, 0x17, State.ESCAPE, Action.EXECUTE, State.ESCAPE);
        addOne(0x19, State.ESCAPE, Action.EXECUTE, State.ESCAPE);
        addRange(0x1C, 0x1F, State.ESCAPE, Action.EXECUTE, State.ESCAPE);
        addOne(0x7F, State.ESCAPE, Action.IGNORE, State.ESCAPE);
        // Escape -> Ground
        addRange(0x30, 0x4F, State.ESCAPE, Action.DISPATCH, State.GROUND);
        addRange(0x51, 0x57, State.ESCAPE, Action.DISPATCH, State.GROUND);
        addOne('Y', State.ESCAPE, Action.DISPATCH, State.GROUND);
        addOne('Z', State.ESCAPE, Action.DISPATCH, State.GROUND);
        addOne('\\', State.ESCAPE, Action.DISPATCH, State.GROUND);
        addRange(0x60, 0x7E, State.ESCAPE, Action.DISPATCH, State.GROUND);
        // Escape -> Escape_intermediate
        addRange(0x20, 0x2F, State.ESCAPE, Action.COLLECT, State.ESCAPE_INTERMEDIATE);
        // Escape -> Sos_pm_apc_string
        addOne('X', State.ESCAPE, Action.START, State.SOS_STRING);
        addOne('^', State.ESCAPE, Action.START, State.PM_STRING);
        addOne('_', State.ESCAPE, Action.START, State.APC_STRING);
        // Escape -> Dcs_entry
        addOne('P', State.ESCAPE, Action.CLEAR, State.DCS_ENTRY);
        // Escape -> Csi_entry
        addOne('[', State.ESCAPE, Action.CLEAR, State.CSI_ENTRY);
        // Escape -> Osc_string
        addOne(']', State.ESCAPE, Action.START, State.OSC_STRING);

        // Sos/Pm/Apc string states
        for (State state : new State[]{State.SOS_STRING, State.PM_STRING, State.APC_STRING}) {
            addRange(0x00, 0x17, state, Action.PUT, state);
            addOne(0x19, state, Action.PUT, state);
            addRange(0x1C, 0x1F, state, Action.PUT, state);
            addRange(0x20, 0x7F, state, Action.PUT, state);
            // ESC, ST, CAN, and SUB terminate the sequence
            addOne(0x1B, state, Action.DISPATCH, State.ESCAPE);
            addOne((byte)0x9C, state, Action.DISPATCH, State.GROUND);
            addMany(new byte[]{0x18, 0x1A}, state, Action.IGNORE, State.GROUND);
        }

        // Dcs_entry
        addRange(0x00, 0x17, State.DCS_ENTRY, Action.IGNORE, State.DCS_ENTRY);
        addRange(0x0E, 0x17, State.DCS_ENTRY, Action.IGNORE, State.DCS_ENTRY);
        addOne(0x19, State.DCS_ENTRY, Action.IGNORE, State.DCS_ENTRY);
        addRange(0x1C, 0x1F, State.DCS_ENTRY, Action.IGNORE, State.DCS_ENTRY);
        addOne(0x7F, State.DCS_ENTRY, Action.IGNORE, State.DCS_ENTRY);
        // Dcs_entry -> Dcs_intermediate
        addRange(0x20, 0x2F, State.DCS_ENTRY, Action.COLLECT, State.DCS_INTERMEDIATE);
        // Dcs_entry -> Dcs_param
        addRange(0x30, 0x3B, State.DCS_ENTRY, Action.PARAM, State.DCS_PARAM);
        addRange(0x3C, 0x3F, State.DCS_ENTRY, Action.MARKER, State.DCS_PARAM);
        // Dcs_entry -> Dcs_passthrough
        addRange(0x08, 0x0D, State.DCS_ENTRY, Action.PUT, State.DCS_STRING);
        addOne(0x1B, State.DCS_ENTRY, Action.PUT, State.DCS_STRING);
        addRange(0x40, 0x7E, State.DCS_ENTRY, Action.START, State.DCS_STRING);

        // Dcs_intermediate
        addRange(0x00, 0x17, State.DCS_INTERMEDIATE, Action.IGNORE, State.DCS_INTERMEDIATE);
        addOne(0x19, State.DCS_INTERMEDIATE, Action.IGNORE, State.DCS_INTERMEDIATE);
        addRange(0x1C, 0x1F, State.DCS_INTERMEDIATE, Action.IGNORE, State.DCS_INTERMEDIATE);
        addRange(0x20, 0x2F, State.DCS_INTERMEDIATE, Action.COLLECT, State.DCS_INTERMEDIATE);
        addOne(0x7F, State.DCS_INTERMEDIATE, Action.IGNORE, State.DCS_INTERMEDIATE);
        // Dcs_intermediate -> Dcs_passthrough
        addRange(0x30, 0x3F, State.DCS_INTERMEDIATE, Action.START, State.DCS_STRING);
        addRange(0x40, 0x7E, State.DCS_INTERMEDIATE, Action.START, State.DCS_STRING);

        // Dcs_param
        addRange(0x00, 0x17, State.DCS_PARAM, Action.IGNORE, State.DCS_PARAM);
        addOne(0x19, State.DCS_PARAM, Action.IGNORE, State.DCS_PARAM);
        addRange(0x1C, 0x1F, State.DCS_PARAM, Action.IGNORE, State.DCS_PARAM);
        addRange(0x30, 0x3B, State.DCS_PARAM, Action.PARAM, State.DCS_PARAM);
        addOne(0x7F, State.DCS_PARAM, Action.IGNORE, State.DCS_PARAM);
        addRange(0x3C, 0x3F, State.DCS_PARAM, Action.IGNORE, State.DCS_PARAM);
        // Dcs_param -> Dcs_intermediate
        addRange(0x20, 0x2F, State.DCS_PARAM, Action.COLLECT, State.DCS_INTERMEDIATE);
        // Dcs_param -> Dcs_passthrough
        addRange(0x40, 0x7E, State.DCS_PARAM, Action.START, State.DCS_STRING);

        // Dcs_passthrough
        addRange(0x00, 0x17, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        addOne(0x19, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        addRange(0x1C, 0x1F, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        addRange(0x20, 0x7E, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        addOne(0x7F, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        addRange(0x80, 0xFF, State.DCS_STRING, Action.PUT, State.DCS_STRING);
        // ST, CAN, SUB, and ESC terminate the sequence
        addOne(0x1B, State.DCS_STRING, Action.DISPATCH, State.ESCAPE);
        addOne((byte)0x9C, State.DCS_STRING, Action.DISPATCH, State.GROUND);
        addMany(new byte[]{0x18, 0x1A}, State.DCS_STRING, Action.IGNORE, State.GROUND);

        // CSI states
        addRange(0x00, 0x17, State.CSI_ENTRY, Action.EXECUTE, State.CSI_ENTRY);
        addOne(0x19, State.CSI_ENTRY, Action.EXECUTE, State.CSI_ENTRY);
        addRange(0x1C, 0x1F, State.CSI_ENTRY, Action.EXECUTE, State.CSI_ENTRY);
        addOne(0x7F, State.CSI_ENTRY, Action.IGNORE, State.CSI_ENTRY);
        // CSI_entry -> Ground
        addRange(0x40, 0x7E, State.CSI_ENTRY, Action.DISPATCH, State.GROUND);
        // CSI_entry -> CSI_param
        addRange(0x30, 0x3B, State.CSI_ENTRY, Action.PARAM, State.CSI_PARAM);
        addRange(0x3C, 0x3F, State.CSI_ENTRY, Action.MARKER, State.CSI_PARAM);
        // CSI_entry -> CSI_intermediate
        addRange(0x20, 0x2F, State.CSI_ENTRY, Action.COLLECT, State.CSI_INTERMEDIATE);

        // CSI_param
        addRange(0x00, 0x17, State.CSI_PARAM, Action.EXECUTE, State.CSI_PARAM);
        addOne(0x19, State.CSI_PARAM, Action.EXECUTE, State.CSI_PARAM);
        addRange(0x1C, 0x1F, State.CSI_PARAM, Action.EXECUTE, State.CSI_PARAM);
        addRange(0x30, 0x3B, State.CSI_PARAM, Action.PARAM, State.CSI_PARAM);
        addOne(0x7F, State.CSI_PARAM, Action.IGNORE, State.CSI_PARAM);
        addRange(0x3C, 0x3F, State.CSI_PARAM, Action.IGNORE, State.CSI_PARAM);
        // CSI_param -> Ground
        addRange(0x40, 0x7E, State.CSI_PARAM, Action.DISPATCH, State.GROUND);
        // CSI_param -> CSI_intermediate
        addRange(0x20, 0x2F, State.CSI_PARAM, Action.COLLECT, State.CSI_INTERMEDIATE);

        // CSI_intermediate
        addRange(0x00, 0x17, State.CSI_INTERMEDIATE, Action.EXECUTE, State.CSI_INTERMEDIATE);
        addOne(0x19, State.CSI_INTERMEDIATE, Action.EXECUTE, State.CSI_INTERMEDIATE);
        addRange(0x1C, 0x1F, State.CSI_INTERMEDIATE, Action.EXECUTE, State.CSI_INTERMEDIATE);
        addRange(0x20, 0x2F, State.CSI_INTERMEDIATE, Action.COLLECT, State.CSI_INTERMEDIATE);
        addOne(0x7F, State.CSI_INTERMEDIATE, Action.IGNORE, State.CSI_INTERMEDIATE);
        // CSI_intermediate -> Ground
        addRange(0x40, 0x7E, State.CSI_INTERMEDIATE, Action.DISPATCH, State.GROUND);
        // CSI_intermediate -> CSI_ignore
        addRange(0x30, 0x3F, State.CSI_INTERMEDIATE, Action.IGNORE, State.GROUND);

        // OSC string
        addRange(0x00, 0x06, State.OSC_STRING, Action.IGNORE, State.OSC_STRING);
        addRange(0x08, 0x17, State.OSC_STRING, Action.IGNORE, State.OSC_STRING);
        addOne(0x19, State.OSC_STRING, Action.IGNORE, State.OSC_STRING);
        addRange(0x1C, 0x1F, State.OSC_STRING, Action.IGNORE, State.OSC_STRING);
        addRange(0x20, 0xFF, State.OSC_STRING, Action.PUT, State.OSC_STRING);

        // ST, CAN, SUB, ESC, and BEL terminate the sequence
        addOne(0x1B, State.OSC_STRING,Action.DISPATCH, State.ESCAPE);
        addOne(0x07, State.OSC_STRING, Action.DISPATCH, State.GROUND);
        addOne(0x9C, State.OSC_STRING, Action.DISPATCH, State.GROUND);
        addMany(new byte[]{0x18, 0x1A}, State.OSC_STRING, Action.IGNORE, State.GROUND);
    }

    private void setDefault(Action action, State nextState) {
        int value = (action.ordinal() << TRANSITION_ACTION_SHIFT) | nextState.ordinal();
        Arrays.fill(table, value);
    }

    private void addOne(int code, State state, Action action, State nextState) {
        int index = (state.ordinal() << INDEX_STATE_SHIFT) | (code & 0xFF); // Ensure code is unsigned
        table[index] = (action.ordinal() << TRANSITION_ACTION_SHIFT) | nextState.ordinal();
    }

    private void addRange(int start, int end, State state, Action action, State nextState) {
        for (int code = start; code <= end; code++) {
            addOne(code, state, action, nextState);
        }
    }

    private void addMany(byte[] codes, State state, Action action, State nextState) {
        int value = (action.ordinal() << TRANSITION_ACTION_SHIFT) | nextState.ordinal();
        for (byte code : codes) {
            int index = state.ordinal() << INDEX_STATE_SHIFT | (code & 0xFF); // Ensure unsigned code
            table[index] = value;
        }
    }

    public Transition transition(State state, byte code) {
        int index = (state.ordinal() << INDEX_STATE_SHIFT) | (code & 0xFF);
        int value = table[index];
        State nextState = State.values()[value & TRANSITION_STATE_MASK];
        Action action = Action.values()[(value >> TRANSITION_ACTION_SHIFT)];

        return new Transition(nextState, action);
    }

    public record Transition(State state, Action action) {}
}
