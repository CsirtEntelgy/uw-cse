package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Arrays;
import java.util.NoSuchElementException;

import cse332.exceptions.NotYetImplementedException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int length;
    
    public MinFourHeap() {
    	//the array representation of the heap will start at index 0
    	//thus index 0 of the array will not be null
    	data = (E[]) new Comparable[10];
		this.length = 0;
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
		//set the root as the last element and remove the last element
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
    
    /*
     * A helper function for adding an element to the heap
     * Sets the heap to be a minimum heap
     * First adds the element to the bottom of the heap, relocates it upward
     * according to the rules of a "four heap"
     */
    private void addFourHeapSort(int position) {
    	//going to be a minimum heap
        E temp = data[position];
        while(position > 0 && temp.compareTo(data[(position - 1) / 4]) < 0) {
        	data[position] = data[(position - 1) / 4];
        	position = (position - 1) / 4;
        }
        data[position] = temp;
    }
    
    /*
     * A helper function for removing an element from the heap
     * From the method call, the array is so that the first element
     * is set to be one of the biggest element in the array, so
     * you take the first element and do a top-down placement, which
     * will resort the whole heap 
     */
    private void removeFourHeapSort(int position) {
    	int index;
    	E temp = data[position];
    	while(4 * position + 1 < length) {
    		index = minHeapIndex(position);
    		if(data[index].compareTo(temp) < 0)
    			data[position] = data[index];
    		else
    			break;
    		position = index;
    	}
    	data[position] = temp;
    }
    
    /*
     * Returns the index of the smallest element in the heap
     */
    private int minHeapIndex(int position) {
    	int i = 2;
    	int smallestHeapIndex = 4 * position + 1;
    	int current = 4 * position + i;
    	while((i < 5) && (current < length)) {
    		if(data[current].compareTo(data[smallestHeapIndex]) < 0)
    			smallestHeapIndex = current;
    		i++;
    		current = 4 * position + i;
    	}
    	return smallestHeapIndex;
    }
}