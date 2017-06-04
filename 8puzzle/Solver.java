import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node solution;
    private class Node implements Comparable<Node> {
        public int distance;
        private Board boarding;
        public Node parent;
        public int move;
        public boolean twins;
        Node(Board that, int distance, Node parent, int move, boolean twins) {
            this.boarding = that;
            this.distance = distance;
            this.parent = parent;
            this.move = move;
            this.twins = twins;
        }
        public int compareTo(Node that) {
            return Integer.compare(this.distance, that.distance);
        }
        public Board getBoard() {
            return boarding;
        }
    }
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        MinPQ<Node> pq = new MinPQ<Node>();
        Node temp = new Node(initial, initial.manhattan(), null, 0, false);
        pq.insert(temp);
        Node tempTwin = new Node(initial.twin(), initial.twin().manhattan(), null, 0, true);
        pq.insert(tempTwin);
        while (!pq.isEmpty()){
            temp = pq.delMin();
            if (temp.getBoard().isGoal()) {
                if (!temp.twins) solution = temp;
                else solution = null;
                break;
            }
            for (Board element: temp.getBoard().neighbors()){
                if (temp.parent == null || !temp.parent.getBoard().equals(element)) {
                    Node slot = new Node(element, element.manhattan()+temp.move+1, temp, temp.move + 1, temp.twins);
                    pq.insert(slot);
                }
            }
        }
    }
    public boolean isSolvable() {           // is the initial board solvable?
        return solution != null;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable()) {
            return solution.move;
        }
        else {
            return -1;
        }
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        Stack<Board> temp = new Stack<Board>();
        Node result = solution;
        while (result != null) {
            temp.push(result.getBoard());
            result = result.parent;
        }
        return temp;
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}