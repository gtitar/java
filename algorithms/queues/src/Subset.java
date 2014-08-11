/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/11/2014 
 * Compilation: javac Subset.java 
 * Execution: echo 11 22 33 44 55 66 77 | java Subset 3
 * Dependencies: stdlib.jar 
 * This is a Subset client for testing
 ****************************************************************************/
public class Subset {
	public static void main(String[] args) {
		int K = Integer.parseInt(args[0]);
 		RandomizedQueue<String> myRandom = new RandomizedQueue<String>();
		while( !StdIn.isEmpty()){
			String s = StdIn.readString();
			myRandom.enqueue(s);
	    }
		for (int j = 0; j < K; j ++ )
					System.out.print(myRandom.dequeue() + "\n");
		}
}
