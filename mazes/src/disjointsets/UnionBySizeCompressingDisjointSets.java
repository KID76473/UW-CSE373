package disjointsets;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private int size;
    private Map<T, Integer> map; // key is element, and value is index in pointers

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        size = 0;
        map = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        pointers.add(-1);
        map.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = map.get(item);
        while (pointers.get(index) >= 0) { // find the id of set
            index = pointers.get(index);
        }
        int temp = map.get(item);
        while (pointers.get(temp) >= 0) { // compress the path
            pointers.set(temp, index);
            temp = pointers.get(temp);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int first = findSet(item1);
        int second = findSet(item2);
        if (first == second) {
            return false;
        }
        else if (pointers.get(findSet(item1)) <= pointers.get(findSet(item2))) {
            pointers.set(first, pointers.get(first) + pointers.get(second));
            pointers.set(second, first);
        }
        else {
            pointers.set(second, pointers.get(first) + pointers.get(second));
            pointers.set(first, second);
        }
        System.out.println(pointers);
        return true;
    }
}
