import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private ArrayList<Board> solutions;

    private class Node implements Comparable<Node> {
        public Node(Board board, int moves, boolean isTwin) {
            this.board = board;
            this.moves = moves;
            this.isTwin = isTwin;
            this.parent = null;
            priority = moves + board.manhattan();
        }

        public Node(Node parent, Board board) {
            this.board = board;
            this.moves = parent.moves + 1;
            this.distance = board.manhattan();
            this.parent = parent;
            this.isTwin = parent.isTwin;
            priority = moves + distance;
        }

        public Node parent;
        public Board board;
        public int moves;
        public int distance;

        public int priority;
        public boolean isTwin;

        public int compareTo(Node node) {
            if (priority == node.priority)
                return distance - node.distance;
            return priority - node.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }


        MinPQ<Node> queue = new MinPQ<>();

        queue.insert(new Node(initial.twin(), 0, true));
        queue.insert(new Node(initial, 0, false));

        Node currentNode = queue.delMin();

        while (!currentNode.board.isGoal()) {
            Iterable<Board> neighbors = currentNode.board.neighbors();
            for (Board neighbor : neighbors) {
                if (currentNode.parent == null || !neighbor.equals(currentNode.parent.board)) {
                    queue.insert(new Node(currentNode, neighbor));
                }
            }

            currentNode = queue.delMin();
        }

        solutions = new ArrayList<>();
        if (!currentNode.isTwin) {
            Node insertedNode = currentNode;

            while (insertedNode != null) {
                solutions.add(insertedNode.board);
                insertedNode = insertedNode.parent;
            }
            Collections.reverse(solutions);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutions.size() != 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return solutions.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solutions;
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


}