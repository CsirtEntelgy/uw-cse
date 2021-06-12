package datastructures.dictionaries;

import cse332.interfaces.misc.BString;
import cse332.interfaces.trie.TrieSet;

/**
 * @Description: Set implementation of the HashTrieMap.
 */
public class HashTrieSet<A extends Comparable<A>, E extends BString<A>> extends TrieSet<A, E> {
    public HashTrieSet(Class<E> Type) {
    	super(new HashTrieMap<A, E, Boolean>(Type));
    }
}