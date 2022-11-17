import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private int[][] tiles;
    private int emptyRow, emptyCol;
    private int hammingValue;
    private int mahattanValue;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        hammingValue = 0;
        mahattanValue = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] != 0) {
                    // compute mahattan
                    mahattanValue += manhattanOf(i, j);
                    // compute hamming
                    if (tiles[i][j] != (i * tiles.length + j + 1)) {
                        hammingValue++;
                    }
                }
                else {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            result.append('\n');
            for (int j = 0; j < tiles.length; j++) {
                result.append(tiles[i][j]).append(' ');
            }
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingValue;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return mahattanValue;
    }

    private int manhattanOf(int row, int col) {
        int num = tiles[row][col];
        int targetRow = (num - 1) / tiles.length;
        int targetCol = (num - 1) % tiles.length;
        return Math.abs(row - targetRow) + Math.abs(col - targetCol);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (getClass() != y.getClass()) {
            return false;
        }

        Board y0 = (Board) y;
        if (y0.dimension() != this.dimension()) {
            return false;
        }

        if (emptyRow != y0.emptyRow || emptyCol != y0.emptyCol) {
            return false;
        }

        return Arrays.deepEquals(tiles, y0.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>(4);

        if (emptyRow != 0) {
            exchange(tiles, emptyRow, emptyCol, emptyRow - 1, emptyCol);
            Board board = new Board(tiles);
            exchange(tiles, emptyRow, emptyCol, emptyRow - 1, emptyCol);
            neighbors.add(board);
        }
        if (emptyRow != tiles.length - 1) {
            exchange(tiles, emptyRow, emptyCol, emptyRow + 1, emptyCol);
            Board board = new Board(tiles);
            exchange(tiles, emptyRow, emptyCol, emptyRow + 1, emptyCol);
            neighbors.add(board);
        }
        if (emptyCol != 0) {
            exchange(tiles, emptyRow, emptyCol, emptyRow, emptyCol - 1);
            Board board = new Board(tiles);
            exchange(tiles, emptyRow, emptyCol, emptyRow, emptyCol - 1);
            neighbors.add(board);
        }
        if (emptyCol != tiles.length - 1) {
            exchange(tiles, emptyRow, emptyCol, emptyRow, emptyCol + 1);
            Board board = new Board(tiles);
            exchange(tiles, emptyRow, emptyCol, emptyRow, emptyCol + 1);
            neighbors.add(board);
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int col = 0, row = 0;
        if (tiles[row][col] == 0 || tiles[row][col + 1] == 0) {
            row++;
        }
        exchange(tiles, row, col, row, col + 1);
        Board twinBoard = new Board(this.tiles);
        exchange(tiles, row, col, row, col + 1);
        return twinBoard;
    }

    private static void exchange(int[][] tiles, int i, int j, int k, int m) {
        int temp = tiles[i][j];
        tiles[i][j] = tiles[k][m];
        tiles[k][m] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] boardArray = {
                { 0, 1, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };
        Board board = new Board(boardArray);
        System.out.println(board.toString());
        System.out.printf("Hamming:%d\n", board.hamming());
        System.out.printf("Manhattan:%d\n", board.manhattan());
        System.out.printf("IsGoal:%b\n", board.isGoal());
        System.out.println(board.twin().toString());

        Iterator<Board> iter = board.neighbors().iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().toString());
        }
    }

}