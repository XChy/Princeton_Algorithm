import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] isOpened;
    private WeightedQuickUnionUF uf;

    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        isOpened = new boolean[n * n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
    }

    private int translatePos(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * n + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col))
            return;
        int index = translatePos(row, col);
        isOpened[index] = true;
        if (row == 1) {
            uf.union(0, index);
        }
        else if (row > 1) {
            if (isOpen(row - 1, col))
                uf.union(translatePos(row - 1, col), index);
        }
        if (row == n) {
            uf.union(n * n + 1, index);
        }
        else if (row < n) {
            if (isOpen(row + 1, col))
                uf.union(translatePos(row + 1, col), index);
        }

        if (col > 1) {
            if (isOpen(row, col - 1))
                uf.union(translatePos(row, col - 1), index);
        }
        if (col < n) {
            if (isOpen(row, col + 1))
                uf.union(translatePos(row, col + 1), index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return isOpened[translatePos(row, col)];
    }

    // Index starts from 1


    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = translatePos(row, col);
        if (isOpen(row, col)) {
            return uf.find(index) == uf.find(0); // check whether connected to the top
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 1; i <= n * n; ++i) {
            if (isOpened[i]) {
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