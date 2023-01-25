public class Trie {


    public TrieNode root;

    public Trie() {
        root = new TrieNode(false);
    }

    public void insert(String value) {
        if (value.contains("Q") && !value.contains("QU"))
            return;
        TrieNode current = root;
        for (int i = 0; i < value.length(); i++) {
            // skip U after Q
            if (i > 0 && value.charAt(i) == 'U' && value.charAt(i - 1) == 'Q')
                continue;
            int index = value.charAt(i) - 'A';

            if (current.children[index] == null) {
                current.children[index] = new TrieNode(false);
            }
            current = current.children[index];
        }
        current.isValue = true;
        current.value = value;
    }

    public boolean contains(String value) {
        TrieNode current = root;
        for (int i = 0; i < value.length(); i++) {
            // skip U after Q
            if (i > 0 && value.charAt(i) == 'U' && value.charAt(i - 1) == 'Q')
                continue;
            int index = value.charAt(i) - 'A';

            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return current.isValue;
    }
}

