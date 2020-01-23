/******************************************************************************
 *  Compilation:  javac-algs4 Deque.java
 *  Execution:    java-algs4 Deque
 *  Dependencies: StdOut.java
 *
 *  Double queue (Deque), implemented using doubly linked list.
 *  Each deque item is of type Item.
 ******************************************************************************/


import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node front;   // to access the front side of the linked-list
    private Node back;    // to access the back side of the linked-list
    private int n;        // number of elements

    // helper linked-list class
    private class Node {
        private Item item;             // value of the node
        private Node next = null;      // next node
        private Node prev = null;      // previous node
    }

    // construct an empty deque
    public Deque() {
        front = null;
        back = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (front == null || back == null);
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Null cannot be added!");

        // save the old front and create a new node in the front side of the linked-list
        Node oldfront = front;
        front = new Node();
        front.item = item;

        // if back is null, it shares the same node with front
        if (isEmpty()) back = front;
            // if back is not null, then the old front is in the middle after the new front
        else {
            front.next = oldfront;
            oldfront.prev = front;
        }

        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Null cannot be added!");

        // save the old back and create a new node in the back side of the linked-list
        Node oldback = back;
        back = new Node();
        back.item = item;
        // back.next = null;

        // if front is null, it shares the same node with back
        if (isEmpty()) front = back;
            // if front is not null, then the old back is in the middle before the new back
        else {
            back.prev = oldback;
            oldback.next = back;
        }

        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("No elements in the queue");

        Item item = front.item;   // save the item
        front = front.next;       // delete the old front
        n--;

        // if there are no more elements, back is null too
        if (isEmpty()) back = null;
        else front.prev = null;  // delete the old front

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("No elements in the queue");


        Item item = back.item;
        back = back.prev;
        n--;

        if (isEmpty()) front = null;
        else back.next = null;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        // make a new front pointer
        private Node current = front;
        private int dequeSize = n;

        public boolean hasNext() {
            return dequeSize != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            dequeSize--;

            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        deque.addFirst(1);
        deque.removeFirst();

        Iterator iterator = deque.iterator();

        StdOut.println("Size: " + deque.size());
        StdOut.println("hasNext? " + iterator.hasNext());

        while (iterator.hasNext()) {
            StdOut.println(iterator.next() + " ");
        }

    }
}
