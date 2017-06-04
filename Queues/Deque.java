import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first;
    private Node last;
    private int cnt;

    private class Node {
        public Item item;
        public Node prev;
        public Node next;
    }
    public Deque() {                          // construct an empty deque
        first = null;
        last = null;
        cnt = 0;
    }
    public boolean isEmpty() {                // is the deque empty?
        return first == null;
    }
    public int size() {                        // return the number of items on the deque
        return cnt;
    }
    public void addFirst(Item item) {        // add the item to the front
    
        if (item == null) throw new java.lang.NullPointerException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (cnt == 0) {
            last = first;
        }
        else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        ++cnt;
    }
    public void addLast(Item item) {       // add the item to the end
    
        if (item == null) throw new java.lang.NullPointerException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (cnt == 0) {
            first = last;
        }
        else {
            last.prev = oldlast;
            oldlast.next = last;
        }
        ++cnt;
    }
    public Item removeFirst() {               // remove and return the item from the front
    
        if (cnt == 0) throw new java.util.NoSuchElementException();
        Item temp = first.item;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        else {
            last = null;
        }
        --cnt;
        return temp;
    }
    public Item removeLast() {                 // remove and return the item from the end
        if (cnt == 0) throw new java.util.NoSuchElementException();
        Item temp = last.item;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        else {
            first = null;
        }
        --cnt;
        return temp;
    }
    
    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if (current == null) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args) { }  // unit testing
}