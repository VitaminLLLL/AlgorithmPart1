/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Compilation:  javac-algs4  Percolation.java
 * Execution:    java-algs4  Percolation
 * Dependencies: algs4.jar
 * <p>
 * Use WeightedQuickUnion as data structure to simulate system.
 * Connect all top and all bottom sites in the initialization.
 * To solve the backwash issue, have another union which only has all top union
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF union;
    private WeightedQuickUnionUF union2;
    private boolean[] openArray;
    private final int size;
    private int count = 0;

    /**
     * Creates n-by-n grid, with all sites initially blocked
     *
     * @throws IllegalArgumentException if n <= 0
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid n!");
        }
        openArray = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            openArray[i] = false;
        }
        size = n;
        union = new WeightedQuickUnionUF(n * n);
        union2 = new WeightedQuickUnionUF(n * n);
        for (int i = 1; i < n; i++) {
            union.union(0, i);
            union2.union(0, i);
        }
        for (int i = n * n - 2; i > n * n - n - 1; i--) {
            union.union(n * n - 1, i);
        }
    }

    /**
     * Open the site (row, col) if it is not open already
     *
     * @throws IllegalArgumentException if row or col is out of index
     */
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        int index = unionIndex(row, col);
        openArray[index] = true;
        count++;
        if (row != 1 && isOpen(row - 1, col)) {
            union.union(index, index - size);
            union2.union(index, index - size);
        }
        if (row != size && isOpen(row + 1, col)) {
            union.union(index, index + size);
            union2.union(index, index + size);
        }
        if (col != 1 && isOpen(row, col - 1)) {
            union.union(index, index - 1);
            union2.union(index, index - 1);
        }
        if (col != size && isOpen(row, col + 1)) {
            union.union(index, index + 1);
            union2.union(index, index + 1);
        }
    }

    /**
     * Check if site (row, col) is open
     *
     * @return True if site is open
     * @throws IllegalArgumentException if row or col is out of index
     */
    public boolean isOpen(int row, int col) {
        if (!isValidIndex(row) || !isValidIndex(col)) {
            throw new IllegalArgumentException("Invalid row or col!");
        }
        return openArray[unionIndex(row, col)];
    }

    /**
     * Check if site (row, col) is full (connected to Top)
     *
     * @return True if site is full
     * @throws IllegalArgumentException if row or col is out of index
     */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && (union2.find(unionIndex(row, col)) == union2.find(0));
    }

    /**
     * Number of the open sites
     *
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return count;
    }

    /**
     * Check if site (row, col) percolates (Bottom connect to Top)
     *
     * @return True if system percolates
     */
    public boolean percolates() {
        if (size == 1) {
            return isOpen(1, 1);
        }
        return union.find(0) == union.find(size * size - 1);
    }

    /**
     * Check if a given index is out of range
     *
     * @return True if index is in (0, n]
     */
    private boolean isValidIndex(int index) {
        return index > 0 && index <= size;
    }

    /**
     * Convert the 2D index to 1D array index
     *
     * @return 1D array index
     */
    private int unionIndex(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    /**
     * Simple test client <p>
     * Verify percolation API correctness.
     */
    public static void main(String[] args) {
        Percolation percolation = new Percolation(2);
        while (!percolation.percolates()) {
            int row = StdRandom.uniformInt(1, 3);
            int col = StdRandom.uniformInt(1, 3);
            StdOut.println("row: " + row + ", col: " + col);
            percolation.open(row, col);
        }
        StdOut.println("The number of the open sites: " + percolation.numberOfOpenSites());
        percolation = new Percolation(1);
        assert !percolation.percolates() : "System should not percolate.";

        percolation = new Percolation(3);
        percolation.open(2, 1);
        assert percolation.isOpen(2, 1) : "site (2, 1) should open.";
        percolation.open(3, 1);
        percolation.open(3, 3);
        assert percolation.isOpen(3, 3) : "site (3, 3) should open.";
        percolation.open(2, 3);
        percolation.open(1, 3);
        assert percolation.isOpen(1, 3) : "site (1, 3) should open.";

        assert !percolation.isOpen(2, 2) : "site (2, 2) should not open.";
        assert !percolation.isOpen(3, 2) : "site (3, 2) should not open.";

        assert !percolation.isFull(2, 1) : "site (2, 1) should not full.";
        assert percolation.numberOfOpenSites() == 5 : "number of open sites = 5";
        StdOut.println("Percolation : " + percolation.percolates());
    }
}
