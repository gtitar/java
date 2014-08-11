import java.util.Arrays;
/**
 * Board.java
 * @author George Titarenko
 * 08/08/2014
 * Dependencies: Dependencies: stdlib.jar algs4.jar
 * 
 * Structure to represent a NxN "8 puzzle" board. Will use A* search algorithm.
 * Two priority functions are implemented:
 * Hamming priority function. The number of blocks in the wrong position, 
 * plus the number of moves made so far to get to the search node. 
 * Intutively, a search node with a small number of blocks in the wrong position is close to the goal, 
 * and we prefer a search node that have been reached using a small number of moves.
 * 
 * Manhattan priority function. The sum of the Manhattan distances 
 * (sum of the vertical and horizontal distance) from the blocks to their goal positions, 
 * plus the number of moves made so far to get to the search node.   
 *
 */
public final class Board {
	private final int N; // board size
	private final short[][] board; // board
	private int manhattan = -1;		//for manhattan() cashing

	private Board(short[][] blocks) // added private constructor, decr. mem,
									// used by 20%
	{
		this.N = blocks.length;
		board = new short[this.N][this.N];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.N; j++)
				board[i][j] = blocks[i][j];
		}
	}
	/**
	 * construct a board from an N-by-N array of blocks
	 * (where blocks[i][j] = block in row i, column j)
	 * @param blocks
	 */
	public Board(int[][] blocks) 	
	{ 								
		this.N = blocks.length;
		board = new short[this.N][this.N];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.N; j++)
				board[i][j] = (short) blocks[i][j];
		}
	}

	/**
	 * @return x coord of the empty slot
	 */
	private int getEmptyX() {
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (0 == board[i][j])
					return i;
		return -99;
	}

	/**
	 * @return y coord of the empty slot
	 */
	private int getEmptyY() {
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (0 == board[i][j]) {
					return j;
				}
		return -99;
	}

	/**
	 * Moves a node into empty slot
	 * @param x0 empty slot
	 * @param y0 empty slot
	 * @param x1 target slot
	 * @param y1 target slot
	 * @throws Exception
	 */
	private void moveEmpty(int x0, int y0, int x1, int y1) throws Exception {
		if (Math.abs(x0 - x1) > 1 || Math.abs(y0 - y1) > 1
				|| (x1 != x0 && y1 != y0))
			throw new Exception("Impossible move requested!");
		board[x0][y0] = board[x1][y1];
		board[x1][y1] = 0;
	}

	/**
	 * Swaps two nodes with given coords (should be neighbors)
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @throws Exception
	 */
	private void swapElement(int x0, int y0, int x1, int y1) throws Exception {
		if (Math.abs(x0 - x1) > 1 || Math.abs(y0 - y1) > 1
				|| (x1 != x0 && y1 != y0))
			throw new Exception("Impossible move requested!");
		if (board[x0][y0] == 0 || board[x1][y1] == 0) {
			x0++;
			x1++;
		}
		short swap = board[x0][y0];
		board[x0][y0] = board[x1][y1];
		board[x1][y1] = swap;
	}

	/**
	 * Calculates a target slot for a given node. (x, y)
	 * @param position
	 * @return
	 */
	private int[] getTargetPosition(int position) {
		int[] targetPosition = new int[2];
		int mod = position % N;
		if (mod == 0) {
			targetPosition[0] = position / N - 1;
			targetPosition[1] = N - 1;
		} else {
			targetPosition[0] = position / N;
			targetPosition[1] = mod - 1;
		}
		return targetPosition;
	}

	/**
	 * board dimension N
	 * @return
	 */
	public int dimension() 
	{
		return this.N;
	}

	/**
	 * number of blocks out of place
	 * @return
	 */
	public int hamming() 
	{
		int count = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (!(i == N - 1 && j == N - 1)
						&& board[i][j] != (i * N + j + 1))
					count++;
		return count;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 * @return
	 */
	public int manhattan() 
	{
		if (this.manhattan != -1)
			return manhattan; // simple cashing
		int x_moves = 0;
		int y_moves = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == 0)
					continue;
				x_moves += Math.abs(i - getTargetPosition(board[i][j])[0]);
				y_moves += Math.abs(j - getTargetPosition(board[i][j])[1]);
			}
		}
		manhattan = x_moves + y_moves;
		return manhattan;
	}

	/**
	 * is this board the goal board?
	 * @return
	 */
	public boolean isGoal() 
	{
		return manhattan() == 0;
	}

	/**
	 * a board obtained by exchanging two adjacent blocks in the same row
	 * @return
	 */
	public Board twin() 
	{
		Board twinBoard = new Board(board);
		try {
			twinBoard.swapElement(0, 0, 0, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return twinBoard;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object y) { // does this board equal y?
		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;
		Board that = (Board) y;
		return (Arrays.deepEquals(this.board, that.board));
	}

	/**
	 * all neighboring boards
	 * @return
	 */
	public Iterable<Board> neighbors() 
	{
		Stack<Board> stackOfNeighbors = new Stack<Board>();
		Board newBoard;
		int x0 = getEmptyX();
		int y0 = getEmptyY();
		if (x0 > 0) {
			newBoard = new Board(board);
			try {
				newBoard.moveEmpty(x0, y0, x0 - 1, y0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stackOfNeighbors.push(newBoard);
		}
		if (x0 < N - 1) {
			newBoard = new Board(board);
			try {
				newBoard.moveEmpty(x0, y0, x0 + 1, y0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stackOfNeighbors.push(newBoard);
		}
		if (y0 > 0) {
			newBoard = new Board(board);
			try {
				newBoard.moveEmpty(x0, y0, x0, y0 - 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stackOfNeighbors.push(newBoard);
		}
		if (y0 < N - 1) {
			newBoard = new Board(board);
			try {
				newBoard.moveEmpty(x0, y0, x0, y0 + 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stackOfNeighbors.push(newBoard);
		}
		return stackOfNeighbors;
	}

	public String toString() { // string representation of the board (in the
								// output format specified below)
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", board[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}
}
