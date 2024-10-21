/* *****************************************************************************
 *  Name: Carter Hills
 *  Date: 19/10/2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        // add to front
        for (int i = 0; i < 3; i++) {
            deque.addFirst(i);
        }
        deque.printForwards();
        StdOut.println();
        deque.printBackwards();
        StdOut.println();

        // add to back
        for (int i = 3; i < 6; i++) {
            deque.addLast(i);
        }
        deque.printForwards();
        StdOut.println();
        deque.printBackwards();

        // remove from front
        StdOut.println();
        StdOut.println(deque.removeFirst());
        deque.printForwards();
        StdOut.println();
        deque.printBackwards();

        // remove from back
        StdOut.println();
        StdOut.println(deque.removeLast());
        deque.printForwards();
        StdOut.println();
        deque.printBackwards();

        // test iterator
        StdOut.println();
        for (int i : deque) {
            StdOut.print(i + " ");
        }


    }

    public void addFirst(Item item) {
        checkItemNotNull(item);
        if (isEmpty()) {
            addFirstElement(item);
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        size++;


    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty!");
        }
        Node oldFirst = first;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        else {
            last = first;
        }

        size--;
        return oldFirst.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty!");
        }
        Node oldLast = last;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        else {
            first = last;
        }
        size--;
        return oldLast.item;
    }

    private void printForwards() {
        Node curr = first;
        while (curr != null) {
            StdOut.print(curr.item + " ");
            curr = curr.next;

        }
    }

    private void printBackwards() {
        Node curr = last;
        while (curr != null) {
            StdOut.print(curr.item + " ");
            curr = curr.prev;

        }
    }

    public void addLast(Item item) {
        checkItemNotNull(item);
        if (isEmpty()) {
            addFirstElement(item);
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.prev = oldLast;
            oldLast.next = last;
        }
        size++;
    }

    private void checkItemNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null items cannot be added to deque.");
        }
    }

    public boolean isEmpty() {
        return first == null;
    }

    private void addFirstElement(Item item) {
        first = new Node();
        first.item = item;
        last = first;
    }

    public int size() {
        return size;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node curr = first;

        public boolean hasNext() {
            return curr != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = curr.item;
            curr = curr.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }

    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }
}
