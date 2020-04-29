import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segs;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        Point[] pointsCopy = points.clone();
        if (points == null) throw new IllegalArgumentException("null input");
        checkForNulls(points);
        checkForDuplicates(points);

        Arrays.sort(pointsCopy);
        this.segs = new ArrayList<>();

        for (int i = 0; i < pointsCopy.length; i++) {
            Point referencePoint = pointsCopy[i];
            Point[] slopeOrderedPoints = pointsCopy.clone();
            Arrays.sort(slopeOrderedPoints, slopeOrderedPoints[i].slopeOrder());

            collectSegments(referencePoint, slopeOrderedPoints);
        }
    }

    private void collectSegments(Point referencePoint, Point[] slopeOrderedPoints) {
        int j = 0;
        while (j < slopeOrderedPoints.length) {
            ArrayDeque<Point> collinearPoints = new ArrayDeque<>();
            final double slope = referencePoint.slopeTo(slopeOrderedPoints[j]);

            while (j < slopeOrderedPoints.length) {
                double slope1 = referencePoint.slopeTo(slopeOrderedPoints[j]);
                if (!(slope1 == slope)) break;
                collinearPoints.add(slopeOrderedPoints[j]);
                j++;
            }

            if (collinearPoints.size() > 2 && isReferencePointAndCollinearInCorrectOrder(referencePoint, collinearPoints)) {
                segs.add(new LineSegment(referencePoint, collinearPoints.getLast()));
            }
        }
    }

    private void checkForNulls(Point[] points) {
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("null point");
        }
    }

    private void checkForDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point point1 = points[j];
                if (point.compareTo(point1) == 0) throw new IllegalArgumentException("duplicate points");
            }
        }
    }

    private boolean isReferencePointAndCollinearInCorrectOrder(Point referencePoint, ArrayDeque<Point> collinearPoints) {
        assert (isAscending(collinearPoints));
        // lower than the first in an ascending list - therefore the correct order from bottom to top.
        return referencePoint.compareTo(collinearPoints.peek()) < 0;
    }

    private boolean isAscending(ArrayDeque<Point> col) {
        ArrayDeque<Point> cpy = col.clone();
        if (cpy.isEmpty()) return true;

        Point prev = cpy.removeFirst();
        for (Point point : cpy) {
            if (cpy.isEmpty()) break;
            boolean smallerThanPrevious = point.compareTo(prev) < 0;
            if (smallerThanPrevious) return false;
            prev = cpy.removeFirst();
        }
        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segs.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segs.toArray(new LineSegment[0]);
    }
}
