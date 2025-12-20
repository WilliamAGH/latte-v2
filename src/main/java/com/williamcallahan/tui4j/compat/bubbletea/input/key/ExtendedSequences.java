package com.williamcallahan.tui4j.compat.bubbletea.input.key;

import com.williamcallahan.tui4j.compat.bubbletea.input.key.KeyAliases.KeyAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * Port of Bubble Tea extended sequences.
 * Bubble Tea: bubbletea/key_sequences.go
 */
public class ExtendedSequences {

    public static final Map<String, Key> EXT_SEQUENCES;

    static {
        EXT_SEQUENCES = new HashMap<>();

        // Add all sequences from SEQUENCES
        for (Map.Entry<String, Key> entry : Sequences.SEQUENCES.entrySet()) {
            String seq = entry.getKey();
            Key key = entry.getValue();

            // Add the original sequence
            EXT_SEQUENCES.put(seq, new Key(key.type(), key.alt()));

            // Add the Alt version of the sequence if not already Alt
            if (!key.alt()) {
                EXT_SEQUENCES.put("\u001B" + seq, new Key(key.type(), true));
            }
        }

        // Add single-character keys and their Alt versions
        for (int i = KeyType.keyNUL.getCode() + 1; i <= KeyType.keyDEL.getCode(); i++) {
            if (i == KeyType.keyESC.getCode()) {
                continue;
            }

            // Add the single-character key
            EXT_SEQUENCES.put(String.valueOf((char) i), new Key(KeyType.fromCode(i)));

            // Add the Alt version of the single-character key
            EXT_SEQUENCES.put("\u001B" + (char) i, new Key(KeyType.fromCode(i), true));

            // Skip to DEL after US
            if (i == KeyType.keyUS.getCode()) {
                i = KeyType.keyDEL.getCode() - 1;
            }
        }

        // Special handling for space and escape sequences
        EXT_SEQUENCES.put(" ", new Key(KeyType.KeySpace, new char[]{' '}));
        EXT_SEQUENCES.put("\u001B ", new Key(KeyType.KeySpace, new char[]{' '}, true));
        EXT_SEQUENCES.put("\u001B\u001B", new Key(KeyAliases.getKeyType(KeyAlias.KeyEscape), true));
    }

    public static Key getKey(String sequence) {
        return EXT_SEQUENCES.get(sequence);
    }
}
