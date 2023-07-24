/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * KdTrees, KdTree implementation of 2D points
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class KdTree {
    private Node root;
    private int size;

    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * Private Node data type
     */
    private static class Node {
        private final Point2D p;     // the point
        private final RectHV rect;   // the axis-aligned rectangle corresponding to this node
        private Node lb;             // the left/bottom subtree
        private Node rt;             // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }

    /**
     * Return True if PointSet is empty
     *
     * @return True if set is empty.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns number of points in the PointSET
     *
     * @return number of points - int
     */
    public int size() {
        return size;
    }

    /**
     * Insert the point into KdTree
     *
     * @param p point
     * @throws IllegalArgumentException if point is null
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1));
            size = 1;
            return;
        }
        insert(true, root, p);
    }

    /**
     * Recursive insert point to KdTree.
     *
     * @param isEven True if parent node is in the even layer
     * @param p      point
     * @param n      parent node
     */
    private void insert(boolean isEven, Node n, Point2D p) {
        if (n.p.equals(p)) return;
        if (isLeftBottom(isEven, p, n)) {
            if (n.lb != null) insert(!isEven, n.lb, p);
            else {
                createLeftBottomNode(isEven, p, n);
                size++;
            }
        } else {
            if (n.rt != null) insert(!isEven, n.rt, p);
            else {
                createRightTopNode(isEven, p, n);
                size++;
            }
        }
    }

    /**
     * Create the new left or bottom node
     *
     * @param isEven True if parent node is in the even layer
     * @param p      point
     * @param n      parent node
     */
    private void createLeftBottomNode(boolean isEven, Point2D p, Node n) {
        RectHV rect;
        if (isEven) // Left node
            rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        else  // Bottom node
            rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
        n.lb = new Node(p, rect);
    }

    /**
     * Create the new right or top node
     *
     * @param isEven True if parent node is in the even layer
     * @param p      point
     * @param n      parent node
     */
    private void createRightTopNode(boolean isEven, Point2D p, Node n) {
        RectHV rect;
        if (isEven) // Right node
            rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
        else // Top node
            rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
        n.rt = new Node(p, rect);
    }

    /**
     * Return if child should be in left or bottom
     * if layers is even, point should compare x;
     * Otherwise point should compare y.
     *
     * @param isEven True if parent node is in the even layer
     * @param p      point
     * @param n      parent node
     * @return True if child should be in left or bottom
     */
    private boolean isLeftBottom(boolean isEven, Point2D p, Node n) {
        if (isEven)
            return less(p, n.p, Point2D.X_ORDER);
        else
            return less(p, n.p, Point2D.Y_ORDER);
    }

    /**
     * Compare two point with comparator.
     *
     * @param p1   point 1
     * @param p2   point 2
     * @param comp point comparator
     * @return True if p1 is less than p2
     */
    private boolean less(Point2D p1, Point2D p2, Comparator<Point2D> comp) {
        return comp.compare(p1, p2) < 0;
    }


    /**
     * Check if set contains point
     *
     * @param p point
     * @return True if set contains point
     * @throws IllegalArgumentException if point is null
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(true, p, root);
    }

    /**
     * Recursive check if set contains point
     *
     * @param isEven True if parent node is in the even layer
     * @param p      point
     * @param n      parent node
     * @return True if child should be in left or bottom
     */
    private boolean contains(boolean isEven, Point2D p, Node n) {
        if (n == null) return false;
        if (n.p.equals(p)) return true;
        if (isLeftBottom(isEven, p, n))
            return contains(!isEven, p, n.lb);
        else
            return contains(!isEven, p, n.rt);
    }

    /**
     * Draw all the points into {@link StdDraw#point(double, double)}
     */
    public void draw() {
        draw(true, root);
    }

    /**
     * Recursive draw point and subdivision line
     *
     * @param isEven True if node is in the even layer
     * @param n      Node
     */
    private void draw(boolean isEven, Node n) {
        if (n == null) return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.p.x(), n.p.y());
        StdDraw.setPenRadius();
        if (isEven) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymax(), n.p.x(), n.rect.ymin());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(!isEven, n.lb);
        draw(!isEven, n.rt);
    }

    /**
     * Return all points that are inside the rectangle (or on the boundary)
     *
     * @param rect rectangle
     * @return A list of points inside rectangle
     * @throws IllegalArgumentException if rect is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> pointStack = new Stack<>();
        range(pointStack, rect, root);
        return pointStack;
    }

    /**
     * Recursive function to handle points inside the rectangle.
     *
     * @param pointStack pointList to store points inside rectangle
     * @param rect       rectangle
     * @param n          node
     */
    private void range(Stack<Point2D> pointStack, RectHV rect, Node n) {
        if (n == null || !rect.intersects(n.rect)) return;
        if (rect.contains(n.p)) pointStack.push(n.p);
        range(pointStack, rect, n.lb);
        range(pointStack, rect, n.rt);
    }

    /**
     * Return the nearest neighbor in the set to point p;
     * return null if the set is empty.
     *
     * @param p point
     * @return The nearest neighbor of p; null if set is empty.
     * @throws IllegalArgumentException if p is null.
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(true, p, root, 2); // maximum square distance is 1^2+1^2=2
    }

    /**
     * Recursive function to return the nearest neighbour.
     *
     * @param isEven   True if parent node is in the even layer
     * @param p        point
     * @param n        parent node
     * @param distance the current shortest path to point.
     * @return The nearest neighbor of p; null if node is null or distance is larger.
     */
    private Point2D nearest(boolean isEven, Point2D p, Node n, double distance) {
        if (n == null) return null;
        if (n.rect.distanceSquaredTo(p) > distance) {
            return null;
        }
        Point2D result = null;
        double disc = n.p.distanceSquaredTo(p);
        if (disc > distance) {
            disc = distance;
        } else {
            result = n.p;
        }
        // check which subtree go first.
        if (isLeftBottom(isEven, p, n)) {
            Point2D lb = nearest(!isEven, p, n.lb, disc);
            if (lb == null) return min(result, nearest(!isEven, p, n.rt, disc), p);
            return min(result, min(lb, nearest(!isEven, p, n.rt, lb.distanceSquaredTo(p)), p), p);
        } else {
            Point2D rt = nearest(!isEven, p, n.rt, disc);
            if (rt == null) return min(result, nearest(!isEven, p, n.lb, disc), p);
            return min(result, min(rt, nearest(!isEven, p, n.lb, rt.distanceSquaredTo(p)), p), p);
        }
    }

    /**
     * Return point has shorter distance compared to p0
     *
     * @param p1 point 1
     * @param p2 point 2
     * @param p0 the pivot point
     * @return point has shorter distance or null if points are null
     */
    private Point2D min(Point2D p1, Point2D p2, Point2D p0) {
        if (p1 == null) return p2;
        if (p2 == null) return p1;
        if (p1.equals(p2)) return p1;
        if (less(p1, p2, p0.distanceToOrder())) return p1;
        else return p2;
    }

    /**
     * Unit test
     *
     * @param args give the number of the random points
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        KdTree kdSet = new KdTree();
        for (int i = 0; i < n; i++) {
            Point2D p;
            do {
                double x = StdRandom.uniformDouble();
                double y = StdRandom.uniformDouble();
                p = new Point2D(x, y);
            } while (kdSet.contains(p));
            kdSet.insert(p);
        }
        StdOut.println("kdSet size: " + kdSet.size());
        StdOut.println("kdSet isEmpty: " + kdSet.isEmpty());
        StdDraw.setPenRadius(0.01);
        kdSet.draw();
        StdDraw.show();

        /// Rect
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.7);
        StdOut.println(" Points in rect " + rect);
        for (Point2D p : kdSet.range(rect)) {
            StdOut.print(p + " ");
        }
        StdOut.println();
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.rectangle(0.3, 0.45, 0.2, 0.25);
        StdDraw.show();

        // Nearest Point
        Point2D p0 = new Point2D(0.2142, 0.57986);
        // Draw the pivot point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.point(0.2142, 0.57986);
        StdDraw.show();
        // Draw the line with the nearest point in the set.
        StdDraw.setPenRadius();
        Point2D p1 = kdSet.nearest(p0);
        StdDraw.line(p0.x(), p0.y(), p1.x(), p1.y());
    }
}
