import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private int numberOfSegments = 0;
    private List<Line> lines = new ArrayList<>();
    private List<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null!");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points[" + i + "] is null!");
            }
        }
        // Sorting points
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        MergeX.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Repeated points!");
            }
        }
        for (int p = 0; p < pointsCopy.length - 1; p++) {
            Point[] sub = Arrays.copyOfRange(pointsCopy, p + 1, pointsCopy.length);
            // Sorting slope
            MergeX.sort(sub, pointsCopy[p].slopeOrder());
            int count = 0;
            double slope = pointsCopy[p].slopeTo(sub[0]);
            for (int i = 1; i < sub.length; i++) {
                double current = pointsCopy[p].slopeTo(sub[i]);
                if (isLineSegmentAdded(current, sub[i])) {
                    count = 0;
                    continue;
                }
                if (slope == current) {
                    count++;
                } else {
                    if (count > 1) {
                        numberOfSegments++;
                        lineSegments.add(new LineSegment(pointsCopy[p], sub[i - 1]));
                        lines.add(new Line(slope, sub[i - 1]));
                    }
                    slope = pointsCopy[p].slopeTo(sub[i]);
                    count = 0;
                }
            }
            if (count > 1) {
                numberOfSegments++;
                lineSegments.add(new LineSegment(pointsCopy[p], sub[sub.length - 1]));
                lines.add(new Line(slope, sub[sub.length - 1]));
            }
        }
    }

    private boolean isLineSegmentAdded(double slope, Point p) {
        for (Line line : lines) {
            if (line.isSubSegments(slope, p))
                return true;
        }
        return false;
    }

    private static class Line {
        double slope;
        Point p;

        public Line(double slope, Point p) {
            this.slope = slope;
            this.p = p;
        }

        // if point pair in the same line
        public boolean isSubSegments(double slope, Point q) {
            return this.slope == slope && p == q;
        }

    }

    public int numberOfSegments() {
        return numberOfSegments;
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
