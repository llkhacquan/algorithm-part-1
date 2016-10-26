import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author quannk
 */
public class Deque<Item> implements Iterable<Item> {
    private int nSize;
    private Node first;
    private Node last;

    public Deque() {
        nSize = 0;
        first = null;
        last = null;
    }

    public static void main(String[] args) {
        int n = StdRandom.uniform(15) + 1000;
        Deque<Integer> queue = new Deque<>();
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            Integer item = Integer.valueOf(StdRandom.uniform(1000));
            int chose = StdRandom.uniform(4);
            switch (chose) {
                case 0:
                    queue.addFirst(item);
                    list.addFirst(item);
                    assert (queue.size() == list.size());
                    break;
                case 1:
                    queue.addLast(item);
                    list.addLast(item);
                    assert (queue.size() == list.size());
                    break;
                case 2:
                    if (!list.isEmpty())
                        assert (list.removeFirst() == queue.removeFirst());
                    break;
                case 3:
                    if (!list.isEmpty())
                        assert (list.removeLast() == queue.removeLast());
                    break;
                default:
                    assert (false);
            }
        }

        Iterator<Integer> i1 = queue.iterator();
        Iterator<Integer> i2 = list.iterator();
        for (int i = 0; i < queue.size() - 1; i++) {
            assert (i1.hasNext());
            assert (i2.hasNext());
            assert (i1.next() == i2.next());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public boolean isEmpty() {
        // is the deque empty?
        return nSize == 0;
    }

    public int size() {
        // return the number of items on the deque
        return nSize;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();
        // add the item to the front
        if (nSize == 0) {
            insertFirstElement(item);
        } else {
            Node newNode = new Node(first, null, item);
            assert (first.back == null);
            first.back = newNode;
            first = newNode;
        }
        nSize++;
    }

    /**
     * Insert a element when there is no element in the queue
     *
     * @param item
     */
    private void insertFirstElement(Item item) {
        assert (first == null);
        assert (last == null);
        first = new Node(null, null, item);
        last = first;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();
        // add the item to the end
        if (nSize == 0) {
            insertFirstElement(item);
        } else {
            Node newNode = new Node(null, last, item);
            assert (last.next == null);
            last.next = newNode;
            last = newNode;
        }
        nSize++;
    }

    public Item removeFirst() {
        // remove and return the item from the front
        if (nSize == 0)
            throw new NoSuchElementException();
        Item backupItem = (Item) first.item;
        Node newFirst = first.next;
        if (newFirst != null)
            newFirst.back = null;
        else
            last = null;
        first.next = null;
        first.back = null;
        first = newFirst;
        nSize--;
        return backupItem;
    }

    public Item removeLast() {
        // remove and return the item from the end
        if (nSize == 0)
            throw new NoSuchElementException();
        Item backupItem = (Item) last.item;
        Node newLast = last.back;
        if (newLast != null)
            newLast.next = null;
        else
            first = null;
        last.next = null;
        last.back = null;
        last = newLast;
        nSize--;
        return backupItem;
    }

    private static class Node {
        private Node next;
        private Node back;
        private Object item;

        public Node(Node next, Node back, Object item) {
            this.next = next;
            this.back = back;
            this.item = item;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item result = (Item) current.item;
            current = current.next;
            return result;
        }
    }
}
