/****************************************************************************
 * Author: George Titarenko
 * Last updated: 7/11/2014 
 * Compilation: javac Deque.java 
 * Dependencies: stdlib.jar 
 * This is a Deque implementation using doubly linked list.
 ****************************************************************************/

import java.util.Iterator;

/**
 *Generic Deque class 
 *
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
   private Node first, last;		//references to teh first and last nodes
   private int numOfNodes;			// number of nodes in the deque
   
   /**
    * 
    * Inner class - a datastrucure for a node
    *
    */
   private class Node				// Node datastructure
   {
	   Item item;					// Data Item
	   Node next;					//Reference to the next node	
	   Node previous;				//reference to the previous node
   }

   /**
    * Constructor
    */
   public Deque()                           // construct an empty deque
   {
	   first = null;
	   last = null;
	   numOfNodes = 0;
   }

   /**
    * boolean to check if Empty
    * @return
    */
   public boolean isEmpty()                 // is the deque empty?
   { return numOfNodes == 0; }
   
   /**
    * Size of the structure
    * @return
    */
   public int size()                        // return the number of items on the deque
   { return numOfNodes; }
   
   /**
    * Adds a node at the beginning
    * @param item
    */
   public void addFirst(Item item)          // insert the item at the front
   {
	   if ( item == null ) throw new NullPointerException();
	   Node oldFirst = first;
	   first = new Node();
	   first.item = item;
	   first.next = oldFirst;	
	   first.previous = null;
	   if ( oldFirst != null )
		   oldFirst.previous = first;
	   else last = first;
	   numOfNodes++;
   }
  
   /**
    * Adds the node at the end
    * @param item
    */
   public void addLast(Item item)           // insert the item at the end
   {
	   if ( item == null ) throw new NullPointerException();
	   Node oldLast = last;
	   last = new Node();
	   last.item = item;
	   last.next = null;
	   last.previous = oldLast;
	   if (oldLast == null) first = last;
	   else { oldLast.next = last; }
	   numOfNodes++;
   }
   
   /**
    * removes a node from the beginning
    * @return
    */
   public Item removeFirst()                // delete and return the item at the front
   {
	   if (numOfNodes == 0) throw new java.util.NoSuchElementException();
	   Item removedItem = first.item;
	   if (numOfNodes == 1) { first = last = null; }
	   else {
		    first.next.previous = null;
	   		first = first.next;
	   }
	   numOfNodes--;
	   return removedItem;
	 }
   
   /**
    * Removes the node form the end
    * @return
    */
   public Item removeLast()                 // delete and return the item at the end
   {
	   if (numOfNodes == 0) throw new java.util.NoSuchElementException();
	   Item removedItem = last.item;
	   if (numOfNodes == 1) { first = last = null; }
	   else {
		   last.previous.next = null;
		   last = last.previous;
	   }
	   numOfNodes--;
	   return removedItem;
   }
   
   /**
    * Iterator implementation
    */
   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   { return new DequeIterator(); }
   
   /**
    * Inner class to impolement iterator interface
    *
    */
   private class DequeIterator implements Iterator<Item>
   {
	   private Node current = first;
	   public boolean hasNext() { return current != null; }
	   public Item next()
	   {
		   if (current == null) throw new java.util.NoSuchElementException();
		   Item item = current.item;
		   current = current.next;
		   return item;
	   }
	   public void remove() { throw new UnsupportedOperationException(); } 
   }
}
