/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {// construct an empty deque

    private class Node {
        Node next;
        Node prev;
        Item item;

        public Node() {
            next = null;
            prev = null;
        }

        public Node(Item item) {
            this.item = item;
            next = prev = null;
        }
    }

    private Node first;
    private Node last;

    private int size;

    public Deque() {
        first = last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (first == null) {
            first = last = new Node(item);
        }
        else if (first == last) {
            first = new Node(item);
            first.next = last;
            last.prev = first;
        }
        else {
            Node toBeFirst = new Node(item);
            first.prev = toBeFirst;
            toBeFirst.next = first;
            first = toBeFirst;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (last == null) {
            first = last = new Node(item);
        }
        else if (first == last) {
            first.next = new Node(item);
            last = first.next;
            last.prev = first;
        }
        else {
            Node toBeLast = new Node(item);
            last.next = toBeLast;
            toBeLast.prev = last;
            last = toBeLast;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        else if (first == last) {
            Item item = first.item;
            first = last = null;
            size--;
            return item;
        }
        else {
            Node toBeFirst = first.next;
            toBeFirst.prev = null;
            Item item = first.item;
            first = toBeFirst;
            size--;
            return item;
        }

    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        else if (first == last) {
            Item item = first.item;
            first = last = null;
            size--;
            return item;
        }
        else {
            Node toBeLast = last.prev;
            toBeLast.next = null;
            Item item = last.item;
            last = toBeLast;
            size--;
            return item;
        }

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        Node startNode = first;

        public DequeIterator() {

        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item temp = startNode.item;
            startNode = startNode.next;
            return temp;
        }

        public boolean hasNext() {
            return startNode != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addLast("2");
        deque.addFirst("1");
        deque.addLast("3");
        deque.addLast("4");
        System.out.println(deque.size());
        Iterator<String> iter = deque.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
