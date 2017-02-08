import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
	private static final int UNKNOWN_DISTANCE = -1;
	private static final int UNKNOWN_VERTEX = -1;
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
		out.println(wn.isNoun("anamorphosis"));
		wn.sap("vicarage", "underpants");
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nouns;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return search(word) >= 0;
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
		int iA = search(nounA), iB = search(nounB);
		if ((iA) < 0 || (iB) < 0) {
			throw new IllegalArgumentException();
		}
		if (iA == iB) {
			return 0;
		} else {
			return sap.length(iA, iB);
		}
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		int iA = search(nounA), iB = search(nounB);
		int ancestor;
		if ((iA) < 0 || (iB) < 0) {
			throw new IllegalArgumentException();
		}
		if (iA == iB) {
			ancestor = iA;
		} else {
			ancestor = sap.ancestor(iA, iB);
		}

		Word w = new Word(ancestor, "");

		int j = Collections.binarySearch(index, w, Comparator.comparing(o -> o.id));
		int a = j, b = j;
		while (a > 0 && index.get(a - 1).id == ancestor) {
			a--;
		}
		while (b < index.size() - 1 && index.get(b + 1).id == ancestor) {
			b++;
		}
		StringBuffer sb = new StringBuffer();
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