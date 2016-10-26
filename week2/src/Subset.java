import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Subset {
	private RandomizedQueue<String> queue = new RandomizedQueue<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Subset s = new Subset();
		try {
			while (true)
				s.queue.enqueue(StdIn.readString());
		} catch (NoSuchElementException e) {
			// do nothing here
		}
		int k = Integer.parseInt(args[0]);
		for (int i = 0; i < k; i++) {
			StdOut.println(s.queue.dequeue());
		}
	}

}
