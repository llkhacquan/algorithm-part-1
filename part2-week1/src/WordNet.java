import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.util.*;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
	private static final int UNKNOWN_DISTANCE = -1;
	private final List<Word> words = new ArrayList<>();
	private final Digraph g;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		In in = new In(synsets);
		while (in.hasNextLine()) {
			words.add(new Word(in.readLine()));
		}
		in.close();
		words.sort(Comparator.comparing(o -> o.word));

		Out out = new Out();
		out.println("Read " + words.size() + " synsets");

		g = new Digraph(words.size());

		in = new In(hypernyms);
		while (in.hasNextLine()) {
			String[] parts = in.readLine().split(",");
			int t = Integer.parseInt(parts[0]);
			for (int i = 1; i < parts.length; i++) {
				int a = Integer.parseInt(parts[i]);
				g.addEdge(t, a);
			}
		}
		out.println("Added " + g.E() + " edges");
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet("part2-week1/wordnet/synsets100-subgraph.txt",
		                         "part2-week1/wordnet/hypernyms100-subgraph.txt");
		if (wn.search("someword") >= 0) {
			throw new AssertionError();
		}
		if (wn.search(wn.words.get(0).word) != 0) {
			throw new AssertionError();
		}
		if (wn.search(wn.words.get(1).word) != 1) {
			throw new AssertionError();
		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		Bag<String> result = new Bag<>();
		Iterator<Word> it = words.iterator();
		while (it.hasNext()) {
			result.add(it.next().word);
		}
		return result;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return search(word) < 0;
	}

	private int search(String word) {
		if (word == null) {
			throw new NullPointerException();
		}
		Word w = new Word("0," + word + ",");
		return Collections.binarySearch(words, w, Comparator.comparing(o -> o.word));
	}

	// dA between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		Word w = findSap(nounA, nounB);
		return w.dA + w.dB;
	}

	private Word findSap(String nounA, String nounB) {
		int iA, iB;
		if ((iA = search(nounA)) < 0 || (iB = search(nounB)) < 0) {
			throw new IllegalArgumentException();
		}
		if (iA == iB) {
			return words.get(iA);
		}

		// we use 2 bfs to search in 2 direction from nounA
		words.forEach(word -> word.dA = word.dB = UNKNOWN_DISTANCE);
		// bfs from nounA
		Word a = words.get(iA);
		a.dA = 0;
		Word b = words.get(iB);
		b.dB = 0;
		Queue<Integer> qA = new LinkedList<>();
		qA.add(a.id);
		Queue<Integer> qB = new LinkedList<>();
		qB.add(b.id);
		int commomAncestor;
		found:
		while (true) {
			if (!qA.isEmpty()) {
				int v = qA.remove();
				if (words.get(v).dB != UNKNOWN_DISTANCE) {
					commomAncestor = v;
					break found;
				}
				for (int i : g.adj(v)) {
					if (words.get(i).dA == UNKNOWN_DISTANCE) {
						qA.add(i);
						words.get(i).dA = words.get(v).dA + 1;
					}
				}

			}

			if (!qB.isEmpty()) {
				int v = qB.remove();
				if (words.get(v).dA != UNKNOWN_DISTANCE) {
					commomAncestor = v;
					break found;
				}
				for (int i : g.adj(v)) {
					if (words.get(i).dB == UNKNOWN_DISTANCE) {
						qB.add(i);
						words.get(i).dB = words.get(v).dB + 1;
					}
				}
			}
		}

		return words.get(commomAncestor);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		Word w = findSap(nounA, nounB);
		return w.word;
	}
}

class Word {
	final int id;
	final String word;
	int dA; // use for calculating distance and sap only
	int dB; // use for calculating distance and sap only

	Word(String s) {
		String[] ss = s.split(",");
		id = Integer.parseInt(ss[0]);
		word = ss[1];
	}
}