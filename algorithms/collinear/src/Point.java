/*************************************************************************
 * Name: George Titarenko
 * Date: 07/25/2014
 * Compilation:  javac Point.java
 * Dependencies: StdDraw.java
 * Description: An immutable data type for points in the plane.
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

	private final int x; // x coordinate
	private final int y; // y coordinate

	// Comparator implementation to use with Java system sort.
	private class SlopeOrder implements Comparator<Point> {
		public int compare(Point o1, Point o2) {
			if (slopeTo(o1) - slopeTo(o2) < 0)
				return -1;
			else if (slopeTo(o1) - slopeTo(o2) > 0)
				return 1;
			else
				return 0;
		}
	}

	// create the point (x, y)
	public Point(int x, int y) {
		/* DO NOT MODIFY */
		this.x = x;
		this.y = y;
	}

	// plot this point to standard drawing
	public void draw() {
		/* DO NOT MODIFY */
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		/* DO NOT MODIFY */
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {
		if (that.y == this.y && that.x == this.x)
			return Double.NEGATIVE_INFINITY;
		else if (that.y == this.y)
			return new Double(0);
		else if (that.x == this.x)
			return Double.POSITIVE_INFINITY;
		else
			return (double) (that.y - this.y) / (that.x - this.x);
	}

	// is this point lexicographically smaller than that one?
	// comparing y-coordinates and breaking ties by x-coordinates
	// This is a Comparable Interface contract.
	public int compareTo(Point that) {
		if (that.y == this.y)
			return this.x - that.x;
		else
			return this.y - that.y;
	}

	// return string representation of this point
	public String toString() {
		/* DO NOT MODIFY */
		return "(" + x + ", " + y + ")";
	}
}
