import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private char[] board_;
    private int dimension_;
    
    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
        dimension_ = blocks.length;
        board_ = new char[dimension_*dimension_];
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++)
                board_[i*dimension_+j] = (char) blocks[i][j];
    }
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                // board dimension n
        return dimension_;
    }
    public int hamming() {                  // number of blocks out of place
        int count = 0;
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++)
                if (board_[i*dimension_+j] != 0 && board_[i*dimension_+j] != i*dimension_+j+1) count++;
        return count;
    }
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int count = 0;
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++) 
                if (board_[i*dimension_+j] != 0) {
                    int value = (int) board_[i*dimension_+j];
                    int tempX = (value - 1) / dimension_;
                    int tempY = value - 1 - tempX * dimension_;
                    count += Math.abs(tempX - i) + Math.abs(tempY - j);
                }
        return count;
    }
    public boolean isGoal() {               // is this board the goal board?
        return hamming() == 0;
    }
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int first = 0;
        int second = 0;
        int blocks[][] = new int[dimension_][dimension_];
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++) {
                blocks[i][j] = (int) board_[i*dimension_+j];
            }  
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            int temp = blocks[0][0];
            blocks[0][0] = blocks[0][1];
            blocks[0][1] = temp;
        } else {
            int temp = blocks[1][0];
            blocks[1][0] = blocks[1][1];
            blocks[1][1] = temp;
        }
        return new Board(blocks);
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension_ != that.dimension_) return false;
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++)
                if (this.board_[i*dimension_+j] != that.board_[i*dimension_+j]) return false;
        return true;
    }
    public Iterable<Board> neighbors() {    // all neighboring boards
        Stack<Board> temp = new Stack<Board>();
        int row = 0;
        int column = 0;
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++) {
                if (board_[i*dimension_+j] == 0) {
                    row = i;
                    column = j;
                    break;
                } 
            }
        if (row != 0) temp.push(create(row, row-1, column, column));
        if (row != dimension_ - 1) temp.push(create(row, row+1, column, column));
        if (column != 0) temp.push(create(row, row, column-1, column));
        if (column != dimension_ - 1) temp.push(create(row, row, column+1, column));
        return temp;
    }
    public String toString() {              // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        int n = dimension_;
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) board_[i*dimension_+j]));
            }
            s.append("\n");
        }
        return s.toString();    
    }

    private Board create(int rowOne, int rowTwo, int colOne, int colTwo) {
        int blocks[][] = new int[dimension_][dimension_];
        for (int i = 0; i < dimension_; i++)
            for (int j = 0; j < dimension_; j++) {
                blocks[i][j] = board_[i*dimension_+j];
            }
        int temp = blocks[rowOne][colOne];
        blocks[rowOne][colOne] = blocks[rowTwo][colTwo];
        blocks[rowTwo][colTwo] = temp;
        Board newBoard = new Board(blocks);
        return newBoard;
    }
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.twin());
    }
}