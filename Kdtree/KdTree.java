import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;  
  
public class KdTree {  
    private static class Node {  
        private Node leftNode;  
        private Node rightNode;  
        private boolean pickX;  
        private double x;  
        private double y;  
  
        public Node(double x, double y, Node leftNode, Node rightNode, boolean pickX) {  
            this.x = x;  
            this.y = y;  
            this.leftNode = leftNode;  
            this.rightNode = rightNode;  
            this.pickX = pickX;  
        }  
    }  
  
    private static RectHV ranging = new RectHV(0, 0, 1, 1);  
    private Node root;  
    private int size;  
  
    public KdTree() {  
        this.size = 0;  
        this.root = null;  
    }  
  
    public boolean isEmpty() {  
        return this.size == 0;  
    }  
  
    public int size() {  
        return this.size;  
    }  
    public void insert(Point2D p) {  
        this.root = insert(this.root, p, true);  
    }  
  
    private Node insert(Node node, Point2D p, boolean pickX) {  
        if (node == null) {  
            size++;  
            return new Node(p.x(), p.y(), null, null, pickX);  
        }  
          if (node.x == p.x() && node.y == p.y()) {  
            return node;  
        }  
        if (node.pickX && p.x() < node.x || !node.pickX  
                && p.y() < node.y) {  
            node.leftNode = insert(node.leftNode, p, !node.pickX);  
        } else {  
            node.rightNode = insert(node.rightNode, p, !node.pickX);  
        }  
        return node;  
    }  
  
    // does the set contain the point p?  
    public boolean contains(Point2D p) {  
        return contains(root, p.x(), p.y());  
    }  
  
    private boolean contains(Node node, double x, double y) {  
        if (node == null) {  
            return false;  
        }  
  
        if (node.x == x && node.y == y) {  
            return true;  
        }  
  
        if (node.pickX && x < node.x || !node.pickX && y < node.y) {  
            return contains(node.leftNode, x, y);  
        } else {  
            return contains(node.rightNode, x, y);  
        }  
    }  
  
    public void draw() {  
        StdDraw.setScale(0, 1);  
  
        StdDraw.setPenColor(StdDraw.BLACK);  
        StdDraw.setPenRadius();  
        ranging.draw();  
  
        draw(root, ranging);  
    }  
  
    private void draw(Node node, RectHV rect) {  
        if (node == null) {  
            return;  
        }  
  
        // draw the point  
        StdDraw.setPenColor(StdDraw.BLACK);  
        StdDraw.setPenRadius(0.1);  
        new Point2D(node.x, node.y).draw();  
  
        // get the min and max points of division line  
        Point2D min, max;  
        if (node.pickX) {  
            StdDraw.setPenColor(StdDraw.RED);  
            min = new Point2D(node.x, rect.ymin());  
            max = new Point2D(node.x, rect.ymax());  
        } else {  
            StdDraw.setPenColor(StdDraw.BLUE);  
            min = new Point2D(rect.xmin(), node.y);  
            max = new Point2D(rect.xmax(), node.y);  
        }  
  
        // draw that division line  
        StdDraw.setPenRadius();  
        min.drawTo(max);  
  
        // recursively draw children  
        draw(node.leftNode, leftRect(rect, node));  
        draw(node.rightNode, rightRect(rect, node));  
    }  
  
    private RectHV leftRect(RectHV rect, Node node) {  
        if (node.pickX) {  
            return new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());  
        } else {  
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);  
        }  
    }  
  
    private RectHV rightRect(RectHV rect, Node node) {  
        if (node.pickX) {  
            return new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());  
        } else {  
            return new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());  
        }  
    }  
  
    // all points in the set that are inside the rectangle  
    public Iterable<Point2D> range(RectHV rect) {  
        TreeSet<Point2D> tempo = new TreeSet<Point2D>();  
        range(root, ranging, rect, tempo);  
  
        return tempo;  
    }  
  
    private void range(Node node, RectHV nrect, RectHV rect, TreeSet<Point2D> tempo) {  
        if (node == null)  
            return;  
  
        if (rect.intersects(nrect)) { 
            Point2D p = new Point2D(node.x, node.y);  
            if (rect.contains(p))  
                tempo.add(p);  
            range(node.leftNode, leftRect(nrect, node), rect, tempo);  
            range(node.rightNode, rightRect(nrect, node), rect, tempo);  
        }  
    }  
  
    // a nearest neighbor in the set to p; null if set is empty  
    public Point2D nearest(Point2D p) {  
        return nearest(root, ranging, p.x(), p.y(), null);  
    }  
  
    private Point2D nearest(Node node, RectHV rect,  
            double x, double y, Point2D candidate) {  
        if (node == null){  
            return candidate;  
        }  
  
        double dqn = 0.0;  
        double drq = 0.0;  
        RectHV left = null;  
        RectHV rigt = null;  
        Point2D query = new Point2D(x, y);  
        Point2D nearest = candidate;  
  
        if (nearest != null) {  
            dqn = query.distanceSquaredTo(nearest);  
            drq = rect.distanceSquaredTo(query);  
        }  
  
        if (nearest == null || dqn > drq) {  
            Point2D point = new Point2D(node.x, node.y);  
            if (nearest == null || dqn > query.distanceSquaredTo(point))  
                nearest = point;  
  
            if (node.pickX) {  
                left = new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());  
                rigt = new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());  
  
                if (x < node.x) {  
                    nearest = nearest(node.leftNode, left, x, y, nearest);  
                    nearest = nearest(node.rightNode, rigt, x, y, nearest);  
                } else {  
                    nearest = nearest(node.rightNode, rigt, x, y, nearest);  
                    nearest = nearest(node.leftNode, left, x, y, nearest);  
                }  
            } else {  
                left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);  
                rigt = new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());  
  
                if (y < node.y) {  
                    nearest = nearest(node.leftNode, left, x, y, nearest);  
                    nearest = nearest(node.rightNode, rigt, x, y, nearest);  
                } else {  
                    nearest = nearest(node.rightNode, rigt, x, y, nearest);  
                    nearest = nearest(node.leftNode, left, x, y, nearest);  
                }  
            }  
        }  
  
        return nearest;  
    }  
}  