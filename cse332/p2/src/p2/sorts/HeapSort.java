package p2.sorts;

import java.util.Comparator;
import datastructures.worklists.MinFourHeap;

/**
 * @Description: Sorts the given array using a heap. Adds and remove all elements from the heap, 
 * which must be sorted according to the definition of a heap.
 */
public class HeapSort {
    
    public static <E> void sort(E[] array) {
        sort(array, (x, y) -> ((Comparable<E>) x).compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        MinFourHeap<E> heap = new MinFourHeap<E>(comparator);
        for(E e : array)
            heap.add(e);
        for(int i = 0; i < array.length; i++)
            array[i] = heap.next();
    }
}