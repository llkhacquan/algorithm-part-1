import java.util.Stack;

/**
 * Created by quannk on 13/10/2016.
 */
public class Board {
    private int[] blocks;
    private int n;

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
    }

    private Board(int[] blocks) {
        n = (int) Math.sqrt(blocks.length);
        this.blocks = new int[blocks.length];
        for (int i = 0; i < n * n; i++) {
            this.blocks[i] = blocks[i];
            if (blocks[i] == 0)
                iZero = i;
        }
    }

    private void swap(int i, int j) {
        int temp = blocks[i];
        blocks[i] = blocks[j];
        blocks[j] = temp;
    }

    private Board move(Move m) {
        Board b = new Board(this.blocks);
        switch (m) {
            case LEFT:
                b.swap(iZero, iZero - 1);
                b.iZero--;
                break;
            case RIGHT:
                b.swap(iZero, iZero + 1);
                b.iZero++;
                break;
            case UP:
                b.swap(iZero, iZero - b.n);
                b.iZero -= n;
                break;
            case DOWN:
                b.swap(iZero, iZero + b.n);
                b.iZero += n;
                break;
            default:
                assert false;
        }

        return b;
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
        int iCount = 0;
        for (int i = 0; i < n * n - 1; i++) {
            if (blocks[i] != i + 1)
                iCount++;
        }
        return iCount;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return
     */
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n * n; i++) {
            int block = blocks[i];
            if (block > 0)
                distance += distance(i, block - 1);
        }
        return distance;
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
        Board b = new Board(this.blocks);
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
        if (!(y instanceof Board))
            return false;
        if (n != ((Board) y).n)
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
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i * n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private enum Move {
        LEFT, RIGHT, UP, DOWN
    }
}
