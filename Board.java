import java.util.ArrayList;
import java.util.Iterator;

public class Board {
    int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
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
        int num = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != (j * tiles.length + i + 1) && tiles[i][j] != 0) {
                    num++;
                }
            }
        }
        return num;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != 0) {
                    distance += manhattanOf(i, j);
                }
            }
        }
        return distance;
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
        if (!Board.class.isInstance(y)) {
            return false;
        }

        Board y0 = (Board) y;
        if (y0.dimension() != this.dimension()) {
            return false;
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (this.tiles[i][j] != y0.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardNeighbors();
    }

    private class BoardNeighbors implements Iterable<Board> {

        public Iterator<Board> iterator() {
            return neighbors.iterator();
        }

        int row, col;// the pos of 0
        ArrayList<Board> neighbors;

        public BoardNeighbors() {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    if (tiles[i][j] == 0) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            neighbors = new ArrayList<>();

            if (row != 0) {
                Board board = new Board(tiles);
                board.exchange(row, col, row - 1, col);
                neighbors.add(board);
            }
            if (row != tiles.length - 1) {
                Board board = new Board(tiles);
                board.exchange(row, col, row + 1, col);
                neighbors.add(board);
            }
            if (col != 0) {
                Board board = new Board(tiles);
                board.exchange(row, col, row, col - 1);
                neighbors.add(board);
            }
            if (col != tiles.length - 1) {
                Board board = new Board(tiles);
                board.exchange(row, col, row, col + 1);
                neighbors.add(board);
            }
        }


    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int col = 0, row = 0;
        if (tiles[row][col] == 0 || tiles[row][col + 1] == 0) {
            row++;
        }
        exchange(row, col, row, col + 1);
        Board twinBoard = new Board(this.tiles);
        exchange(row, col, row, col + 1);
        return twinBoard;
    }

    private void exchange(int i, int j, int k, int m) {
        int temp = tiles[i][j];
        tiles[i][j] = tiles[k][m];
        tiles[k][m] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] boardArray = {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
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