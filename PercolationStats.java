import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;

    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        thresholds = new double[trials];
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            int j;
            for (j = 1; j <= n * n; ++j) {
                int row, col;
                row = StdRandom.uniformInt(n) + 1;
                col = StdRandom.uniformInt(n) + 1;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
                else {
                    j--;
                }

                if (percolation.percolates()) {
                    break;
                }
            }
            thresholds[i] = (double) j / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(n);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(n);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(2, 100000);
        System.out.println(stats.mean());
        System.out.println(stats.stddev());
    }

}