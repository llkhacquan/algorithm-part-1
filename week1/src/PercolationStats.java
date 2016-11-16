import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final double mean;
	private final double stddev;
	private final double low, high;

	public PercolationStats(int n, int trials) {
		if (n < 1 || trials < 1) {
			throw new IllegalArgumentException();
		}
		double[] array = new double[trials];

		for (int iTrial = 0; iTrial < trials; iTrial++) {
			array[iTrial] = (double) testPercolation(n) / n / n;
		}
		mean = StdStats.mean(array);
		stddev = StdStats.stddev(array);
		low = mean - 1.96 * stddev / Math.sqrt(trials);
		high = mean + 1.96 * stddev / Math.sqrt(trials);
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int nTrials = Integer.parseInt(args[1]);
		PercolationStats starts = new PercolationStats(n, nTrials);
		System.out.println("mean                    = " + starts.mean());
		System.out.println("stddev                  = " + starts.stddev());
		System.out.println("95% confidence interval = " + starts.confidenceLo() + ", " + starts.confidenceHi());
	}

	private static int testPercolation(int n) {
		Percolation p = new Percolation(n);
		int count = 0;
		while (!p.percolates()) {
			int x = StdRandom.uniform(n) + 1;
			int y = StdRandom.uniform(n) + 1;
			if (!p.isOpen(x, y)) {
				p.open(x, y);
				count++;
			}
		}
		return count;
	}

	public double mean() {
		// sample mean of percolation threshold
		return mean;
	}

	public double stddev() {
		// sample standard deviation of percolation threshold
		return stddev;
	}

	public double confidenceLo() {
		// low endpoint of 95% confidence interval
		return low;
	}

	public double confidenceHi() {
		// high endpoint of 95% confidence interval
		return high;
	}

}
