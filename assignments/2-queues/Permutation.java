/* *****************************************************************************
 *  Name: Carter Hills
 *  Date: 19/10/2024
 *  Description: Uses reservoir sampling to only place at most k elements
 *               in queue.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        // todo use structure of size k not n
        int k = Integer.parseInt(args[0]);

        // read all the items into a randomized queue
        int count = 0;
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            count++;
            String s = StdIn.readString();
            if (queue.size() < k) {
                queue.enqueue(s);
            }
            else {
                if (StdRandom.uniformDouble() < (double) k / count) {
                    queue.dequeue();
                    queue.enqueue(s);
                }
            }
        }

        // iterate over queue
        for (String s : queue) {
            StdOut.println(s);
        }

    }
}
