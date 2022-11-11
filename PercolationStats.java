import static java.lang.Math.sqrt;

public class PercolationStats {

    private Percolation[] percolations;
    private double[] thresholds;

    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        percolations = new Percolation[n];
        for (int i = 0; i < trials; ++i) {
            percolations[i] = new Percolation(n);
        }
        this.n = n;
        // TODO:perform trials
    }

    // sample mean of percolation threshold
    public double mean() {
        double meanNum = 0;
        for (int i = 0; i < n; ++i) {
            meanNum += thresholds[i];
        }
        meanNum /= n;
        return meanNum;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double meanNum = mean();
        double s_2 = 0;
        for (int i = 0; i < n; i++) {
            s_2 += (thresholds[i] - meanNum) * (thresholds[i] - meanNum);
        }
        s_2 /= n - 1;
        return sqrt(s_2);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / sqrt(n);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / sqrt(n);
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}