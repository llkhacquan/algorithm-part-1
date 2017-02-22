import edu.princeton.cs.algs4.Picture;

import java.awt.*;

/**
 * Created by quannk on 22/02/2017.
 */
public class SeamCarver {
	private static final int BORDER_ENERGY = 1000;
	private static final byte NONE = 3;
	private static final byte UP = 0;
	private static final byte LEFT = 1;
	private static final byte RIGHT = 2;
	private final Color c[][];
	private int w, h;

	public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	{
		w = picture.width();
		h = picture.height();
		c = new Color[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				c[x][y] = picture.get(x, y);
			}
		}
	}

	public static void main(String[] args) {
		Picture picture = new Picture("part2-week2/seamCarving/HJocean.png");
		rotateLeft(picture).show();
	}

	private static Picture rotateLeft(Picture p) {
		Picture r = new Picture(p.height(), p.width());
		for (int x = 0; x < p.width(); x++) {
			for (int y = 0; y < p.height(); y++) {
				r.set(y, x, p.get(x, y));
			}
		}
		return r;
	}

	public Picture picture()                          // current picture
	{
		Picture p = new Picture(w, h);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				p.set(x, y, c[x][y]);
			}
		}
		return p;
	}

	public int width()                            // width of current picture
	{
		return w;
	}

	public int height()                           // height of current picture
	{
		return h;
	}

	/**
	 * energy of pixel at column x and row y
	 */
	public double energy(int x, int y) {
		if (x < 0 || x >= w || y < 0 || y >= h) {
			throw new IndexOutOfBoundsException();
		}
		if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
			return BORDER_ENERGY;
		}
		return Math.sqrt(d(c[x - 1][y], c[x + 1][y])
				+ d(c[x][y - 1], c[x][y + 1]));
	}

	private int d(Color c1, Color c2) {
		int result = 0;
		result += (c2.getGreen() - c1.getGreen()) * (c2.getGreen() - c1.getGreen());
		result += (c2.getBlue() - c1.getBlue()) * (c2.getBlue() - c1.getBlue());
		result += (c2.getRed() - c1.getRed()) * (c2.getRed() - c1.getRed());
		return result;
	}

	public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	{
		return new SeamCarver(rotateLeft(picture())).findVerticalSeam();
	}

	public int[] findVerticalSeam()                 // sequence of indices for vertical seam
	{
		// init result array
		int r[] = new int[h];

		// init total energy array: e[i][j] = smallest energy of paths from top to [i, j]
		// init the track array: e[i][j] = {UP, LEFT, RIGHT}
		byte track[][] = new byte[w][h];
		double e[][] = new double[w][h];

		// calculate first row of e
		for (int i = 0; i < w; i++) {
			e[i][0] = energy(i, 0);
			track[i][0] = NONE;
		}

		// calculate other row of e, from 1 to height - 1
		for (int y = 1; y < h; y++) {
			if (w > 1) {
				// calculate energy for the first element
				if (e[0][y - 1] < e[1][y - 1]) {
					e[0][y] = e[0][y - 1] + energy(0, y);
					track[0][y] = UP;
				} else {
					e[0][y] = e[1][y - 1] + energy(0, y);
					track[0][y] = RIGHT;
				}
				// calculate energy for the last element
				int x = w - 1;
				if (e[x][y - 1] < e[x - 1][y - 1]) {
					e[x][y] = e[x][y - 1] + energy(x, y);
					track[x][y] = UP;
				} else {
					e[x][y] = e[x - 1][y - 1] + energy(x, y);
					track[x][y] = LEFT;
				}
			}
			// calculate energy for other elements: from 1 to w - 2;
			for (int x = 1; x < w - 1; x++) {
				double min = e[x - 1][y - 1];
				byte t = LEFT;
				if (min > e[x][y - 1]) {
					min = e[x][y - 1];
					t = UP;
				}
				if (min > e[x + 1][y - 1]) {
					min = e[x + 1][y - 1];
					t = RIGHT;
				}
				e[x][y] = min + energy(x, y);
				track[x][y] = t;
			}
		}

		// ok, we got the energy array, let calculate the seam
		// find the end point of the seam
		int xMinEnergy = 0;
		double minE = e[xMinEnergy][h - 1];
		for (int i = 1; i < w; i++) {
			if (minE > e[i][h - 1]) {
				xMinEnergy = i;
				minE = e[i][h - 1];
			}
		}

		// fill the result array
		for (int j = h - 1; j >= 0; j--) {
			r[j] = xMinEnergy;
			switch (track[xMinEnergy][j]) {
				case LEFT:
					xMinEnergy--;
					break;
				case RIGHT:
					xMinEnergy++;
					break;
			}
		}

		return r;
	}

	public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
		for (int x = 0; x < w; x++) {
			for (int y = seam[x]; y < h - 1; y++) {
				c[x][y] = c[x][y + 1];
			}
		}
		h--;
	}

	public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
	{
		for (int y = 0; y < h; y++) {
			for (int x = seam[y]; x < w - 1; w++) {
				c[x][y] = c[x + 1][y];
			}
		}
		w--;
	}
}