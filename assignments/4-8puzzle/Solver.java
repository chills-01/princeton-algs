/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node solution;
    private boolean isSolvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board board) {
        if (board == null) throw new IllegalArgumentException();
        MinPQ<Node> originalPQ = new MinPQ<>();
        MinPQ<Node> parallelPQ = new MinPQ<>();
        originalPQ.insert(new Node(board));
        parallelPQ.insert(new Node(board.twin())); // track unsolvable

        while (!originalPQ.isEmpty() && !parallelPQ.isEmpty()) {
            Node originalResult = addNeighbors(originalPQ);
            if (originalResult != null) {
                solution = originalResult;
                isSolvable = true;
                return;
            }

            Node parallelResult = addNeighbors(parallelPQ);
            if (parallelResult != null) {
                // since a board and its twin cannot simultaneously be a solution
                isSolvable = false;
                return;
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable) return solution.movesSoFar;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        // follow the chain up
        Stack<Board> stack = new Stack<>();
        Node cur = solution;

        while (cur != null) {
            stack.push(cur.board);
            cur = cur.previous;
        }
        return stack;

    }

    // test client (see below)
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

    private Node addNeighbors(MinPQ<Node> pq) {
        Node cur = pq.delMin();
        if (cur.board.isGoal()) return cur;
        for (Board n : cur.board.neighbors()) {
            // don’t enqueue a neighbor if its board is the same as the
            // board of the previous search node in the game tree
            if (cur.previous == null || !n.equals(cur.previous.board)) {
                pq.insert(new Node(n, cur.movesSoFar + 1, cur));
            }
        }
        return null;
    }


    private class Node implements Comparable<Node> {
        private Node previous;
        private int movesSoFar;
        private int priority = -1;
        private Board board;

        public Node(Board board) {
            this(board, 0, null); // call the other constructor
        }

        public Node(Board initial, int movesSoFar, Node prev) {
            this.board = initial;
            this.movesSoFar = movesSoFar;
            this.previous = prev;
        }

        public int priority() {
            if (priority == -1) {
                priority = board.manhattan() + movesSoFar;
            }
            return priority;
        }

        public int compareTo(Node node) {
            return priority() - node.priority();
        }
    }

}
