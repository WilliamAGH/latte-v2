package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests mouse selection tracker.
 * tui4j: src/test/java/com/williamcallahan/tui4j/input/MouseSelectionTrackerTest.java
 */
class MouseSelectionTrackerTest {

    @Test
    void test_ShouldStartSelection_OnLeftPress() {
        MouseSelectionTracker tracker = new MouseSelectionTracker();
        MouseMessage press = new MouseMessage(
                4,
                2,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        );

        MouseSelectionUpdate update = tracker.update(press);

        assertThat(update.selectionStarted()).isTrue();
        assertThat(update.selectionActive()).isTrue();
        assertThat(update.selectionEnded()).isFalse();
        assertThat(update.selectionScrollUpdate()).isNull();
    }

    @Test
    void test_ShouldEndSelection_OnRelease() {
        MouseSelectionTracker tracker = new MouseSelectionTracker();
        tracker.update(new MouseMessage(
                1,
                1,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        ));

        MouseMessage release = new MouseMessage(
                1,
                1,
                false,
                false,
                false,
                MouseAction.MouseActionRelease,
                MouseButton.MouseButtonNone
        );

        MouseSelectionUpdate update = tracker.update(release);

        assertThat(update.selectionStarted()).isFalse();
        assertThat(update.selectionActive()).isFalse();
        assertThat(update.selectionEnded()).isTrue();
        assertThat(update.selectionScrollUpdate()).isNull();
    }

    @Test
    void test_ShouldCreateSelectionUpdate_OnWheelWhileSelecting() {
        MouseSelectionTracker tracker = new MouseSelectionTracker();
        tracker.update(new MouseMessage(
                3,
                6,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        ));

        MouseMessage wheel = new MouseMessage(
                3,
                6,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonWheelUp
        );

        MouseSelectionUpdate update = tracker.update(wheel);

        assertThat(update.selectionActive()).isTrue();
        assertThat(update.selectionScrollUpdate()).isNotNull();
        assertThat(update.selectionScrollUpdate().getAction()).isEqualTo(MouseAction.MouseActionMotion);
        assertThat(update.selectionScrollUpdate().getButton()).isEqualTo(MouseButton.MouseButtonLeft);
        assertThat(update.selectionScrollUpdate().column()).isEqualTo(3);
        assertThat(update.selectionScrollUpdate().row()).isEqualTo(6);
    }

    @Test
    void test_ShouldNotCreateSelectionUpdate_OnWheelWithoutSelection() {
        MouseSelectionTracker tracker = new MouseSelectionTracker();
        MouseMessage wheel = new MouseMessage(
                7,
                9,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonWheelDown
        );

        MouseSelectionUpdate update = tracker.update(wheel);

        assertThat(update.selectionActive()).isFalse();
        assertThat(update.selectionScrollUpdate()).isNull();
    }

    @Test
    void test_ShouldEndSelection_OnMotionWithNoButton() {
        MouseSelectionTracker tracker = new MouseSelectionTracker();
        tracker.update(new MouseMessage(
                2,
                2,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        ));

        MouseMessage motionNoButton = new MouseMessage(
                3,
                3,
                false,
                false,
                false,
                MouseAction.MouseActionMotion,
                MouseButton.MouseButtonNone
        );

        MouseSelectionUpdate update = tracker.update(motionNoButton);

        assertThat(update.selectionActive()).isFalse();
        assertThat(update.selectionEnded()).isTrue();
    }
}
