package p2.experiment;

import java.util.NoSuchElementException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;
import datastructures.worklists.CircularArrayFIFOQueue;

/**
 * @Description: Fixed sized array with the First-In-First-Out implementation.
 * Has the ability to peek at certain locations, remove and return with next, and
 * update the value of a specific element.
 */
public class CircularArrayFIFOQueue2<E extends Comparable<E>> extends CircularArrayFIFOQueue<E> {

    public CircularArrayFIFOQueue2(int capacity) {
        super(capacity);
    }

    @Override
    public int hashCode() {
        return this.length;
    }
}