/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size = 0;
    private boolean isInitiallyHorizontal = true;

    // construct an empty set of points
    public KdTree() {
    }

    private static class Node {
        private Point2D point;   //  the point
        private RectHV rect; // axis aligned rectangle corresponding to this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0, 1, 0, 1, isInitiallyHorizontal);
    }


    private Node insert(Node cur, Point2D p, double xmin, double xmax, double ymin, double ymax,
                        boolean isHorizontal) {
        if (cur == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }

        // already in the tree
        if (!p.equals(cur.point)) {
            if (isHorizontal) {
                if (p.x() < cur.point.x())
                    cur.lb = insert(cur.lb, p, xmin, cur.point.x(), ymin, ymax, !isHorizontal);
                else cur.rt = insert(cur.rt, p, cur.point.x(), xmax, ymin, ymax, !isHorizontal);
            }
            else {
                if (p.y() < cur.point.y())
                    cur.lb = insert(cur.lb, p, xmin, xmax, ymin, cur.point.y(), !isHorizontal);
                else cur.rt = insert(cur.rt, p, xmin, xmax, cur.point.y(), ymax, !isHorizontal);
            }

        }


        return cur;

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Node cur = root;
        boolean isHorizontal = isInitiallyHorizontal;
        while (cur != null) {
            if (cur.point.equals(p)) return true;
            if (isHorizontal) {
                if (p.x() < cur.point.x()) cur = cur.lb;
                else cur = cur.rt;
            }
            else {
                if (p.y() < cur.point.y()) cur = cur.lb;
                else cur = cur.rt;
            }
            isHorizontal = !isHorizontal;
        }
        return false;
    }

    // draw all the points to standard draw
    public void draw() {
        draw(root, 0, 1, 0, 1, !isInitiallyHorizontal);
    }

    private void draw(Node cur, double minX, double maxX, double minY, double maxY,
                      boolean isHorizontal) {
        if (cur == null) return;
        if (isHorizontal) {
            // draw the horizontal splitting line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, cur.point.y(), maxX, cur.point.y());

            // recurse
            draw(cur.lb, minX, maxX, minY, cur.point.y(), !isHorizontal);
            draw(cur.rt, minX, maxX, cur.point.y(), maxY, !isHorizontal);
        }
        else {
            // draw the vertical splitting line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(cur.point.x(), minY, cur.point.x(), maxY);

            // recurse
            draw(cur.lb, minX, cur.point.x(), minY, maxY, !isHorizontal);
            draw(cur.rt, cur.point.x(), maxX, minY, maxY, !isHorizontal);
        }
    }

    // all the points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> result = new ArrayList<Point2D>();
        range(root, rect, isInitiallyHorizontal, result);
        return result;

    }

    private void range(Node cur, RectHV rect, boolean isHorizontal, List<Point2D> result) {
        if (cur == null) return;
        if (rect.contains(cur.point)) result.add(cur.point);

        // now recurse
        if (isHorizontal) {
            if (rect.xmin() < cur.point.x()) range(cur.lb, rect, !isHorizontal, result);
            if (rect.xmax() >= cur.point.x()) range(cur.rt, rect, !isHorizontal, result);
        }
        else {
            if (rect.ymin() < cur.point.y()) range(cur.lb, rect, !isHorizontal, result);
            if (rect.ymax() >= cur.point.y()) range(cur.rt, rect, !isHorizontal, result);
        }
    }

    // a nearest neighbor in the set to point; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node nearest = nearest(root, p, root, isInitiallyHorizontal);
        return (nearest == null) ? null : nearest.point;
    }

    private Node nearest(Node cur, Point2D p, Node nearest, boolean isHorizontal) {
        if (cur == null) return nearest;

        // no need to search the other subtree if the nearest distance is smaller than the
        // distance between p and the other half
        if (cur.rect.distanceSquaredTo(p) > nearest.point.distanceSquaredTo(p)) return nearest;
        if (cur.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p)) nearest = cur;
        Node subtree1;
        Node subtree2;

        if (isHorizontal) {
            if (p.x() < cur.point.x()) {
                subtree1 = cur.lb;
                subtree2 = cur.rt;
            }
            else {
                subtree1 = cur.rt;
                subtree2 = cur.lb;
            }
        }
        else {
            if (p.y() < cur.point.y()) {
                subtree1 = cur.lb;
                subtree2 = cur.rt;
            }
            else {
                subtree1 = cur.rt;
                subtree2 = cur.lb;
            }

        }
        nearest = nearest(subtree1, p, nearest, !isHorizontal);
        nearest = nearest(subtree2, p, nearest, !isHorizontal);
        return nearest;
    }

    public static void main(String[] args) {
        // check empty
        KdTree kd = new KdTree();
        StdOut.println(kd.size());

        // check has one item once inserted, and that it can only be inserted
        // once
        kd.insert(new Point2D(0.5, 0.5));
        StdOut.println(kd.size());
        kd.insert(new Point2D(0.5, 0.5));
        StdOut.println(kd.size());

        StdOut.println(kd.contains(new Point2D(0.5, 0.5)));
        StdOut.println(kd.contains(new Point2D(0.5, 0.55)));

    }
}


