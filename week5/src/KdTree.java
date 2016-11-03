import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 * Created by wind on 01/11/2016.
 */
public class KdTree {
	private int nNode;
	private Node root;

	// construct an empty set of points
	public KdTree() {
	}

	private static boolean shouldGoLeft(boolean useX, Point2D current, Point2D next) {
		assert !current.equals(next);
		if (useX)
			return next.x() < current.x();
		else
			return next.y() < current.y();
	}

	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set
	public int size() {
		return nNode;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		Node n = put(root, p, true);
		if (root == null)
			root = n;
	}

	private Node put(Node current, Point2D p, boolean useX) {
		if (current == null) {
			nNode++;
			return new Node(p, useX);
		} else {
			if (current.p.equals(p))
				return current;
			boolean left = shouldGoLeft(current.useX, current.p, p);
			if (left)
				current.left = put(current.left, p, !useX);
			else
				current.right = put(current.right, p, !useX);
			return current;
		}
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		Node current = root;
		while (current != null) {
			if (current.p.equals(p))
				return true;
			current = shouldGoLeft(current.useX, current.p, p) ? current.left : current.right;
		}
		return current != null;
	}

	// draw all points to standard draw
	public void draw() {
		if (root != null)
			draw(root);
	}

	private void draw(Node node) {
		assert node != null;
		node.p.draw();
		if (node.left != null)
			draw(node.left);
		if (node.right != null)
			draw(node.right);
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();
		SET<Point2D> result = new SET<>();
		range(rect, root, result);
		return result;
	}

	private void range(RectHV rect, Node root, SET<Point2D> a) {
		assert root != null;
		if (rect.contains(root.p))
			a.add(root.p);
		if (root.useX) {
			if (root.left != null && rect.xmin() < root.p.x())
				range(rect, root.left, a);
			if (root.right != null && rect.xmax() >= root.p.x())
				range(rect, root.right, a);
		} else {
			if (root.left != null && rect.ymin() < root.p.y())
				range(rect, root.left, a);
			if (root.right != null && rect.ymax() >= root.p.y())
				range(rect, root.right, a);
		}
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();
		if (root == null)
			return null;
		NearestStructure ns = new NearestStructure();
		ns.distance = root.p.distanceSquaredTo(p);
		ns.point = root.p;
		nearest(p, root, ns);
		assert ns != null;
		return ns.point;
	}

	private void nearest(Point2D p, Node root, NearestStructure current) {
		assert root != null;
		assert root.p != null;
		{ // update current.distance based on root.p
			double t = p.distanceSquaredTo(root.p);
			if (current.distance < t)
				current.distance = t;
		}
		if (root.useX) {
			double deltaX = p.x() - root.p.x();
			if (root.left != null && (deltaX < 0 || deltaX * deltaX < current.distance)) {
				nearest(p, root.left, current);
			}

			if ((root.right != null) && (deltaX >= 0 || deltaX * deltaX < current.distance)) {
				nearest(p, root.right, current);
			}
		} else {
			double deltaY = p.y() - root.p.y();
			if (root.left != null && (deltaY < 0 || deltaY * deltaY < current.distance)) {
				nearest(p, root.left, current);
			}

			if ((root.right != null) && (deltaY >= 0 || deltaY * deltaY < current.distance)) {
				nearest(p, root.right, current);
			}
		}
	}

	private static class NearestStructure {
		private Point2D point;
		private double distance = Double.POSITIVE_INFINITY;
	}

	private static class Node {
		private final Point2D p;
		private final boolean useX;
		private Node left, right;

		private Node(Point2D p, boolean useX) {
			assert p != null;
			this.p = p;
			this.useX = useX;
		}
	}
}
