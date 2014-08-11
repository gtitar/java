/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/11/2014 
 * Compilation: javac RandomizedQueue.java 
 * Dependencies: stdlib.jar 
 * This is a RandomizedQueue implementation using resizing array
 ****************************************************************************/

import java.util.Iterator;

/**
 * 
 * Generic RandomizedQueue class
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
   private Item[] items;			//an array of items to store
   private int counter;				//counter of items
   private int queueSize;			//size of ten array
   
	@SuppressWarnings("unchecked")
	public RandomizedQueue()                 // construct an empty randomized queue
   {
		items = (Item[]) new Object[1];
		counter = 0;	
		queueSize = 1;
   }
   public boolean isEmpty()                 // is the queue empty?
   { return counter == 0; }
   
   public int size()                        // return the number of items on the queue
   { return counter; }

/**
 * engueue - add the item to the array   
 * @param item
 */
   public void enqueue(Item item)           // add the item
   {
	   if ( item == null ) throw new NullPointerException();
	   if (counter == queueSize) resize (queueSize = queueSize*2);
	   items[counter++] = item;
   }

/**
 * private method to dynamically resize the array
 * @param newSize
 */
   private void resize ( int newSize )
   {
	   @SuppressWarnings("unchecked")
	Item[] copyItems = (Item[]) new Object[newSize];
	   for (int i = 0; i < this.counter; i++)
		   copyItems[i] = this.items[i];
	   items = copyItems;	   
   }

/**
 * Dequeue - removes and returns the item
 * @return
 */
   public Item dequeue()                    // delete and return a random item
   {
	   if (counter == 0) throw new java.util.NoSuchElementException();
	   int i = StdRandom.uniform(counter);
	   Item dequeueItem = items[i];
	   items[i] = items[--counter];
	   items[counter] = null;
	   if (counter > 0 && counter <= queueSize/4) resize (queueSize =  queueSize/2);
	   return dequeueItem;
   }

/**
 * Returns the random item without the deletion   
 * @return
 */
   public Item sample()                     // return (but do not delete) a random item
   {
	   if (counter == 0) throw new java.util.NoSuchElementException();
	   return items[StdRandom.uniform(counter)];
   }

/**
 * Iterator implementation
 */
   public Iterator<Item> iterator()         // return an independent iterator over items in random order
   { return new RandomIterator(); }

/**
 * Inner class to implement iterator   
 *
 */
	private class RandomIterator implements Iterator<Item>
   {
		private int iterWalker;				//Iterator counter
		private int[] randomIndexes;		//array to shuffle the elements via indexes

		public RandomIterator()
		{
			this.iterWalker = 0;
			this.randomIndexes = new int[counter];
			for (int i = 0; i < counter; i ++)
				this.randomIndexes[i] = i;
			StdRandom.shuffle(randomIndexes);
		}
		@Override
		public boolean hasNext() {
			return iterWalker < counter;
		}

		@Override
		public Item next() {
			if (iterWalker >= counter) throw new java.util.NoSuchElementException();
			return items[randomIndexes[iterWalker++]];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
   }

	/**
	 * Main to test the queue/array
	 * @param args
	 */
   public static void main(String[] args)   // unit testing
   {
		int K = Integer.parseInt(args[0]);
 		RandomizedQueue<String> myRandom = new RandomizedQueue<String>();
		while( !StdIn.isEmpty()){
			String s = StdIn.readString();
			myRandom.enqueue(s);
			if (s.equals("000"))break;
	    }
		
		while(true)
			 System.out.print(myRandom.dequeue() + "\n");
		
		/*		for (int j = 0; j < K; j ++ )
			{
					System.out.print(myRandom.dequeue() + "\n");
			}
		}*/
   }
}
