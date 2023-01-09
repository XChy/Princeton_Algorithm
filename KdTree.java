import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private class TreeNode {

        public TreeNode left;
        public TreeNode right;
        public Point2D point;
        public RectHV rect;
        public boolean isVertical;

        public TreeNode() {
        }

        public TreeNode(boolean isVertical) {
            this.isVertical = isVertical;
        }

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
        if (!contains(p)) {
            root = insert(root, null, p);
            size++;
        }
    }

    private TreeNode insert(TreeNode node, TreeNode parent, Point2D p) {
        if (node == null) {
            if (parent == null) {
                TreeNode insertedNode = new TreeNode(true);
                insertedNode.point = p;
                insertedNode.rect = new RectHV(0, 0, 1, 1);
                return insertedNode;
            }
            else {
                boolean isleft = isLeft(parent, p);
                TreeNode insertedNode = new TreeNode(!parent.isVertical);
                insertedNode.point = p;

                if (parent.isVertical) {
                    if (isleft)
                        insertedNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                                       parent.point.x(), parent.rect.ymax());
                    else
                        insertedNode.rect = new RectHV(parent.point.x(), parent.rect.ymin(),
                                                       parent.rect.xmax(), parent.rect.ymax());
                }
                else {// Horizonally splitted
                    if (isleft)
                        insertedNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                                       parent.rect.xmax(), parent.point.y());
                    else
                        insertedNode.rect = new RectHV(parent.rect.xmin(), parent.point.y(),
                                                       parent.rect.xmax(), parent.rect.ymax());
                }
                return insertedNode;
            }
        }

        boolean isleft = isLeft(node, p);
        if (isleft)
            node.left = insert(node.left, node, p);
        else
            node.right = insert(node.right, node, p);

        return node;
    }

    private boolean isLeft(TreeNode node, Point2D p) {
        if (node.isVertical) {
            if (p.x() <= node.point.x()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (p.y() <= node.point.y()) {
                return true;
            }
            else {
                return false;
            }
        }
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
            if (p.x() <= node.point.x()) {
                return find(node.left, p);
            }
            else {
                return find(node.right, p);
            }
        }
        else {
            if (p.y() <= node.point.y()) {
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
            if (rect.xmin() <= node.point.x()) {
                search(node.left, rect, output);
            }

            if (rect.xmax() > node.point.x()) {
                search(node.right, rect, output);
            }
        }
        else {
            if (rect.ymin() <= node.point.y()) {
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

        if (isEmpty())
            return null;

        return searchNearest(p, root, root, Double.POSITIVE_INFINITY).point;
    }

    // currentDistance: the minimum square of distance at this moment
    // node: the subtree to search
    private TreeNode searchNearest(Point2D p, TreeNode node, TreeNode currentMinNode,
                                   double currentMinDistance) {
        if (node == null)
            return currentMinNode;

        double currentDistance = node.point.distanceSquaredTo(p);
        if (currentDistance < currentMinDistance) {
            currentMinDistance = currentDistance;
            currentMinNode = node;
        }

        TreeNode first, second;
        if (isLeft(node, p)) {
            first = node.left;
            second = node.right;
        }
        else {
            first = node.right;
            second = node.left;
        }

        if (first != null && first.rect.distanceSquaredTo(p) < currentMinDistance) {
            TreeNode left = searchNearest(p, first, currentMinNode, currentMinDistance);
            currentDistance = left.point.distanceSquaredTo(p);
            if (currentDistance < currentMinDistance) {
                currentMinDistance = currentDistance;
                currentMinNode = left;
            }
        }
        if (second != null && second.rect.distanceSquaredTo(p) < currentMinDistance) {
            TreeNode right = searchNearest(p, second, currentMinNode, currentMinDistance);
            currentDistance = right.point.distanceSquaredTo(p);
            if (currentDistance < currentMinDistance) {
                currentMinDistance = currentDistance;
                currentMinNode = right;
            }
        }


        return currentMinNode;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.9, 0.6));
        StdOut.println(tree.nearest(new Point2D(0.213, 0.531)).toString());
        StdOut.println(tree.size());
    }
}
