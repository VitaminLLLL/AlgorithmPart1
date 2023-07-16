/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Board data type
 ******************************************************************************/

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private int[][] tiles;
    private final int n;
    private int zero;
    private int twinTile1 = -1, twinTile2 = -1;

    /**
     * Create a board from an n-by-n array of tiles,
     * where tiles[row][col] = tile at (row, col)
     *
     * @param tiles - n by n int array,
     *              Assume tiles containing the n^2 integers between 0 and n^2 − 1,
     *              where 0 represents the blank square. 2 ≤ n < 128.
     */
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    zero = i * n + j;
                }
            }
        }
    }

    /**
     * String representation of this board
     *
     * @return String
     */
    public String toString() {
        StringBuilder tmp = new StringBuilder(String.valueOf(n));
        tmp.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmp.append("  ");
                tmp.append(tiles[i][j]);
            }
            tmp.append("\n");
        }
        return tmp.toString();
    }

    /**
     * Board dimension n
     *
     * @return n - int
     */
    public int dimension() {
        return n;
    }

    /**
     * Number of tiles out of place
     *
     * @return int
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != i * n + j + 1) {
                    count++;
                }
            }
        }
        // Remove the empty tile count.
        return --count;
    }

    /**
     * Sum of Manhattan distances between tiles and goal
     *
     * @return distance - int
     */
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != i * n + j + 1 && tile != 0) {
                    int expectI = (tile - 1) / n;
                    int expectJ = (tile - 1) % n;
                    distance += Math.abs(i - expectI);
                    distance += Math.abs(j - expectJ);
                }
            }
        }
        return distance;
    }

    /**
     * if board is goal board
     *
     * @return True if the board is the target.
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * if two boards have the same tiles
     *
     * @param y - Board objects
     * @return True if tiles are same.
     */
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Swap the tile at two positions in the Board.
     */
    private void swap(int current, int next) {
        int i = current / n;
        int j = current % n;
        int nextI = next / n;
        int nextJ = next % n;
        int temp = tiles[i][j];
        tiles[i][j] = tiles[nextI][nextJ];
        tiles[nextI][nextJ] = temp;
    }


    /**
     * Count number of neighbour's board and add tile index in the List
     */
    private Iterable<Board> addNeighbours() {
        Stack<Board> neighbours = new Stack<>();
        int zeroI = zero / n;
        int zeroJ = zero % n;
        int adjacent;
        if (zeroI != 0) {
            adjacent = zero - n;
            swap(zero, adjacent);
            neighbours.push(new Board(tiles));
            swap(adjacent, zero);
        }
        if (zeroI != n - 1) {
            adjacent = zero + n;
            swap(zero, adjacent);
            neighbours.push(new Board(tiles));
            swap(adjacent, zero);
        }
        if (zeroJ != 0) {
            adjacent = zero - 1;
            swap(zero, adjacent);
            neighbours.push(new Board(tiles));
            swap(adjacent, zero);
        }
        if (zeroJ != n - 1) {
            adjacent = zero + 1;
            swap(zero, adjacent);
            neighbours.push(new Board(tiles));
            swap(adjacent, zero);
        }
        return neighbours;
    }

    /**
     * Current board's neighbour boards.
     *
     * @return Stack<Board>
     */
    public Iterable<Board> neighbors() {
        return addNeighbours();
    }

    /**
     * A board that is obtained by exchanging any pair of tiles
     *
     * @return Random twin board.
     */
    public Board twin() {
        if (twinTile1 == -1) {
            setTile();
        }
        swap(twinTile1, twinTile2);
        Board result = new Board(tiles);
        swap(twinTile2, twinTile1);
        return result;
    }

    /**
     * Set tile for twin board.
     */
    private void setTile() {
        twinTile1 = randomTile();
        do {
            twinTile2 = randomTile();
        }
        while (twinTile1 == twinTile2);
    }

    /**
     * Random non-empty tile position
     *
     * @return tile index - int[]
     */
    private int randomTile() {
        int i;
        int j;
        do {
            i = StdRandom.uniformInt(n);
            j = StdRandom.uniformInt(n);
        } while (zero == i * n + j);
        return i * n + j;
    }

    // Unit testing.
    public static void main(String[] args) {
        int[][] exampleTiles = new int[][]{{2, 5, 13, 11}, {1, 3, 4, 15}, {8, 6, 0, 7}, {12, 14, 9, 10}};
        Board board = new Board(exampleTiles);
        StdOut.println(board);
        StdOut.println("Equals:" + board.equals(new Board(exampleTiles)));
        StdOut.println("Equals:" + board.equals(board.twin()));
        StdOut.println("manhattan: " + board.manhattan());
        StdOut.println("hamming: " + board.hamming());
        StdOut.println("is Goal: " + board.isGoal());
        StdOut.println("Neighbours:");
        for (Board b : board.neighbors()) {
            StdOut.println(b);
        }
        StdOut.println("Twins:");
        int i = 2;
        while (i > 0) {
            StdOut.println(board.twin());
            i--;
        }
    }
}
