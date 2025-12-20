package com.williamcallahan.tui4j.ansi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests transition table.
 * tui4j: src/test/java/com/williamcallahan/tui4j/ansi/TransitionTableTest.java
 */
class TransitionTableTest {

    @Test
    void testDefaultTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = 0x00;

        TransitionTable.Transition transition = table.transition(initialState, code);

        // Update expectation to match the Go behavior
        assertEquals(State.GROUND, transition.state());
        assertEquals(Action.EXECUTE, transition.action()); // Correct expectation
    }

    @Test
    void testEscapeTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = 0x1B; // ESC character

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.ESCAPE, transition.state());
        assertEquals(Action.CLEAR, transition.action());
    }

    @Test
    void testCsiEntryTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = (byte) 0x9B; // CSI_ENTRY byte

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.CSI_ENTRY, transition.state());
        assertEquals(Action.CLEAR, transition.action());
    }

    @Test
    void testUtf8Transition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = (byte) 0xC2; // UTF-8 multibyte start

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.UTF8, transition.state());
        assertEquals(Action.COLLECT, transition.action());
    }

    @Test
    void testInvalidCode() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = (byte) 0xFF; // Unhandled byte

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.GROUND, transition.state()); // Should remain in GROUND
        assertEquals(Action.NONE, transition.action());
    }

    @Test
    void testRangeTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.ESCAPE;
        byte code = (byte) 0x31; // In range [0x30, 0x4F]

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.GROUND, transition.state());
        assertEquals(Action.DISPATCH, transition.action());
    }

    @Test
    void testIntermediateStateTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.ESCAPE_INTERMEDIATE;
        byte code = (byte) 0x20; // Intermediate range

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.ESCAPE_INTERMEDIATE, transition.state());
        assertEquals(Action.COLLECT, transition.action());
    }

    @Test
    void testSosStringTransition() {
        TransitionTable table = TransitionTable.get();
        State initialState = State.GROUND;
        byte code = (byte) 0x98; // SOS byte

        TransitionTable.Transition transition = table.transition(initialState, code);

        assertEquals(State.SOS_STRING, transition.state());
        assertEquals(Action.START, transition.action());
    }
}
