import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by wind on 01/11/2016.
 */
public class KdTree {
    private int nNode;
    private Node root;

    /**
     * construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * is the set empty?
     *
     * @return
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * number of points in the set
     *
     * @return
     */
    public int size() {
        return nNode;
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        Node n = put(root, p, true);
        if (root == null) {
            root = n;
        }
    }

    /**
     * recursively find the position of new point, then create a Node for it
     *
     * @param current
     * @param p
     * @param useX
     * @return new node contains the point
     */
    private Node put(Node current, Point2D p, boolean useX) {
        if (current == null) {
            nNode++;
            return new Node(p, useX);
        } else {
            if (current.p.equals(p)) {
                return current;
            }
            boolean left = shouldGoLeft(current.useX, current.p, p);
            if (left) {
                current.left = put(current.left, p, !useX);
            } else {
                current.right = put(current.right, p, !useX);
            }
            return current;
        }
    }

    /**
     * decide the next point should be on the left or on the right of current node. current node use useX
     *
     * @param useX
     * @param current
     * @param next
     * @return
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
     * does the set contain point p?
     *
     * @param p
     * @return
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
            draw(root, 0, 1, 0, 1);
        }
    }

    /**
     * draw the current point in node, then recursively draw left and right nodes
     *
     * @param node
     */
    private void draw(Node node, double minX, double maxX, double minY, double maxY) {
        assert node != null;
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        if (node.useX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), minY, node.p.x(), maxY);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, node.p.y(), maxX, node.p.y());
        }
        if (node.left != null) {
            if (node.useX) {
                draw(node.left, minX, node.p.x(), minY, maxY);
            } else {
                draw(node.left, minX, maxX, minY, node.p.y());
            }
        }
        if (node.right != null) {
            if (node.useX) {
                draw(node.right, node.p.x(), maxX, minY, maxY);
            } else {
                draw(node.right, minX, maxX, node.p.y(), maxY);
            }
        }
    }

    /**
     * @param rect
     * @return all points that are inside the rectangle
     */
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
     *
     * @param rect
     * @param root
     * @param a
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
     *
     * @param p
     * @return
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
        assert ns != null;
        return ns.point;
    }

    /**
     * given a reference point, current node, current nearest point
     * recursively find a nearer point in the root and in the left and right sub-trees
     *
     * @param p
     * @param root
     * @param current
     */
    private void nearest(Point2D p, Node root, NearestStructure current) {
        assert root != null;
        assert root.p != null;
        {
            double t = p.distanceSquaredTo(root.p);
            if (current.distance > t) {
                current.distance = t;
                current.point = root.p;
            }
        }
        if (root.useX) {
            double t = (root.p.x() - p.x()) * (root.p.x() - p.x());
            if (p.x() < root.p.x()) {
                // we search for left first
                search(p, root.left, root.right, current, t);
            } else {
                // we search for right first
                search(p, root.right, root.left, current, t);
            }
        } else {
            double t = (root.p.y() - p.y()) * (root.p.y() - p.y());
            if (p.y() < root.p.y()) {
                // we search for left first
                search(p, root.left, root.right, current, t);
            } else {
                // we search for right first
                search(p, root.right, root.left, current, t);
            }
        }
    }

    private void search(Point2D p, Node first, Node second, NearestStructure current, double delta) {
        if (first != null) {
            nearest(p, first, current);
        }
        if (second != null && delta < current.distance) {
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

        private Node(Point2D p, boolean useX) {
            assert p != null;
            this.p = p;
            this.useX = useX;
        }
    }
}
