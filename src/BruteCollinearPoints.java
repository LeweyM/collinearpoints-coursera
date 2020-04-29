import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> segs;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        Point[] pointsCopy = points.clone();

        if (pointsCopy == null) throw new IllegalArgumentException("null input");
        checkForDuplicates(pointsCopy);
        checkForNulls(pointsCopy);

        segs = new ArrayList<>();
        Arrays.sort(pointsCopy);

        for (int first = 0; first < pointsCopy.length -3; first++) {
            for (int second = first + 1; second < pointsCopy.length -2; second++) {
                double firstToSecond = pointsCopy[first].slopeTo(pointsCopy[second]);
                for (int third = second + 1; third < pointsCopy.length -1; third++) {
                    double secondToThird = pointsCopy[second].slopeTo(pointsCopy[third]);
                    if (firstToSecond == secondToThird) {
                        for (int fourth = third + 1; fourth < pointsCopy.length; fourth++) {
                            double thirdToFourth = pointsCopy[third].slopeTo(pointsCopy[fourth]);
                            if (secondToThird == thirdToFourth) {
                                segs.add(new LineSegment(pointsCopy[first], pointsCopy[fourth]));
                            }

                        }
                    }
                }
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
            for (int j = i+1; j < points.length; j++) {
                Point point1 = points[j];
                if (point.compareTo(point1) == 0) throw new IllegalArgumentException("duplicate points");
            }
        }
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