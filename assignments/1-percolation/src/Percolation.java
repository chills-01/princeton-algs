/*
 *****************************************************************************
 *  Name:              Carter Hills
 *  Coursera User ID:  123456
 *  Last modified:     7/10/2024
 ****************************************************************************
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int gridSize;
    private int countOpen;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufTop;
    private int virtualTopSite;
    private int virtualBottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be at least 1");
        grid = new boolean[n][n];
        gridSize = n;
        virtualTopSite = 0;
        virtualBottomSite = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufTop = new WeightedQuickUnionUF(n * n + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return grid[row - 1][col - 1];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);
        if (!isOpen(row, col)) {
            int index = getIndexFromRowAndColumn(row, col);
            // if in top row, connect to virtual top
            if (row == 1) {
                uf.union(virtualTopSite, index);
                ufTop.union(virtualTopSite, index);
            }
            // if in the bottom row, connect to virtual bottom
            if (row == gridSize) {
                uf.union(virtualBottomSite, index);
            }
            // connect to open cells around it
            connectIfOpen(index, row + 1, col);
            connectIfOpen(index, row, col + 1);
            connectIfOpen(index, row - 1, col);
            connectIfOpen(index, row, col - 1);
            grid[row - 1][col - 1] = true;

            countOpen++;

        }


    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return countOpen;
    }

    private void checkBounds(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IllegalArgumentException("row or col is out of bounds");
        }
    }

    private int getIndexFromRowAndColumn(int row, int col) {
        return (row - 1) * gridSize + col;
    }

    private void connectIfOpen(int index, int row, int col) {
        // if not in bounds, return
        try {
            checkBounds(row, col);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        // union
        if (isOpen(row, col)) {
            int newIndex = getIndexFromRowAndColumn(row, col);
            uf.union(index, newIndex);
            ufTop.union(index, newIndex);
        }

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        if (isOpen(row, col)) {
            int index = getIndexFromRowAndColumn(row, col);
            return ufTop.find(virtualTopSite) == ufTop.find(index);
        }
        return false;
    }

    //
    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTopSite) == uf.find(virtualBottomSite);
    }
}
