package datastructures.worklists;

import java.util.NoSuchElementException;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E> extends FixedSizeFIFOWorkList<E> {
	
	private E[] data;
	private int length;
	
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        data = (E[]) new Object[capacity];
        this.length = 0;
    }

    @Override
    public void add(E work) {
        if(super.isFull())
        	throw new IllegalStateException();
        data[length] = work;
        length++;
    }

    @Override
    public E peek() {
    	if(!hasWork())
        	throw new NoSuchElementException();
    	return data[0];
    }
    
    @Override
    public E peek(int i) {
        if(!hasWork())
        	throw new NoSuchElementException();
        if(i < 0 || i >= size())
        	throw new IndexOutOfBoundsException();
        return data[i];
    }
    
    @Override
    public E next() {
        if(!hasWork())
        	throw new NoSuchElementException();
        E temp = data[0];
        for(int i = 0; i < length - 1; i++)
        	data[i] = data[i + 1];
        length--;
        return temp;
    }
    
    @Override
    public void update(int i, E value) {
    	if(!hasWork())
        	throw new NoSuchElementException();
        if(i < 0 || i >= size())
        	throw new IndexOutOfBoundsException();
        data[i] = value;
    }
    
    @Override
    public int size() {
        return length;
    }
    
    @Override
    public void clear() {
    	data = (E[]) new Object[super.capacity()];
        this.length = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in p2. Leave this method unchanged for p1.
        throw new NotYetImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in p2. Leave this method unchanged for p1.
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        }
        else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            // Your code goes here
            throw new NotYetImplementedException();
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in p2. Leave this method unchanged for p1.
        throw new NotYetImplementedException();
    }
}