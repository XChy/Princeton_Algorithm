import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (items.length == size) {
            resize(size * 2);
        }

        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniformInt(size);
        Item result = items[randomIndex];
        items[randomIndex] = items[size - 1];
        size--;
        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);
        }
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniformInt(size);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int size) {
        Item[] newItems = (Item[]) new Object[size];
        for (int i = 0; i < this.size; ++i) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] randomItems = (Item[]) new Object[size];
        private int len = size;

        public RandomizedQueueIterator() {
            for (int i = 0; i < randomItems.length; i++) {
                randomItems[i] = items[i];
            }
        }

        public boolean hasNext() {
            return len != 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int randomIndex = StdRandom.uniformInt(len);
            Item item = randomItems[randomIndex];
            randomItems[randomIndex] = randomItems[len - 1];
            randomItems[len - 1] = null;
            len--;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }


    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}