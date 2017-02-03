import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.util.*;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
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
		words.sort((o1, o2) -> o1.word.compareTo(o2.word));

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
		new WordNet("part2-week1/wordnet/synsets100-subgraph.txt", "part2-week1/wordnet/hypernyms100-subgraph.txt");
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
		Word w = new Word("0," + word + ",");
		return Collections.binarySearch(words, w, (o1, o2) -> o1.word.compareTo(o2.word)) < 0;
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException();
		}

		return -1;
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException();
		}

		return null;
	}
}

class Word {
	public final int id;
	public final String word;
	// public final String meanning;

	public Word(String s) {
		String[] ss = s.split(",");
		id = Integer.parseInt(ss[0]);
		word = ss[1];
		// meanning = ss[2];
	}
}