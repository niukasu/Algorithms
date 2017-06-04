import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import java.lang.*;
import java.util.*;

public class WordNet {

    private HashMap<String,LinkedList<Integer>> wordToIndex;
    private ArrayList<String> indexToWord;
    private int len;
    private final SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new java.lang.NullPointerException();
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        String line;
        int count = 0;
        wordToIndex = new HashMap<String,LinkedList<Integer>>();
        indexToWord = new ArrayList<String>();
        while ((line = inSynsets.readLine()) != null) {
            count++;
            String[] fields;
            fields = line.split(",");
            int index = Integer.parseInt(fields[0]);
            String[] nouns = fields[1].split(" ");
            for (String s : nouns) {
                if (wordToIndex.containsKey(s)) {
                    LinkedList<Integer> temp = wordToIndex.get(s);
                    temp.add(index);
                    wordToIndex.put(s,temp);
                }
                else {
                    LinkedList<Integer> temp = new LinkedList<Integer>();
                    temp.add(index);
                    wordToIndex.put(s,temp);
                }
            }
            indexToWord.add(fields[1]);
        }
        Digraph g = new Digraph(count);
        len = count;
        boolean[] notRoot = new boolean[len];
        while ((line = inHypernyms.readLine()) != null) {
            String[] fields;
            fields = line.split(",");
            int n = fields.length;
            int selfIndex = Integer.parseInt(fields[0]);
            notRoot[selfIndex] = true;
            if (selfIndex >= len) throw new java.lang.IllegalArgumentException();
            for (int i = 1; i < n; i++){
                int parent = Integer.parseInt(fields[i]);
                if (parent >= len) throw new java.lang.IllegalArgumentException();
                g.addEdge(selfIndex, parent);
            }
        }
        DirectedCycle directedCycle = new DirectedCycle(g);  
        if (directedCycle.hasCycle()){  
            throw new java.lang.IllegalArgumentException();  
        }  
        int rootCount = 0;  
        for (boolean isNotRoot : notRoot){  
            if (!isNotRoot){  
                rootCount++;  
            }  
        }  
        if (rootCount > 1){  
            throw new java.lang.IllegalArgumentException();  
        }
        sap = new SAP(g);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToIndex.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
        return wordToIndex.containsKey(word);
    }
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.NullPointerException();
        if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        return sap.length(wordToIndex.get(nounA),wordToIndex.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        return indexToWord.get(sap.ancestor(wordToIndex.get(nounA),wordToIndex.get(nounB)));
    }

    // do unit testing of this class
}