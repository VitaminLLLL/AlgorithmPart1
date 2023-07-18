/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Fast, quite close in the initial design, learn the idea from the post.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        nullPointCheck(points);
        int n = points.length;
        Point[] copy = Arrays.copyOf(points, n);
        MergeX.sort(copy);
        repeatedPointCheck(copy);
        if (n < 4) return;
        for (int i = 0; i < n; i++) {
            Point p = copy[i];
            Point[] sub = Arrays.copyOf(copy, n);
            // Sorting slope
            MergeX.sort(sub, p.slopeOrder());
            int count = 0;
            Point p1 = sub[1];
            double slope = p.slopeTo(p1);
            for (int j = 2; j < n; j++) {
                double current = p.slopeTo(sub[j]);
                if (slope == current) {
                    count++;
                } else {
                    if (count > 1 && p.compareTo(p1) < 0)
                        lineSegments.add(new LineSegment(p, sub[j - 1]));
                    count = 0;
                    slope = current;
                    p1 = sub[j];
                }
            }
            if (count > 1 && p.compareTo(p1) < 0)
                lineSegments.add(new LineSegment(p, sub[sub.length - 1]));
        }
    }

    private void nullPointCheck(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null!");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points[" + i + "] is null!");
            }
        }
    }

    private void repeatedPointCheck(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Repeated points!");
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
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
