import edu.princeton.cs.algs4.Picture;

import java.awt.*;

/**
 * Created by quannk on 22/02/2017.
 */
public class SeamCarver {
	private static final int BORDER_COLOR_ELEMENT = 1000;
	private static final byte NONE = 3;
	private static final byte UP = 0;
	private static final byte LEFT = 1;
	private static final byte RIGHT = 2;

	private Picture p;

	public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	{
		p = new Picture(picture);
	}

	public Picture picture()                          // current picture
	{
		return p;
	}

	public int width()                            // width of current picture
	{
		return p.width();
	}

	public int height()                           // height of current picture
	{
		return p.height();
	}

	public double energy(int x, int y)               // energy of pixel at column x and row y
	{
		if (x < 0 || x >= p.width() || y < 0 || y >= p.height()) {
			throw new IndexOutOfBoundsException();
		}
		return Math.sqrt(dX2(x, y) + dY2(x, y));
	}

	private int dX2(int x, int y) {
		Color c = p.get(x, y);
		int result = 0;
		if (x > 0) {
			result += distance(c, p.get(x - 1, y));
		} else {
			result += distance(c, null);
		}
		if (x < p.width() - 1) {
			result += distance(c, p.get(x + 1, y));
		} else {
			result += distance(c, null);
		}
		return result;
	}

	private int dY2(int x, int y) {
		Color c = p.get(x, y);
		int result = 0;
		if (y > 0) {
			result += distance(c, p.get(x, y - 1));
		} else {
			result += distance(c, null);
		}
		if (y < p.height() - 1) {
			result += distance(c, p.get(x, y + 1));
		} else {
			result += distance(c, null);
		}
		return result;
	}

	private int distance(Color c, Color c1) {
		int result = 0;
		if (c1 != null) {
			result += (c1.getAlpha() - c.getAlpha()) * (c1.getAlpha() - c.getAlpha());
			result += (c1.getBlue() - c.getBlue()) * (c1.getBlue() - c.getBlue());
			result += (c1.getRed() - c.getRed()) * (c1.getRed() - c.getRed());
		} else {
			result += (BORDER_COLOR_ELEMENT - c.getAlpha()) * (BORDER_COLOR_ELEMENT - c.getAlpha());
			result += (BORDER_COLOR_ELEMENT - c.getBlue()) * (BORDER_COLOR_ELEMENT - c.getBlue());
			result += (BORDER_COLOR_ELEMENT - c.getRed()) * (BORDER_COLOR_ELEMENT - c.getRed());
		}
		return result;
	}

	public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	{
		return null;
	}

	public int[] findVerticalSeam()                 // sequence of indices for vertical seam
	{
		// init result array
		int r[] = new int[p.height()];

		// init total energy array: e[i][j] = smallest energy of paths from top to [i, j]
		// init the track array: e[i][j] = {UP, LEFT, RIGHT}
		byte track[][] = new byte[p.height()][p.width()];
		double e[][] = new double[p.height()][p.width()];

		// calculate first row of e
		for (int i = 0; i < p.width(); i++) {
			e[0][i] = energy(0, i);
			track[0][i] = NONE;
		}

		// calculate other row of e, from 1 to height - 1
		for (int j = 1; j < p.height(); j++) {
			// calculate energy for the first element
			if (e[j - 1][0] > e[j - 1][1]) {
				e[j][0] = e[j - 1][0] + energy(j, 0);
				track[j][0] = UP;
			} else {
				e[j][0] = e[j - 1][1] + energy(j, 0);
				track[j][0] = RIGHT;
			}

			// calculate energy for the last element
			int i = p.width() - 1;
			if (e[j - 1][i] > e[j - 1][i - 1]) {
				e[j][0] = e[j - 1][i] + energy(j, i);
				track[j][0] = UP;
			} else {
				e[j][0] = e[j - 1][i - 1] + energy(j, i);
				track[j][0] = LEFT;
			}

			// calculate energy for other elements: from 1 to p.width() - 2;
			for (i = 1; i < p.width() - 1; i++) {
				double max = e[j - 1][i - 1];
				byte t = LEFT;
				if (max < e[j - 1][i]) {
					max = e[j - 1][i];
					t = UP;
				}
				if (max < e[j - 1][i + 1]) {
					max = e[j - 1][i + 1];
					t = RIGHT;
				}
				e[j][i] = max + energy(i, j);
				track[j][i] = t;
			}
		}

		// ok, we got the energy array, let calculate the seam
		// find the end point of the seam
		int xMaxEnergy = 0;
		double maxE = e[p.height() - 1][xMaxEnergy];
		for (int i = 1; i < p.width(); i++) {
			if (maxE < e[p.height() - 1][i]) {
				xMaxEnergy = i;
				maxE = e[p.height() - 1][i];
			}
		}

		// fill the result array
		for (int j = p.height() - 1; j >= 0; j--) {
			r[j] = xMaxEnergy;
			switch (track[j][xMaxEnergy]) {
				case LEFT:
					xMaxEnergy--;
					break;
				case RIGHT:
					xMaxEnergy++;
					break;
			}
		}

		return r;
	}

	public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
	}

	public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
	{

	}
}