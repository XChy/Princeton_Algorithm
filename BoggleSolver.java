import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.Arrays;

public class BoggleSolver {
    private Trie dictionaryTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException();
        dictionaryTrie = new Trie();
        for (String word : dictionary)
            dictionaryTrie.insert(word);
        StdOut.println(dictionaryTrie.contains("QUIS"));
    }

    private boolean isValid(int row, int col, BoggleBoard board) {
        return row >= 0 && row < board.rows() && col >= 0 && col < board.cols();
    }

    private void searchWords(int row, int col, BoggleBoard board, TrieSET output,
                             TrieNode previousNode, boolean[][] visited) {
        if (!isValid(row, col, board) || visited[row][col])
            return;

        TrieNode currentNode = previousNode.children[board.getLetter(row, col) - 'A'];
        if (currentNode == null)
            return;

        visited[row][col] = true;
        if (currentNode.isValue && currentNode.value.length() >= 3) {
            output.add(currentNode.value);
        }

        searchWords(row - 1, col - 1, board, output, currentNode, visited);
        searchWords(row, col - 1, board, output, currentNode, visited);
        searchWords(row + 1, col - 1, board, output, currentNode, visited);
        searchWords(row - 1, col, board, output, currentNode, visited);
        searchWords(row + 1, col, board, output, currentNode, visited);
        searchWords(row - 1, col + 1, board, output, currentNode, visited);
        searchWords(row, col + 1, board, output, currentNode, visited);
        searchWords(row + 1, col + 1, board, output, currentNode, visited);

        visited[row][col] = false;
    }

    private void clearVisited(boolean[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            Arrays.fill(arr[i], false);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSET validWords = new TrieSET();

        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                clearVisited(visited);
                searchWords(i, j, board, validWords, dictionaryTrie.root, visited);
            }
        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionaryTrie.contains(word)) {
            if (word.length() >= 3 && word.length() <= 4)
                return 1;
            if (word.length() == 5)
                return 2;
            if (word.length() == 6)
                return 3;
            if (word.length() == 7)
                return 5;
            if (word.length() >= 8)
                return 11;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
