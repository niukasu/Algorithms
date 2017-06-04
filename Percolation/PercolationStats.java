import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    /**
     * Performs T independent computational experiments on an N-by-N grid.
     */
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("Given N <= 0 || T <= 0");
        }
        double[] percolationThresholds = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);

            int tests = 0;
            while (!percolation.percolates()) {
                int column;
                int row;

                do {
                    column = 1 + StdRandom.uniform(n);
                    row = 1 + StdRandom.uniform(n);
                } while (percolation.isOpen(row, column));

                percolation.open(row, column);
                tests++;
            }

            percolationThresholds[i] = tests / (double) (n * n);
        }

        mean = StdStats.mean(percolationThresholds);
        stddev = StdStats.stddev(percolationThresholds);
        double confidenceFraction = (1.96 * stddev()) / Math.sqrt(t);
        confidenceLo = mean - confidenceFraction;
        confidenceHi = mean + confidenceFraction;
    }

    public double mean() {  // sample mean of percolation threshold
        return mean;
    }

    public double stddev() {    // sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceLo() {  // low  endpoint of 95% confidence interval
        return confidenceLo;
    }

    public double confidenceHi() {   // high endpoint of 95% confidence interval
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}