package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import cse332.datastructures.containers.*;
import cse332.interfaces.misc.DeletelessDictionary;

/**
 * @Description: A linked list implementation but with no delete function.
 * If an element is added for looked for, the element is moved to the front
 * of the list.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    /**
     * @Description: Private class for the linked list implementation.
     */
    private class LinkedList {
        
        private Node<Item<K, V>> head;
        
        public LinkedList() {
            head = null;
        }
        
        // adds the node to the front of the list
        public void add(Item<K, V> item) {
                head = new Node<Item<K, V>>(item, head);
        }
        
        // finds the node with a certain key and removes the node from
        // the linked list
        public V findAndRemoveNode(K key) {
            if (head == null) 
                return null;
            if (head.data.key.equals(key)) {
                V temp = head.data.value;
                head = head.next;
                return temp;
            }
            Node<Item<K, V>> previous = head;
            Node<Item<K, V>> current = head.next;
            while (current != null) {
                if (current.data.key.equals(key)) {
                    V temp = current.data.value;
                    previous.next = current.next;
                    return temp;
                }
                previous = current;
                current = current.next;
            }
            return null;
        }
    }
    
    /**
     * @Description: Private node class for the linked list implementation.
     */
    private static class Node<G> {
        
        private G data;
        private Node<G> next;
        
        public Node(G data, Node<G> next) {
            this.data = data;
            this.next = next;
        }
    }
    
    // the linked list implementation
    private LinkedList data;
    // current size of the list
    private int length;

    public MoveToFrontList() {
        data = new LinkedList();
    }

    @Override
    public V insert(K key, V value) {
        V temp = data.findAndRemoveNode(key);
        if (temp == null) 
            length++;
        data.add(new Item<K, V>(key, value));
        return temp;
    }

    @Override
    public V find(K key) {
        V temp = data.findAndRemoveNode(key);
        data.add(new Item<K, V>(key, temp));
        return temp;
    }

    @Override
    public int size() {
        return this.length;
    }

    /**
     * @Description: Iterator for the list. Doesn't comply with java
     * built in data structures, so needs a unique iterator.
     */
    @Override
    public Iterator<Item<K, V>> iterator() {
        return new Iterator<Item<K, V>>() {
            
            private Node<Item<K, V>> current = data.head;
            
            @Override
            public boolean hasNext() {
                if (current == null) 
                    return false;
                return true;
            }
            
            @Override
            public Item<K, V> next() {
                if (!hasNext()) 
                    throw new NoSuchElementException();
                Item<K, V> temp = current.data;
                current = current.next;
                return temp;
            }
        };
    }
}