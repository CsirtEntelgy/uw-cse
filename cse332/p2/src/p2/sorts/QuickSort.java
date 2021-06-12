package p2.sorts;

import java.util.Comparator;

/**
 * @Description: A quick sort method, classic implementation of quick sort.
 */
public class QuickSort {
    
    public static <E> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> ((Comparable<E>) x).compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sortHelper(array, comparator, 0, array.length - 1);
    }
    
    /**
     * @Description: Helper method for the sort.
     */
    private static <E> void sortHelper(E[] array, Comparator<E> comp, int min, int max) {
        if(min < max) {
            int partitionIndex = partition(array, comp, min, max);
            sortHelper(array, comp, min, partitionIndex - 1);
            sortHelper(array, comp, partitionIndex + 1, max);
        }
    }
    
    /**
     * @Description: Partitions the array.
     */
    private static <E> int partition(E[] array, Comparator<E> comp, int min, int max) {
        E pivotPoint = array[max];
        int i = min - 1;
        for(int j = min; j < max; j++) {
            if(comp.compare(array[j], pivotPoint) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i+1, max);
        return i + 1;
    }
    
    /**
     * @Description: Swaps two elements in the array.
     */
    private static <E> void swap(E[] array, int from, int to){
        E temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }
}