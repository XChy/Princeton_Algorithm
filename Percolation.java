import edu.princeton.cs.algs4.QuickUnionUF;

public class Percolation {
    private boolean[][] isOpened;
    private QuickUnionUF uf;

    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        isOpened = new boolean[n][n];
        uf = new QuickUnionUF(n * n + 2);
        this.n = n;
    }

    int translatePos(int row, int col) {
        if (row < 0 || row > n || col < 0 || col > n) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * n + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isOpened[row - 1][col - 1] = true;
        int index = translatePos(row, col);
        // if (row == 1) {
        //     uf.union(0, index);
        //     uf.union(translatePos(row + 1, col), index);
        // }
        // else if (row < n) {
        //     uf.union();
        // }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return isOpened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = translatePos(row, col);
        if (isOpen(row, col)) {
            return uf.find(index) == uf.find(0);// check whether connected to the top
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 0; i < n * n; ++i) {
            if (isOpened[i / n][i % n]) {
                num++;
            }
        }
        return num;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n * n + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}