import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
	private static final int NUMBER_OF_SLOPE_TO_CREATE_LINE_SEGMENTS = 3;
	private LineSegment[] segments;

	/**
	 * @param inPoints
	 */
	public FastCollinearPoints(Point[] inPoints) {
		// finds all line segments containing 4 or more points
		if (inPoints == null)
			throw new NullPointerException();
		Point[] points = inPoints.clone();
		ArrayList<LineSegment> lines = new ArrayList<>();
		Arrays.sort(points);

		// make sure all points are different compare to each other
		for (int a = 0; a < points.length - 1; a++) {
			if (points[a].compareTo(points[a + 1]) == 0)
				throw new IllegalArgumentException();
		}

		// for each points
		Point[] p2 = points.clone();
		for (Point p : p2) {
			// sort the other points with default sort then with slopeOrder
			points = p2.clone();
			Arrays.sort(points, p.slopeOrder());
			double lastSlope = Double.NEGATIVE_INFINITY;
			assert lastSlope == p.slopeTo(points[0]);
			int nCount = 1;
			for (int i = 1; i < points.length; i++) {
				double currentSlope = p.slopeTo(points[i]);
				if (currentSlope == lastSlope) {
					nCount++;
				} else {
					if (nCount >= NUMBER_OF_SLOPE_TO_CREATE_LINE_SEGMENTS) {
						assert isSorted(points, i - nCount, i);
						if (p.compareTo(points[i - nCount]) < 0)
							lines.add(new LineSegment(p, points[i-1]));
					}
					nCount = 1;
					lastSlope = currentSlope;
				}
			}
			int i = points.length;
			if (nCount >= NUMBER_OF_SLOPE_TO_CREATE_LINE_SEGMENTS) {
				assert isSorted(points, i - nCount, i);
				if (p.compareTo(points[i - nCount]) < 0)
					lines.add(new LineSegment(p, points[i-1]));
			}
		}
		segments = lines.toArray(new LineSegment[lines.size()]);
	}

	private static boolean isSorted(Point[] a, int start, int end) {
		for (int i = start; i < end - 1; i++) {
			if (a[i].compareTo(a[i + 1]) > 0)
				return false;
		}
		return true;
	}

	public int numberOfSegments() {
		return segments.length;
	}

	/**
	 * @return a clone of segments
	 */
	public LineSegment[] segments() {
		return segments.clone();
	}
}
