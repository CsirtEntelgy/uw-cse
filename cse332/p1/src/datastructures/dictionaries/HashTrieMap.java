package datastructures.dictionaries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.BString;
import cse332.interfaces.trie.TrieMap;

/**
 * See cse332/interfaces/trie/TrieMap.java and
 * cse332/interfaces/misc/Dictionary.java for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
	public HashTrieNode() {
	    this(null);
	}

	public HashTrieNode(V value) {
	    this.pointers = new HashMap<A, HashTrieNode>();
	    this.value = value;
	}

	@Override
	public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
	    return pointers.entrySet().iterator();
	}
    }

    public HashTrieMap(Class<K> KClass) {
	super(KClass);
	this.root = new HashTrieNode();
    }

    /**
     * Associates the specified value with the specified key in this map. If the map
     * previously contained a mapping for the key, the old value is replaced.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if
     *         there was no mapping for <tt>key</tt>.
     * @throws IllegalArgumentException
     *             if either key or value is null.
     */
    @Override
    public V insert(K key, V value) {
	if (key == null || value == null) {
	    throw new IllegalArgumentException();
	}
	if (key.size() == 0) {
	    this.root.value = value;
	    size++;
	}
	HashTrieNode result = iterateKey(key, true);
	V temp = result.value;
	if (result.value == null) {
	    size++;
	}
	result.value = value;
	return temp;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if
     * this map contains no mapping for the key.
     *
     * @param key
     *            the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if
     *         this map contains no mapping for the key
     * @throws IllegalArgumentException
     *             if key is null.
     */
    @Override
    public V find(K key) {
	if (key == null) {
	    throw new IllegalArgumentException();
	}
	HashTrieNode temp = iterateKey(key, false);
	if (temp == null)
	    return null;
	else
	    return temp.value;
    }

    /**
     * Helper method for insert and find that will traverse the map and return the
     * key it found
     * 
     * @param key
     *            the key that we wnat to find
     * @param bool
     *            indication variable to know which method is calling
     * @return will return the node that the key points to
     * @effect this will add new nodes to the triemap if being called by insert
     */
    private HashTrieNode iterateKey(K key, boolean bool) {
	Iterator<A> keyIt = key.iterator();
	HashTrieNode current = (HashTrieMap<A, K, V>.HashTrieNode) this.root;
	while (keyIt.hasNext()) {
	    A temp = keyIt.next();
	    if (!current.pointers.containsKey(temp)) {
		if (bool)
		    current.pointers.put(temp, new HashTrieNode());
		else
		    return null;
	    }
	    current = current.pointers.get(temp);
	}
	return current;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for which the key starts
     * with the specified key prefix.
     *
     * @param keyPrefix
     *            The prefix of a key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping whose key starts with
     *         the specified key prefix.
     * @throws IllegalArgumentException
     *             if the key is null.
     */
    @Override
    public boolean findPrefix(K key) {
	if (key == null) {
	    throw new IllegalArgumentException();
	}
	Iterator<A> keyIt2 = key.iterator();
	HashTrieNode current = (HashTrieMap<A, K, V>.HashTrieNode) this.root;
	while (keyIt2.hasNext()) {
	    A temp = keyIt2.next();
	    if (!current.pointers.containsKey(temp)) {
		return false;
	    }
	    current = current.pointers.get(temp);
	}
	return true;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key
     *            key whose mapping is to be removed from the map
     * @throws IllegalArgumentException
     *             if key is null.
     */
    @Override
    public void delete(K key) {
	if (key == null) {
	    throw new IllegalArgumentException();
	}
	if (key.size() == 0) {
	    if (this.root.value != null) {
		this.root.value = null;
		size--;
	    }
	}
	Iterator<A> keyIt3 = key.iterator();
	HashTrieNode current = (HashTrieNode) this.root;
	HashTrieNode deletePoint = current;
	A deleteKey = null;
	while (keyIt3.hasNext()) {
	    A temp = keyIt3.next();
	    if (deleteKey == null) { // initialize deleteKey to first letter
		deleteKey = temp;
	    }
	    if (!current.pointers.containsKey(temp)) { // key does not exist
		return;
	    }
	    if (current.pointers.keySet().size() > 1 || current.value != null) {
		// more than one branch or value stored
		deletePoint = current;
		deleteKey = temp;
	    }
	    current = current.pointers.get(temp);
	}

	if (current.value != null) { // if half key, do not do anything
	    if (!current.pointers.isEmpty()) {
		current.value = null;
	    } else {
		deletePoint.pointers.remove(deleteKey);
	    }
	    size--;
	}
    }

    /**
     * Resets the state of this map to be the same as if the constructor were just
     * called.
     */
    @Override
    public void clear() {
	this.root = new HashTrieNode();
	this.size = 0;
    }
}
