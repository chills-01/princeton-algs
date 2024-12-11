/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;

    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int maxDistance = sumDistances(nouns[0], nouns);
        for (int i = 1; i < nouns.length; i++) {
            int tempDistance = sumDistances(nouns[i], nouns);
            if (tempDistance > maxDistance) {
                maxDistance = tempDistance;
                outcast = nouns[i];
            }
        }
        return outcast;

    }

    private int sumDistances(String noun, String[] nouns) {
        int res = 0;
        for (String currentNoun : nouns) {
            res += wordnet.distance(noun, currentNoun);
        }
        return res;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
