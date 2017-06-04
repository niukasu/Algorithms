import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] opened;
    private int[] status;
    private int size;
    private WeightedQuickUnionUF qf;
    private boolean perco;

    /**
     * Creates N-by-N grid, with all sites blocked.
     */
    public Percolation(int n) {
        if (n < 1) throw new java.lang.IllegalArgumentException();
        size = n;
        qf = new WeightedQuickUnionUF(size * size + 2);
        opened = new boolean[size][size];
        status = new int[size * size + 2];   // 1 = top 2 = bottom 3 = top&bottom
        perco = false;
    }

    /**
     * Opens site (row i, column j) if it is not already.
     */
    public void open(int i, int j) {
        if (i > size || i < 1 || j < 1 || j > size) throw new java.lang.IndexOutOfBoundsException();
        opened[i - 1][j - 1] = true;
        boolean tempTop = false;
        boolean tempBot = false;        
        if (i == 1) {
            tempTop = true;
        }
        if (i == size) {
            tempBot = true;
        }

        if (j > 1 && isOpen(i, j - 1)) {
            int temp = qf.find(getQFIndex(i, j - 1));
            if (status[temp] == 1) {
                tempTop = true;
            }
            if (status[temp] == 2) {
                tempBot = true;
            }
            if (status[temp] == 3) {
                tempTop = true;
                tempBot = true;
            }
            qf.union(getQFIndex(i, j), getQFIndex(i, j - 1));
        }
        if (j < size && isOpen(i, j + 1)) {
            int temp = qf.find(getQFIndex(i, j + 1));
            if (status[temp] == 1) {
                tempTop = true;
            }
            if (status[temp] == 2) {
                tempBot = true;
            }
            if (status[temp] == 3) {
                tempTop = true;
                tempBot = true;
            }
            qf.union(getQFIndex(i, j), getQFIndex(i, j + 1));
        }
        if (i > 1 && isOpen(i - 1, j)) {
            int temp = qf.find(getQFIndex(i - 1, j));
            if (status[temp] == 1) {
                tempTop = true;
            }
            if (status[temp] == 2) {
                tempBot = true;
            }
            if (status[temp] == 3) {
                tempTop = true;
                tempBot = true;
            }
            qf.union(getQFIndex(i, j), getQFIndex(i - 1, j));
        }
        if (i < size && isOpen(i + 1, j)) {
            int temp = qf.find(getQFIndex(i + 1, j));
            if (status[temp] == 1) {
                tempTop = true;
            }
            if (status[temp] == 2) {
                tempBot = true;
            }
            if (status[temp] == 3) {
                tempTop = true;
                tempBot = true;
            }
            qf.union(getQFIndex(i, j), getQFIndex(i + 1, j));
        }
        int newRoot = qf.find(getQFIndex(i, j));
        if (tempTop && tempBot) {
            perco = true;
            status[newRoot] = 3;
        }
        else if (tempTop) {
            status[newRoot] = 1;
        }
        else if (tempBot) {
            status[newRoot] = 2;
        }
    }

    /**
     * Is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        if (i > size || i < 1 || j < 1 || j > size) throw new java.lang.IndexOutOfBoundsException();
        return opened[i - 1][j - 1];
    }

    /**
     * Is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        if (i > size || i < 1 || j < 1 || j > size) throw new java.lang.IndexOutOfBoundsException();
        int temp = qf.find(getQFIndex(i, j));
        return (status[temp] == 1 || status[temp] == 3);
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return perco;
    }

    private int getQFIndex(int i, int j) {
        return size * (i - 1) + j;
    }
}