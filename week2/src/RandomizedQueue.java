import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author quannk
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] a; // array of items
	private int nSize; // number of elements on stack

	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		a = (Item[]) new Object[2];
		nSize = 0;
	}

	private RandomizedQueue(RandomizedQueue<Item> queue) {
		a = queue.a.clone();
	}

	public static void main(String[] args) {
		int n = StdRandom.uniform(15) + 1000;
		RandomizedQueue<Integer> queue = new RandomizedQueue<>();
		LinkedList<Integer> list = new LinkedList<>();
		for (int i = 0; i < n; i++) {
			Integer item = new Integer(StdRandom.uniform(1000));
			int chose = StdRandom.uniform(2);
			switch (chose) {
				case 0:
					queue.enqueue(item);
					list.addFirst(item);
					assert (queue.size() == list.size());
					break;
				case 1:
					queue.enqueue(item);
					list.addLast(item);
					assert (queue.size() == list.size());
					break;
				default:
					assert (false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Item> iterator() {
		return new RandomIterator<Item>(this);
	}

	public boolean isEmpty() {
		// is the deque empty?
		return nSize == 0;
	}

	public int size() {
		// return the number of items on the deque
		return nSize;
	}

	public void enqueue(Item item) {
		if (item == null)
			throw new NullPointerException();
		// add the item
		if (nSize == a.length)
			resize(2 * a.length); // double size of array if necessary
		a[nSize++] = item; // add item
		// swap to a random position
		int i = StdRandom.uniform(nSize);
		Item temp = a[i];
		a[i] = a[nSize - 1];
		a[nSize - 1] = temp;
	}

	// resize the underlying array holding the elements
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		assert (capacity >= nSize);

		// textbook implementation
		Item[] temp = (Item[]) new Object[capacity];
		for (int i = 0; i < nSize; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	public Item dequeue() {
		// remove and return a random item
		if (nSize == 0)
			throw new NoSuchElementException();

		Item result = a[nSize - 1];
		a[nSize - 1] = null;
		nSize--;
		// shrink size of array if necessary
		if (nSize > 0 && nSize == a.length / 4)
			resize(a.length / 2);
		return result;
	}

	public Item sample() {
		// return a random item
		if (nSize == 0)
			throw new NoSuchElementException();
		return a[StdRandom.uniform(nSize)];
	}

	private static class RandomIterator<T> implements Iterator<T> {
		private RandomizedQueue<T> queue;

		public RandomIterator(RandomizedQueue<T> orinalQueue) {
			queue = new RandomizedQueue<T>(orinalQueue);
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return queue.dequeue();
		}
	}
}
