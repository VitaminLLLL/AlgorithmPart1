/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Compilation:  javac-algs4  PercolationStats.java
 * Execution:    java-algs4  PercolationStats <sizeofGrid> <numberOfTrials>
 * Dependencies: algs4.jar
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final int trial;

    /**
     * Perform independent trials on an n-by-n grid
     *
     * @throws IllegalArgumentException if n or trials <= 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid n or trials!");
        }
        trial = trials;
        Percolation percolation;
        int row;
        int col;
        int[] counts = new int[trials];
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                row = StdRandom.uniformInt(1, n + 1);
                col = StdRandom.uniformInt(1, n + 1);
                percolation.open(row, col);
            }
            counts[i] = percolation.numberOfOpenSites();
        }
        mean = StdStats.mean(counts) / (n * n);
        stddev = StdStats.stddev(counts) / (n * n);
    }

    /**
     * Sample mean of percolation threshold
     *
     * @return sample mean
     */
    public double mean() {
        return mean;
    }

    /**
     * Sample standard deviation of percolation threshold
     *
     * @return sample standard deviation
     */
    public double stddev() {
        return stddev;
    }

    /**
     * Low endpoint of 95% confidence interval
     *
     * @return Low endpoint
     */
    public double confidenceLo() {
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(trial);
    }

    /**
     * High endpoint of 95% confidence interval
     *
     * @return High endpoint
     */
    public double confidenceHi() {
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(trial);
    }

    /**
     * Test client
     * <p>
     * Print the simulation statistic in Standard output.
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trails);
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() +
                ", " + percolationStats.confidenceHi() + "]");
    }

}
