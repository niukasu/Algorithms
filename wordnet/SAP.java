import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.lang.*;
import java.util.*;

public class SAP {

    private final Digraph g;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException();
        g = new Digraph(G);    
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= g.V()) throw new IndexOutOfBoundsException();
        if (w < 0 || w >= g.V()) throw new IndexOutOfBoundsException();
        int[] result = solve(v, w);  
        return result[0];  
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= g.V()) throw new IndexOutOfBoundsException();
        if (w < 0 || w >= g.V()) throw new IndexOutOfBoundsException();
        int[] result = solve(v, w);  
        return result[1];      
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.NullPointerException();
        for (int temp : v) {
            if (temp < 0 || temp >= g.V()) throw new IndexOutOfBoundsException();
        }
        for (int temp : w) {
            if (temp < 0 || temp >= g.V()) throw new IndexOutOfBoundsException();
        }
        int[] result = solve(v, w);  
        return result[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.NullPointerException();
        for (int temp : v) {
            if (temp < 0 || temp >= g.V()) throw new IndexOutOfBoundsException();
        }
        for (int temp : w) {
            if (temp < 0 || temp >= g.V()) throw new IndexOutOfBoundsException();
        }
        int[] result = solve(v, w);  
        return result[1];
    }

    private int[] solve(int v, int w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        int distance = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (int i = 0; i < g.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {    
                if (bfsv.distTo(i) + bfsw.distTo(i) < distance){  
                    distance = bfsv.distTo(i) + bfsw.distTo(i);  
                    minAncestor = i;  
                }  
            }  
        }
        int[] result = new int[2];
        result[0] = -1;  
        result[1] = -1; 
        if (distance == Integer.MAX_VALUE){   
            return result;  
        }  
        result[0] = distance;  
        result[1] = minAncestor;  
        return result;            
    }

    private int[] solve(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(g, w);
        int distance = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (int i = 0; i < g.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {    
                if (bfsv.distTo(i) + bfsw.distTo(i) < distance){  
                    distance = bfsv.distTo(i) + bfsw.distTo(i);  
                    minAncestor = i;  
                }  
            }  
        }
        int[] result = new int[2];  
        result[0] = -1;  
        result[1] = -1;   
        if (distance == Integer.MAX_VALUE) {  
            return result;  
        }  
        result[0] = distance;  
        result[1] = minAncestor;  
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
           int length   = sap.length(v, w);
           int ancestor = sap.ancestor(v, w);
           StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
    }

}
