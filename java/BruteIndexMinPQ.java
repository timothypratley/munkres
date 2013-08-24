/*************************************************************************
 *  Compilation:  javac BruteIndexMinPQ.java
 *  Execution:    java BruteIndexMinPQ
 *
 *  Indexed PQ implementation using an array.
 *  The elements are integers between 0 and N-1.
 *
 *  Operation   Running time
 *  -------------------------
 *  Construct      N
 *  is empty       1
 *  size           1
 *  contains       1
 *  insert         1
 *  change key     1
 *  increase key   1
 *  decrease key   1
 *  delete min     N
 *  min            N
 *
 *********************************************************************/

import java.util.NoSuchElementException;

public class BruteIndexMinPQ<Key extends Comparable<Key>> {
    private int N;           // number of elements on PQ
    private Key[] keys;      // keys[i] = key of element i

    public BruteIndexMinPQ(int NMAX) {
        keys = (Key[]) new Comparable[NMAX];
    }

    // is the priority queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the size of the priority queue
    public int size() {
        return N;
    }

    // does pq contain element k?
    public boolean contains(int k) {
        return keys[k] != null;
    }

    // insert element k with given priority
    public void insert(int k, Key key) {
        if (contains(k)) throw new NoSuchElementException("index is already in priority queue");
        N++;
        keys[k] = key;
    }

    // delete and return the minimum element
    public int delMin() {
        int min = min();
        keys[min] = null;
        N--;
        return min;
    }

    // return the index of the minimum element
    public int min() {
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = -1;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null) continue;
            else if (min == -1) min = i;
            else if (keys[i].compareTo(keys[min]) < 0) min = i;
        }
        return min;
    }

    // change the priority of element k to specified key
    public void changeKey(int k, Key key) {
        if (!contains(k)) throw new NoSuchElementException("index is not in the priority queue");
        keys[k] = key;
    }

    // decrease the priority of element k to specified key
    public void decreaseKey(int k, Key key) {
        if (!contains(k)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys[k].compareTo(key) <= 0) throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
        keys[k] = key;
    }

    // decrease the priority of element k to specified key
    public void increaseKey(int k, Key key) {
        if (!contains(k)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys[k].compareTo(key) >= 0) throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
        keys[k] = key;
    }
}

