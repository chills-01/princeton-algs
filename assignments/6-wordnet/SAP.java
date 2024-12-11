/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }

        // defensive copy
        digraph = new Digraph(G);

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        int[] res = sap(v, w);
        return res[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        int[] res = sap(v, w);
        return res[1];
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= digraph.V()) throw new IllegalArgumentException();
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) throw new IllegalArgumentException();

        int count = 0;
        for (Object vertex : vertices) {
            if (vertex == null) throw new IllegalArgumentException();
            validateVertex((int) vertex);
        }
    }

    private int[] sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return shortestPath(bfsv, bfsw);

    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);
        return shortestPath(bfsv, bfsw);


    }

    private int[] shortestPath(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = -1; // init
        for (int i = 0; i < digraph.V(); ++i) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int tempLength = bfsv.distTo(i) + bfsw.distTo(i);
                if (tempLength < shortestLength) {
                    shortestLength = tempLength;
                    shortestAncestor = i;
                }
            }
        }

        int[] res = new int[2];
        if (shortestAncestor == -1) {
            res[0] = -1;
            res[1] = -1;
        }
        else {
            res[0] = shortestLength;
            res[1] = shortestAncestor;
        }
        return res;
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = sap(v, w);
        return res[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = sap(v, w);
        return res[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
        sap.length(new ArrayList<>(), new ArrayList<>());
    }
}
