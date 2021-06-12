package datastructures.dictionaries;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.trie.TrieMap;

/**
 * @Description: HashTrieMap is a tree with any number of branches. Every node holds
 * a value along with the next nodes to be reached via branches. There are no limits
 * to the number of branches each nodes can have.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    
    /**
     * @Description: A node class for HashTrieMap. It uses ChainingHashTable to
     * hold the next node information.
     */
    public class HashTrieNode extends TrieNode<Dictionary<A, HashTrieNode>, HashTrieNode> {
        
        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<A, HashTrieNode>(() -> new MoveToFrontList<>());
            this.value = value;
        }
        
        /**
         * @Description: Returns a SimpleEntry version of the nodes from 
         * ChainingHashTable so it complies with the HashTrieNode implementations.
         */
        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return new Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>>() {
                // iterator for ChainingHashMap
                Iterator<Item<A, HashTrieMap<A, K, V>.HashTrieNode>> itr = pointers.iterator();
                
                @Override
                public boolean hasNext() {
                    return itr.hasNext();
                }
                
                @Override
                public Entry<A, HashTrieMap<A, K, V>.HashTrieNode> next() {
                    if (!hasNext()) 
                        throw new NoSuchElementException();
                    Item<A, HashTrieMap<A, K, V>.HashTrieNode> temp = itr.next();
                    Entry<A, HashTrieMap<A, K, V>.HashTrieNode> result = 
                            new AbstractMap.SimpleEntry<A, HashTrieMap<A, K, V>.HashTrieNode>(temp.key, temp.value);
                    return result;
                }
            };
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode(null);
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();
        if (key.size() == 0) {
            this.root.value = value;
            size++;
        }
        HashTrieNode result = iterateKey(key, true);
        V temp = result.value;
        if (result.value == null)
            size++;
        result.value = value;
        return temp;
    }

    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        HashTrieNode temp = iterateKey(key, false);
        if (temp == null)
            return null;
        else
            return temp.value;
    }

    /**
     * @Description: Helper method for insert and find that will traverse the map 
     * and return the key it found.
     */
    private HashTrieNode iterateKey(K key, boolean bool) {
        Iterator<A> keyIt = key.iterator();
        HashTrieNode current = (HashTrieMap<A, K, V>.HashTrieNode) this.root;
        while (keyIt.hasNext()) {
            A temp = keyIt.next();
            if (current.pointers.find(temp) == null) {
                if (bool)
                    current.pointers.insert(temp, new HashTrieNode(null));
                else
                    return null;
            }
            current = current.pointers.find(temp);
        }
        return current;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        Iterator<A> keyIt2 = key.iterator();
        HashTrieNode current = (HashTrieMap<A, K, V>.HashTrieNode) this.root;
        while (keyIt2.hasNext()) {
            A temp = keyIt2.next();
            if (current.pointers.find(temp) == null)
                return false;
            current = current.pointers.find(temp);
        }
        return true;
    }

    @Override
    public void delete(K key) {
        if (key == null)
            throw new IllegalArgumentException();
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
            if (deleteKey == null)
                deleteKey = temp;
            if (current.pointers.find(temp) == null)
                return;
            if (current.pointers.size() > 1 || current.value != null) {
                deletePoint = current;
                deleteKey = temp;
            }
            current = current.pointers.find(temp);
        }
        if (current.value != null) {
            if (!current.pointers.isEmpty())
                current.value = null;
            else
                deletePoint.pointers.find(deleteKey);
	    size--;
        }
    }

    @Override
    public void clear() {
        this.root = new HashTrieNode(null);
        this.size = 0;
    }
}