import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

/**
 * Created by quannk on 02/02/2017.
 */
public class WordNet {
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		File synsetFile = new File(synsets);
		File hypernymsFile = new File(hypernyms);
		throw new NotImplementedException();
	}

	// do unit testing of this class
	public static void main(String[] args) {

	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		throw new NotImplementedException();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		throw new NotImplementedException();
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		throw new NotImplementedException();
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		throw new NotImplementedException();
	}
}
