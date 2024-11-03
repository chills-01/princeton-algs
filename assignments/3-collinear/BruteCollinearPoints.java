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

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // Throw an IllegalArgumentException if the argument to the constructor is null
        // if any point in the array is null
        // if the argument to the constructor contains a repeated point.
        if (points == null || containsNull(points) || containsRepeatedPoint(points))
            throw new IllegalArgumentException();

        lineSegments = new ArrayList<LineSegment>();
        if (points.length < 4) return; // not considering

        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                double slopePQ = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < points.length; r++) {
                    double slopePR = points[p].slopeTo(points[r]);
                    if (slopePQ == slopePR) {
                        for (int s = r + 1; s < points.length; s++) {
                            // check if collinear
                            Point[] temp = { points[p], points[q], points[r], points[s] };
                            double slopePS = points[p].slopeTo(points[s]);
                            Point minPoint = getMin(temp);
                            Point maxPoint = getMax(temp);
                            if (slopePQ == slopePS) {
                                lineSegments.add(new LineSegment(maxPoint, minPoint));
                            }
                        }
                    }
                }
            }
        }
    }

    private Point getMin(Point[] temp) {
        Point minPoint = temp[0];
        for (int i = 1; i < temp.length; i++) {
            if (temp[i].compareTo(minPoint) < 0) minPoint = temp[i];
        }
        return minPoint;
    }

    private Point getMax(Point[] temp) {
        Point maxPoint = temp[0];
        for (int i = 1; i < temp.length; i++) {
            if (temp[i].compareTo(maxPoint) > 0) maxPoint = temp[i];
        }
        return maxPoint;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();

    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
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

    private boolean containsNull(Point[] points) {
        for (Point p : points) {
            if (p == null) return true;
        }
        return false;
    }

    private boolean containsRepeatedPoint(Point[] points) {
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        if (sortedPoints.length <= 1) {
            return false; // can't contain duplicates
        }
        else {
            for (int i = 1; i < points.length; i++) {
                if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) return true;
            }
            return false;
        }
    }
}
