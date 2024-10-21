/* *****************************************************************************
 *  Name:              Carter Hills
 *  Coursera User ID:  123456
 *  Last modified:     10/10/2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }

        double[] percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            int runs = 0;
            while (!percolation.percolates()) {
                int column;
                int row;

                do {
                    column = 1 + StdRandom.uniformInt(n);
                    row = 1 + StdRandom.uniformInt(n);
                } while (percolation.isOpen(row, column));

                percolation.open(row, column);
                runs++;
            }
            percolationThresholds[i] = runs / (double) (n * n);
        }
        mean = StdStats.mean(percolationThresholds);
        stddev = StdStats.stddev(percolationThresholds);
        double confidenceThreshold = 1.96 * stddev / Math.sqrt(trials);
        confidenceLo = mean - confidenceThreshold;
        confidenceHi = mean + confidenceThreshold;

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println(
                "95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi()
                        + "]");

    }

}
