/**
 * KdTree.java
 * 
 * @author George Titarenko 08/24/2014 Dependencies: Dependencies: stdlib.jar
 *         algs4.jar
 * 
 *         Data type KdTree uses a 2d-tree. A 2d-tree is a generalization of a
 *         BST to two-dimensional keys. The idea is to build a BST with points
 *         in the nodes, using the x- and y-coordinates of the points as keys in
 *         strictly alternating sequence.
 * 
 *         The prime advantage of a 2d-tree over a BST is that it supports
 *         efficient implementation of range search and nearest neighbor search.
 *         Each node corresponds to an axis-aligned rectangle in the unit
 *         square, which encloses all of the points in its subtree. The root
 *         corresponds to the unit square; the left and right children of the
 *         root corresponds to the two rectangles split by the x-coordinate of
 *         the point at the root; and so forth.
 */

public class KdTree {

	private Node rootNode;
	private int size;
	private Point2D closestNeighbor;
	private double minSquaredDistance; // use squared to skip expensive
										// sqrt()call

	private static class Node {
		private Point2D p; 					// the point
		private RectHV rect; 				// the axis-aligned rectangle corresponding to this
											// node
		private Node lb; 					// the left/bottom subtree
		private Node rt; 					// the right/top subtree
		private boolean isLevelVertical;    // true for vertical false for horizontal

		public Node(Point2D p, boolean isLevelVertical, RectHV thatRect) {
			this.p = p;
			this.isLevelVertical = isLevelVertical;
			if (thatRect == null)
				this.rect = new RectHV(0, 0, 1, 1);
			else
				this.rect = thatRect;
		}

		private void drawNodeLine() {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);
			p.draw();
			Point2D p1;
			Point2D p2;

			if (isLevelVertical) {
				StdDraw.setPenColor(StdDraw.RED);
				p1 = new Point2D(p.x(), rect.ymin());
				p2 = new Point2D(p.x(), rect.ymax());
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				p1 = new Point2D(rect.xmin(), p.y());
				p2 = new Point2D(rect.xmax(), p.y());
			}

			StdDraw.setPenRadius();
			p1.drawTo(p2);
		}
	}

	public KdTree() // construct an empty set of points
	{
		rootNode = null;
		this.size = 0;
	}

	public boolean isEmpty() // is the set empty?
	{
		return this.size == 0;
	}

	public int size() // number of points in the set
	{
		return this.size;
	}

	public void insert(Point2D p) // add the point p to the set (if it is not
									// already in the set)
	{
		rootNode = insert(rootNode, p, true, null, null);
	}

	/**
	 * Recursively build a 2D Tree
	 * 
	 * @param node - New node to be inserted
	 * @param p - Point of the new Node
	 * @param orientation - Orientation of the new Node
	 * @param parentRect - Rect assosiated with the parent node
	 * @param parent - Point of the parent Node
	 * @return - Newly added node
	 */
	private Node insert(Node node, Point2D p, boolean orientation,
			RectHV parentRect, Point2D parent) {
		if (node == null) {
			if (parentRect == null) {
				size++;
				return new Node(p, orientation, null);
			} else {
				RectHV newRect;
				if (orientation) {
					if (p.y() < parent.y()) {
						newRect = new RectHV(parentRect.xmin(),
								parentRect.ymin(), parentRect.xmax(),
								parent.y());
					} else {
						newRect = new RectHV(parentRect.xmin(), parent.y(),
								parentRect.xmax(), parentRect.ymax());
					}
				} else {
					if (p.x() < parent.x()) {
						newRect = new RectHV(parentRect.xmin(),
								parentRect.ymin(), parent.x(),
								parentRect.ymax());
					} else {
						newRect = new RectHV(parent.x(), parentRect.ymin(),
								parentRect.xmax(), parentRect.ymax());
					}
				}
				size++;
				return new Node(p, orientation, newRect);
			}
		}

		if (node.p.equals(p))  // Ignore Dups..
			return node;
		if (node.isLevelVertical) {
			if (p.x() < node.p.x())
				node.lb = insert(node.lb, p, !node.isLevelVertical, node.rect, node.p);
			else
				node.rt = insert(node.rt, p, !node.isLevelVertical, node.rect, node.p);
		} else {
			if (p.y() < node.p.y())
				node.lb = insert(node.lb, p, !node.isLevelVertical, node.rect, node.p);
			else
				node.rt = insert(node.rt, p, !node.isLevelVertical, node.rect, node.p);
		}

		return node;
	}

	public boolean contains(Point2D p) // does the set contain the point p?
	{
		return getPoint(rootNode, p) != null;
	}

	/**
	 * Recursively searches 2D three for a particular Point, returns it if
	 * found, null otherwise lg N performance
	 * 
	 * @param node
	 * @param p
	 * @return
	 */
	private Point2D getPoint(Node node, Point2D p) {

		if (node == null)
			return null;
		if (node.p.equals(p) == true) {
			return p;
		}
		if (node.isLevelVertical) {
			if (p.x() < node.p.x())
				return getPoint(node.lb, p);
			else
				return getPoint(node.rt, p);
		} else {
			if (p.y() < node.p.y())
				return getPoint(node.lb, p);
			else
				return getPoint(node.rt, p);
		}
	}

	public void draw() 					// draw all of the points + lines to standard draw
	{
		drawNode(rootNode);
	}

	/**
	 * Recursively draws points and associated lines for the 2D tree
	 * 
	 * @param node
	 */
	private void drawNode(Node node) {
		if (node == null)
			return;
		node.drawNodeLine();
		drawNode(node.lb);
		drawNode(node.rt);
	}

	public Iterable<Point2D> range(RectHV rect) 		// all points in the set that
														// are inside the rectangle
	{
		SET<Point2D> rangeSet = new SET<Point2D>();
		rangePoints(rootNode, rect, rangeSet);
		return rangeSet;
	}

	/**
	 * Recursive effective search for all points within a given range
	 * (rectangle).
	 * 
	 * @param node
	 * @param rect
	 * @param rangeSet
	 */
	private void rangePoints(Node node, RectHV rect, SET<Point2D> rangeSet) {
		if (node == null)
			return;
		if (rect.intersects(node.rect)) {
			if (rect.contains(node.p))
				rangeSet.add(node.p);
			rangePoints(node.lb, rect, rangeSet);
			rangePoints(node.rt, rect, rangeSet);
		}
	}

	public Point2D nearest(Point2D p) 			// a nearest neighbor in the set to p;
												// null if set is empty
	{
		minSquaredDistance = Double.POSITIVE_INFINITY;
		closestNeighbor = null;
		getClosestNeighbor(p, rootNode);

		return closestNeighbor;
	}

	/**
	 * Recursive effective search implementation for the Point in the 2D tree
	 * which is the CLOSEST to a given query Point. Eliminates from the search
	 * path subtrees with guaranteed no-closer points.
	 * When there are two possible subtrees to go down, we always choose the subtree 
	 * that is on the same side of the splitting line as the query point 
	 * as the first subtree to explore the closest point found while exploring the first 
	 * subtree may enable pruning of the second subtree.
	 * 
	 * @param p
	 * @param node
	 */
	private void getClosestNeighbor(Point2D p, Node node) {
		if (node == null)
			return;

		double currentDistanceTosqRoot;

		if (node.rect.distanceSquaredTo(p) < minSquaredDistance) {
			if ((currentDistanceTosqRoot = node.p.distanceSquaredTo(p)) < minSquaredDistance) {
				minSquaredDistance = currentDistanceTosqRoot;
				closestNeighbor = node.p;
			}

			if (node.isLevelVertical) {
				if (p.x() < node.p.x()) {
					getClosestNeighbor(p, node.lb);
					getClosestNeighbor(p, node.rt);
				} else {
					getClosestNeighbor(p, node.rt);
					getClosestNeighbor(p, node.lb);
				}
			} else {
				if (p.y() < node.p.y()) {
					getClosestNeighbor(p, node.lb);
					getClosestNeighbor(p, node.rt);
				} else {
					getClosestNeighbor(p, node.rt);
					getClosestNeighbor(p, node.lb);
				}
			}
		}
	}
}
