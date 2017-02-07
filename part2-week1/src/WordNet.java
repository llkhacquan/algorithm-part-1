import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
	private static final int UNKNOWN_DISTANCE = -1;
	private static final int UNKNOWN_VERTEX = -1;
	private final List<Word> words = new ArrayList<>();
	private final Digraph g;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		In in = new In(synsets);
		while (in.hasNextLine()) {
			words.addAll(Word.fromLine(in.readLine()));
		}
		in.close();
		words.sort(Comparator.comparing(o -> o.word));
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
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet("part2-week1/wordnet/synsets.txt",
				"part2-week1/wordnet/hypernyms.txt");
		wn.sap("Near_East", "tomograph");
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
		Word w = new Word(0, word);
		return Collections.binarySearch(words, w, Comparator.comparing(o -> o.word));
	}

	// dA between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		Word w = findSap(nounA, nounB);
		return w.dA + w.dB;
	}

	private Word findSap(String nounA, String nounB) {
		int iA = search(nounA), iB = search(nounB);
		if ((iA) < 0 || (iB) < 0) {
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
		int commomAncestor = UNKNOWN_VERTEX;
		while (!qA.isEmpty() || !qB.isEmpty()) {
			if (!qA.isEmpty()) {
				int v = qA.remove();
				if (words.get(v).dB != UNKNOWN_DISTANCE) {
					if (commomAncestor == UNKNOWN_VERTEX || words.get(commomAncestor).dA + words.get(commomAncestor).dB >
							words.get(v).dA + words.get(v).dB) {
						commomAncestor = v;
					}
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
					if (commomAncestor == UNKNOWN_VERTEX || words.get(commomAncestor).dA + words.get(commomAncestor).dB >
							words.get(v).dA + words.get(v).dB) {
						commomAncestor = v;
					}
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

	static ArrayList<Word> fromLine(String s){
		String[] ss = s.split(",");
		int id = Integer.parseInt(ss[0]);
		String ws[] = ss[1].split(" ");
		ArrayList<Word> r= new ArrayList<>();
		for (String w: ws) {
			r.add(new Word(id, w));
		}
		return r;
	}

	Word(int id, String w) {
		this.id = id;
		word = w;
	}
}