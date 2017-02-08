import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by quannk on 03/02/2017.
 */
public class Outcast {
	private final WordNet wn;

	public Outcast(WordNet wordnet)         // constructor takes a WordNet object
	{
		wn = wordnet;
	}

	public static void main(String[] args)  // see test client below
	{
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}

	public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
	{
		String max = null;
		int maxD = -1;
		int d;
		Iterable<String> words = wn.nouns();
		for (String noun : nouns) {
			d = 0;
			for (String w : words) {
				if (!w.equals(noun)) {
					d += wn.distance(w, noun);
				}
			}
			if (maxD < d) {
				maxD = d;
				max = noun;
			}
		}
		return max;
	}
}
