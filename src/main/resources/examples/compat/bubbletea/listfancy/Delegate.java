package com.williamcallahan.tui4j.examples.listfancy;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbles.key.Binding;
import com.williamcallahan.tui4j.compat.bubbles.list.DefaultDataSource;
import com.williamcallahan.tui4j.compat.bubbles.list.DefaultDelegate;

/**
 * Defines a custom list delegate and key map for the fancy list example.
 * Upstream: bubbletea/examples/list-fancy/delegate.go
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/Delegate.java
 */
public class Delegate {

    /**
     * Key bindings for choosing and removing list items.
     */
    public static class DelegateKeyMap implements KeyMap {
        private final Binding choose;
        private final Binding remove;

        /**
         * Initializes the choose/remove bindings.
         */
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

        /**
         * Returns the short help bindings.
         *
         * @return short help bindings
         */
        @Override
        public Binding[] shortHelp() {
            return new Binding[]{
                    choose,
                    remove
            };
        }

        /**
         * Returns the full help bindings.
         *
         * @return full help bindings
         */
        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{choose, remove},
            };
        }

        /**
         * Returns the binding for choosing an item.
         *
         * @return choose binding
         */
        public Binding choose() {
            return choose;
        }

        /**
         * Returns the binding for removing an item.
         *
         * @return remove binding
         */
        public Binding remove() {
            return remove;
        }
    }

    /**
     * Builds the list delegate with custom update and help hooks.
     *
     * @param keyMap key bindings for actions
     * @return configured delegate
     */
    public static DefaultDelegate newItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();
        defaultDelegate.onUpdate((msg, list) -> {
            if (!(msg instanceof KeyPressMessage)) {
               return null;
            }

            var keyPressMessage = (KeyPressMessage) msg;

            if (!Binding.matches(keyPressMessage, keyMap.choose(), keyMap.remove())) {
               return null;
            }

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
            } 
            if (Binding.matches(keyPressMessage, keyMap.remove()) && index != -1) {
                return Command.batch(
                        defaultDataSource.removeItem(index, () -> {
                            if (defaultDataSource.isEmpty()) {
                                keyMap.remove().setEnabled(false);
                            }
                        }),
                        list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Deleted", title}))
                );
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
