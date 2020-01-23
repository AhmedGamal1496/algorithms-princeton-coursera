/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Execution:    java KdTree
 *  Dependencies: Point2D.algs4 RectHV.algs StdDraw.algs4 In.algs4 LinkedList.java
 *
 * Data type to represent a set of points in the unit square using a 2d-tree to
 * support efficient range search (find all of the points contained in a query rectangle)
 * and nearest-neighbor search (find a closest point to a query point).
 *
 * A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to
 * build a BST with points in the nodes, using the x- and y-coordinates of the points
 * as keys in strictly alternating sequence.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {
    private enum Separator {VERTICAL, HORIZONTAL} // 2d-tree nodes vertical or horizontal

    private Node root;     // root of 2d-tree
    private int size;      // size of 2d-tree

    private static class Node {
        private final Point2D point;   // the point
        private final RectHV rect;     // the axis-aligned rectangle corresponding to this node
        private Node leftBottom;       // the left/bottom subtree
        private Node rightTop;         // the right/top subtree
        private final Separator sepr;  // vertical or horizontal

        Node(Point2D p, RectHV rect, Separator sepr) {
            point = p;
            this.rect = rect;
            this.sepr = sepr;
        }

        // each node is aligned opposite to the parent node
        public Separator nextSepr() {
            return sepr == Separator.VERTICAL ?
                   Separator.HORIZONTAL : Separator.VERTICAL;
        }

        // the relation between a given point and this node
        // true if left or bottom, false otherwise
        public boolean isLeftBottom(Point2D p) {
            return sepr == Separator.VERTICAL ?
                   p.x() < point.x() : p.y() < point.y();
        }

        // get the rectangle of a given point using this node's rectangle
        public RectHV getRect(Point2D p) {
            boolean dir = isLeftBottom(p);
            if (sepr == Separator.VERTICAL) {
                if (dir)
                    return new RectHV(rect.xmin(), rect.ymin(), point.x(),
                                      rect.ymax());
                else
                    return new RectHV(point.x(), rect.ymin(), rect.xmax(),
                                      rect.ymax());
            }
            else {
                if (dir)
                    return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                      point.y());
                else
                    return new RectHV(rect.xmin(), point.y(), rect.xmax(),
                                      rect.ymax());
            }
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        if (this.contains(p)) return;  // return if point p is already there

        root = insert(root, p, null);
    }

    // insert a new point by constructing its new node
    private Node insert(Node node, Point2D p, Node parent) {
        // if the tree is empty, make a new node with unit rectangle (0,0,1,1)
        // else make a new node using getRect method
        if (root == null) {
            size++;
            return new Node(p, new RectHV(0, 0, 1, 1), Separator.VERTICAL);
        }
        else if (node == null) {
            size++;
            return new Node(p, parent.getRect(p), parent.nextSepr());
        }

        if (node.isLeftBottom(p))
            node.leftBottom = insert(node.leftBottom, p, node);
        else
            node.rightTop = insert(node.rightTop, p, node);

        return node;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return get(root, p) != null;
    }

    // recursively get the point p given the root of the tree
    private Point2D get(Node node, Point2D p) {
        if (node == null) return null;

        if (p.equals(node.point)) return p;
        else if (node.isLeftBottom(p)) return get(node.leftBottom, p);
        else return get(node.rightTop, p);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        if (root == null) return null;

        return range(root, rect, new LinkedList<>());
    }

    // given a rectangle, check if it intersects with any of the nodes in the 2d-tree recursively
    // if it does, check if its point inside the rectangle
    private LinkedList<Point2D> range(Node node, RectHV rect, LinkedList<Point2D> rangePoints) {
        if (node == null) return rangePoints;

        if (rect.intersects(node.rect)) {
            if (rect.contains(node.point))
                rangePoints.add(node.point);
            rangePoints = range(node.leftBottom, rect, rangePoints);
            rangePoints = range(node.rightTop, rect, rangePoints);
        }

        return rangePoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (root == null) return null;

        return isEmpty() ? null : nearest(root, p, root.point);
    }

    // if the distance between the target point and node's rectangle is smaller than
    // minimum distance. If it is, check the distance between this node's point and target point
    // if not, do not check its subtrees as they will be far away
    private Point2D nearest(Node node, Point2D target, Point2D closestPoint) {
        if (node == null) return closestPoint;

        double closestDist = closestPoint.distanceSquaredTo(target);
        if (node.rect.distanceSquaredTo(target) < closestDist) {
            if (node.point.distanceSquaredTo(target) < closestDist)
                closestPoint = node.point;

            // it the target point on the left or bottom of this node,
            // check left/bottom subtrees first
            if (node.isLeftBottom(target)) {
                closestPoint = nearest(node.leftBottom, target, closestPoint);
                closestPoint = nearest(node.rightTop, target, closestPoint);
            }
            else {
                closestPoint = nearest(node.rightTop, target, closestPoint);
                closestPoint = nearest(node.leftBottom, target, closestPoint);
            }
        }

        return closestPoint;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        if (node.sepr == Separator.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        draw(node.leftBottom);
        draw(node.rightTop);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(1, 0, 1, 0);
        StdDraw.line(1, 1, 0, 1);
        StdDraw.line(0, 1, 0, 0);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        // kdtree.draw();
        // kdtree.nearest(new Point2D(0.87, 0.91));
    }
}
