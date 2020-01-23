/******************************************************************************
 *  Compilation:  javac-algs4 Solver.java
 *  Execution:    java-algs4 Solver < input.txt
 *  Dependencies: In.algs4 StdOut.algs4 Stack.algs4 MinPQ.algs4 Board.java
 *
 * This program solves 8puzzle program where you are given 3-by-3 random grid
 * with 8 square tiles labeled 1 through 8, plus a blank square. It solves the problem
 * using A* search algorithm.
 *
 * We define a search node of the game to be a board, the number of moves to reach
 * the goal board, and the previous search node.
 * We delete from the priority queue the search node with minimum priority, and
 * insert all neighboring search nodes.
 * We repeat until the goal board is popped from the queue.
 *
 * Consider the problem like a game tree where each search node is a node in
 * game tree and the children corresponds to its neighboring search nodes.
 * The root is the initial search node.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // inner class to represent the search node
    private static class Node implements Comparable<Node> {
        private final Node prev;      // previous node
        private final Board board;    // current Board
        private final int numMoves;   // number of moves made

        private final int manhattan;  // cache manhattan method to prevent overhead calculations
        private final int priority;
                // cache priority of each node (sum of manhattan and number of moves)

        public Node(Board board, Node previous, int numMoves) {
            this.board = board;
            prev = previous;
            this.numMoves = numMoves;

            manhattan = board.manhattan();
            priority = manhattan + numMoves;
        }

        // lower priority to be deqeued from the PQ first
        public int compareTo(Node that) {
            int result = Integer.compare(this.priority, that.priority);
            // if priorities are equal, compare manhattan values only
            if (result != 0)
                return result;
            else {
                return Integer.compare(this.manhattan, that.manhattan);
            }
        }
    }

    // the shortest path to find the goal Board.
    private final Stack<Board> solutionBoards = new Stack<>();
    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument is null!");

        // create two PQs. One for the main Board and the other for the twin Board
        // if the twin Board reached the goal Board, then the main board is unsolvable
        MinPQ<Node> pqMain = new MinPQ<>();
        MinPQ<Node> pqTwin = new MinPQ<>();
        pqMain.insert(new Node(initial, null, 0));
        pqTwin.insert(new Node(initial.twin(), null, 0));
        Board prevBoard = null;
        Board prevTwinBoard = null;

        while (true) {
            // delete the minimum priority Node
            Node dequeuedNode = pqMain.delMin();
            Node dequeuedTwinNode = pqTwin.delMin();

            Board dequeuedBoard = dequeuedNode.board;
            Board dequeuedTwinBoard = dequeuedTwinNode.board;

            // check if it is the initial Node
            if (dequeuedNode.prev != null)
                prevBoard = dequeuedNode.prev.board;
            if (dequeuedTwinNode.prev != null)
                prevTwinBoard = dequeuedTwinNode.prev.board;

            // build the solutionBoards stack if we reached the goal Board
            // the stack must have the initial board popped up first
            if (dequeuedBoard.isGoal()) {
                moves = dequeuedNode.numMoves;
                Node current = dequeuedNode;
                while (current.prev != null) {
                    solutionBoards.push(current.board);
                    current = current.prev;
                }
                solutionBoards.push(current.board);
                break;
            }
            // if the twin reached the goal Board, the main Board is impossible to solve
            else if (dequeuedTwinBoard.isGoal()) {
                moves = -1;
                break;
            }

            // insert the dequeued Board neighbors except the one that matches
            // the previous board to improve optimization and avoid redundant calculations
            for (Board nextBoard : dequeuedBoard.neighbors()) {
                if (dequeuedNode.prev == null || !nextBoard.equals(prevBoard))
                    pqMain.insert(new Node(nextBoard, dequeuedNode, dequeuedNode.numMoves + 1));
            }
            for (Board nextBoard : dequeuedTwinBoard.neighbors()) {
                if (dequeuedTwinNode.prev == null || !nextBoard.equals(prevTwinBoard))
                    pqTwin.insert(
                            new Node(nextBoard, dequeuedTwinNode, dequeuedTwinNode.numMoves + 1));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return isSolvable() ? solutionBoards : null;
    }

    // test client
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
