/****************************************************************************
 * Author: George Titarenko Last updated: 7/1/2014
 * 
 * Compilation: javac Percolation.java Dependencies: stdlib.jar algs4.jar
 * Percolation data type with implemented API's to model percolation problem
 * 
 ****************************************************************************/

public class Percolation {
	private final int gridSize;				// size of the grid tested
	private boolean[][] isOpenStatus;		// array to store the status of every node (open/close)
	private WeightedQuickUnionUF percolationGrid;  // data structure used ( WQU )
	private final int virtualTopIndex;		// virtual top node
	private boolean[] hasBottomSite;

	/**
	 * create N-by-N grid, with all sites blocked
	 * 
	 * @param N
	 */
	public Percolation(int N) {
		if (N <= 0) 
			throw new java.lang.IllegalArgumentException();
			
		this.gridSize = N;
		this.isOpenStatus = new boolean[N][N];
		this.percolationGrid = new WeightedQuickUnionUF(N * N + 2);
		this.virtualTopIndex = N * N;
		this.hasBottomSite = new boolean[N*N+1];
	}

	/**
	 * open site (row i, column j) if it is not already
	 * 
	 * @param i
	 * @param j
	 */
	public void open(int i, int j) {
		if (!checkIndexes(i, j)) {
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
		if (isOpen(i, j)) {
			return;
		}
		isOpenStatus[i - 1][j - 1] = true;
		
		boolean root = false;	// flag to capture if an adjacent node's tree has a bottom node

		if (i == 1) {
			percolationGrid.union(xyTo1D(i, j), virtualTopIndex);
		} else if (i > 1 && isOpen(i - 1, j)) {
			if (hasBottomSite[percolationGrid.find(xyTo1D(i - 1, j))])
				root = true;
			percolationGrid.union(xyTo1D(i, j), xyTo1D(i - 1, j));
		}
		if (i < gridSize && isOpen(i + 1, j)) {
			if (hasBottomSite[percolationGrid.find(xyTo1D(i + 1, j))])
				root = true;
			percolationGrid.union(xyTo1D(i, j), xyTo1D(i + 1, j));
		}
		if (j > 1 && isOpen(i, j - 1)) {
			if (hasBottomSite[percolationGrid.find(xyTo1D(i, j - 1))])
				root = true;
			percolationGrid.union(xyTo1D(i, j), xyTo1D(i, j - 1));
		}
		if (j < gridSize && isOpen(i, j + 1)) {
			if (hasBottomSite[percolationGrid.find(xyTo1D(i, j + 1))])
				root = true;
			percolationGrid.union(xyTo1D(i, j), xyTo1D(i, j + 1));
		}
		if (root || i == gridSize) {
			hasBottomSite[percolationGrid.find(xyTo1D(i, j))] = true;
		}
	}
	

	/**
	 * is site (row i, column j) open?
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isOpen(int i, int j) {
		if (!checkIndexes(i, j)) {
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
		return isOpenStatus[i - 1][j - 1];
	}

	/**
	 * is site (row i, column j) full?
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isFull(int i, int j) {
		if (!checkIndexes(i, j)) {
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
		return percolationGrid.connected(virtualTopIndex, (xyTo1D(i, j)));
	}

	/**
	 * does the system percolate?
	 * 
	 * @return
	 */
	public boolean percolates() {
	
		if (hasBottomSite[percolationGrid.find(virtualTopIndex) ])
			return true;
		return false;
	}

	/**
	 * Converts i and j coordinates to algorithm's index
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private int xyTo1D(final int i, final int j) {
		return (i - 1) * gridSize + (j - 1);
	}

	/**
	 * Checks if indexes provided are within the valid range
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean checkIndexes(final int i, final int j) {
		return !(i < 1 || i > gridSize || j < 1 || j > gridSize);
	}
}
