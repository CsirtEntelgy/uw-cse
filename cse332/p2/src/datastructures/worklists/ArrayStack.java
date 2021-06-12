package datastructures.worklists;

import java.util.NoSuchElementException;
import cse332.interfaces.worklists.LIFOWorkList;

/**
 * @Description: A First-In-First-Out data structure.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {

    // array for holding the data
	private E[] data;
	// current size of the stack
	private int length;

	public ArrayStack() {
		data = (E[]) new Object[10];
		this.length = 0;
	}

	@Override
	public void add(E work) {
		// if reached max capacity, double capacity
		if (data.length == length) {
			E[] temp = (E[]) new Object[length * 2];
			for (int i = 0; i < length; i++)
				temp[i] = data[i];
			data = temp;
		}
		data[length] = work;
		length++;
	}

	@Override
	public E peek() {
		if (!hasWork())
			throw new NoSuchElementException();
		return data[length - 1];
	}

	@Override
	public E next() {
		if (!hasWork())
			throw new NoSuchElementException();
		E result = data[length - 1];
		length--;
		return result;
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public void clear() {
		data = (E[]) new Object[10];
		this.length = 0;
	}
}