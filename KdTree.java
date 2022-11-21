import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {
    private class TreeNode {
        public TreeNode() {

        }

        public TreeNode(TreeNode parent) {
            this.parent = parent;
            this.isVertical = !parent.isVertical;
        }

        public TreeNode(boolean isVertical) {
            this.isVertical = isVertical;
        }

        public TreeNode parent;
        public TreeNode left;
        public TreeNode right;
        public Point2D point;
        public boolean isVertical;
    }

    private TreeNode root;
    private int size;

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException();
        // TODO: complete insert part
    }

    private TreeNode insert(TreeNode node, Point2D p, boolean isVertical) {
        if (node == null) {
            TreeNode insertedNode = new TreeNode(isVertical);
            return insertedNode;
        }

        if (node.isVertical) {
            if (p.x() < node.point.x()) {
                node.left = insert(node.left, p, !isVertical);
            }
            else {
                node.right = insert(node.right, p, !isVertical);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                node.left = insert(node.left, p, !isVertical);
            }
            else {
                node.right = insert(node.right, p, !isVertical);
            }
        }

        return node;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();

        return find(root, p) != null;
    }

    private TreeNode find(TreeNode node, Point2D p) {
        if (node == null)
            return null;

        if (node.point.equals(p)) {
            return node;
        }

        if (node.isVertical) {
            if (p.x() < node.point.x()) {
                return find(node.left, p);
            }
            else {
                return find(node.right, p);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                return find(node.left, p);
            }
            else {
                return find(node.right, p);
            }
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        drawNode(root);
    }

    private void drawNode(TreeNode node) {
        if (node == null)
            return;
        drawNode(node.left);
        node.point.draw();
        drawNode(node.right);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> rangedPoints = new ArrayList<>();
        search(root, rect, rangedPoints);
        return rangedPoints;
    }

    private void search(TreeNode node, RectHV rect, ArrayList<Point2D> output) {
        if (node == null)
            return;
        if (rect.contains(node.point)) {
            output.add(node.point);
        }

        if (node.isVertical) {
            if (rect.xmin() < node.point.x()) {
                search(node.left, rect, output);
            }

            if (rect.xmax() > node.point.x()) {
                search(node.right, rect, output);
            }
        }
        else {
            if (rect.ymin() < node.point.y()) {
                search(node.left, rect, output);
            }

            if (rect.ymax() > node.point.y()) {
                search(node.right, rect, output);
            }
        }
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException();

        return null;
    }

    // currentDistance: the minimum square of distance at this moment
    // node: the subtree to search
    public TreeNode searchNearest(Point2D p, TreeNode node, double currentDistance) {
        if (node == null)
            return null;

        currentDistance = Math.min(node.point.distanceSquaredTo(p), currentDistance);

        TreeNode a = null, b = null;

        if (node.isVertical) {

            // p is left to node, must search left node
            if (p.x() < node.point.x()) {
                a = searchNearest(p, node.left, currentDistance);
                if (node.point.x() - p.x() < currentDistance) {
                    b = searchNearest(p, node.right, currentDistance);
                }
            }
            // p is right to node, must search right node
            if (p.x() > node.point.x()) {
                a = searchNearest(p, node.right, currentDistance);
                if (p.x() - node.point.x() < currentDistance) {
                    b = searchNearest(p, node.left, currentDistance);
                }
            }
        }
        else {
            // p is down to node, must search down node
            if (p.y() < node.point.y()) {
                a = searchNearest(p, node.left, currentDistance);
                if (node.point.y() - p.y() < currentDistance) {
                    b = searchNearest(p, node.right, currentDistance);
                }
            }
            // p is up to node, must search up node
            if (p.y() > node.point.y()) {
                a = searchNearest(p, node.right, currentDistance);
                if (p.y() - node.point.y() < currentDistance) {
                    b = searchNearest(p, node.left, currentDistance);
                }
            }
        }

        return minDistance(p, node, minDistance(p, a, b));
    }

    private TreeNode minDistance(Point2D p, TreeNode a, TreeNode b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        if (a.point.distanceSquaredTo(p) > b.point.distanceSquaredTo(p)) {
            return b;
        }
        else {
            return a;
        }
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {

    }
}
