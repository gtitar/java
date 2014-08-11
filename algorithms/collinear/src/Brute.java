/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/25/2014 
 * Compilation: javac Brute.java 
 * Dependencies: stdlib.jar 
 * This is a "Brute" implementation of a pattern recognition of collinear points on a given plane; all 4 
 * colliner points will have their coords printed. The line connected them is drawn. 
 * NOTE: This implementation is not efficiens as it involves 4x nested for loop, killing performance on big-size 
 * inputs. 
 ****************************************************************************/

import java.util.Arrays;

public class Brute {
	/**
	 * @param args
	 *            Reads the input file, the first integer is the number of
	 *            points, following by te pairst of x, y coords.
	 */
	public static void main(String[] args) {

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);
		StdDraw.setPenRadius(0.01); // make the points a bit larger // read in
									// the input

		In in = new In(args[0]);
		int N = in.readInt();

		// Create an array of points and populate coords
		Point[] brute = new Point[N];
		for (int i = 0; i < N; i++) {
			if (in.isEmpty())
				throw new java.util.InputMismatchException("Not enough points provided!\n");
			int x = in.readInt();
			int y = in.readInt();
			Point p = new Point(x, y);
			brute[i] = p;
			p.draw();
			StdDraw.show();
		}

		// Brute-force search.... Can exit after comparing 1-2 and 2-3, but it
		// does not matter from
		// overall performance cost as it is still 4x nested for loop..
		for (int p = 0; p < N; p++) {
			for (int q = p + 1; q < N; q++) {
				for (int r = q + 1; r < N; r++) {
					for (int s = r + 1; s < N; s++) {
						if ((brute[p].slopeTo(brute[q]) == brute[p].slopeTo(brute[r]))
								&& (brute[p].slopeTo(brute[q]) == brute[p].slopeTo(brute[s])))

						{
							Point[] brutePrint = new Point[4];
							brutePrint[0] = brute[p];
							brutePrint[1] = brute[q];
							brutePrint[2] = brute[r];
							brutePrint[3] = brute[s];

							Arrays.sort(brutePrint);
							System.out.print(brutePrint[0].toString() + " -> "
									+ brutePrint[1].toString() + " -> "
									+ brutePrint[2].toString() + " -> "
									+ brutePrint[3].toString() + "\n");
							brutePrint[0].drawTo(brutePrint[3]);
							brutePrint = null;
						}
					}
				}
			}
		}

		// display to screen all at once
		StdDraw.show();

		// reset the pen radius
		StdDraw.setPenRadius();
	}
}
