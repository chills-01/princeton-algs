/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private final int n;
    private int hamming = -1;
    private int manhattan = -1;

    public Board(int[][] board) {
        if (board == null) throw new IllegalArgumentException();
        n = board.length;
        this.blocks = copyBlocks(board);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        if (hamming != -1) return hamming; // only compute it once

        // indicate start with 0 blockk
        hamming++;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = rowColToIndex(i, j);
                if (blocks[i][j] != 0 && index != blocks[i][j]) hamming++;
            }
        }

        return hamming;
    }

    public int manhattan() {
        if (manhattan != -1) return manhattan; // only compute it once
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int targetPosition = blocks[i][j];
                if (targetPosition == 0) continue;
                int[] targetCoord = indextoRowCol(targetPosition);
                int targetRow = targetCoord[0];
                int targetCol = targetCoord[1];
                sum = sum + Math.abs(targetRow - i) + Math.abs(targetCol - j);
            }
        }
        manhattan = sum;
        return manhattan;

    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() == this.getClass()) {
            Board target = (Board) y;
            if (this.n != target.dimension()) return false;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (target.blocks[i][j] != blocks[i][j]) return false;
                }
            }
            return true;
        }
        return false;
    }

    public Iterable<Board> neighbors() {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        List<Board> neighbors = new ArrayList<Board>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // find the empty block
                if (this.blocks[i][j] == 0) {
                    for (int[] dir : directions) {
                        // todo duplicated code
                        int newRow = i + dir[0];
                        int newCol = j + dir[1];

                        if (newRow >= 0 && newCol >= 0 && newRow < n && newCol < n) {
                            int[][] neighbourBlocks = exchange(i, j, newRow, newCol, this.blocks);
                            Board neighbor = new Board(neighbourBlocks);
                            neighbors.add(neighbor);
                        }
                    }
                }
            }
        }

        return neighbors;
    }

    public Board twin() {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // find a non-empty block
                if (this.blocks[i][j] != 0) {
                    for (int[] dir : directions) {
                        int newRow = i + dir[0];
                        int newCol = j + dir[1];

                        if (newRow >= 0 && newCol >= 0 && newRow < n && newCol < n
                                && blocks[newRow][newCol] != 0) {
                            int[][] neighbourBlocks = exchange(i, j, newRow, newCol, this.blocks);
                            return new Board(neighbourBlocks);
                        }
                    }
                }
            }
        }
        return null;
    }

    private int[][] exchange(int oldRow, int oldCol, int newRow, int newCol, int[][] oldBlocks) {
        int[][] newBlocks = copyBlocks(oldBlocks);
        int tmp = newBlocks[oldRow][oldCol];
        newBlocks[oldRow][oldCol] = oldBlocks[newRow][newCol];
        newBlocks[newRow][newCol] = tmp;
        return newBlocks;
    }


    private int[][] copyBlocks(int[][] board) {
        int[][] copy = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    private int rowColToIndex(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) throw new IllegalArgumentException();
        return row * n + col + 1; // +1 as 1 should be in pos 0, etc
    }

    // transform 1D position to 2D position row and col
    private int[] indextoRowCol(int index) {
        int row, col;
        row = (index - 1) / dimension();
        col = (index - 1) % dimension();
        return new int[] { row, col };
    }

    public static void main(String[] args) {
        int[][] testBlocks = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board1 = new Board(testBlocks);
        Board board2 = new Board(testBlocks);
        StdOut.println("Board 1 complete:");
        StdOut.println(board1.toString());
        StdOut.println("Board 2 complete:");
        StdOut.println(board2.toString());

        StdOut.println("Hamming: " + board1.hamming()); // 8
        StdOut.println("Manhattan: " + board1.manhattan()); // 10
        StdOut.println("Duplicate boards are equal? " + board1.equals(board2));
        StdOut.println("Board is solved? " + board1.isGoal());

        // test equality
        Board solvedBoard = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } });
        StdOut.println("\nSolved Board complete:");
        StdOut.println("Hamming: " + solvedBoard.hamming()); // 8
        StdOut.println("Manhattan: " + solvedBoard.manhattan()); // 10
        StdOut.println(solvedBoard.toString());
        StdOut.println("Board is solved? " + solvedBoard.isGoal());

        // test neighbors
        StdOut.println("\nTesting neighbors:");
        Board neighborsBoard = new Board(new int[][] { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } });
        for (Board n : neighborsBoard.neighbors()) {
            StdOut.println(n.toString());
            StdOut.println();
        }

        // test twin
        StdOut.println("\nTesting twin:");
        Board twinBoard = new Board(new int[][] { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } });
        StdOut.println(twinBoard.twin().toString());

    }
}
