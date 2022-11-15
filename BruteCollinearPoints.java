import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segs;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        // check whether contains null point
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        // check whether repeats
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        segs = new ArrayList<>();
        // Brute find
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        Point a = points[i];
                        Point b = points[j];
                        Point c = points[k];
                        Point d = points[l];
                        if (a.slopeTo(b) == b.slopeTo(c) && b.slopeTo(c) == c.slopeTo(d)) {
                            segs.add(new LineSegment(min(min(a, b), min(c, d)),
                                                     max(max(a, b), max(c, d))));
                        }
                    }
                }
            }
        }

        // resize
    }

    private Point max(Point a, Point b) {
        if (a.compareTo(b) >= 0) {
            return a;
        }
        else {
            return b;
        }
    }

    private Point min(Point a, Point b) {
        if (a.compareTo(b) <= 0) {
            return a;
        }
        else {
            return b;
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return segs.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] segments = new LineSegment[segs.size()];
        segments = segs.toArray(segments);
        return segments;
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