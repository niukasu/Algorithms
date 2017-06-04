import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    
    private LineSegment[] segment;
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new java.lang.NullPointerException();
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
            }
        }
        ArrayList<LineSegment> temp = new ArrayList<LineSegment>();
        Point[] tempPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(tempPoints);
        for (int p = 0; p < tempPoints.length - 3; p++)
            for (int q = p + 1; q < tempPoints.length - 2; q++)
                for (int r = q + 1; r < tempPoints.length - 1; r++)
                    if (tempPoints[p].slopeTo(tempPoints[q]) == tempPoints[p].slopeTo(tempPoints[r])) {
                        for (int s = r + 1; s < tempPoints.length; s++) {
                            if (tempPoints[p].slopeTo(tempPoints[q]) == tempPoints[p].slopeTo(tempPoints[s])) {
                                LineSegment temporary = new LineSegment(tempPoints[p], tempPoints[s]);
                                temp.add(temporary);
                            }
                        }
                    }
        segment = temp.toArray(new LineSegment[temp.size()]);
    }
    public int numberOfSegments() {       // the number of line segments
        return segment.length;
    }
    public LineSegment[] segments() {               // the line segments
        LineSegment[] temp = Arrays.copyOf(segment, segment.length);
        return temp;
    }
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
