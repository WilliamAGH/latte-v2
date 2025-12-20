package com.williamcallahan.tui4j.compat.bubbletea.bubbles.key;

import com.williamcallahan.tui4j.compat.bubbletea.input.key.Key;
import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyType;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding.withHelp;
import static com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding.withKeys;

/**
 * Tests binding.
 * Bubble Tea: bubbletea/examples/help/main.go
 */
class BindingTest {

    @Test
    void test_ShouldTestEnabledScenario() {
        // given
        Binding binding = new Binding(
                withKeys("k", "up"),
                withHelp("↑/k", "move up")
        );

        // when
        KeyPressMessage keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyUp));
        boolean matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isTrue();

        // when
        keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{'k'}));
        matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isTrue();

        // when
        keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{'x'}));
        matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isFalse();
    }
}