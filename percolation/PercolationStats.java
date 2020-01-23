/* *****************************************************************************
 *  Name:    Ahmed Gamaleldin
 *  Description:  Using Percolation class, it calculates mean, standard deviation,
 *                  confidence high and low bya fixed amount of trials, and in each trial
 *                  the number of open sites is calculated.
 *
 *  Written:       10/12/2019
 *  Last updated:  19/12/2019
 *
 *  % javac-algs4 PercolationStats.java
 *  % java-algs4 PercolationStats
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final int trials;    // number of trials
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 && trials < 0)
            throw new IllegalArgumentException("n or trials number is nonsense");

        int row;
        int col;
        this.trials = trials;
        double[] percolationThresholds
                = new double[trials];   // number of open sites divided by n * n

        /*
         * creates new percolation object, then open random sites untill the system percolates.
         * calculate the number of open sites and repeat a mount of trials.
         */
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            percolationThresholds[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }

        // store the mean and stddev in variables to prevent unecessary repetition.
        mean = StdStats.mean(percolationThresholds);
        stddev = StdStats.stddev(percolationThresholds);
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
        return (mean() - (CONFIDENCE_95 * (Math.sqrt(stddev())) / Math.sqrt(trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + (CONFIDENCE_95 * (Math.sqrt(stddev())) / Math.sqrt(trials)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());

        System.out.println("95% confidence interval = " + "[" + stats.confidenceLo() + ", " + stats
                .confidenceHi() + "]");
    }

}
