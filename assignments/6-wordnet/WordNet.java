/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private final Map<String, ArrayList<Integer>> nounToIds;
    private final Map<Integer, String> synsetMap;
    private int numSynsets;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        nounToIds = new HashMap<>();
        synsetMap = new HashMap<>();

        readSynsets(synsets);
        readHypernyms(hypernyms);

    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        Digraph digraph = new Digraph(numSynsets);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] strings = line.split(",");
            if (strings.length < 2) continue;

            int v = Integer.parseInt(strings[0]);
            for (int j = 1; j < strings.length; j++) {
                int w = Integer.parseInt(strings[j]);
                digraph.addEdge(v, w);
            }

        }

        if (!isValidDigraph(digraph)) throw new IllegalArgumentException();
        sap = new SAP(digraph);
    }

    private boolean isValidDigraph(Digraph digraph) {
        // check if has directed cycle
        DirectedCycle dc = new DirectedCycle(digraph);
        if (dc.hasCycle()) return false;

        // check if is single rooted
        int numRoots = 0;
        for (int i = 0; i < numSynsets; i++) {
            if (digraph.outdegree(i) == 0) {
                numRoots++;
            }
        }
        return (numRoots == 1);
    }

    private void readSynsets(String synsets) {
        In in = new In(synsets);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] strings = line.split(",");
            if (strings.length < 2) continue;

            // map id to synset
            int id = Integer.parseInt(strings[0]);
            synsetMap.put(id, strings[1]);
            numSynsets++;

            // map noun to id's
            String[] nouns = strings[1].split(" ");
            for (String noun : nouns) {
                ArrayList<Integer> ids = nounToIds.get(noun);
                if (ids == null) {
                    ids = new ArrayList<Integer>();
                }
                ids.add(id);
                nounToIds.put(noun, ids);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> v = nounToIds.get(nounA);
        ArrayList<Integer> w = nounToIds.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // TODO
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> v = nounToIds.get(nounA);
        ArrayList<Integer> w = nounToIds.get(nounB);
        int ancestorId = sap.ancestor(v, w);
        return synsetMap.get(ancestorId);

    }

    // do unit testing of this class
    public static void main(String[] args) {
        String hypernyms = "hypernyms.txt";
        String synsets = "synsets.txt";
        WordNet wordNet = new WordNet(synsets, hypernyms);
        StdOut.println(wordNet.synsetMap.get(41));
        StdOut.println(wordNet.nounToIds.get("worm"));
    }
}
