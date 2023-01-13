import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private Digraph graph;
    private HashMap<String, ArrayList<Integer>> ids;
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
            int id = Integer.parseInt(args[0]);
            id_synsets.put(id, args[1]);
            String[] nouns = args[1].split(" ");
            for (String noun : nouns) {
                if (!ids.containsKey(noun))
                    ids.put(noun, new ArrayList<>());
                ids.get(noun).add(id);
            }
            V++;
        }

        graph = new Digraph(V);
        In in1 = new In((hypernyms));
        while (!in1.isEmpty()) {
            String[] hyper_ids = in1.readLine().split(",");
            int base = Integer.parseInt(hyper_ids[0]);
            for (int i = 1; i < hyper_ids.length; ++i) {
                graph.addEdge(base, Integer.parseInt(hyper_ids[i]));
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

        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle()) {
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
        if (word == null)
            throw new IllegalArgumentException();
        return ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap_helper.length(ids.get(nounA), ids.get(nounB));
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
