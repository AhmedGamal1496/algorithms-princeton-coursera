/******************************************************************************
 *  Compilation:  javac-algs4 RandomizedQueue.java
 *  Execution:    java-algs4 RandomizedQueue
 *  Dependencies: StdOut.java StdRandom.java
 *
 * Randomized queue is the same as the normal queue, but it dequeues its elements
 * randomly based on uniform distribution.
 * Two RandomizedQueue objects are indepenedent of each other, meaning they
 * will output different results.
 *
 * Example:
 * enqueue("A") => ["A", null]
 * enqueue("B") => ["A", "B"]
 * enqueue("C") => ["A", "B", "C", null]
 * .....
 *              => ["A", "B", "C", "D", "E", null, null, null]
 * deqeue() based of the random pick, it will choose an index between 0 and 4 (5 elements) (e.g. 2)
 *              => ["A", "B", "E", "D", null, null, null, null]
 * dequeue() (e.g. 0) => ["D", "B", "E", null, null, null, null, null]
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // we will not use head and tail, because the head is not important
    private Item[] queue;   // queue array
    private int n;          // number of the elements

    // construct an empty randomized queue
    public RandomizedQueue() {
        n = 0;
        queue = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Null cannot be added!");

        // if the number of elements reached the full size of the array, then resize
        if (queue.length == n) resize(2 * queue.length);
        queue[n++] = item; // save the item in the index, then update n
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("No elements in the randomized queue");

        // select a random index between 0 and number of elements in the array
        int randomInt = StdRandom.uniform(size());
        Item item = queue[randomInt];               // save the item
        queue[randomInt] = null;                    // remove it
        indexSwap(queue, randomInt, n - 1);         // swap the element in the tail with this index
        n--;

        // if the number of elements reached 1/4 of its size, then resize
        if (n > 0 && n == queue.length / 4) resize(queue.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("No elements in the randomized queue");

        int randomInt = StdRandom.uniform(size());
        return queue[randomInt];
    }

    private void resize(int capacity) {
        // make sure the new capacity is bigger or equal to n
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];   // create a new array and copy
        for (int i = 0; i < n; i++) {
            temp[i] = queue[i];
        }

        queue = temp;
    }

    // swap the indices of the given array
    private void indexSwap(Item[] array, int i, int j) {
        Item temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] copy = queue.clone();
        private int queueSize = n;

        public boolean hasNext() {
            return queueSize != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int randomInt;

            randomInt = StdRandom.uniform(queueSize);
            Item item = copy[randomInt];
            copy[randomInt] = null;
            indexSwap(copy, randomInt, queueSize - 1);

            queueSize--;

            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();

        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        q.enqueue("D");
        q.dequeue();

        Iterator iterator = q.iterator();

        StdOut.println("Size: " + q.size());
        StdOut.println("hasNext? " + iterator.hasNext());

        while (iterator.hasNext()) {
            StdOut.println(iterator.next() + " ");
        }
    }

}
