package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 10;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 10;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private double size; // number of chain
    private final double lambda;
    private int chainCapacity;

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        lambda = resizingLoadFactorThreshold;
        size = 0;
        chains = createArrayOfChains(initialChainCount);
        chainCapacity = chainInitialCapacity;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode() % chains.length);
        }
        if (chains[hash] != null) {
            return chains[hash].get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode() % chains.length);
        }
        if (chains[hash] == null) {
            chains[hash] = createChain(chainCapacity);
        }
        V temp = chains[hash].put(key, value);
        if (temp == null) {
            size++;
        }
        if ((size / (double) chains.length) >= lambda) {
            resize(chains.length * 2);
        }
        return temp;
    }

    @Override
    public V remove(Object key) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode() % chains.length);
        }
        if (chains[hash] != null) {
            V temp = chains[hash].remove(key);
            if (temp != null) {
                size--;
            }
            if (chains[hash].size() == 0) {
                chains[hash] = null;
            }
            return temp;
        }
        return null;
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = 0;
        if (key != null) {
            hash = Math.abs(key.hashCode() % chains.length);
        }
        if (chains[hash] != null) {
            return chains[hash].containsKey(key);
        }
        return false;
    }

    @Override
    public int size() {
        return (int) size;
    }

    private void resize(int newChainCapacity) {
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(newChainCapacity);
        for (Entry<K, V> item : this) {
            K key = item.getKey();
            V value = item.getValue();
            int hash = 0;
            if (key != null) {
                hash = Math.abs(key.hashCode() % newChains.length);
            }
            if (newChains[hash] == null) {
                newChains[hash] = createChain(chainCapacity);
            }
            newChains[hash].put(key, value);
        }
        chains = newChains;
    }

    // public void resize() {
    //     chainCapacity = chainCapacity * 2;
    //     AbstractIterableMap<K, V>[] newChains = createArrayOfChains(chainCapacity);
    //     for (Entry<K, V> temp : this) {
    //         help(newChains, temp.getKey(), temp.getValue());
    //     }
    //     chains = newChains;
    // }
    //
    // private void help(AbstractIterableMap<K, V>[] newChains, K key, V value) {
    //     int hash = 0;
    //     if (key != null) {
    //         hash = Math.abs(key.hashCode() % newChains.length);
    //     }
    //     if (newChains[hash] == null) { // chain not created
    //         newChains[hash] = createChain(chainCapacity);
    //     }
    //     newChains[hash].put(key, value);
    // }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int pointer;
        private Iterator<Map.Entry<K, V>> itr;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            pointer = 0;
            for (AbstractIterableMap<K, V> temp : chains) {
                if (temp != null && temp.size() != 0) {
                    itr = temp.iterator();
                    break;
                }
                pointer++;
            }
        }

        @Override
        public boolean hasNext() {
            if (itr == null) {
                return false;
            }
            if (itr.hasNext()) {
                return true;
            }
            else {
                pointer++;
                while (pointer < chains.length) {
                    if (chains[pointer] != null && chains[pointer].size() != 0) {
                        itr = chains[pointer].iterator();
                        return true;
                    }
                    pointer++;
                }
                return false;
            }
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                return itr.next();
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }
}
