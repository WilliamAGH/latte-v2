package com.williamcallahan.tui4j.examples.listfancy;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultDataSource;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.list.DefaultDelegate;

/**
 * Example program for delegate.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/Delegate.java
 */
public class Delegate {

    public static class DelegateKeyMap implements KeyMap {
        private final Binding choose;
        private final Binding remove;

        public DelegateKeyMap() {
            this.choose = new Binding(
                    Binding.withKeys("enter"),
                    Binding.withHelp("enter", "choose")
            );
            this.remove = new Binding(
                    Binding.withKeys("x", "backspace"),
                    Binding.withHelp("x", "delete")
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
                    new Binding[]{choose, remove},
            };
        }

        public Binding choose() {
            return choose;
        }

        public Binding remove() {
            return remove;
        }
    }

    public static DefaultDelegate newItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();
        defaultDelegate.onUpdate((msg, list) -> {
            if (msg instanceof KeyPressMessage keyPressMessage) {
                String title = null;
                int index = -1;
                if (list.selectedItem() instanceof FancyItem fancyItem && list.dataSource() instanceof DefaultDataSource defaultDataSource) {
                    index = defaultDataSource.indexOf(fancyItem);
                    title = fancyItem.title();
                } else {
                    return null;
                }

                if (Binding.matches(keyPressMessage, keyMap.choose())) {
                    return list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"You choose", title}));
                } else if (Binding.matches(keyPressMessage, keyMap.remove()) && index != -1) {
                    return Command.batch(
                            defaultDataSource.removeItem(index, () -> {
                                if (defaultDataSource.isEmpty()) {
                                    keyMap.remove().setEnabled(false);
                                }
                            }),
                            list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Deleted", title}))
                    );
                }
            }
            return null;
        });

        Binding[] help = new Binding[]{
                keyMap.choose(), keyMap.remove()
        };
        defaultDelegate.setShortHelpFunc(() -> help);
        defaultDelegate.setFullHelpFunc(() -> new Binding[][]{help});
        return defaultDelegate;
    }

}
