/******************************************************************************
 *  Compilation:  javac-algs4 Permutation.java
 *  Execution:    java-algs4 Permutation k < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *
 * Permutation is a program that uses RandomizedQueue to select at most k elements
 * randomly from an input text file.
 *
 * Example:
 * cat distinct.txt
 * A B C D E F G H I
 *
 * Java Permutation 3 < distinct.txt
 * C
 * G
 * A
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue queue = new RandomizedQueue();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
