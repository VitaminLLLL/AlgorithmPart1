/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * PointSET, Bruteforce implementation of unit square points
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

public class PointSET {
    SET<Point2D> sets;

    public PointSET() {
        sets = new SET<>();
    }

    /**
     * Return True if PointSet is empty
     *
     * @return True if set is empty.
     */
    public boolean isEmpty() {
        return sets.isEmpty();
    }

    /**
     * Returns number of points in the PointSET
     *
     * @return number of points - int
     */
    public int size() {
        return sets.size();
    }

    /**
     * Insert the point into PointSet
     *
     * @param p point
     * @throws IllegalArgumentException if point is null
     */
    public void insert(Point2D p) {
        sets.add(p);
    }

    /**
     * Check if set contains point
     *
     * @param p point
     * @return True if set contains point
     * @throws IllegalArgumentException if point is null
     */
    public boolean contains(Point2D p) {
        return sets.contains(p);
    }

    /**
     * Draw all the points into {@link StdDraw#point(double, double)}
     */
    public void draw() {
        for (Point2D p : sets) {
            StdDraw.point(p.x(), p.y());
        }
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
        for (Point2D p : sets) {
            if (isPointInRect(p, rect)) pointStack.push(p);
        }
        return pointStack;
    }

    /**
     * Check if the point is inside the rectangle.
     *
     * @param p    point
     * @param rect rectangle
     * @return True if the point is inside rectangle.
     */
    private boolean isPointInRect(Point2D p, RectHV rect) {
        return p.x() <= rect.xmax() && p.x() >= rect.xmin() &&
                p.y() <= rect.ymax() && p.y() >= rect.ymin();
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
        if (sets.isEmpty()) return null;
        Point2D nearestP = sets.min();
        double shortestDistance = nearestP.distanceTo(p);
        for (Point2D P : sets) {
            double distance = P.distanceTo(p);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestP = P;
            }
        }
        return nearestP;
    }

    /**
     * Unit testing
     *
     * @param args give the number of the random points
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        PointSET pointSET = new PointSET();
        for (int i = 0; i < n; i++) {
            Point2D p;
            do {
                double x = StdRandom.uniformDouble();
                double y = StdRandom.uniformDouble();
                p = new Point2D(x, y);
            } while (pointSET.contains(p));
            pointSET.insert(p);
        }
        StdOut.println("PointSet size: " + pointSET.size());
        StdOut.println("PointSet isEmpty: " + pointSET.isEmpty());
        StdDraw.setPenRadius(0.01);
        pointSET.draw();
        StdDraw.show();

        /// Rect
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.7);
        StdOut.println(" Points in rect " + rect);
        for (Point2D p : pointSET.range(rect)) {
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
        Point2D p1 = pointSET.nearest(p0);
        StdDraw.line(p0.x(), p0.y(), p1.x(), p1.y());
    }
}
