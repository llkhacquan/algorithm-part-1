import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by quannk on 13/10/2016.
 */
public class Solver {
	private static Comparator<Node> nodeComparator = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.f() - o2.f();
		}
	};
	private List<Board> solution;
	private boolean solvable;
	private Board init;

	public Solver(Board initial) {
		init = initial;
		MinPQ<Node> openQueue1 = new MinPQ<>(nodeComparator);
		MinPQ<Node> openQueue2 = new MinPQ<>(nodeComparator);
		openQueue1.insert(new Node(initial, null));
		openQueue2.insert(new Node(initial.twin(), null));

		while (!openQueue1.isEmpty() || !openQueue2.isEmpty()) {
			if (processQueue(openQueue1, true)) {
				solvable = true;
				return;
			}
			if (processQueue(openQueue2, false)) {
				solvable = false;
				return;
			}
		}
	}

	private boolean processQueue(MinPQ<Node> openQueue, boolean build) {
		if (openQueue.isEmpty())
			return false;
		Node current = openQueue.delMin();

		assert current != null;
		if (current.board.isGoal()) {
			if (build)
				buildSolution(current);
			solvable = true;
			return true;
		}
		//closeSet.add(current);
		Iterable<Board> bs = current.board.neighbors();
		for (Board b : bs) {
			Node newNode = new Node(b, current);
			if (current.before != null && b.equals(current.before.board))
				continue;
			openQueue.insert(newNode);
		}
		return false;
	}

	private void buildSolution(Node current) {
		solution = new LinkedList<>();
		while (!current.board.equals(init)) {
			solution.add(0, current.board);
			current = current.before;
		}
		assert current.before == null;
		solution.add(0, current.board);
	}

	public boolean isSolvable() {
		return solvable;
	}

	public int moves() {
		if (solvable)
			return solution.size() - 1;
		else
			return -1;
	}

	public Iterable<Board> solution() {
		return solution;
	}

	private class Node {
		private Board board;
		private int distanceToRoot;

		private Node before;

		public Node(Board b, Node parrent) {
			board = b;
			before = parrent;
			if (parrent == null)
				distanceToRoot = 0;
			else
				distanceToRoot = parrent.distanceToRoot + 1;
		}

		private int f() {
			return distanceToRoot + board.manhattan();
		}
	}
}