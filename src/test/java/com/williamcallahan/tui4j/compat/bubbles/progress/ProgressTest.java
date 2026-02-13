package com.williamcallahan.tui4j.compat.bubbles.progress;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;
import com.williamcallahan.tui4j.compat.lipgloss.color.ColorProfile;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProgressTest {

    @Test
    void testDefaultValues() {
        Progress progress = new Progress();

        assertThat(progress.width()).isEqualTo(40);
        assertThat(progress.full()).isEqualTo('█');
        assertThat(progress.empty()).isEqualTo('░');
        assertThat(progress.fullColor()).isEqualTo("#7571F9");
        assertThat(progress.emptyColor()).isEqualTo("#606060");
        assertThat(progress.showPercentage()).isTrue();
        assertThat(progress.percent()).isEqualTo(0.0);
        assertThat(progress.percentShown()).isEqualTo(0.0);
        assertThat(progress.isAnimating()).isFalse();
    }

    @Test
    void testWithWidth() {
        Progress progress = new Progress().withWidth(80);
        assertThat(progress.width()).isEqualTo(80);
    }

    @Test
    void testWithFullCharacter() {
        Progress progress = new Progress().withFull('#');
        assertThat(progress.full()).isEqualTo('#');
    }

    @Test
    void testWithEmptyCharacter() {
        Progress progress = new Progress().withEmpty('-');
        assertThat(progress.empty()).isEqualTo('-');
    }

    @Test
    void testWithFullColor() {
        Progress progress = new Progress().withFullColor("#ff0000");
        assertThat(progress.fullColor()).isEqualTo("#ff0000");
    }

    @Test
    void testWithEmptyColor() {
        Progress progress = new Progress().withEmptyColor("#0000ff");
        assertThat(progress.emptyColor()).isEqualTo("#0000ff");
    }

    @Test
    void testWithoutPercentage() {
        Progress progress = new Progress().withoutPercentage();
        assertThat(progress.showPercentage()).isFalse();
    }

    @Test
    void testWithPercentFormat() {
        Progress progress = new Progress().withPercentFormat(" %5.1f%%");
        assertThat(progress.percentFormat()).isEqualTo(" %5.1f%%");
    }

    @Test
    void testSetPercentCommand() {
        Progress progress = new Progress();
        Command cmd = progress.setPercent(0.5);

        assertThat(progress.targetPercent()).isEqualTo(0.5);
        assertThat(cmd).isNotNull();
    }

    @Test
    void testSetPercentClampedToOne() {
        Progress progress = new Progress();
        progress.setPercent(1.5);

        assertThat(progress.targetPercent()).isEqualTo(1.0);
    }

    @Test
    void testSetPercentClampedToZero() {
        Progress progress = new Progress();
        progress.setPercent(-0.5);

        assertThat(progress.targetPercent()).isEqualTo(0.0);
    }

    @Test
    void testIncrPercent() {
        Progress progress = new Progress();
        progress.setPercent(0.5);
        Command cmd = progress.incrPercent(0.2);

        assertThat(progress.targetPercent()).isEqualTo(0.7);
        assertThat(cmd).isNotNull();
    }

    @Test
    void testDecrPercent() {
        Progress progress = new Progress();
        progress.setPercent(0.5);
        Command cmd = progress.decrPercent(0.2);

        assertThat(progress.targetPercent()).isEqualTo(0.3);
        assertThat(cmd).isNotNull();
    }

    @Test
    void testUpdateWithFrameMsg() {
        Progress progress = new Progress();
        progress.setPercent(0.5);
        int currentTag = progress.tag();

        FrameMessage frameMsg = new FrameMessage(progress.id(), currentTag);
        UpdateResult<Progress> result = progress.update(frameMsg);

        assertThat(result.model()).isEqualTo(progress);
        assertThat(result.command()).isNotNull();
    }

    @Test
    void testUpdateWithWrongIdIgnored() {
        Progress progress = new Progress();
        progress.setPercent(0.5);
        double beforePercentShown = progress.percentShown();

        FrameMessage frameMsg = new FrameMessage(-1, progress.tag());
        progress.update(frameMsg);

        assertThat(progress.percentShown()).isEqualTo(beforePercentShown);
    }

    @Test
    void testUpdateWithWrongTagIgnored() {
        Progress progress = new Progress();
        progress.setPercent(0.5);
        double beforePercentShown = progress.percentShown();

        FrameMessage frameMsg = new FrameMessage(progress.id(), -1);
        progress.update(frameMsg);

        assertThat(progress.percentShown()).isEqualTo(beforePercentShown);
    }

    @Test
    void testViewWithZeroPercent() {
        Progress progress = new Progress().withWidth(10).withoutPercentage();
        String view = progress.view();

        assertThat(view).isNotNull();
        assertThat(view.length()).isGreaterThanOrEqualTo(10);
        assertThat(view).contains("░");
    }

    @Test
    void testViewWithFullPercent() {
        Progress progress = new Progress().withWidth(10).withoutPercentage();
        progress.setPercent(1.0);

        for (int i = 0; i < 60; i++) {
            FrameMessage frameMsg = new FrameMessage(progress.id(), progress.tag());
            progress.update(frameMsg);
        }

        String view = progress.view();

        assertThat(view).isNotNull();
        assertThat(view.length()).isGreaterThanOrEqualTo(10);
        assertThat(view).contains("█");
    }

    @Test
    void testWithDefaultGradient() {
        Progress progress = new Progress().withDefaultGradient();

        assertThat(progress.percentShown()).isEqualTo(0.0);
    }

    @Test
    void testWithScaledGradient() {
        Progress progress = new Progress().withScaledGradient("#ff0000", "#00ff00");

        assertThat(progress.percentShown()).isEqualTo(0.0);
    }

    @Test
    void testIsAnimatingWhenAtTarget() {
        Progress progress = new Progress();
        progress.setPercent(0.5);

        while (progress.isAnimating()) {
            FrameMessage frameMsg = new FrameMessage(progress.id(), progress.tag());
            progress.update(frameMsg);
        }

        assertThat(progress.isAnimating()).isFalse();
    }

    @Test
    void testInitReturnsNull() {
        Progress progress = new Progress();
        assertThat(progress.init()).isNull();
    }

    @Test
    void testViewAsWithPercentage() {
        Progress progress = new Progress().withWidth(10).withShowPercentage(true);
        progress.setColorProfile(ColorProfile.ANSI256);

        String view = progress.viewAs(0.5);

        assertThat(view).contains("50%");
    }

    @Test
    void testSetWidth() {
        Progress progress = new Progress();
        progress.setWidth(60);
        assertThat(progress.width()).isEqualTo(60);
    }
}
