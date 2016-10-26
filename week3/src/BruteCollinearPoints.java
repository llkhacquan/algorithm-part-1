import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
	private LineSegment[] segments;


	/**
	 * required: input of BruteCollinearPoints never has 5 or more collinear
	 * points
	 *
	 * @param inPoints
	 */
	public BruteCollinearPoints(Point[] inPoints) {
		// finds all line segments containing 4 points
		if (inPoints == null)
			throw new NullPointerException();
		Point[] points = inPoints.clone();
		LinkedList<LineSegment> lines = new LinkedList<>();
		Arrays.sort(points);

		// make sure all points are different compare to each other
		for (int a = 0; a < points.length - 1; a++)
			if (points[a].compareTo(points[a + 1]) == 0)
				throw new IllegalArgumentException();

		// loop for all each 4 points
		for (int a = 0; a < points.length; a++)
			for (int b = a + 1; b < points.length; b++)
				for (int c = b + 1; c < points.length; c++)
					for (int d = c + 1; d < points.length; d++)

						if (isInALine(points, a, b, c, d)) {
							// a and d are always start and end points of this
							// line segments
							// because points are sorted
							lines.add(new LineSegment(points[a], points[d]));
						}
		segments = lines.toArray(new LineSegment[0]);
	}

	private static boolean isInALine(Point[] points, int a, int b, int c, int d) {
		double s1 = points[a].slopeTo(points[b]);
		double s2 = points[a].slopeTo(points[c]);
		if (s1 != s2)
			return false;
		double s3 = points[a].slopeTo(points[d]);
		return s1 == s3;
	}

	public int numberOfSegments() {
		return segments.length;
	}

	public LineSegment[] segments() {
		// the line segments
		return segments.clone();
	}
}
