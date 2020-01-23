/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies: Point2D.algs4 RectHV.algs LinkedList.java TreeSet.java
 *
 *  PointSET is a data type which is a brute force searching algorithm for points
 *  in 2D plane.
 *  It implements two important methods (Range and Nearest Neighbor Search), but with
 *  a normal Binary Search Tree (java built-in library) with no modifications, so it
 *  will be slower than a 2d-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points)
            point.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);

        LinkedList<Point2D> enclosedPoints = new LinkedList<>();
        for (Point2D point : points) {
            if (rect.contains(point))
                enclosedPoints.add(point);
        }
        return enclosedPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);

        Point2D minPoint = null;
        double min = Double.POSITIVE_INFINITY;

        for (Point2D point : points) {
            double minDistance = p.distanceSquaredTo(point);
            if (minDistance < min) {
                minPoint = point;
                min = minDistance;
            }
        }

        return minPoint;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // empty
    }
}
