import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
	private final List<Word> words = new ArrayList<>();
	private final List<Word> index = new ArrayList<>();
	private final Set<String> nouns = new HashSet<>();
	private final SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		In in = new In(synsets);
		while (in.hasNextLine()) {
			ArrayList<Word> ws = Word.fromLine(in.readLine());
			words.addAll(ws);
			index.addAll(ws);
			nouns.addAll(ws.stream().map(word -> word.word).collect(Collectors.toSet()));
		}
		in.close();
		words.sort(Comparator.comparing(o -> o.word));
		index.sort(Comparator.comparing(o -> o.id));
		Digraph g = new Digraph(words.size());
		in = new In(hypernyms);
		while (in.hasNextLine()) {
			String[] parts = in.readLine().split(",");
			int t = Integer.parseInt(parts[0]);
			for (int i = 1; i < parts.length; i++) {
				int a = Integer.parseInt(parts[i]);
				g.addEdge(t, a);
			}
		}
		sap = new SAP(g);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet("part2-week1/wordnet/synsets.txt",
		                         "part2-week1/wordnet/hypernyms.txt");
		Out out = new Out();
		out.print(wn.distance("shell_plating", "welcome"));
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nouns;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new NullPointerException();
		}
		return nouns.contains(word);
	}

	private ArrayList<Integer> search(String word) {
		if (word == null) {
			throw new NullPointerException();
		}
		if (!isNoun(word)) {
			throw new IllegalArgumentException();
		}
		Word w = new Word(0, word);
		int j = Collections.binarySearch(words, w, Comparator.comparing(o -> o.word));
		int a = j, b = j;
		while (a > 0 && words.get(a - 1).word.equals(word)) {
			a--;
		}
		while (b < words.size() - 1 && words.get(b + 1).word.equals(word)) {
			b++;
		}
		ArrayList<Integer> r = new ArrayList<>();
		for (int k = a; k <= b; k++) {
			r.add(words.get(k).id);
		}
		return r;
	}

	// dA between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		ArrayList<Integer> iA = search(nounA), iB = search(nounB);
		return sap.length(iA, iB);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		ArrayList<Integer> iA = search(nounA), iB = search(nounB);
		int ancestor;
		ancestor = sap.ancestor(iA, iB);
		Word w = new Word(ancestor, "");

		int j = Collections.binarySearch(index, w, Comparator.comparing(o -> o.id));
		int a = j, b = j;
		while (a > 0 && index.get(a - 1).id == ancestor) {
			a--;
		}
		while (b < index.size() - 1 && index.get(b + 1).id == ancestor) {
			b++;
		}
		StringBuilder sb = new StringBuilder();
		for (int k = a; k <= b; k++) {
			sb.append(index.get(k).word).append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}

class Word {
	final int id;
	final String word;

	Word(int id, String w) {
		this.id = id;
		word = w;
	}

	static ArrayList<Word> fromLine(String s) {
		String[] ss = s.split(",");
		int id = Integer.parseInt(ss[0]);
		String ws[] = ss[1].split(" ");
		ArrayList<Word> r = new ArrayList<>();
		for (String w : ws) {
			r.add(new Word(id, w));
		}
		return r;
	}
}