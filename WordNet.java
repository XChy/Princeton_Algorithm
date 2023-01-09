import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {
    private Digraph graph;
    private HashMap<String, Integer> ids;
    private HashMap<Integer, String> id_synsets;

    private SAP sap_helper;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        ids = new HashMap<>();
        id_synsets = new HashMap<>();
        In in = new In(synsets);
        int V = 0;
        while (!in.isEmpty()) {
            String[] args = in.readLine().split(",");
            int id = Integer.valueOf(args[0]);
            id_synsets.put(id, args[1]);
            String[] nouns = args[1].split(" ");
            for (String noun : nouns) {
                ids.put(noun, id);
            }
            V++;
        }

        graph = new Digraph(V);
        In in1 = new In((hypernyms));
        while (!in.isEmpty()) {
            String[] hyper_ids = in.readLine().split(",");
            int base = Integer.valueOf(hyper_ids[0]);
            for (int i = 1; i < hyper_ids.length; ++i) {
                graph.addEdge(base, Integer.valueOf(hyper_ids[i]));
            }
        }

        int root_num = 0;
        for (int i = 0; i < graph.V(); ++i) {
            if (graph.outdegree(i) == 0) {
                root_num++;
            }
        }
        if (root_num != 1) {
            throw new IllegalArgumentException();
        }

        sap_helper = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return id_synsets.get(sap_helper.ancestor(ids.get(nounA), ids.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
