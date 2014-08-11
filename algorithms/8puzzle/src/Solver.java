/**
 * Solver.java
 * @author George Titarenko
 * 08/08/2014
 * Dependencies: Dependencies: stdlib.jar algs4.jar Board.java
 * 
 * Solver to run an A* algorithm on NxN puzzle. Uses binary heap implementation of PriorityQueue with
 * priority calculated by Manhattan priority function.
 * We define a search node of the game to be a board, the number of moves made to reach the board, 
 * and the previous search node. First, insert the initial search node 
 * (the initial board, 0 moves, and a null previous search node) into a priority queue. 
 * Then, delete from the priority queue the search node with the minimum priority, 
 * and insert onto the priority queue all neighboring search nodes 
 * (those that can be reached in one move from the dequeued search node). 
 * Repeat this procedure until the search node dequeued corresponds to a goal board
 * 
 */
public class Solver {

	private boolean isSolvable = false;
	private Stack<Board> historyBoards;			//solution history

	/**
	 * A Search Node structure
	 *
	 */
	private class SearchNode implements Comparable<SearchNode> {
		Board searchBoard;				// a NxN board
		SearchNode previousSearchNode;  // the previous Node
		private int movesDone;			// Moves done so far
		private int priority;			// Priority function's cashe result

		public SearchNode(Board board, int moves, SearchNode previous) {
			this.searchBoard = board;
			this.previousSearchNode = previous;
			this.movesDone = moves;
			if (previous != null)
				this.movesDone += previous.getMovesDone();
			this.priority = board.manhattan() + movesDone;
		}

		@Override
		public int compareTo(SearchNode o) {
			if (this.priority == o.priority)
				return o.movesDone - this.movesDone;
			if (this.priority > o.priority)
				return 1;
			else
				return -1;
		}

		public Board getSearchBoard() {
			return this.searchBoard;
		}

		public int getMovesDone() {
			return this.movesDone;
		}

		public SearchNode getPreviousSearchNode() {
			return this.previousSearchNode;
		}
	}

	/**
	 * find a solution to the initial board (using the A* algorithm). Two queues implementation
	 * @param initial
	 */
	public Solver(Board initial) 
	{
		SearchNode searchNode = new SearchNode(initial, 0, null);
		SearchNode searchNodeTwin = new SearchNode(initial.twin(), 0, null);

		MinPQ<SearchNode> puzzleQueue = new MinPQ<SearchNode>();
		MinPQ<SearchNode> puzzleQueueTwin = new MinPQ<SearchNode>();
		puzzleQueue.insert(searchNode);
		puzzleQueueTwin.insert(searchNodeTwin);

		while (!puzzleQueue.isEmpty() && !puzzleQueueTwin.isEmpty()) {
			SearchNode dequeuedTwin = puzzleQueueTwin.delMin();
			if (dequeuedTwin.getSearchBoard().isGoal()) {
				isSolvable = false;
				return;
			}

			SearchNode dequeued = puzzleQueue.delMin();
			if (dequeued.getSearchBoard().isGoal()) {
				isSolvable = true;
				historyBoards = new Stack<Board>();
				historyBoards.push(dequeued.getSearchBoard());
				while (dequeued.getPreviousSearchNode() != null) {
					historyBoards.push(dequeued.getPreviousSearchNode().getSearchBoard());
					dequeued = dequeued.getPreviousSearchNode();
				}
				return;
			}
			Iterable<Board> boardNeighbors = dequeued.getSearchBoard().neighbors();
			for (Board b : boardNeighbors) {
				SearchNode searchNodeNeighbor = new SearchNode(b, 1, dequeued);
				if (dequeued.getPreviousSearchNode() != null
						&& searchNodeNeighbor.getSearchBoard().equals(
								dequeued.getPreviousSearchNode().getSearchBoard()))
					continue;
				puzzleQueue.insert(searchNodeNeighbor);
			}

			Iterable<Board> boardNeighborsTwin = dequeuedTwin.getSearchBoard().neighbors();
			for (Board b : boardNeighborsTwin) {
				SearchNode searchNodeNeighborTwin = new SearchNode(b, 1,
						dequeuedTwin);
				if (dequeuedTwin.getPreviousSearchNode() != null
						&& searchNodeNeighborTwin.getSearchBoard().equals(
								dequeuedTwin.getPreviousSearchNode().getSearchBoard()))
					continue;
				puzzleQueueTwin.insert(searchNodeNeighborTwin);
			}
		}
	}

	/**
	 * is the initial board solvable?
	 * @return
	 */
	public boolean isSolvable() 
	{
		return isSolvable;
	}

	/**
	 * min number of moves to solve initial board; -1 if no solution
	 * @return
	 */
	public int moves() 
	{
		if (isSolvable)
			return historyBoards.size() - 1;
		else
			return -1;
	}

	/**
	 * sequence of boards in a shortest solution; null if no solution
	 * @return
	 */
	public Iterable<Board> solution() 
	{
		if (!isSolvable)
			return null;
		return historyBoards;
	}

	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = (short) in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
