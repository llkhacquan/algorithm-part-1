import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by quannk on 03/02/2017.
 */
public class SAP {
	// constructor takes a digraph (not necessarily a DAG)
	private static final int UNKNOWN_DISTANCE = -1;
	private static final int UNKNOWN_VERTEX = -1;
	private final Digraph g;

	public SAP(Digraph G) {
		g = new Digraph(G);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		Digraph g = new Digraph(new In("part2-week1/wordnet/digraph2.txt"));
		Out out = new Out();
		SAP sap = new SAP(g);
		out.println(sap.length(3, 1));
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		ArrayList<Integer> v_ = new ArrayList<>(1);
		v_.add(v);
		ArrayList<Integer> w_ = new ArrayList<>(1);
		w_.add(w);
		return length(v_, w_);
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		ArrayList<Integer> v_ = new ArrayList<>(1);
		v_.add(v);
		ArrayList<Integer> w_ = new ArrayList<>(1);
		w_.add(w);
		return ancestor(v_, w_);
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int[] r = calculate(v, w);
		return r[1];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		int[] r = calculate(v, w);
		return r[0];
	}

	private int[] calculate(Iterable<Integer> v, Iterable<Integer> w) {
		for (int i : v) {
			if (i < 0 || i >= g.V()) {
				throw new IndexOutOfBoundsException();
			}
		}
		for (int i : w) {
			if (i < 0 || i >= g.V()) {
				throw new IndexOutOfBoundsException();
			}
		}

		int[] d1 = new int[g.V()];
		Arrays.setAll(d1, operand -> UNKNOWN_DISTANCE);
		int[] d2 = new int[g.V()];
		Arrays.setAll(d2, operand -> UNKNOWN_DISTANCE);
		// bfs from both w and v
		Queue<Integer> qA = new LinkedList<>();
		Queue<Integer> qB = new LinkedList<>();
		v.forEach(qA::add);
		v.forEach(integer -> d1[integer] = 0);
		w.forEach(qB::add);
		w.forEach(integer -> d2[integer] = 0);
		int commomAncestor = UNKNOWN_VERTEX;
		while (!qA.isEmpty() || !qB.isEmpty()) {
			if (!qA.isEmpty()) {
				int c = qA.remove();
				if (d2[c] != UNKNOWN_DISTANCE) {
					if (commomAncestor == UNKNOWN_VERTEX || d1[commomAncestor] + d2[commomAncestor] > d1[c] + d2[c]) {
						commomAncestor = c;
					}
				}
				for (int i : g.adj(c)) {
					if (d1[i] == UNKNOWN_DISTANCE) {
						qA.add(i);
						d1[i] = d1[c] + 1;
					}
				}

			}

			if (!qB.isEmpty()) {
				int c = qB.remove();
				if (d1[c] != UNKNOWN_DISTANCE) {
					if (commomAncestor == UNKNOWN_VERTEX || d1[commomAncestor] + d2[commomAncestor] > d1[c] + d2[c]) {
						commomAncestor = c;
					}
				}
				for (int i : g.adj(c)) {
					if (d2[i] == UNKNOWN_DISTANCE) {
						qB.add(i);
						d2[i] = d2[c] + 1;
					}
				}
			}
		}

		return new int[]{commomAncestor, commomAncestor >= 0 ? d1[commomAncestor] + d2[commomAncestor] : UNKNOWN_DISTANCE};
	}
}
