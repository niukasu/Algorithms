import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int n = 0;

    public RandomizedQueue() {                 // construct an empty randomized queue
        s = (Item[]) new Object[1];
    }
    public boolean isEmpty() {                 // is the queue empty?
        return n == 0;
    }
    public int size() {                        // return the number of items on the queue
        return n;
    }
    public void enqueue(Item item) {          // add the item
        if (item == null) throw new java.lang.NullPointerException();
        if (n == s.length) resize(2 * s.length);
        s[n++] = item;
    }
    public Item dequeue() {
        if (n == 0) throw new java.util.NoSuchElementException();
        int rand = StdRandom.uniform(n);
        Item temp = s[rand];
        s[rand] = s[n-1];
        --n;
        s[n] = null;
        if (s.length > 4 && s.length > 4 * n) resize(s.length/2);
        return temp;
    }                    // remove and return a random item
    public Item sample() {                     // return (but do not remove) a random item
        if (n == 0) throw new java.util.NoSuchElementException();
        return s[StdRandom.uniform(n)];
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = s[i];
        s = copy;
    }
    public Iterator<Item> iterator() {         // return an independent iterator over items in random order
        return new ListIterator(s, n);
    }

    private class ListIterator implements Iterator<Item> {
        
        private Item[] iteratorQueue;
        private int iteratorIndex = 0;
        
        public ListIterator(Item[] queue, int capacity) {
            
            iteratorQueue = (Item[]) new Object[capacity];
            
            for (int i = 0; i < iteratorQueue.length; i++) {
                iteratorQueue[i] = queue[i];
            }
            
            for (int j = 1; j < iteratorQueue.length; j++) {
                int index = StdRandom.uniform(j + 1);
                
                Item temp = iteratorQueue[j];
                iteratorQueue[j] = iteratorQueue[index];
                iteratorQueue[index] = temp;
            }
        }
        
        public boolean hasNext() {
            return (iteratorIndex < iteratorQueue.length);
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            Item item = iteratorQueue[iteratorIndex];
            iteratorIndex++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) { }    // unit testing
}
