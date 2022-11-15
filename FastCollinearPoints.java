/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segs;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // check whether contains null element
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

        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        // Fast find
        for (int i = 0; i < points.length; i++) {
            Point current = points[i];
            Arrays.sort(sortedPoints, current.slopeOrder());
            for (int j = 0; j < sortedPoints.length; ) {

                double slope = current.slopeTo(sortedPoints[j]);
                Point minPoint = min(current, sortedPoints[j]);
                Point maxPoint = max(current, sortedPoints[j]);
                int count = 1;
                j++;

                for (; j < sortedPoints.length; j++) {
                    if (current.slopeTo(sortedPoints[j]) == slope) {
                        if (sortedPoints[j].compareTo(maxPoint) > 0) {
                            maxPoint = sortedPoints[j];
                        }
                        else if (sortedPoints[j].compareTo(minPoint) < 0) {
                            minPoint = sortedPoints[j];
                        }
                        count++;
                    }
                    else {
                        break;
                    }
                }

                if (count >= 3 && minPoint == current) {
                    LineSegment segment = new LineSegment(minPoint, maxPoint);
                    segs.add(segment);
                }

            }
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
