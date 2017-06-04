import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int[] distance = new int[nouns.length];
        for (int i=0; i<nouns.length; i++){
            for (int j=i; j<nouns.length; j++){
                int dist = wordnet.distance(nouns[i], nouns[j]);
                distance[i] += dist;
                if (i != j){
                    distance[j] += dist;
                }
            }
        }
        int min = 0;
        int maxDistance = 0;
        for (int i = 0;i < nouns.length; ++i) {
            if (distance[i] > maxDistance) {
                maxDistance = distance[i];
                min = i;
            }
        }
        return nouns[min];
    }
    // given an array of WordNet nouns, return an outcast
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
    // see test client below
}
