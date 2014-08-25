/**
 * PointSET.java
 * 
 * @author George Titarenko 
 * 08/24/2014 
 * Dependencies: Dependencies: stdlib.jar algs4.jar
 * 
 *         Data type PointSET represents a set of points in the unit square.
 *         Implemented using a red-black BST (SET from algs4.jar, a customized build on
 *         java.util.TreeSet). Implementation supports insert() and contains()
 *         in time proportional to the logarithm of the number of points in the
 *         set in the worst case; supports nearest() and range() in time
 *         proportional to the number of points in the set.
 * 
 */

public class PointSET {

	private SET<Point2D> pointSet;

	public PointSET() // construct an empty set of points
	{
		pointSet = new SET<Point2D>();
	}

	public boolean isEmpty() // is the set empty?
	{
		return pointSet.isEmpty();
	}

	public int size() // number of points in the set
	{
		return pointSet.size();
	}

	public void insert(Point2D p) // add the point p to the set (if it is not
									// already in the set)
	{
		pointSet.add(p);
	}

	public boolean contains(Point2D p) // does the set contain the point p?
	{
		return pointSet.contains(p);
	}

	public void draw() // draw all of the points to standard draw
	{
		for (Point2D p : pointSet) {
			p.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect) // all points in the set that
												// are inside the rectangle
	{
		SET<Point2D> insidePoints = new SET<Point2D>();

		for (Point2D p : pointSet) {
			if (rect.contains(p))
				insidePoints.add(p);
		}
		return insidePoints;
	}

	public Point2D nearest(Point2D p) // a nearest neighbor in the set to p;
										// null if set is empty
	{
		double refDistance = 0;
		double minSquaredDistance = Double.POSITIVE_INFINITY;
		Point2D closestNeighbor = null;
		for (Point2D neighbor : pointSet) {
			if ((refDistance = p.distanceTo(neighbor)) < minSquaredDistance) {
				minSquaredDistance = refDistance;
				closestNeighbor = neighbor;
			}
		}
		return closestNeighbor;
	}
}
