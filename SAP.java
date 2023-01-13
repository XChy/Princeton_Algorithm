import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> vs = new ArrayList<Integer>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<Integer>();
        ws.add(w);
        return length(vs, ws);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> vs = new ArrayList<Integer>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<Integer>();
        ws.add(w);
        return ancestor(vs, ws);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return find(v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return find(v, w)[0];
    }

    // find(w,x)[0] is ancestor ,[1] is length
    private int[] find(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        int[] result = new int[2];
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return new int[] { -1, -1 };
        }

        for (Integer node_v : v) {
            for (Integer node_w : w) {
                if (node_v == null || node_w == null)
                    throw new IllegalArgumentException();
                if (node_v < 0 || node_v >= graph.V() || node_w < 0 || node_w >= graph.V())
                    throw new IllegalArgumentException();
            }
        }

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(graph, w);
        int ans = -1;
        int len = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (bfs_v.hasPathTo(i) && bfs_w.hasPathTo(i)) {
                if (len > bfs_v.distTo(i) + bfs_w.distTo(i)) {
                    ans = i;
                    len = bfs_v.distTo(i) + bfs_w.distTo(i);
                }
            }
        }

        result[0] = ans;
        if (len == Integer.MAX_VALUE)
            result[1] = -1;
        else
            result[1] = len;
        return result;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
