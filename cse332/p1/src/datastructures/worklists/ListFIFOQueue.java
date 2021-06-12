package datastructures.worklists;

import java.util.NoSuchElementException;
import cse332.interfaces.worklists.FIFOWorkList;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

	//our implementation of linked list with current node pointer
	//current node pointer is for the constant runtime
	private class LinkedList<F> {
		
		private Node<F> head;
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

		public F getHead() {
			if (head == null)
				throw new NoSuchElementException();
			return head.data;
		}

		public void removeHead() {
			if (head == null)
				throw new NoSuchElementException();
			head = head.next;
		}
	}

	//our implementation of node class
	private static class Node<G> {
		
		private G data;
		private Node<G> next;

		public Node(G data, Node<G> next) {
			this.data = data;
			this.next = next;
		}
	}

	private LinkedList<E> data;
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