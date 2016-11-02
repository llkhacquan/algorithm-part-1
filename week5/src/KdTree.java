import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 * Created by wind on 01/11/2016.
 */
public class KdTree {
	private final SET<Point2D> pointsSet;

	// construct an empty set of points
	public KdTree() {
		pointsSet = new SET<>();
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
	}

	// is the set empty?
	public boolean isEmpty() {
		return pointsSet.isEmpty();
	}

	// number of points in the set
	public int size() {
		return pointsSet.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		pointsSet.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		return pointsSet.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p : pointsSet) {
			p.draw();
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();
		SET<Point2D> result = new SET<>();
		for (Point2D p : pointsSet) {
			if (rect.contains(p))
				result.add(p);
		}
		return result;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		if (pointsSet.size() == 0)
			return null;
		double minDistance = 10;
		Point2D result = null;
		for (Point2D p2 : pointsSet) {
			double currentDistance = p.distanceSquaredTo(p2);
			if (currentDistance < minDistance) {
				result = p2;
				minDistance = currentDistance;
			}
		}
		assert result != null;
		return result;
	}
}
