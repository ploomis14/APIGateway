package gateway;

import java.util.ArrayList;

/**
 * ArrayList implementation of a binary heap. Only includes heap methods that are needed for this application -
 * heapify, meld, merge, and delete methods are not included.
 *
 * @author Peter Loomis
 */

public class BinaryHeap<T extends Comparable<T>> {

    private ArrayList<T> items;

    public BinaryHeap() {
        items = new ArrayList<T>();
    }

    /**
     * The new item is initially appended to the end of the ArrayList, and the shifted up through the heap.
     *
     * @param item the element to be inserted
     */
    public void insert(T item) {
        items.add(item);
        int itemIndex = items.size()-1;
        shiftUp(itemIndex);
    }

    /**
     * An item is compared with its parent and swapped with its parent if it has a greater value. This process
     * repeats until the value of the parent is greater than or equal to that of the new item.
     *
     * @param itemIndex the index of the item that is being compared
     */
    public void shiftUp(int itemIndex) {
        while(itemIndex > 0) {
            int parentIndex = (itemIndex-1)/2;
            T item = items.get(itemIndex);
            T parent = items.get(parentIndex);
            if (item.compareTo(parent) > 0) {
                // swap the item with its parent
                items.set(itemIndex, parent);
                items.set(parentIndex, item);
                itemIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public T getRoot() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
