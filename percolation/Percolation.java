/* *****************************************************************************
 *  Name:    Ahmed Gamaleldin
 *  Description:  Generate n * n grid and open sites different sites sequentially
 *                  depending on the input. When there is a flow from top to bottom,
 *                  the system percolates. If not, the system does not percolates.
 *                  Open sites are connected using WeightedQuickUnion data structure.
 *
 *  Written:       10/12/2019
 *  Last updated:  15/12/2019
 *
 *  % javac-algs4 Percolation.java
 *  % java-algs4 Percolation
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSitesCounter;    // count number of open sites
    private boolean[][] gridOpen;    // open sites in the grid
    private final int gridLength;    // the length of a grid (length of a row or col)
    private final int top;           // virtual top
    private final int bottom;        // virtual bottom

    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n is not legal number");

        openSitesCounter = 0;
        gridOpen = new boolean[n][n];
        gridLength = n;
        bottom = n * n;
        top = (n * n) + 1;

        uf = new WeightedQuickUnionUF((n * n) + 2);
    }

    /*
     * opens the site (row, col) if it is not open already and connect it to the neighbor
     * open sites. Increment the number of open sites.
     */
    public void open(int row, int col) {
        // validate that the row and col are in the right range
        validate(row - 1, col - 1);
        if (!isOpen(row, col)) {
            gridOpen[row - 1][col - 1] = true;
            openSitesCounter++;

            // caolculate the corresponding index as if it is 1D array instead of 2D
            int p = (((row - 1) * gridLength) + col) - 1;

            // if it is in the top row, connect to the virtual top
            if (row - 1 == 0)
                uf.union(p, top);

            // if in the bottom row, connect to the virtual bottom
            if (row == gridLength)
                uf.union(p, bottom);

            // if between, connect to the right, left, up, and down if they are open
            if (col < gridLength && isOpen(row, col + 1)) {
                int qRight = p + 1;
                uf.union(p, qRight);
            }

            if (col - 1 > 0 && isOpen(row, col - 1)) {
                int qLeft = p - 1;
                uf.union(p, qLeft);
            }

            if (row - 1 > 0 && isOpen(row - 1, col)) {
                int qUp = p - gridLength;
                uf.union(p, qUp);
            }

            if (row < gridLength && isOpen(row + 1, col)) {
                int qDown = p + gridLength;
                uf.union(p, qDown);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row - 1, col - 1);
        return gridOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row - 1, col - 1);
        int p = (((row - 1) * gridLength) + col) - 1;
        // if connected to the top, then it is already full
        return uf.connected(p, top);
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCounter;
    }

    // does the system percolate?
    public boolean percolates() {
        // if the virtual top and bottom are connected, the system percolates
        return uf.connected(top, bottom);
    }

    /*
     * validate if the row and col is the specified range.
     * throws an IllegalArgumentException if not.
     */
    private void validate(int row, int col) {
        if (row < 0 || row > gridLength - 1 || col < 0 || col > gridLength - 1)
            throw new IllegalArgumentException(
                    "row or column is beyond the limits of the grid");
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 2);
        percolation.open(2, 2);
        if (percolation.percolates())
            System.out.print("Success");
        else
            System.out.print("Failed");
    }
}
