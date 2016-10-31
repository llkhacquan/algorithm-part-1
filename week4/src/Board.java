import java.util.Stack;

/**
 * Created by quannk on 13/10/2016.
 */
public class Board {
	private final int n;
	private final int hamming;
	private final int manhattan;
	private final int[] blocks;
	/**
	 * position of zero
	 */

	private int iZero;

	/**
	 * construct a board from an n-by-n array of blocks
	 *
	 * @param blocks
	 */
	public Board(int[][] blocks) {
		n = blocks.length;
		this.blocks = new int[blocks.length * blocks.length];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				this.blocks[i * n + j] = blocks[i][j];
				if (blocks[i][j] == 0)
					iZero = i * n + j;
			}

		// calculating haming and manhattan
		int manhattan = 0;
		int hamming = 0;
		for (int i = 0; i < n * n - 1; i++) {
			int block = this.blocks[i];
			if (block > 0)
				manhattan += distance(i, block - 1);
			if (block != i + 1)
				hamming++;
		}
		int t = this.blocks[n * n - 1];
		if (t > 0)
			manhattan += distance(n * n - 1, t - 1);
		this.manhattan = manhattan;
		this.hamming = hamming;

	}

	private Board(int[] blocks, Move m) {
		n = (int) Math.sqrt(blocks.length);
		this.blocks = new int[blocks.length];
		for (int i = 0; i < n * n; i++) {
			this.blocks[i] = blocks[i];
			if (blocks[i] == 0)
				iZero = i;
		}

		if (m != null)
			switch (m) {
				case LEFT:
					swap(iZero, iZero - 1);
					iZero--;
					break;
				case RIGHT:
					swap(iZero, iZero + 1);
					iZero++;
					break;
				case UP:
					swap(iZero, iZero - n);
					iZero -= n;
					break;
				case DOWN:
					swap(iZero, iZero + n);
					iZero += n;
					break;
				default:
					assert false;
			}

		// calculating haming and manhattan
		int manhattan = 0;
		int haming = 0;
		for (int i = 0; i < n * n - 1; i++) {
			int block = this.blocks[i];
			if (block > 0)
				manhattan += distance(i, block - 1);
			if (block != i + 1)
				haming++;
		}
		int t = this.blocks[n * n - 1];
		if (t > 0)
			manhattan += distance(n * n - 1, t - 1);
		this.manhattan = manhattan;
		this.hamming = haming;
	}

	private void swap(int i, int j) {
		int temp = blocks[i];
		blocks[i] = blocks[j];
		blocks[j] = temp;
	}

	private Board move(Move m) {
		return new Board(this.blocks, m);
	}

	/**
	 * board dimension n
	 *
	 * @return
	 */
	public int dimension() {
		return n;
	}

	/**
	 * number of blocks out of place
	 *
	 * @return
	 */
	public int hamming() {
		return hamming;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 *
	 * @return
	 */
	public int manhattan() {
		return manhattan;
	}

	private int distance(int a, int b) {
		assert a >= 0 && a < n * n && b >= 0 && b < n * n;
		return Math.abs(a % n - b % n) + Math.abs(a / n - b / n);
	}

	public boolean isGoal() {
		for (int i = 0; i < n * n - 1; i++)
			if (blocks[i] != i + 1)
				return false;
		return true;
	}

	/**
	 * board that is obtained by exchanging 1 pair of random blocks
	 *
	 * @return
	 */
	public Board twin() {
		Board b = new Board(this.blocks, null);
		if (b.blocks[0] != 0 && b.blocks[1] != 0)
			b.swap(0, 1);
		else
			b.swap(2, 3);
		return b;
	}

	public boolean equals(Object y) {
		if (y == null)
			return false;
		if (y == this)
			return true;
		if (y.getClass() != Board.class)
			return false;
		Board b = (Board) y;
		if (n != b.n || manhattan != b.manhattan || hamming != b.hamming)
			return false;
		for (int i = 0; i < n * n; i++)
			if (blocks[i] != ((Board) y).blocks[i])
				return false;
		return true;
	}

	public Iterable<Board> neighbors() {
		Stack<Board> boards = new Stack<>();
		int x = iZero % n;
		int y = iZero / n;
		if (x > 0)
			boards.add(move(Move.LEFT));
		if (x < n - 1)
			boards.add(move(Move.RIGHT));
		if (y > 0)
			boards.add(move(Move.UP));
		if (y < n - 1)
			boards.add(move(Move.DOWN));
		return boards;
	}

	public String toString() {
		StringBuilder s = new StringBuilder(n + "\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(String.format("%2d ", blocks[i * n + j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	private static enum Move {
		LEFT, RIGHT, UP, DOWN
	}
}
