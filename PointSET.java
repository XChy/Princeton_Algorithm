import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;

public class PointSET {
    private SET<Point2D> points;

    public PointSET()                               // construct an empty set of points
    {
        points = new SET<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return points.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        Iterator<Point2D> iterator = points.iterator();
        ArrayList<Point2D> rangedPoints = new ArrayList<>();
        while (iterator.hasNext()) {
            rangedPoints.add(iterator.next());
        }
        return rangedPoints;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException();

        if (isEmpty())
            return null;

        Iterator<Point2D> iterator = points.iterator();

        Point2D near = iterator.next();
        double nearestDistance = near.distanceSquaredTo(p);

        while (iterator.hasNext()) {
            Point2D current = iterator.next();
            if (current.distanceSquaredTo(p) < nearestDistance) {
                near = current;
                nearestDistance = current.distanceSquaredTo(p);
            }
        }

        return near;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        PointSET set = new PointSET();
        set.insert(new Point2D(3, 3));
        set.insert(new Point2D(5, 6));
        set.insert(new Point2D(4, 7));
        set.insert(new Point2D(7, 2));
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        set.draw();

        Point2D p = new Point2D(5, 5);
        StdOut.print(set.nearest(p).toString());
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }
}