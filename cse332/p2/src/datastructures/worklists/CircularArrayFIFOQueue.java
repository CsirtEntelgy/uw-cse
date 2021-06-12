package datastructures.worklists;

import java.util.NoSuchElementException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

/**
 * @Description: Fixed sized array with the First-In-First-Out implementation.
 * Has the ability to peek at certain locations, remove and return with next, and
 * update the value of a specific element.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {

    // array to hold the list
    private E[] data;
    // current size of list
    protected int length;

    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        data = (E[]) new Comparable[capacity];
        this.length = 0;
    }

    @Override
    public void add(E work) {
        if (super.isFull()) 
            throw new IllegalStateException();
        data[length] = work;
        length++;
    }

    @Override
    public E peek() {
        if (!hasWork()) 
            throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public E peek(int i) {
        if (!hasWork()) 
            throw new NoSuchElementException();
        if (i < 0 || i >= size()) 
            throw new IndexOutOfBoundsException();
        return data[i];
    }

    @Override
    public E next() {
        if (!hasWork()) 
            throw new NoSuchElementException();
        E temp = data[0];
        for (int i = 0; i < length - 1; i++)
            data[i] = data[i + 1];
        length--;
        return temp;
    }

    @Override
    public void update(int i, E value) {
        if (!hasWork()) 
            throw new NoSuchElementException();
        if (i < 0 || i >= size()) 
            throw new IndexOutOfBoundsException();
        data[i] = value;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
        data = (E[]) new Comparable[super.capacity()];
        this.length = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        String thisOne = "";
        String thatOne = "";
        for (int i = 0; i < other.size(); i++)
            thatOne = thatOne.concat(other.peek(i).toString());
        for (int j = 0; j < size(); j++)
            thisOne = thisOne.concat(data[j].toString());
        return thisOne.compareTo(thatOne);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>))
            return false;
        else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            if (this.compareTo(other) == 0) 
                return true;
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < length; i++)
            result += (peek(i).hashCode() * (i + 1));
        result += length;
        return result;
    }
}