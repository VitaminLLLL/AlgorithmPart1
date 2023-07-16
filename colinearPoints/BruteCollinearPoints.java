import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private int end;
    private Point[] pointsCopy;
    private List<LineSegment> lineSegments = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null!");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points[" + i + "] is null!");
            }
        }
        pointsCopy = Arrays.copyOf(points, points.length);
        MergeX.sort(pointsCopy);
        for (int p = 0; p < pointsCopy.length; p++) {
            boolean found = false;
            for (int q = p + 1; q < pointsCopy.length; q++) {
                if (pointsCopy[p].compareTo(pointsCopy[q]) == 0) {
                    throw new IllegalArgumentException("Repeated points!");
                }
                double slopePq = pointsCopy[p].slopeTo(pointsCopy[q]);
                for (int r = q + 1; r < pointsCopy.length - 1; r++) {
                    double slopePr = pointsCopy[p].slopeTo(pointsCopy[r]);
                    if (slopePq != slopePr) {
                        continue;
                    }
                    for (int s = r + 1; s < pointsCopy.length; s++) {
                        double slopePs = pointsCopy[p].slopeTo(pointsCopy[s]);
                        if (slopePq != slopePs || isLineSegmentAdded(pointsCopy[p], pointsCopy[s])) {
                            continue;
                        }
                        // Found the Segments!
                        found = true;
                        end = s;
                    }
                    if (found) {
                        numberOfSegments++;
                        lineSegments.add(new LineSegment(pointsCopy[p], pointsCopy[end]));
                        lines.add(new Line(pointsCopy[p], pointsCopy[end]));
                        found = false;
                    }
                }
            }
        }
    }

    private boolean isLineSegmentAdded(Point p1, Point p2) {
        for (Line line : lines) {
            if (line.isSubSegments(p1, p2))
                return true;
        }
        return false;
    }

    private static class Line {
        Point p1;
        Point p2;

        public Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        // if point pair in the same line
        public boolean isSubSegments(Point q1, Point q2) {
            if (p2.compareTo(q1) == 0) {
                return q1.slopeTo(p1) == q1.slopeTo(p2);
            }
            return p1.slopeTo(q1) == p1.slopeTo(q2) && p2.slopeTo(q1) == p2.slopeTo(q2);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
