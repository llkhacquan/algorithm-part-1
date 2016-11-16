import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
	private boolean[] opened;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF uf2;
	private final int TOP;
	private final int BOTTOM;
	private boolean percolates = false;

    public Percolation(int inputN) {
        if (inputN < 1)
            throw new IllegalArgumentException("N should be greater than 0");
        n = inputN;
		opened = new boolean[n * n];

		TOP = n * n;
		BOTTOM = TOP + 1;
		// initiate Union-Find
		uf = new WeightedQuickUnionUF(n * n + 2);
		uf2 = new WeightedQuickUnionUF(n * n + 1);
	}

	/**
	 * 1<=i,j<=n
	 */
	private int _2to1(int i, int j) {
		assert isLegal(i, j);
		return (i - 1) * n + j - 1;
	}

    public void open(int i, int j) {
		if (!isLegal(i, j)) {
			throw new IndexOutOfBoundsException();
		}

		if (isOpen(i, j)) {
			return;
		}
		opened[_2to1(i, j)] = true;

		if (isLegal(i - 1, j) && opened[_2to1(i - 1, j)]) {
			uf.union(_2to1(i, j), _2to1(i - 1, j));
			uf2.union(_2to1(i, j), _2to1(i - 1, j));
		}

		if (isLegal(i + 1, j) && opened[_2to1(i + 1, j)]) {
			uf.union(_2to1(i, j), _2to1(i + 1, j));
			uf2.union(_2to1(i, j), _2to1(i + 1, j));
		}

		if (isLegal(i, j - 1) && opened[_2to1(i, j - 1)]) {
			uf.union(_2to1(i, j), _2to1(i, j - 1));
			uf2.union(_2to1(i, j), _2to1(i, j - 1));
		}

		if (isLegal(i, j + 1) && opened[_2to1(i, j + 1)]) {
			uf.union(_2to1(i, j), _2to1(i, j + 1));
			uf2.union(_2to1(i, j), _2to1(i, j + 1));
		}

		if (i == 1) {
			uf.union(_2to1(i, j), TOP);
			uf2.union(_2to1(i, j), TOP);
		}
		if (i == n) {
			uf.union(_2to1(i, j), BOTTOM);
		}
	}

    public boolean isOpen(int i, int j) {
		if (!isLegal(i, j)) {
			throw new IndexOutOfBoundsException();
		}
		return opened[_2to1(i, j)];
	}

    public boolean isFull(int i, int j) {
		if (!isLegal(i, j)) {
			throw new IndexOutOfBoundsException();
		}
		return uf2.connected(_2to1(i, j), TOP);
	}

    public boolean percolates() {
		return uf.connected(TOP, BOTTOM);
	}

	/**
	 * 1<=i, j<=n
	 */
	private boolean isLegal(int i, int j) {
		return !(i < 1 || j < 1 || i > n || j > n);
	}

}
