import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {

    private LineSegment[] segment;
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException();
            if (i == points.length - 1) continue;
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
            }
        }
        ArrayList<LineSegment> temp = new ArrayList<LineSegment>();
        Point[] tempPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(tempPoints);
        for (int i = 0; i < tempPoints.length; i++) {
            Point[] sloping = Arrays.copyOf(tempPoints, tempPoints.length);
            Arrays.sort(sloping, tempPoints[i].slopeOrder());
            int count = 0;
            for (int j = 0; j < tempPoints.length; j++) {
                if (j != tempPoints.length - 1 && 
                    tempPoints[i].slopeTo(sloping[j]) == tempPoints[i].slopeTo(sloping[j + 1])) 
                    count++;
                else {
                    if (count >= 2) {
                        ArrayList<Point> check = new ArrayList<Point>();
                        check.add(tempPoints[i]);
                        for (int k = j - count; k <= j; k++) {
                            check.add(sloping[k]);
                        }
                        Point[] checking = check.toArray(new Point[check.size()]);
                        Arrays.sort(checking);
                        if (checking[0].compareTo(tempPoints[i]) == 0) {
                            LineSegment temporary = new LineSegment(checking[0], checking[checking.length-1]);
                            temp.add(temporary);
                        }
                    }
                    count = 0;
                }
            }
        }
        segment = temp.toArray(new LineSegment[temp.size()]);
    }
    public           int numberOfSegments() {       // the number of line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}