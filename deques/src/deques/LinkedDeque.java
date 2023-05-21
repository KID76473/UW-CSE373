package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<T>(null);
        back = new Node<T>(null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> add = new Node<T>(item);
        add.next = front.next;
        add.prev = front;
        front.next.prev = add;
        front.next = add;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> add = new Node<T>(item);
        add.prev = back.prev;
        add.next = back;
        back.prev.next = add;
        back.prev = add;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T temp = front.next.value;
        front.next.next.prev = front;
        front.next = front.next.next;
        return temp;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T temp = back.prev.value;
        back.prev.prev.next = back;
        back.prev = back.prev.prev;
        return temp;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        if (index <= size / 2) {
            if (index == 0) {
                return front.next.value;
            }
            Node<T> current = front.next.next;
            int count = 1;
            while (current != null) {
                if (count == index) {
                    return current.value;
                }
                count++;
                current = current.next;
            }
        }
        else {
            if (index == size - 1) {
                return back.prev.value;
            }
            Node<T> current = back.prev.prev;
            int count = size - 2;
            while (current != null) {
                if (count == index) {
                    return current.value;
                }
                count--;
                current = current.prev;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }
}
