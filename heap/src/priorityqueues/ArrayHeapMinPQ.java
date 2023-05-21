package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    private int size;
    private HashMap<T, Integer> hashTable;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
        hashTable = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        hashTable.put(items.get(a).getItem(), b);
        hashTable.put(items.get(b).getItem(), a);
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (hashTable.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> added = new PriorityNode<>(item, priority);
        items.add(size, added);
        percolateUp(size);
        size++;
    }

    @Override
    public boolean contains(T item) {
        return hashTable.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        T temp = items.get(START_INDEX).getItem();
        hashTable.remove(items.get(START_INDEX).getItem()); // remove from hash table
        items.set(START_INDEX, items.get(size - 1)); // put the last one to the first place
        percolateDown(START_INDEX);
        items.remove(size - 1); // remove the last one
        size--;
        return temp;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!hashTable.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int pointer = hashTable.get(item);
        items.get(pointer).setPriority(priority);
        percolateUp(pointer);
        percolateDown(pointer);
    }

    @Override
    public int size() {
        return size;
    }

    public String toString() {
        return items.toString();
    }

    private void percolateUp(int pointer) {
        while (pointer > 0 && items.get(pointer).getPriority() < items.get((pointer - 1) / 2).getPriority()) {
            swap(pointer, (pointer - 1) / 2);
            pointer = (pointer - 1) / 2;
        }
        hashTable.put(items.get(pointer).getItem(), pointer);
    }

    private void percolateDown(int pointer) {
        while (pointer * 2 + 2 < size && needToPercolateDown(pointer)) { // when it has two children
            if (items.get(pointer * 2 + 1).getPriority() < items.get(pointer * 2 + 2).getPriority()) {
                swap(pointer, pointer * 2 + 1);
                pointer = pointer * 2 + 1;
            }
            else if (items.get(pointer * 2 + 1).getPriority()  >= items.get(pointer * 2 + 2).getPriority()) {
                swap(pointer, pointer * 2 + 2);
                pointer = pointer * 2 + 2;
            }
        }
        if (pointer * 2 + 2 == size &&
            items.get(pointer).getPriority() > items.get(pointer * 2 + 1).getPriority()) {
            swap(pointer, pointer * 2 + 1);
        }
    }

    private boolean needToPercolateDown(int pointer) {
        return items.get(pointer).getPriority() > items.get(pointer * 2 + 1).getPriority() ||
            items.get(pointer).getPriority() > items.get(pointer * 2 + 2).getPriority();
    }
}
