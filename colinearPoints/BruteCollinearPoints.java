/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Bruteforce will return all 4 points line segments
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        nullPointCheck(points);
        int n = points.length;
        Point[] copy = Arrays.copyOf(points, n);
        MergeX.sort(copy);
        for (int p = 0; p < n; p++) {
            for (int q = p + 1; q < n; q++) {
                repeatedPointCheck(copy[p], copy[q]);
                double slopePq = copy[p].slopeTo(copy[q]);
                for (int r = q + 1; r < n - 1; r++) {
                    double slopePr = copy[p].slopeTo(copy[r]);
                    if (slopePq != slopePr) continue;
                    for (int s = r + 1; s < n; s++) {
                        double slopePs = copy[p].slopeTo(copy[s]);
                        if (slopePq != slopePs) continue;
                        // Found the Segments!
                        lineSegments.add(new LineSegment(copy[p], copy[s]));
                    }
                }
            }
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

    private void repeatedPointCheck(Point p, Point q) {
        if (p.compareTo(q) == 0)
            throw new IllegalArgumentException("Repeated point!");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
