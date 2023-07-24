import edu.princeton.cs.algs4.*;

import java.util.Comparator;

/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 ******************************************************************************/

public class KdTree {
    Node root;

    public KdTree() {
        root = null;
    }

    /**
     * Private Node data type
     */
    private static class Node {
        private final Point2D p;     // the point
        private final RectHV rect;   // the axis-aligned rectangle corresponding to this node
        private Node lb;       // the left/bottom subtree
        private Node rt;       // the right/top subtree

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
        return size(root);
    }

    /**
     * Recursive function to find the size of the parent node.
     *
     * @param node parent node.
     * @return size of the parent node. return 0 if node is null.
     */
    private int size(Node node) {
        if (node == null) return 0;
        return size(node.lb) + size(node.rt) + 1;
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
            return;
        }
        Node n = root;
        int layer = 0;
        while (true) {
            if (n.p.equals(p)) return;
            if (isLeftBottom(layer, p, n)) {
                if (n.lb != null) n = n.lb;
                else {
                    createLeftBottomNode(layer, p, n);
                    return;
                }
            } else {
                if (n.rt != null) n = n.rt;
                else {
                    createRightTopNode(layer, p, n);
                    return;
                }
            }
            layer++;
        }
    }

    /**
     * Create the new left or bottom node
     *
     * @param layer parent node layer
     * @param p     point
     * @param n     parent node
     */
    private void createLeftBottomNode(int layer, Point2D p, Node n) {
        RectHV rect;
        if (isEvenLayer(layer)) // Left node
            rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        else  // Bottom node
            rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
        n.lb = new Node(p, rect);
    }

    /**
     * Create the new right or top node
     *
     * @param layer parent node layer
     * @param p     point
     * @param n     parent node
     */
    private void createRightTopNode(int layer, Point2D p, Node n) {
        RectHV rect;
        if (isEvenLayer(layer)) // Right node
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
     * @param layer parent node layer
     * @param p     point
     * @param n     parent node
     * @return True if child should be in left or bottom
     */
    private boolean isLeftBottom(int layer, Point2D p, Node n) {
        if (isEvenLayer(layer))
            return less(p, n.p, Point2D.X_ORDER);
        else
            return less(p, n.p, Point2D.Y_ORDER);
    }

    /**
     * Return if node is in even layer
     *
     * @param layer node layer
     * @return True if node is in even layer
     */
    private boolean isEvenLayer(int layer) {
        return layer % 2 == 0;
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
        Node n = root;
        int layer = 0;
        while (n != null) {
            if (n.p.equals(p)) return true;
            if (isLeftBottom(layer, p, n))
                n = n.lb;
            else
                n = n.rt;
            layer++;
        }
        return false;
    }

    /**
     * Draw all the points into {@link StdDraw#point(double, double)}
     */
    public void draw() {
        draw(root);
    }

    /**
     * Recursive draw point n.lb -> n ->n.rt
     *
     * @param n Node
     */
    private void draw(Node n) {
        if (n == null) return;
        draw(n.lb);
        StdDraw.point(n.p.x(), n.p.y());
        draw(n.rt);
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
        // TODO

        return null;
    }

    private Point2D nearest(Point2D p, Node n, double distance) {
        if (n == null) return null;
        if (n.rect.distanceSquaredTo(p) >= distance) {
            return null;
        }
        double disc = n.p.distanceSquaredTo(p);
        if (disc < distance) distance = disc;
        return null;
    }

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
    }
}
