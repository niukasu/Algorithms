import java.util.TreeSet;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private TreeSet<Point2D> redBlackTree;
    public         PointSET() {                              // construct an empty set of points 
        redBlackTree = new TreeSet<Point2D>();
    }
    public           boolean isEmpty() {                     // is the set empty? 
        return redBlackTree.isEmpty();
    }
    public               int size() {                        // number of points in the set 
        return redBlackTree.size();
    }
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new java.lang.NullPointerException();
        redBlackTree.add(p);
    }
    public           boolean contains(Point2D p) {           // does the set contain point p? 
        if (p == null) throw new java.lang.NullPointerException();
        return redBlackTree.contains(p);
    }
    public              void draw() {                        // draw all points to standard draw 
        for ( Point2D temp : redBlackTree) {
            temp.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if (rect == null) throw new java.lang.NullPointerException();
        TreeSet<Point2D> tempp = new TreeSet<Point2D>();
        for ( Point2D temp : redBlackTree) {
            if (rect.contains(temp)) tempp.add(temp);
        }
        return tempp;        
    }
    public           Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new java.lang.NullPointerException(); 
        Point2D near = null;
        Double distance = Double.POSITIVE_INFINITY;
        for ( Point2D temp : redBlackTree) {
            if (p.distanceSquaredTo(temp) < distance) {
                near = temp;
                distance = p.distanceSquaredTo(temp);
            }
        }
        return near;
    }
}
