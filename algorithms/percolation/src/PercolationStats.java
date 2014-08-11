/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/1/2014 
 * Compilation: javac PercolationStats.java 
 * Execution: java PercolationStats N T 
 * Dependencies: Percolation.java stdlib.jar algs4.jar
 * This program takes the grid size N and number of experiments T as
 * command-line arguments. It computes and prints a mean, stddev and 95%
 * confidence intervals for Percolation thresholds calculations.
 ****************************************************************************/

public class PercolationStats {	
	private final int gridSize;				// size of the grid to test
	private final int countExperiments;		// number of experiments to run
	private double[] fractionOfOpened;		// array to store the fractions of open nodes
	private Percolation perc;				// percolation datatype

	/**
	 * perform T independent computational experiments on an N-by-N grid
	 * @param N
	 * @param T
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new java.lang.IllegalArgumentException();
		}
		gridSize = N;
		countExperiments = T;
		fractionOfOpened = new double[T];
		for (int t = 0; t < countExperiments; t++) {	// iterator for T experiments
			perc = new Percolation(N);
			int countOfOpened = 0;						// keeps count of open sites
			while (true) {
				int i = StdRandom.uniform(1, gridSize + 1);		//random i coord
				int j = StdRandom.uniform(1, gridSize + 1);		//random j coord
				if (!perc.isOpen(i, j)) {
					perc.open(i, j);
					countOfOpened++;
					if (perc.percolates()) {
						fractionOfOpened[t] = ((double) countOfOpened)/(gridSize * gridSize);
						perc = null;
						break;
					}
				}
				continue;
			}
		}
	}

	/**
	 * sample mean of percolation threshold
	 * @return
	 */
	public double mean() {
		return StdStats.mean(fractionOfOpened);
	}

	/**
	 * sample standard deviation of percolation threshold
	 * 
	 * @return
	 */
	public double stddev() {
		if (countExperiments == 1) {
			return Double.NaN;
		} else {
			return StdStats.stddev(fractionOfOpened);
		}
	}

	/**
	 * returns lower bound of the 95% confidence interval
	 * 
	 * @return
	 */
	public double confidenceLo() {
		return (mean() - (1.96 * stddev() / Math.sqrt((double) countExperiments)));

	}

	/**
	 * returns upper bound of the 95% confidence interval
	 * 
	 * @return
	 */
	public double confidenceHi() {
		return (mean() + (1.96 * stddev() / Math.sqrt((double) countExperiments)));
	}

	/**
	 * Main method, accepts size N and number of experiments T as a command-line
	 * arguments Computes and prints a mean, stddev and 95% confidence intervals
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);		//input arg for grid size
		int T = Integer.parseInt(args[1]);		//input arg for number of experiments
		PercolationStats stats = new PercolationStats(N, T);
		System.out.print("mean                    = " + stats.mean() + "\n");
		System.out.print("stddev                  = " + stats.stddev() + "\n");
		System.out.print("95% confidence interval = " + stats.confidenceLo() 
				                               + ", " + stats.confidenceHi());
	}
}
