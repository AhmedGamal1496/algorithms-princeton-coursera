/******************************************************************************
 *  Compilation:  javac-algs4 BruteCollinearPoints.java
 *  Execution:    java-algs4 BruteCollinearPoints
 *  Dependencies: StdOut.java StdDraw.java In.java
 *
 *  Brute Force solution to find the collinear points in a plane.
 *  Collinear points which consists of 4 or more points having the same slope.
 *  Brute force depends on a for loop solution without using any kind of
 *  sorting algorithm.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

// this class is for 4 points only which make a collinear point
// no more than 4 at once to create a line segment
public class BruteCollinearPoints {
    // array list to add new line segments
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // throw IllegalArgumentException for null points or duplicates ones
        checkIllegal(points);

        // copy the points
        Point[] sortedPoints = points.clone();
        // sort them using compareTo function overriden method in Point class
        Arrays.sort(sortedPoints);
        findSegments(sortedPoints);
    }

    // loop over the points, if 4 points have the same slop, they are collinear
    private void findSegments(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point q = points[j];
                for (int k = j + 1; k < points.length; k++) {
                    Point r = points[k];
                    for (int m = k + 1; m < points.length; m++) {
                        Point s = points[m];

                        if (Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0 &&
                                Double.compare(p.slopeTo(q), p.slopeTo(s)) == 0)
                            lineSegments.add(new LineSegment(p, s));
                    }
                }
            }
        }
    }

    private void checkIllegal(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();

        // check for duplicate points
        for (int i = 0; i < points.length; i++) {
            for (int k = i + 1; k < points.length; k++) {
                if (points[i].slopeTo(points[k]) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException();
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        // convert from ArrayList to Array, then return
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
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
