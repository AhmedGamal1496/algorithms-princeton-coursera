/******************************************************************************
 *  Compilation:  javac-algs4 FastCollinearPoints.java
 *  Execution:    java-algs4 FastCollinearPoints
 *  Dependencies: StdOut.java StdDraw.java StdOut.java In.java
 *
 *  faster solution to find the collinear points in a plane.
 *  Collinear points which consists of 4 or more points having the same slope.
 *  FastCollinearPoints algorithm depends on a sorting technique. It sorts
 *  the points by calculating the slope of each points relative to
 *  a selected point (the original point).
 *  Equal slopes means they are collinear and share the same line segment.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check for any null or duplicate points
        checkIllegal(points);
        // create an array for sorting the points according to the slope
        Point[] sortedPoints = points.clone();

        // sort all the points by calculating the slope of each relative to points[i]
        for (int i = 0; i < points.length; i++) {
            // using the comparator, sort the array of points
            Arrays.sort(sortedPoints, points[i].slopeOrder());
            findSegments(sortedPoints);
        }
    }

    private void findSegments(Point[] points) {
        // temp array to store the points that are collinear
        ArrayList<Point> temp = new ArrayList<>();
        Point originalPoint = points[0];

        /* ***********
         loop until you find some consecutive equal slopes, save them in temp and continue
         e.g.
         Original, slope: infinity
         2nd point, slope: -1
         3rd, slope: -1
         4th, slope: -1
         5th, slope: -0.5
         save original, 2nd, 3rd, and 4th in temp array
         ************** */
        for (int i = 1; i < points.length; i++) {
            if (i != points.length - 1 && originalPoint.slopeTo(points[i]) ==
                    originalPoint.slopeTo(points[i + 1]))
                temp.add(points[i]);
            else if (!temp.isEmpty()) {
                temp.add(points[i]);
                temp.add(originalPoint);

                // if we have 4 points or more, that is collinear
                if (temp.size() >= 4) {
                    // convert from ArrayList to Array to be able to sort the points
                    // add the two end points to lineSegments ArrayList
                    Point[] tempArray = temp.toArray(new Point[temp.size()]);
                    Arrays.sort(tempArray);

                    // check for any duplicate line segments.
                    if (originalPoint.compareTo(tempArray[0]) == 0)
                        lineSegments.add(new LineSegment(originalPoint,
                                                         tempArray[tempArray.length - 1]));
                }
                temp.clear();
            }
        }
    }

    private void checkIllegal(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++) {
            for (int k = i + 1; k < points.length; k++) {
                if (points[i].compareTo(points[k]) == 0)
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
        // convert from ArrayList to Array
        LineSegment[] arrSegments = new LineSegment[lineSegments.size()];
        arrSegments = lineSegments.toArray(arrSegments);
        return arrSegments;
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



