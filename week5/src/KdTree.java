import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by wind on 01/11/2016.
 */
public class KdTree {
	private int nNode = 0;
	private Node root = null;

    /**
     * construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * decide the next point should be on the left or on the right of current node. current node use useX
     */
    private static boolean shouldGoLeft(boolean useX, Point2D current, Point2D next) {
        assert !current.equals(next);
        if (useX) {
            return next.x() < current.x();
        } else {
            return next.y() < current.y();
        }
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * number of points in the set
     */
    public int size() {
        return nNode;
    }

    /**
     * add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
		if (root == null) {
			nNode++;
			root = new Node(p, true, new RectHV(0, 0, 1, 1));
		} else {
			put(root, p);
		}
	}

	/**
	 * recursively find the position of new point, then create a Node for it
	 */
	private void put(Node root, Point2D p) {
		assert root != null;
		assert p != null;
		if (root.p.equals(p)) {
			return;
		}
		nNode++;
		boolean left = shouldGoLeft(root.useX, root.p, p);
		if (left) {
			if (root.left == null) {
				root.left = createNewNode(root, true, p);
			} else {
				put(root.left, p);
			}
		} else {
			if (root.right == null) {
				root.right = createNewNode(root, false, p);
			} else {
				put(root.right, p);
			}
		}
	}

	private static Node createNewNode(Node root, boolean left, Point2D p) {
		RectHV r = root.rect;
		if (left) {
			if (root.useX) {
				return new Node(p, false, new RectHV(r.xmin(), r.ymin(), root.p.x(), r.ymax()));
			} else {
				return new Node(p, true, new RectHV(r.xmin(), r.ymin(), r.xmax(), root.p.y()));
			}
		} else {
			if (root.useX) {
				return new Node(p, false, new RectHV(root.p.x(), r.ymin(), r.xmax(), r.ymax()));
			} else {
				return new Node(p, true, new RectHV(r.xmin(), root.p.y(), r.xmax(), r.ymax()));
			}
		}
	}

    /**
     * does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        Node current = root;
        while (current != null) {
            if (current.p.equals(p)) {
                return true;
            }
            current = shouldGoLeft(current.useX, current.p, p) ? current.left : current.right;
        }
        return false;
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        if (root != null) {
			draw(root);
		}
	}

    /**
     * draw the current point in node, then recursively draw left and right nodes
	 */
	private void draw(Node node) {
		assert node != null;
		StdDraw.setPenColor(StdDraw.BLACK);
		node.p.draw();
		if (node.useX) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
		}
		if (node.left != null) {
			draw(node.left);
		}
		if (node.right != null) {
			draw(node.right);
		}
	}

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        SET<Point2D> result = new SET<>();
        if (root != null) {
            range(rect, root, result);
        }
        return result;
    }

    /**
     * check if the point of root lies in the rect and
     * recursively find on the left and right sub-tree for points lie in rect
     */
    private void range(RectHV rect, Node root, SET<Point2D> a) {
        assert root != null;
        if (rect.contains(root.p)) {
            a.add(root.p);
        }
        if (root.useX) {
            if (root.left != null && rect.xmin() < root.p.x()) {
                range(rect, root.left, a);
            }
            if (root.right != null && rect.xmax() >= root.p.x()) {
                range(rect, root.right, a);
            }
        } else {
            if (root.left != null && rect.ymin() < root.p.y()) {
                range(rect, root.left, a);
            }
            if (root.right != null && rect.ymax() >= root.p.y()) {
                range(rect, root.right, a);
            }
        }
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (root == null) {
            return null;
        }
        NearestStructure ns = new NearestStructure();
        ns.distance = root.p.distanceSquaredTo(p);
        ns.point = root.p;
        nearest(p, root, ns);
        return ns.point;
    }

    /**
     * given a reference point, current node, current nearest point
     * recursively find a nearer point in the root and in the left and right sub-trees
     */
    private void nearest(Point2D p, Node root, NearestStructure current) {
        assert root != null;
        {
            double t = p.distanceSquaredTo(root.p);
            if (current.distance > t) {
                current.distance = t;
                current.point = root.p;
            }
        }
        if (root.useX) {
            if (p.x() < root.p.x()) {
                // we search for left first
				search(p, root.left, root.right, current);
			} else {
				// we search for right first
				search(p, root.right, root.left, current);
			}
		} else {
			if (p.y() < root.p.y()) {
				// we search for left first
				search(p, root.left, root.right, current);
			} else {
				// we search for right first
				search(p, root.right, root.left, current);
			}
		}
	}

	private void search(Point2D p, Node first, Node second, NearestStructure current) {
		if (first != null && first.rect.distanceSquaredTo(p) <= current.distance) {
			nearest(p, first, current);
		}
		if (second != null && second.rect.distanceSquaredTo(p) <= current.distance) {
			nearest(p, second, current);
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
		private final RectHV rect;

		private Node(Point2D p, boolean useX, RectHV rect) {
			assert p != null;
			this.p = p;
			this.useX = useX;
			this.rect = rect;
		}
	}
}
