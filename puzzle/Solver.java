/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves;
    private boolean isSolved = false;
    private Stack<Board> boardList = new Stack<>();

    /**
     * Find a solution to the initial board (using the A* algorithm).
     *
     * @param initial - Board
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));
        SearchNode current = null, currentTwin = null;
        while (!pq.isEmpty() && !pqTwin.isEmpty()) {
            current = pq.delMin();
            currentTwin = pqTwin.delMin();
            if (current.board.isGoal()) {
                moves = current.moves;
                isSolved = true;
                break;
            }
            if (currentTwin.board.isGoal()) {
                moves = -1;
                isSolved = false;
                break;
            }
            for (Board b : current.board.neighbors()) {
                if (current.prev != null && b.equals(current.prev.board)) continue;
                pq.insert(new SearchNode(b, current.moves + 1, current));
            }
            for (Board b : currentTwin.board.neighbors()) {
                if (currentTwin.prev != null && b.equals(currentTwin.prev.board)) continue;
                pqTwin.insert(new SearchNode(b, currentTwin.moves + 1, currentTwin));
            }
        }
        SearchNode next = current;
        do {
            boardList.push(next.board);
            next = next.prev;
        } while (next != null);
    }

    /**
     * Define a search node of the game to be a board,
     * the number of moves made to reach the board,
     * and the previous search node.
     * <p>
     * Use two priority function as comparable.
     */
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattanPrio;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            manhattanPrio = board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            return this.manhattanPrio - that.manhattanPrio;
        }
    }

    /**
     * If the initial board is solvable
     *
     * @return True if puzzle solved.
     */
    public boolean isSolvable() {
        return isSolved;
    }

    /**
     * Min number of moves to solve initial board.
     *
     * @return moves - -1 if unsolvable
     */
    public int moves() {
        return moves;
    }

    /**
     * Sequence of boards in the shortest move solution.
     *
     * @return Iterable<Board> - null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolved) {
            return null;
        }
        return boardList;
    }

    // Test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
