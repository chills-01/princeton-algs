/* *****************************************************************************
 *  Name: Carter Hills
 *  Date: 19/10/2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        StdOut.println(q.isEmpty());
        // enqueue
        StdOut.println("Testing enqueue:");
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
            q.print();
        }
        StdOut.println();

        // iterators
        StdOut.println("Testing iterators:");
        for (Integer item1 : q) {
            for (Integer item2 : q) {
                StdOut.print(item1 + "-" + item2 + " ");
            }
            StdOut.println();
        }
        StdOut.println();
        StdOut.println();

        // sample
        StdOut.println("Testing sample:");
        for (int i = 0; i < 15; i++) {
            StdOut.print(q.sample() + " ");
        }

        StdOut.println("\n" + q.isEmpty());
        StdOut.println();

        // deque
        StdOut.println("Testing dequeue:");
        for (int i = 0; i < 10; i++) {
            q.dequeue();
            q.print();
        }
        StdOut.println(q.isEmpty());

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;

    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Argument must not be null");
        if (size == items.length) {
            resize(2 * items.length);
        }
        items[size++] = item;

    }

    // return the number of items on the randomized queue
    public int size() {
        return size;

    }

    private void print() {
        for (int i = 0; i < size; i++) {
            StdOut.print(items[i] + " ");
        }
        StdOut.println();
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) throw new NoSuchElementException("The queue is empty!");
        // get a random integer
        int index = getRandomIndex();

        // swap it to end
        Item temp = items[index];
        items[index] = items[size - 1];
        items[size - 1] = temp;

        // decrease size if less than 25% full
        if (size == items.length / 4) {
            resize(items.length / 2);
        }

        // pop end item
        Item item = items[--size];
        items[size] = null;
        return item;
    }

    private void resize(int newSize) {
        Item[] copy = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }
        items = copy;

    }

    private int getRandomIndex() {
        return StdRandom.uniformInt(size);
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) throw new NoSuchElementException("The queue is empty!");
        return items[getRandomIndex()];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private int[] order;

        public RandomizedQueueIterator() {
            order = new int[size];
            for (int j = 0; j < size; j++) {
                order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return i < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[order[i++]];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported.");
        }
    }
}
