/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/28/2014 
 * Compilation: javac Fast.java 
 * Dependencies: stdlib.jar 
 * This is an implementation of a pattern recognition of collinear points on a given plane; all 4 or more
 * colliner points will have their coords printed. The line connected them is drawn. 
 * NOTE: This implementation takes care of NOT printing permutations and NOT printing subsets (where 5 or more 
 * collinear points discovered, we only print a segment of all 5 and not any 4-points subsegments.
 ****************************************************************************/

import java.util.Arrays;

public class Fast {
	/**
	 * @param args
	 *            Reads the input file, the first integer is the number of
	 *            points, following by the pairs of x, y coords.
	 */
	public static void main(String[] args) {
		// re-scale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);
		StdDraw.setPenRadius(0.01); // make the points a bit larger // read in
									// the input

		In in = new In(args[0]);
		int N = in.readInt();

		// Create an array of points and populate coords
		Point[] fast = new Point[N];
		for (int i = 0; i < N; i++) {
			if (in.isEmpty())
				throw new java.util.InputMismatchException(
						"Not enough points provided!\n");
			int x = in.readInt();
			int y = in.readInt();
			Point p = new Point(x, y);
			fast[i] = p;
			p.draw();
			StdDraw.show();
		}

		// Iterate thru all input points
		for (int ij = 0; ij < N; ij++) {
			// Create a reference copy to sort..
			Point[] refManyPoints = new Point[N];

			for (int z = 0; z < N; z++)
				refManyPoints[z] = fast[z];

			// Sort by slopes with regards to the reference point for each
			// iteration
			Arrays.sort(refManyPoints, fast[ij].SLOPE_ORDER);

			// Check if 3 or more have the same slope wrt the reference point
			for (int j = 1; j + 2 < refManyPoints.length; j++) {
				if ((refManyPoints[j].slopeTo(fast[ij])     == refManyPoints[j + 1].slopeTo(fast[ij]))
			   	&&  (refManyPoints[j + 1].slopeTo(fast[ij]) == refManyPoints[j + 2].slopeTo(fast[ij]))) 
				{
					// check if there is more than 3 with the same slope..
					int a = j + 3;
					int count = 0;
					while ((a < refManyPoints.length)
							&& refManyPoints[j].slopeTo(fast[ij]) == refManyPoints[a++].slopeTo(fast[ij]))
						count++;

					// Copy all found collinears into new array
					Point[] fastPrint = new Point[4 + count];

					// First element is always the reference point used
					fastPrint[0] = fast[ij];
					for (int b = 1, d = j; b < fastPrint.length; b++, d++)
						fastPrint[b] = refManyPoints[d];

					j+=count;
					j++;
					// Check to get rid of permutations and subsets - only those
					// where 1st point (reference)
					// is lexicographically smaller than the others is the one
					// we keep, ignore the rest
					boolean anotherFlag = false;
					for (int m = 1; m < fastPrint.length; m++) {
						if ((fastPrint[0].compareTo(fastPrint[m]) < 0)) {
							anotherFlag = true;
							continue;
						} else {
							anotherFlag = false;
							break;
						}
					}
					// Print the ones we kept, sorting first lexicographically
					if (anotherFlag) {
						int c = 0;
						Arrays.sort(fastPrint);
						while (c < fastPrint.length) {
							if (c != 0)
								System.out.print(" -> ");
							System.out.print(fastPrint[c++].toString());
						}
						System.out.print("\n");
						fastPrint[0].drawTo(fastPrint[fastPrint.length - 1]);
					}

					fastPrint = null;
				}
			}
		}

		// display to screen all at once
		StdDraw.show();

		// reset the pen radius
		StdDraw.setPenRadius();
	}
}
