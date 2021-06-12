package datastructures.worklists;

import java.util.NoSuchElementException;
import cse332.interfaces.worklists.FIFOWorkList;

/**
 * @Description: A First-In-First-Out data structure with linked list implementation.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

	/**
	 * @Description: Private class for the linked list implementation.
	 */
	private class LinkedList<F> {
		
		private Node<F> head;
		// stores the most recent node so the add method runs in O(1) time
		private Node<F> current;

		public LinkedList() {
			head = null;
			current = null;
		}

		public void add(F item) {
			if (head == null) {
				head = new Node<F>(item, head);
				current = head;
			}
			else{
				Node<F> temp = current;
				current.next = new Node<F>(item, null);
				current = temp.next;
			}
		}
		
		// returns the first element of the list
		public F getHead() {
			if (head == null)
				throw new NoSuchElementException();
			return head.data;
		}

		// removes the first element of the list
		public void removeHead() {
			if (head == null)
				throw new NoSuchElementException();
			head = head.next;
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

	// linked list implementation of list
	private LinkedList<E> data;
	// current size of list
	private int length;

	public ListFIFOQueue() {
		data = new LinkedList<E>();
		this.length = 0;
	}

	@Override
	public void add(E work) {
		data.add(work);
		length++;
	}

	@Override
	public E peek() {
		if (!hasWork())
			throw new NoSuchElementException();
		return data.getHead();
	}

	@Override
	public E next() {
		if (!hasWork())
			throw new NoSuchElementException();
		E temp = data.getHead();
		data.removeHead();
		length--;
		return temp;
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public void clear() {
		data = new LinkedList<E>();
		length = 0;
	}
}