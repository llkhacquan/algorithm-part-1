import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Main {
	public static void main(String[] args) {
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));
		// read the n points from a file
		In in = new In("week3/collinear/input8.txt");
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
		FastCollinearPoints fast = new FastCollinearPoints(points);
		StdOut.println("Brute:" + collinear.numberOfSegments());
		StdOut.println("Fast:" + fast.numberOfSegments());
		int nSegments = Math.min(collinear.numberOfSegments(), fast.numberOfSegments());

		LineSegment[] fastSegment = fast.segments();
		for (int i = 0; i < fastSegment.length; i++) {
			StdOut.println(fastSegment[i]);
			fastSegment[i].draw();
		}

		StdDraw.show();
	}
}
