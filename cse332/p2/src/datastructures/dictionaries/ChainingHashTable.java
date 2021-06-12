package datastructures.dictionaries;

import java.util.Iterator;
import java.util.function.Supplier;
import cse332.datastructures.containers.*;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import datastructures.worklists.ArrayStack;

/**
 * @Description: A data structure that holds data via a ChainingHashTable.
 * ChainingHashTable places elements via modulo of the hash value of the element.
 * If the modulo value has collision, it stores the element as a chain to the
 * existing element.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    // private field for supplier of dictionary
    private Supplier<Dictionary<K, V>> newChain;
    // private field for prime numbers
    private final int[] PRIME_SIZES = {11, 23, 41, 83, 163, 331, 641, 
            1283, 2579, 5147, 10243, 20483, 40961, 81929, 163841, 327869};
    // private field for the table (a.k.a. the chain)
    private Dictionary<K, V>[] table;
    // private field for the prime number to be used
    private int primeSizesIndex;

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        this.table =  new Dictionary[PRIME_SIZES[1]];
        this.primeSizesIndex = 0;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();
        V result = this.hash(key, value, this.table);
        if (result == null)
            this.size++;
        double loadFactor = 1.0 * size / this.table.length;
        if (loadFactor > 1.5 && loadFactor < 3)
            reHash();
        return result;
    }
    
    /**
     * @Description: Hashes the new element and adds it to the table.
     */
    private V hash(K key, V value, Dictionary<K, V>[] table2) {
        int position = Math.abs(key.hashCode() % table2.length);
        if (table2[position] == null)
            table2[position] = this.newChain.get();
        return table2[position].insert(key, value);
    }
    
    /**
     * @Description: Hashes and sorts the entire table again.
     */
    private void reHash() {
        if (this.primeSizesIndex + 1 < this.PRIME_SIZES.length) {
            this.primeSizesIndex++;
            Iterator<Item<K, V>> iter = this.iterator();
            Dictionary<K, V>[] temp = new Dictionary[this.PRIME_SIZES[this.primeSizesIndex]];
            while (iter.hasNext()) {
                Item<K, V> item = iter.next();
                this.hash(item.key, item.value, temp);
            }
            this.table = temp;
        }
    }

    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        int position = Math.abs(key.hashCode() % table.length);
        if (table[position] == null)
            return null;
        return table[position].find(key);
    }  

    @Override
    public Iterator<Item<K, V>> iterator() {
        ArrayStack<Item<K, V>> dic = new ArrayStack<Item<K, V>>();
        for (int i = 0; i < ChainingHashTable.this.table.length; i++) {
            Dictionary<K, V> temp = ChainingHashTable.this.table[i];
            if (temp != null) {
                Iterator<Item<K, V>> it = ChainingHashTable.this.table[i].iterator();
                while (it.hasNext())
                    dic.add(it.next());
            }
        }
        return dic.iterator();
    }
}