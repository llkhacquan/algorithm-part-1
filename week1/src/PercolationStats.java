import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private int nTrials;
    private double[] array;
    private double mean;
    private double stddev;

    public PercolationStats(int inputN, int trials) {
        if (inputN < 1 || trials < 1)
            throw new IllegalArgumentException();
        n = inputN;
        nTrials = trials;

        array = new double[nTrials];

        for (int iTrial = 0; iTrial < nTrials; iTrial++) {
            array[iTrial] = (double) testPercolation(inputN) / inputN / inputN;
        }
        mean = StdStats.mean(array);
        stddev = StdStats.stddev(array);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int nTrials = Integer.parseInt(args[1]);
        // System.out.println("Start running");
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
        return mean - (1.96 * Math.sqrt(stddev) / n);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mean + (1.96 * Math.sqrt(stddev) / n);
    }

}
