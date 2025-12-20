package com.williamcallahan.tui4j.compat.bubbletea.bubbles.key;

import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;

/**
 * Port of Bubbles binding.
 * Bubble Tea: bubbletea/examples/help/main.go
 */
public class Binding {

    public interface BindingOption {

        void apply(Binding binding);
    }
    private String[] keys;

    private Help help;
    private boolean enabled;

    public Binding(BindingOption... opts) {
        this.help = new Help();
        this.enabled = true;

        for (BindingOption option : opts) {
            option.apply(this);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void unbind() {
        this.keys = new String[0];
        this.help = new Help();
    }

    public Help help() {
        return help;
    }

    public boolean matches(KeyPressMessage keyPressMessage) {
        for (String bindingKey : keys) {
            if (bindingKey.equals(keyPressMessage.key())) {
                return true;
            }
        }
        return false;
    }

    public static BindingOption withKeys(String... keys) {
        return binding -> binding.keys = keys;
    }

    public static BindingOption withHelp(String key, String desc) {
        return binding -> binding.help = new Help(key, desc);
    }

    public static boolean matches(KeyPressMessage keyPressMessage, Binding... bindings) {
        for (Binding binding : bindings) {
            if (!binding.isEnabled()) {
                continue;
            }
            if (binding.matches(keyPressMessage)) {
                return true;
            }
        }
        return false;
    }
}
