/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();

    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size() == 0;

    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);

    }

    // draw all the points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }

    }

    // all the points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D otherPoint : points) {
            if (p.distanceSquaredTo(otherPoint) < minDistance) {
                minDistance = p.distanceSquaredTo(otherPoint);
                minPoint = otherPoint;
            }
        }
        return minPoint;

    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0.3, 0.3));
        ps.insert(new Point2D(0.6, 0.6));
        ps.draw();

        Point2D newPoint = new Point2D(0.8, 0.8);
        newPoint.draw();
        Point2D nearest = ps.nearest(newPoint);
        StdDraw.setPenColor(StdDraw.RED);
        nearest.draw();
    }
}
