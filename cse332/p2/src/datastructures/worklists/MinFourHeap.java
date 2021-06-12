package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * @Description: 
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    
    private E[] data;
    private int length;
    private Comparator<E> comp;
    
    public MinFourHeap(Comparator<E> comp) {
    	this(comp, 10);
    }
    
    public MinFourHeap(Comparator<E> comp, int k) {
        data = (E[]) new Comparable[k];
        this.length = 0;
        this.comp = comp;
    }

    @Override
    public boolean hasWork() {
        return length != 0;
    }

    @Override
    public void add(E work) {
    	if(data.length == length) {
    		E[] temp = (E[]) new Comparable[length * 2];
			for (int i = 0; i < length; i++)
				temp[i] = data[i];
			data = temp;
    	}
        data[length] = work;
        addFourHeapSort(length);
        length++;
    }

    @Override
    public E peek() {
    	if (!hasWork())
			throw new NoSuchElementException();
		return data[0];
    }

    @Override
    public E next() {
    	if (!hasWork())
			throw new NoSuchElementException();
		E result = data[0];
		// set the root as the last element and remove the last element
		data[0] = data[length - 1];
		data[length - 1] = null;
		length--;
		removeFourHeapSort(0);
		return result;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
    	data = (E[]) new Comparable[10];
		this.length = 0;
    }
    
    /**
     * @Description: A helper function for adding an element to the heap.
     * Sets the heap to be a minimum heap. First adds the element to the 
     * bottom of the heap, relocates it upward according to the rules 
     * of a "four heap".
     */
    private void addFourHeapSort(int position) {
        E temp = data[position];
        while(position > 0 && comp.compare(temp, data[(position - 1) / 4]) < 0) {
        	data[position] = data[(position - 1) / 4];
        	position = (position - 1) / 4;
        }
        data[position] = temp;
    }
    
    /**
     * @Description: A helper function for removing an element from the heap.
     * From the method call, the array is so that the first element
     * is set to be one of the biggest element in the array, so
     * you take the first element and do a top-down placement, which
     * will resort the whole heap.
     */
    private void removeFourHeapSort(int position) {
    	int index;
    	E temp = data[position];
    	while(4 * position + 1 < length) {
    		index = minHeapIndex(position);
    		if(comp.compare(data[index], temp) < 0)
    			data[position] = data[index];
    		else
    			break;
    		position = index;
    	}
    	data[position] = temp;
    }
    
    /**
     * @Description: Returns the index of the smallest element in the heap.
     */
    private int minHeapIndex(int position) {
    	int i = 2;
    	int smallestHeapIndex = 4 * position + 1;
    	int current = 4 * position + i;
    	while((i < 5) && (current < length)) {
    		if(comp.compare(data[current], data[smallestHeapIndex]) < 0)
    			smallestHeapIndex = current;
    		i++;
    		current = 4 * position + i;
    	}
    	return smallestHeapIndex;
    }
}