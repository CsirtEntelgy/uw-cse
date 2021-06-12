package p2.sorts;

import java.util.Comparator;
import datastructures.worklists.MinFourHeap;

/**
 * @Description: Leaves only k, a given constant, number of largest elements in
 * an array. Runs in O(nlogk) time since only k elements are kept in the heap.
 */
public class TopKSort {
    
    public static <E> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> ((Comparable<E>) x).compareTo(y));
    }

    /**
     * @Description: Adds max k elements to the heap. Removes the element,
     * which is the minimum element currently, if more than k elements are added.
     * Sets rest of the array to null, with only k largest elements in front.
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        MinFourHeap<E> heap = new MinFourHeap<E>(comparator, k);
        for(E e : array) {
            heap.add(e);
            if(heap.size() > k)
                heap.next();
            e = null;
        }
        for(int i = 0; i < k; i++)
            array[i] = heap.next();
    }
}