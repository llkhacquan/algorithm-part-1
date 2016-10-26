import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private boolean[][] openned;
    private WeightedQuickUnionUF uf;

    public Percolation(int inputN) {
        if (inputN < 1)
            throw new IllegalArgumentException("N should be greater than 0");
        n = inputN;
        openned = new boolean[n][];
        for (int i = 0; i < n; i++) {
            openned[i] = new boolean[n];
            for (int j = 0; j < n; j++)
                openned[i][j] = false;
        }

        // initiate Union-Find
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    private int twoDtoOneD(int i, int j) {
        if (!isLegalPosition(i + 1, j + 1))
            throw new IndexOutOfBoundsException();
        return i * n + j;
    }

    public void open(int i, int j) {
        if (!isLegalPosition(i, j))
            throw new IndexOutOfBoundsException();

        if (isOpen(i, j))
            return;

        openned[i - 1][j - 1] = true;
        if (isLegalPosition(i - 1, j)) {
            if (openned[i - 2][j - 1]) {
                uf.union(twoDtoOneD(i - 1, j - 1), twoDtoOneD(i - 2, j - 1));
            }
        }

        if (isLegalPosition(i + 1, j)) {
            if (openned[i][j - 1]) {
                uf.union(twoDtoOneD(i - 1, j - 1), twoDtoOneD(i, j - 1));
            }
        }

        if (isLegalPosition(i, j - 1)) {
            if (openned[i - 1][j - 2]) {
                uf.union(twoDtoOneD(i - 1, j - 1), twoDtoOneD(i - 1, j - 2));
            }
        }

        if (isLegalPosition(i, j + 1)) {
            if (openned[i - 1][j]) {
                uf.union(twoDtoOneD(i - 1, j - 1), twoDtoOneD(i - 1, j));
            }
        }

        if (i == 1) {
            uf.union(twoDtoOneD(i - 1, j - 1), n * n);
        }
        if (i == n) {
            uf.union(twoDtoOneD(i - 1, j - 1), n * n + 1);
        }
    }

    public boolean isOpen(int i, int j) {
        if (!isLegalPosition(i, j))
            throw new IndexOutOfBoundsException();
        return openned[i - 1][j - 1];
    }

    public boolean isFull(int i, int j) {
        if (!isLegalPosition(i, j))
            throw new IndexOutOfBoundsException();
        // is site (row i, column j) full?
        return uf.connected(twoDtoOneD(i - 1, j - 1), n * n);
    }

    public boolean percolates() {
        // does the system percolate?
        return uf.connected(n * n, n * n + 1);
    }

    private boolean isLegalPosition(int i, int j) {
        if (i < 1 || j < 1 || i > n || j > n)
            return false;
        else
            return true;
    }

}
