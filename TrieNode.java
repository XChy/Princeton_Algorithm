public class TrieNode {
    public TrieNode(boolean isValue) {
        this.isValue = isValue;
        children = new TrieNode[26];
    }

    public boolean isValue;
    public String value;
    public TrieNode[] children;
}
