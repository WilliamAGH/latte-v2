package com.williamcallahan.tui4j.input;

import com.williamcallahan.tui4j.compat.bubbletea.input.MouseAction;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseButton;
import com.williamcallahan.tui4j.compat.bubbletea.input.MouseMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests mouse click tracker.
 * tui4j: src/test/java/com/williamcallahan/tui4j/input/MouseClickTrackerTest.java
 */
class MouseClickTrackerTest {

    @Test
    void test_ClickEmitted_OnPressReleaseSameCell() {
        MouseClickTracker tracker = new MouseClickTracker();
        MouseMessage press = new MouseMessage(
                5,
                7,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        );
        MouseMessage release = new MouseMessage(
                5,
                7,
                false,
                false,
                false,
                MouseAction.MouseActionRelease,
                MouseButton.MouseButtonNone
        );

        assertThat(tracker.handle(press, null, 1000L)).isNull();
        MouseClickMessage click = tracker.handle(release, null, 1100L);

        assertThat(click).isNotNull();
        assertThat(click.column()).isEqualTo(5);
        assertThat(click.row()).isEqualTo(7);
        assertThat(click.button()).isEqualTo(MouseButton.MouseButtonLeft);
        assertThat(click.clickCount()).isEqualTo(1);
    }

    @Test
    void test_ClickNotEmitted_OnDifferentReleaseCell() {
        MouseClickTracker tracker = new MouseClickTracker();
        MouseMessage press = new MouseMessage(
                2,
                3,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        );
        MouseMessage release = new MouseMessage(
                4,
                3,
                false,
                false,
                false,
                MouseAction.MouseActionRelease,
                MouseButton.MouseButtonNone
        );

        assertThat(tracker.handle(press, null, 1000L)).isNull();
        assertThat(tracker.handle(release, null, 1100L)).isNull();
    }

    @Test
    void test_ClickCount_IncrementsWithinThreshold() {
        MouseClickTracker tracker = new MouseClickTracker();
        MouseMessage press = new MouseMessage(
                1,
                1,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        );
        MouseMessage release = new MouseMessage(
                1,
                1,
                false,
                false,
                false,
                MouseAction.MouseActionRelease,
                MouseButton.MouseButtonNone
        );

        assertThat(tracker.handle(press, null, 1000L)).isNull();
        MouseClickMessage first = tracker.handle(release, null, 1050L);
        assertThat(first).isNotNull();
        assertThat(first.clickCount()).isEqualTo(1);

        assertThat(tracker.handle(press, null, 1200L)).isNull();
        MouseClickMessage second = tracker.handle(release, null, 1250L);
        assertThat(second).isNotNull();
        assertThat(second.clickCount()).isEqualTo(2);
    }

    @Test
    void test_TargetPropagated() {
        MouseClickTracker tracker = new MouseClickTracker();
        MouseTarget target = MouseTarget.click("test", 0, 0, 1, 1);
        MouseMessage press = new MouseMessage(
                0,
                0,
                false,
                false,
                false,
                MouseAction.MouseActionPress,
                MouseButton.MouseButtonLeft
        );
        MouseMessage release = new MouseMessage(
                0,
                0,
                false,
                false,
                false,
                MouseAction.MouseActionRelease,
                MouseButton.MouseButtonNone
        );

        assertThat(tracker.handle(press, target, 1000L)).isNull();
        MouseClickMessage click = tracker.handle(release, target, 1100L);
        assertThat(click).isNotNull();
        assertThat(click.target()).isEqualTo(target);
    }
}
