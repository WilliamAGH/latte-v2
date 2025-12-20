package com.williamcallahan.tui4j.springexample.view;

import com.williamcallahan.tui4j.compat.bubbletea.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultDelegate;
import org.springframework.stereotype.Component;

/**
 * Example program for book item delegate factory.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/view/BookItemDelegateFactory.java
 */
@Component
public class BookItemDelegateFactory {

    public static class DelegateKeyMap implements KeyMap {

        private final Binding remove;
        private final Binding choose;

        public DelegateKeyMap() {
            this.remove = new Binding(
                    Binding.withKeys("x"),
                    Binding.withHelp("x", "delete")
            );
            this.choose = new Binding(
                    Binding.withKeys("enter"),
                    Binding.withHelp("enter", "choose")
            );
        }

        @Override
        public Binding[] shortHelp() {
            return new Binding[]{
                    choose,
                    remove
            };
        }

        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{remove, choose}
            };
        }

        public Binding remove() {
            return remove;
        }

        public Binding choose() {
            return choose;
        }
    }

    public DefaultDelegate newBokItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();
        defaultDelegate.setShortHelpFunc(keyMap::shortHelp);
        defaultDelegate.setFullHelpFunc(keyMap::fullHelp);
        return defaultDelegate;
    }
}
