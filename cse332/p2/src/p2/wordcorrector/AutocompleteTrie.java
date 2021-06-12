package p2.wordcorrector;

import cse332.types.AlphabeticString;
import datastructures.dictionaries.HashTrieMap;

public class AutocompleteTrie extends HashTrieMap<Character, AlphabeticString, Integer> {

    public AutocompleteTrie() {
        super(AlphabeticString.class);
    }

    public String autocomplete(String key) {
        @SuppressWarnings("unchecked")
        HashTrieNode current = (HashTrieNode) this.root;
        for (Character item : key.toCharArray()) {
            if (current.pointers.find(item) == null) {
                return null;
            }
            else {
                current = current.pointers.find(item);
            }
        }

        String result = key;

        while (current.pointers.size() == 1) {
            if (current.value != null) {
                return null;
            }
            result += current.pointers.iterator().next().key;
            current = current.pointers.iterator().next().value;
        }

        // Switch this to return null to only complete if we're at the end of
        // the word
        if (current.pointers.size() != 0) {
            return result;
        }
        return result;
    }
}
