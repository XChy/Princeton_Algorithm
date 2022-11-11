import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;

    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        this.thresholds = new double[trials];
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
            thresholds[i] = (double) percolation.numberOfOpenSites() / (double) (n * n);
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
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean=" + ps.mean());
        System.out.println("stddev=" + ps.stddev());
        System.out.println(
                "95% confidence interval=[" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
    }

}