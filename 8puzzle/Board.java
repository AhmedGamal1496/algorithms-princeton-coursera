/******************************************************************************
 *  Compilation:  javac-algs4 Board.java
 *  Execution:    java-algs4 Board < input.txt
 *  Dependencies: In.algs4 StdOut.algs4 util.Arrays.java util.LinkedList.java
 *
 * Board class to represent each move in 8puzzle problem (3x3 grid)
 * including computing hamming and manhattan distances of each board.
 * We override equals and toString functions.
 *
 * The goal of the program to reach a board with tiles arranged in row-major order,
 * using as few moves as possible.
 * We are permitted to slide tiles either vertically or horizontally into the blank sqaure.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private int[][] tiles;          // for a defensive copy (make it immutable)
    private final int dimesion;     // dimension of the 2d array

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimesion = tiles.length;

        // defensive copy for immutability
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimesion + "\n");
        for (int i = 0; i < dimesion; i++) {
            for (int j = 0; j < dimesion; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimesion; i++) {
            for (int j = 0; j < dimesion; j++) {
                if (tiles[i][j] == 0) continue;               // do not compute blank square
                if (tiles[i][j] != i * dimesion + j + 1) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    // sum of the vertical and horizontal distance from the tiles to their goal position
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimesion; i++) {
            for (int j = 0; j < dimesion; j++) {
                int goalCell = tiles[i][j];
                if (goalCell == 0) continue;      // do not compute blank square

                int goalCellRow = (goalCell - 1) / dimesion;
                int goalCellCol = (goalCell + (dimesion - 1)) % dimesion;
                manhattan += Math.abs(i - goalCellRow);
                manhattan += Math.abs(j - goalCellCol);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // self check
        if (this == y) return true;
        // null and type check
        if (y == null || y.getClass() != this.getClass()) return false;
        // cast check
        Board that = (Board) y;
        // field comparison using deepEquals for 2d array
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards by exchanging the blank square with
    // each of its neighbors square
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = swap(getBlankIndex(), "Neighbor");
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        LinkedList<Board> twin = swap(getBlankIndex(), "twin");
        return twin.get(0);
    }

    // swap the indices and return a new board for neighbor and twin
    private LinkedList<Board> swap(int[] index, String type) {
        // blank square index
        int row = index[0];
        int col = index[1];
        LinkedList<Board> swappedBoards = new LinkedList<>();

        if (type.equals("twin")) {
            Board b = null;
            if (col != 0 && row != 0)
                b = this.swap(row, col - 1, row - 1, col);

            else if (col != 0 && row != dimesion)
                b = this.swap(row, col - 1, row + 1, col);

            else if (col != dimesion && row != 0)
                b = this.swap(row, col + 1, row - 1, col);

            else if (col != dimesion && row != dimesion)
                b = this.swap(row, col + 1, row + 1, col);

            swappedBoards.add(b);
            return swappedBoards;
        }

        else if (type.equals("Neighbor")) {
            if (col != 0) {
                Board b = this.swap(row, col, row, col - 1);
                swappedBoards.add(b);
            }

            if (col != dimesion - 1) {
                Board b = this.swap(row, col, row, col + 1);
                swappedBoards.add(b);
            }

            if (row != dimesion - 1) {
                Board b = this.swap(row, col, row + 1, col);
                swappedBoards.add(b);
            }

            if (row != 0) {
                Board b = this.swap(row, col, row - 1, col);
                swappedBoards.add(b);
            }
        }

        return swappedBoards;
    }

    // swap the given indices and return a new Board
    private Board swap(int row1, int col1, int row2, int col2) {
        Board newBoard = new Board(this.tiles);
        int temp = newBoard.tiles[row1][col1];
        newBoard.tiles[row1][col1] = newBoard.tiles[row2][col2];
        newBoard.tiles[row2][col2] = temp;
        return newBoard;
    }

    private int[] getBlankIndex() {
        int[] index = new int[2];
        outer:
        for (int i = 0; i < dimesion; i++) {
            for (int j = 0; j < dimesion; j++) {
                if (tiles[i][j] == 0) {
                    index[0] = i;
                    index[1] = j;
                    break outer;
                }
            }
        }
        return index;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);

        StdOut.println("The Board is: ");
        StdOut.println(board);
        StdOut.println("Dimension: " + board.dimension() + " x " + board.dimension());
        StdOut.println("The blank square index: " + Arrays.toString(board.getBlankIndex()));
        StdOut.println("Hamming= " + board.hamming() +
                               " and Manhattan= " + board.manhattan());
        StdOut.println("Is this the goal board? " + board.isGoal());
        Iterable<Board> neighbors = board.neighbors();
        StdOut.println("Board Neighbors are: ");
        for (Board nei : neighbors)
            StdOut.println(nei);
        StdOut.println("Twin board is: ");
        StdOut.println(board.twin());
    }

}
