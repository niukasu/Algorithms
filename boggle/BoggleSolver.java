import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Bag;

import java.util.ArrayList;

public class BoggleSolver
{

    private static final int R = 26;        // extended ASCII

    private Node root;      // root of trie

    private boolean[] onPath;        // vertices in current path
    private BoggleBoard board;
    private ArrayList<Bag<Integer>> adj;
    private int rows;
    private int cols;

    // R-way trie node
    private static class Node {
        private boolean val;
        private Node[] next = new Node[R];
    }

    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            put(s,true);
        }
    }

    private void dfs(int v, Node x, SET<String> results, StringBuilder path) {
        if (x == null) return;
        if (board.getLetter(v / cols, v % cols) == 'Q' && x.next['U' - 65] == null) return;
        // add v to current path
        path.append(board.getLetter(v / cols, v % cols));
        boolean deleteU = false;
        if (board.getLetter(v / cols, v % cols) == 'Q' && x.next['U' - 65] != null) {
            deleteU = true;
            path.append('U');
            x = x.next['U' - 65];
        }
        onPath[v] = true;
        if (x.val && path.length() >= 3) results.add(path.toString());
        // found path from s to t
        for (int w : adj.get(v)) {
            if (!onPath[w]) {
                dfs(w, x.next[board.getLetter(w / cols, w % cols) - 65], results, path);
            }
        }
        onPath[v] = false;
        path.deleteCharAt(path.length() - 1);
        if (deleteU) path.deleteCharAt(path.length() - 1);
    }

    private boolean get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.val;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     */
    private boolean contains(String key) {
        return get(key);
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 65;
        return get(x.next[c], key, d+1);
    }

    private void put(String key, boolean val) {
        if (val) root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, boolean val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        int c = key.charAt(d) - 65;
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        onPath = new boolean[board.rows() * board.cols()];
        this.rows = board.rows();
        this.cols = board.cols();
        adj = new ArrayList<Bag<Integer>>();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                int v = i * this.cols + j;
                Bag<Integer> temp = new Bag<Integer>();
                if (i - 1 >= 0 && j - 1 >= 0) temp.add((i - 1) * this.cols + j - 1);
                if (i - 1 >= 0) temp.add((i - 1) * this.cols + j);
                if (i - 1 >= 0 && j + 1 < cols) temp.add((i - 1) * this.cols + j + 1);
                if (j - 1 >= 0) temp.add(i * this.cols + j - 1);
                if (j + 1 < cols) temp.add(i * this.cols + j + 1);
                if (i + 1 < rows && j - 1 >= 0) temp.add((i + 1) * this.cols + j - 1);
                if (i + 1 < rows) temp.add((i + 1) * this.cols + j);
                if (i + 1 < rows && j + 1 < cols) temp.add((i + 1) * this.cols + j + 1);
                adj.add(temp);
            }
        }
        SET<String> results = new SET<String>();
        if (root == null) return results;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(i * cols + j, root.next[board.getLetter(i, j) - 65], results, new StringBuilder());
            }
        }
        return results;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word)) {
            if (word.length() >= 8) {
                return 11;
            }
            else if (word.length() == 7) {
                return 5;
            }
            else if (word.length() == 6) {
                return 3;
            }
            else if (word.length() == 5) {
                return 2;
            }
            else if (word.length() == 4 || word.length() == 3) {
                return 1;
            }
            return 0;
        }
        return 0;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }

        StdOut.println("Score = " + score);
    }

}