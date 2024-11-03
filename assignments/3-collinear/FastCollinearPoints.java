/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        // null check
        if (points == null || containsNull(points)) {
            throw new IllegalArgumentException();

        }

        Point[] pointsByNatural = points.clone();
        Arrays.sort(pointsByNatural);

        if (containsRepeatedPoint(
                pointsByNatural))
            throw new IllegalArgumentException();

        lineSegments = new ArrayList<LineSegment>();


        if (points.length < 4) return; // not considering

        for (int i = 0; i < pointsByNatural.length; i++) {

            Point p = pointsByNatural[i];

            // sort points by natural order, then by slope wrt p
            // Arrays.sort(pointsByNatural);
            Point[] pointsBySlope = pointsByNatural.clone();
            Arrays.sort(pointsBySlope, p.slopeOrder());

            // sliding window to add n points to segment, guaranteed for
            // left point to be min, right to be max
            int left = 1;
            int right = 2;
            while (right < pointsBySlope.length) {
                // if collinear
                while (right < pointsBySlope.length && Double.compare(
                        p.slopeTo(pointsBySlope[left]), p.slopeTo(pointsBySlope[right])) == 0) {
                    right++;
                }
                if (right - left >= 3 && p.compareTo(pointsBySlope[left]) < 0) {
                    lineSegments.add(new LineSegment(p, pointsBySlope[right - 1]));
                }
                left = right;
                right++;
            }

        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);

    }

    private boolean containsNull(Point[] points) {
        for (Point p : points) {
            if (p == null) return true;
        }
        return false;
    }

    private boolean containsRepeatedPoint(Point[] sortedPoints) {
        if (sortedPoints.length <= 1) {
            return false; // can't contain duplicates
        }
        else {
            for (int i = 1; i < sortedPoints.length; i++) {
                if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) return true;
            }
            return false;
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
